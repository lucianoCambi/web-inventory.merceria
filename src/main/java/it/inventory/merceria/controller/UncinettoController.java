package it.inventory.merceria.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.inventory.merceria.controller.validator.UncinettoValidator;
import it.inventory.merceria.model.Carrello;
import it.inventory.merceria.model.Credentials;
import it.inventory.merceria.model.Filato;
import it.inventory.merceria.model.Uncinetto;
import it.inventory.merceria.service.CarrelloService;
import it.inventory.merceria.service.CredentialsService;
import it.inventory.merceria.service.UncinettoService;

@Controller
public class UncinettoController {

	@Autowired
	private UncinettoService uncinettoService;
	
	@Autowired 
	private CredentialsService credentialsService;
	
	@Autowired
	private CarrelloService carrelloService;
	
	@Autowired
	private UncinettoValidator uncinettoValidator;
	
	
	@GetMapping(value = "/uncinetti") 
	public String showUncinetti (Model model) {
		isAdmin(model);
		model.addAttribute("uncinetti", this.uncinettoService.findAll());
		return "listaUncinetti";
	}
	
	@PostMapping(value="/ricercaUncinetto")
	public String searchUncinetto(Model model, @RequestParam int misura) {
		isAdmin(model);
		model.addAttribute("uncinetti", this.uncinettoService.findByMisura(misura));
		return "listaUncinetti";
	}
	
	@GetMapping(value="/uncinetto/{id}")
	public String getUncinetto(@PathVariable("id") Long uncinettoId, Model model) {
		isAdmin(model);
		model.addAttribute("uncinetto", this.uncinettoService.getUncinetto(uncinettoId));
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "uncinetto";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
				return "default/uncinettoDefault";
			}
			if(credentials.getRole().equals(Credentials.ADMIN_ROLE))
			return "admin/uncinettoAdmin";
		}
		
		return "uncinetto";
	}
	
	@GetMapping(value="/addUncinettoWhishlist/{id}")
	public String addFilatoWhishlist(@PathVariable("id") Long uncinettoId, Model model) {
		isAdmin(model);
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Uncinetto uncinetto = this.uncinettoService.findById(uncinettoId);
		if(credentials.getUser().getCarrello().notHasUncienetto(uncinetto) && this.uncinettoService.diminuisci(uncinetto)) {
		this.carrelloService.addFilato(credentials.getUser().getCarrello(), uncinetto);
		model.addAttribute("uncinetto", uncinetto);
		if(credentials.getRole().equals(Credentials.ADMIN_ROLE))
			return "admin/uncinettoAdmin";
		return "default/uncinettoDefault";}
		else {
			model.addAttribute("messaggioErrore", "Non e' disponibile al momento o e' gia' presente nella tua WhishList");
			model.addAttribute("uncinetto", uncinetto);
			if(credentials.getRole().equals(Credentials.ADMIN_ROLE))
				return "admin/uncinettoAdmin";
		return "default/uncinettoDefault";}
	}
	
	@GetMapping(value="/togliUncinetto/{uncinettoId}/{carrelloId}")
	public String togliFilatoDaWhishlist(@PathVariable("uncinettoId") Long unicnettoId, @PathVariable("carrelloId") Long carrelloId, Model model) {
		isAdmin(model);
		Uncinetto uncinetto=this.uncinettoService.findById(unicnettoId);
		this.uncinettoService.aumenta(uncinetto);
		this.carrelloService.remove(this.carrelloService.getCarrello(carrelloId), uncinetto);
		
		model.addAttribute("carrello", this.carrelloService.getCarrello(carrelloId));
		return "default/whishlist";
	}
	
	
	@GetMapping(value="/addUncinettoDb")
	public String formNewMovie(Model model) {
		isAdmin(model);
		model.addAttribute("uncinetto", new Uncinetto());
		return "admin/formNewUncinetto";
	}
	
	@PostMapping(value="/admin/newUncinetto")
	public String newFilato(@Valid @ModelAttribute("uncinetto") Uncinetto uncinetto, BindingResult bindingResult, Model model) {
		isAdmin(model);
		this.uncinettoValidator.validate(uncinetto, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.uncinettoService.save(uncinetto); 
			model.addAttribute("filato", uncinetto);
			return "admin/uncinettoAdmin";
		} else {
			model.addAttribute("messaggioErrore", "Uncinetto gia' presente");
			return "admin/formNewUncinetto"; 
		}
	}
	
	
	@GetMapping(value="/gestisciUncinetto/{id}")
	public String gestisciUncinetto(@PathVariable("id") Long uncinettoId, Model model) {
		isAdmin(model);
		model.addAttribute("uncinetto", this.uncinettoService.findById(uncinettoId));
		model.addAttribute("altriFilati", this.uncinettoService.filatiNotForUncinetto(uncinettoId));
		return "admin/manageUncinetto";
	}
	
	@GetMapping(value="/moreQtaUncinetto/{id}")
	public String addOneQta(@PathVariable("id") Long uncinettoId, Model model) {
		isAdmin(model);
		Uncinetto uncinetto = this.uncinettoService.findById(uncinettoId);
		this.uncinettoService.aumenta(uncinetto);
		model.addAttribute("uncinetto", uncinetto);
		model.addAttribute("altriFilati", this.uncinettoService.filatiNotForUncinetto(uncinettoId));
		return "admin/manageUncinetto";
	}
	
	@GetMapping(value="/lessQtaUncinetto/{id}")
	public String lessQta(@PathVariable("id") Long uncinettoId, Model model) {
		isAdmin(model);
		Uncinetto uncinetto = this.uncinettoService.findById(uncinettoId);
		this.uncinettoService.diminuisci(uncinetto);
		model.addAttribute("uncinetto", uncinetto);
		model.addAttribute("altriFilati", this.uncinettoService.filatiNotForUncinetto(uncinettoId));
		return "admin/manageUncinetto";
	}
	
	
	
	@GetMapping(value="/eliminaUncinetto/{id}")
	public String eliminaUncinetto(@PathVariable("id") Long uncinettoId, Model model) {
		isAdmin(model);
		for(Carrello carrello: this.carrelloService.findAll()) {
			carrello.getUncinetti().remove(this.uncinettoService.findById(uncinettoId));
		}
		this.uncinettoService.elimina(uncinettoId);
		model.addAttribute("uncinetti", this.uncinettoService.findAll());
		return "listaUncinetti";
	}
	
	
	@GetMapping(value="/togliFilatoDaUncinetto/{filatoId}/{uncinettoId}")
	public String togliUncinettoDaFilato(@PathVariable("filatoId") Long filato, @PathVariable("uncinettoId") Long uncinetto, Model model) {
		isAdmin(model);
		model.addAttribute("uncinetto", this.uncinettoService.removeFilatoDaUncinetto(uncinetto, filato));
		model.addAttribute("altriFilati", this.uncinettoService.filatiNotForUncinetto(uncinetto));
		return "admin/manageUncinetto";
	}
	
	@GetMapping(value="/aggiungiFilatoAUncinetto/{filatoId}/{uncinettoId}")
	public String aggiungiUncinettoAFilato(@PathVariable("filatoId") Long filato, @PathVariable("uncinettoId") Long uncinetto, Model model) {
		isAdmin(model);
		model.addAttribute("uncinetto", this.uncinettoService.addFilatoAUncienetto(uncinetto, filato));
		model.addAttribute("altriFilati", this.uncinettoService.filatiNotForUncinetto(uncinetto));
		return "admin/manageUncinetto";
	}
	
	
	
	//funzione per capire se Admin e quindi aggiungere nav per gestione db
	public void isAdmin(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("valueAdmin", false);
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				model.addAttribute("valueAdmin", true);
			}
			else model.addAttribute("valueAdmin", false);
		}
	}
	
}
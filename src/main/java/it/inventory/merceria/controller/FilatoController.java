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

import it.inventory.merceria.controller.validator.FilatoValidator;
import it.inventory.merceria.model.Carrello;
import it.inventory.merceria.model.Credentials;
import it.inventory.merceria.model.Filato;
import it.inventory.merceria.model.Uncinetto;
import it.inventory.merceria.repository.FilatoRepository;
import it.inventory.merceria.service.CarrelloService;
import it.inventory.merceria.service.CredentialsService;
import it.inventory.merceria.service.FilatoService;
import it.inventory.merceria.service.UncinettoService;

@Controller
public class FilatoController {

	@Autowired
	private FilatoService filatoService;
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private CarrelloService carrelloService;
	
	@Autowired
	private FilatoValidator filatoValidator;

	@Autowired
	private UncinettoService uncinettoService;
	
	
	@GetMapping(value = "/filati") 
	public String showFilati (Model model) {
		isAdmin(model);
		model.addAttribute("filati", this.filatoService.findAll());
		return "listaFilati";
	}
	
	@PostMapping(value="/ricercaFilato")
	public String searchFilato(Model model, @RequestParam String nome) {
		isAdmin(model);
		model.addAttribute("filati", this.filatoService.findByNome(nome));
		return "listaFilati";
	}
	
	@GetMapping(value="/filato/{id}")
	public String getFilato(@PathVariable("id") Long filatoId, Model model) {
		isAdmin(model);
		model.addAttribute("filato", this.filatoService.findById(filatoId));
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "filato";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.DEFAULT_ROLE)) {
				return "default/filatoDefault";
			}
			else if(credentials.getRole().equals(Credentials.ADMIN_ROLE))
				return "admin/filatoAdmin";
		}
		
		return "filato";
	}
	
	@GetMapping(value="/addFilatoWhishlist/{id}")
	public String addFilatoWhishlist(@PathVariable("id") Long filatoId, Model model) {
		isAdmin(model);
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Filato filato = this.filatoService.findById(filatoId);
		if(credentials.getUser().getCarrello().notHasFilato(filato) && this.filatoService.diminuisci(filato)) {
		this.carrelloService.addFilato(credentials.getUser().getCarrello(), filato);
		model.addAttribute("filato", filato);
		if(credentials.getRole().equals(Credentials.ADMIN_ROLE))
			return "admin/filatoAdmin";
		return "default/filatoDefault";}
		else {
			model.addAttribute("messaggioErrore", "Non e' disponibile al momento o e' gia' presente nella tua WhishList");
			model.addAttribute("filato", filato);
			if(credentials.getRole().equals(Credentials.ADMIN_ROLE))
				return "admin/filatoAdmin";
		return "default/filatoDefault";}
	}
	
	@GetMapping(value="/togliFilato/{filatoId}/{carrelloId}")
	public String togliFilatoDaWhishlist(@PathVariable("filatoId") Long filatoId, @PathVariable("carrelloId") Long carrelloId, Model model) {
		isAdmin(model);
		Filato filato=this.filatoService.findById(filatoId);
		this.filatoService.aumenta(filato);
		this.carrelloService.remove(this.carrelloService.getCarrello(carrelloId), filato);
		
		model.addAttribute("carrello", this.carrelloService.getCarrello(carrelloId));
		return "default/whishlist";
	}
	
	
	@GetMapping(value="/addFilatoDb")
	public String formNewMovie(Model model) {
		isAdmin(model);
		model.addAttribute("filato", new Filato());
		return "admin/formNewFilato";
	}
	
	@PostMapping(value="/admin/newFilato")
	public String newFilato(@Valid @ModelAttribute("filato") Filato filato, BindingResult bindingResult, Model model) {
		isAdmin(model);
		this.filatoValidator.validate(filato, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.filatoService.save(filato); 
			model.addAttribute("filato", filato);
			return "admin/filatoAdmin";
		} else {
			model.addAttribute("messaggioErrore", "Filato gia' presente");
			return "admin/formNewFilato"; 
		}
	}
	
	@GetMapping(value="/gestisciFilato/{id}")
	public String gestisciFilato(@PathVariable("id") Long filatoId, Model model) {
		isAdmin(model);
		model.addAttribute("filato", this.filatoService.findById(filatoId));
		model.addAttribute("altriUncinetti", this.filatoService.UncinettiNotForFilato(filatoId));
		return "admin/manageFilato";
	}
	
	
	
	@GetMapping(value="/moreQtaFilato/{id}")
	public String addOneQta(@PathVariable("id") Long filatoId, Model model) {
		isAdmin(model);
		Filato filato = this.filatoService.findById(filatoId);
		this.filatoService.aumenta(filato);
		model.addAttribute("filato", filato);
		model.addAttribute("altriUncinetti", this.filatoService.UncinettiNotForFilato(filatoId));
		return "admin/manageFilato";
	}
	
	@GetMapping(value="/lessQtaFilato/{id}")
	public String lessQta(@PathVariable("id") Long filatoId, Model model) {
		isAdmin(model);
		Filato filato = this.filatoService.findById(filatoId);
		this.filatoService.diminuisci(filato);
		model.addAttribute("filato", filato);
		model.addAttribute("altriUncinetti", this.filatoService.UncinettiNotForFilato(filatoId));
		return "admin/manageFilato";
	}
	
	
	
	@GetMapping(value="/eliminaFilato/{id}")
	public String eliminaFilato(@PathVariable("id") Long filatoId, Model model) {
		isAdmin(model);
		for(Carrello carrello: this.carrelloService.findAll()) {
			carrello.getFilati().remove(this.filatoService.findById(filatoId));
		}
		this.filatoService.elimina(filatoId);
		model.addAttribute("filati", this.filatoService.findAll());
		return "listaFilati";
	}
	
	
	@GetMapping(value="/togliUncinettoDaFilato/{uncinettoId}/{filatoId}")
	public String togliUncinettoDaFilato(@PathVariable("uncinettoId") Long uncinettoId, @PathVariable("filatoId") Long filatoId, Model model) {
		isAdmin(model);
		model.addAttribute("filato", this.filatoService.removeUncinettoDaFilato(filatoId, uncinettoId));
		model.addAttribute("altriUncinetti", this.filatoService.UncinettiNotForFilato(filatoId));
		return "admin/manageFilato";
	}
	
	@GetMapping(value="/aggiungiUncinettoAFilato/{uncinettoId}/{filatoId}")
	public String aggiungiUncinettoAFilato(@PathVariable("uncinettoId") Long uncinettoId, @PathVariable("filatoId") Long filatoId, Model model) {
		isAdmin(model);
		model.addAttribute("filato", this.filatoService.addUncinettoAFilato(filatoId, uncinettoId));
		model.addAttribute("altriUncinetti", this.filatoService.UncinettiNotForFilato(filatoId));
		return "admin/manageFilato";
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

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
import org.springframework.web.bind.annotation.PostMapping;

import it.inventory.merceria.controller.validator.CredentialsValidator;
import it.inventory.merceria.model.Carrello;
import it.inventory.merceria.model.Credentials;
import it.inventory.merceria.model.User;
import it.inventory.merceria.service.CarrelloService;
import it.inventory.merceria.service.CredentialsService;
import it.inventory.merceria.service.UserService;


@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CarrelloService carrelloService;
	
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser";
	}
	
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin";
	}

	@GetMapping(value = "/") 
	public String index(Model model) {
		isAdmin(model);
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication instanceof AnonymousAuthenticationToken) {
//	        return "index.html";
//		}
//		else {		
//			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
//			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
//				return "admin/indexAdmin.html";
//			}
//		}
        return "index.html";
	}
		
    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
    	isAdmin(model);
        
//    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
//    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
//            return "admin/indexAdmin.html";
//        }
        return "index.html";
    }

	@PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                 BindingResult userBindingResult, @Valid
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult,
                 Model model) {
		
		this.credentialsValidator.validate(credentials, credentialsBindingResult);

        // se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            Carrello carrello = new Carrello();
            user.setCarrello(carrello);
            carrello.setUser(user);
            this.carrelloService.saveCarrello(carrello);
            this.userService.saveUser(user);
            model.addAttribute("user", user);
            return "registrationSuccessful";
        }
        model.addAttribute("messaggioErrore", "Username già in uso");
        return "formRegisterUser.html";
    }
	
	
	@GetMapping(value = "/contatti") 
	public String showContatti (Model model) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication instanceof AnonymousAuthenticationToken) {
//			model.addAttribute("valueAdmin", false);
//		}
//		else {		
//			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
//			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
//				model.addAttribute("valueAdmin", true);
//			}
//			else model.addAttribute("valueAdmin", false);
//		}
		isAdmin(model);
		return "contatti";
	}
	
	
	@GetMapping("/whishlist")
	public String showWhislist (Model model) {
		isAdmin(model);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "formLogin";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			model.addAttribute("carrello", credentials.getUser().getCarrello()); 
		    return "default/whishlist.html";
			
		}
		
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
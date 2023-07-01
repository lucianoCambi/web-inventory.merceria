package it.inventory.merceria.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.inventory.merceria.model.Carrello;
import it.inventory.merceria.model.Filato;
import it.inventory.merceria.model.Uncinetto;
import it.inventory.merceria.repository.CarrelloRepository;


@Service
public class CarrelloService {

	@Autowired
	private CarrelloRepository carrelloRepository;
	
	@Transactional
	public Carrello getCarrello(Long id) {
		Optional<Carrello> result = this.carrelloRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Iterable<Carrello> findAll(){
		return this.carrelloRepository.findAll();
	}
	
	
    @Transactional
    public Carrello saveCarrello(Carrello carrello) {
        return this.carrelloRepository.save(carrello);
    }



    @Transactional
	public void addFilato(Carrello carrello, Filato filato) {

		carrello.getFilati().add(filato);
		this.carrelloRepository.save(carrello);
	}
    
    @Transactional
	public void addFilato(Carrello carrello, Uncinetto uncinetto) {

		carrello.getUncinetti().add(uncinetto);
		this.carrelloRepository.save(carrello);
	}



    @Transactional
	public void remove(Carrello carrello, Filato filato) {
		carrello.getFilati().remove(filato);
		this.carrelloRepository.save(carrello);
	}



    @Transactional
	public void remove(Carrello carrello, Uncinetto uncinetto) {

		carrello.getUncinetti().remove(uncinetto);
		this.carrelloRepository.save(carrello);
	}
}
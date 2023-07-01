package it.inventory.merceria.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.inventory.merceria.model.Credentials;
import it.inventory.merceria.model.Filato;
import it.inventory.merceria.model.Uncinetto;
import it.inventory.merceria.repository.FilatoRepository;
import it.inventory.merceria.repository.UncinettoRepository;

@Service
public class UncinettoService {

	@Autowired
	private UncinettoRepository uncinettoRepository;
	
	@Autowired
	private FilatoRepository filatoRepository;
	
	@Transactional
	public Uncinetto getUncinetto(Long id) {
		Optional<Uncinetto> result = this.uncinettoRepository.findById(id);
		return result.orElse(null);
	}

		
    @Transactional
    public Uncinetto saveUncinetto(Uncinetto uncinetto) {
        return this.uncinettoRepository.save(uncinetto);
    }


    @Transactional
	public Iterable<Uncinetto> findAll() {
		return this.uncinettoRepository.findAll();
	}


    @Transactional
	public Object findByMisura(int misura) {
		return this.uncinettoRepository.findByMisura(misura);
	}


	public Uncinetto findById(Long uncinettoId) {
		return this.uncinettoRepository.findById(uncinettoId).get();
	}


	public boolean diminuisci(Uncinetto uncinetto) {
		if(uncinetto.diminuisciQta()) {
			this.uncinettoRepository.save(uncinetto);
			return true;
		}
	    else return false;
	}


	public void aumenta(Uncinetto uncinetto) {

		uncinetto.aumentaQta();
		this.uncinettoRepository.save(uncinetto);
	}


	public void save(@Valid Uncinetto uncinetto) {

		this.uncinettoRepository.save(uncinetto);
	}
	
	@Transactional

	public List<Filato> filatiNotForUncinetto(Long uncinettoId) {
    	List<Filato> filatiToAdd = new ArrayList<>();  
    	for (Filato f : this.filatoRepository.findFilatiNotForUncinetto(uncinettoId)) { //qui
			filatiToAdd.add(f);
		}
		return filatiToAdd;
		
	}



    @Transactional
	public Uncinetto removeFilatoDaUncinetto(Long uncinettoId, Long filatoId) {

		Filato filato = this.filatoRepository.findById(filatoId).get();
		Uncinetto uncinetto = this.uncinettoRepository.findById(uncinettoId).get();
		uncinetto.getEsempioFilati().remove(filato);
		filato.getUncinettiAdatti().remove(uncinetto);
		this.filatoRepository.save(filato);
		this.uncinettoRepository.save(uncinetto);
		return uncinetto;
	}



    @Transactional
	public Uncinetto addFilatoAUncienetto(Long uncinettoId, Long filatoId) {

		Uncinetto uncinetto= this.uncinettoRepository.findById(uncinettoId).get();
		Filato filato= this.filatoRepository.findById(filatoId).get();
		uncinetto.getEsempioFilati().add(filato);
		filato.getUncinettiAdatti().add(uncinetto);
		this.filatoRepository.save(filato);
		this.uncinettoRepository.save(uncinetto);
		return uncinetto;
	}


    @Transactional
	public void elimina(Long uncinetto) {

    	for(Filato filato : this.uncinettoRepository.findById(uncinetto).get().getEsempioFilati()) {
    		filato.getUncinettiAdatti().remove(this.uncinettoRepository.findById(uncinetto).get());
    	}
		
		this.uncinettoRepository.deleteById(uncinetto);
	}
	
}

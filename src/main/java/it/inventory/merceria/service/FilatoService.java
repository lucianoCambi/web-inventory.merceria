package it.inventory.merceria.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.inventory.merceria.model.Filato;
import it.inventory.merceria.model.Uncinetto;
import it.inventory.merceria.repository.FilatoRepository;
import it.inventory.merceria.repository.UncinettoRepository;

@Service
public class FilatoService {

	@Autowired
	private FilatoRepository filatoRepository;
	
	@Autowired
	private UncinettoRepository uncinettoRepository;
	
	
	@Transactional
	public Filato getFilato(Long id) {
		Optional<Filato> result = this.filatoRepository.findById(id);
		return result.orElse(null);
	}

	
		
    @Transactional
    public Filato saveFilato(Filato filato) {
        return this.filatoRepository.save(filato);
    }



    @Transactional
	public Iterable<Filato> findAll() {
    	return this.filatoRepository.findAll();
	}



    @Transactional
	public Object findByNome(String nome) {
    	
		return this.filatoRepository.findByNome(nome);
	}



    @Transactional
	public Filato findById(Long filatoId) {
		return this.filatoRepository.findById(filatoId).get();
	}



    @Transactional
	public Set<Uncinetto> getUncinetti(Long filatoId) {
    	Filato filato= this.filatoRepository.findById(filatoId).get();
		return filato.getUncinettiAdatti();
	}



    @Transactional
	public boolean diminuisci(Filato filato) {

		if(filato.diminuisciQta()) {
		  this.filatoRepository.save(filato);
		  return true;
		}
		else return false;
		  
	}
    
    @Transactional
    public void aumenta(Filato filato) {
    	
    	filato.aumentaQta();
    	this.filatoRepository.save(filato);
    	
    }



    @Transactional

	public void save(@Valid Filato filato) {

		this.filatoRepository.save(filato);
	}


    @Transactional

	public void elimina(Long filatoId) {

    	Filato filato = this.filatoRepository.findById(filatoId).get();
    	for(Uncinetto uncinetto : this.filatoRepository.findById(filatoId).get().getUncinettiAdatti()) {
    		uncinetto.getEsempioFilati().remove(filato);
    	}
    		
		this.filatoRepository.deleteById(filatoId);
	}


    @Transactional

	public List<Uncinetto> UncinettiNotForFilato(Long filatoId) {
    	List<Uncinetto> uncinettiToAdd = new ArrayList<>();  
    	for (Uncinetto u : this.uncinettoRepository.findUncinettoNotForFilato(filatoId)) { //qui
			uncinettiToAdd.add(u);
		}
		return uncinettiToAdd;
		
//		return this.filatoRepository.findUncinettoNotForFilato(filatoId);
	}



    @Transactional
	public Filato removeUncinettoDaFilato(Long filatoId, Long uncinettoId) {

		Uncinetto uncinetto = this.uncinettoRepository.findById(uncinettoId).get();
		Filato filato = this.filatoRepository.findById(filatoId).get();
		filato.getUncinettiAdatti().remove(uncinetto);
		uncinetto.getEsempioFilati().remove(filato);
		this.uncinettoRepository.save(uncinetto);
		this.filatoRepository.save(filato);
		return filato;
	}



    @Transactional
	public Filato addUncinettoAFilato(Long filatoId, Long uncinettoId) {

		Filato filato = this.filatoRepository.findById(filatoId).get();
		Uncinetto uncinetto = this.uncinettoRepository.findById(uncinettoId).get();
		filato.getUncinettiAdatti().add(uncinetto);
		uncinetto.getEsempioFilati().add(filato);
		this.uncinettoRepository.save(uncinetto);
		this.filatoRepository.save(filato);
		return filato;
	}
    
  
}

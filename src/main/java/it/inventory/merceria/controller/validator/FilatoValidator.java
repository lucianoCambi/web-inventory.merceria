package it.inventory.merceria.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.inventory.merceria.model.Filato;
import it.inventory.merceria.repository.FilatoRepository;

@Component
public class FilatoValidator implements Validator {
	@Autowired
	private FilatoRepository filatoRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Filato filato = (Filato)o;
		if (filato.getNome()!=null && filato.getPeso()>=0
				&& filatoRepository.existsByNomeAndPeso(filato.getNome(), filato.getPeso())) {
			errors.reject("filato.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Filato.class.equals(aClass);
	}
}
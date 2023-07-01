package it.inventory.merceria.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.inventory.merceria.model.Uncinetto;
import it.inventory.merceria.repository.UncinettoRepository;

@Component
public class UncinettoValidator implements Validator {
	@Autowired
	private UncinettoRepository uncinettoRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Uncinetto uncinetto = (Uncinetto)o;
		if (uncinetto.getMisura()>=0 && uncinetto.getMisura()<=20
				&& uncinettoRepository.existsByMisura(uncinetto.getMisura())) {
			errors.reject("uncinetto.duplicate");
		}
	}
	@Override
	public boolean supports(Class<?> aClass) {
		return Uncinetto.class.equals(aClass);
	}
}

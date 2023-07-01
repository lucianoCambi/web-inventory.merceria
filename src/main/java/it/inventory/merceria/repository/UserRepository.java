package it.inventory.merceria.repository;

import org.springframework.data.repository.CrudRepository;

import it.inventory.merceria.model.User;


public interface UserRepository extends CrudRepository<User, Long> {
	
}
package it.inventory.merceria.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.inventory.merceria.model.Credentials;


public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);

	public boolean existsByUsername(String username);

}
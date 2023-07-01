package it.inventory.merceria.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
public class Carrello {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@OneToOne
	private User user;
	
	@OneToMany
	private Set<Uncinetto> uncinetti;
	
	@OneToMany
	private Set<Filato> filati;

	

	public Set<Uncinetto> getUncinetti() {
		return uncinetti;
	}

	public void setUncinetti(Set<Uncinetto> uncinetti) {
		this.uncinetti = uncinetti;
	}

	public Set<Filato> getFilati() {
		return filati;
	}

	public void setFilati(Set<Filato> filati) {
		this.filati = filati;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User credentials) {
		this.user = credentials;
	}

	@Override
	public int hashCode() {
		return Objects.hash(user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Carrello other = (Carrello) obj;
		return Objects.equals(user, other.user);
	}

	public boolean notHasFilato(Filato filato) {
		return !this.filati.contains(filato);
	}
	
	public boolean notHasUncienetto(Uncinetto uncinetto) {
		return !this.uncinetti.contains(uncinetto);
	}
}

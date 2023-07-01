package it.inventory.merceria.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;


@Entity
public class Filato {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotBlank
	private String nome;
	
	private String descr;
	

	@NotNull
	private int peso;
	
	private int qta;
	
	private String urlImage;
	
	@ManyToMany
	private Set<Uncinetto> uncinettiAdatti;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public int getQta() {
		return qta;
	}

	public void setQta(Integer qta) {
		this.qta = qta;
	}

	public Set<Uncinetto> getUncinettiAdatti() {
		return uncinettiAdatti;
	}

	public void setUncinettiAdatti(Set<Uncinetto> uncinettiAdatti) {
		this.uncinettiAdatti = uncinettiAdatti;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome, peso);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Filato other = (Filato) obj;
		return Objects.equals(nome, other.nome) && Objects.equals(peso, other.peso);
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlOfPicture) {
		this.urlImage = urlOfPicture;
	}
	
	public String getDescr() {
		return descr;
	}

	public void setDescr(String desc) {
		this.descr = desc;
	}

	public boolean diminuisciQta() {

		if(this.qta>0) {
		  this.qta = qta -1;
		  return true;
		}
		else return false;
	}
	
	public void aumentaQta() {
		
		this.qta = qta + 1;
	}

}

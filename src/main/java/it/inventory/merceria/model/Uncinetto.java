package it.inventory.merceria.model;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
public class Uncinetto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	
    @NotNull
    @Min(1)
    @Max(20)
	private int misura;
	
    @Min(0)
	private int qta;
    
    private String urlImage;
	
	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	@ManyToMany(mappedBy="uncinettiAdatti")
	private Set<Filato> esempioFilati;

	
	public Uncinetto() {
		this.esempioFilati= new HashSet<>();
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getMisura() {
		return misura;
	}

	public void setMisura(Integer misura) {
		this.misura = misura;
	}

	public int getQta() {
		return qta;
	}

	public void setQta(Integer qta) {
		this.qta = qta;
	}
	


	public Set<Filato> getEsempioFilati() {
		return esempioFilati;
	}

	public void setEsempioFilati(Set<Filato> esempioFilati) {
		this.esempioFilati = esempioFilati;
	}

	@Override
	public int hashCode() {
		return Objects.hash(misura);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Uncinetto other = (Uncinetto) obj;
		return Objects.equals(misura, other.misura);
	}
	
	public boolean diminuisciQta() {

		if(this.qta>0) {
		this.qta = qta -1;
		return true;}
		else return false;
		
	}
	
	public void aumentaQta() {
		this.qta = qta + 1;
	}

}

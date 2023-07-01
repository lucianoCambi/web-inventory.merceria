package it.inventory.merceria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.inventory.merceria.model.Filato;
import it.inventory.merceria.model.Uncinetto;


public interface UncinettoRepository extends CrudRepository<Uncinetto, Long>{
	
	public boolean existsByMisura(Integer misura);

	
	@Query(value="select * " //corretto
			+ "from uncinetto "
			+ "where uncinetto.id not in "
			+ "(select uncinetti_adatti_id "
			+ "from filato_uncinetti_adatti "
			+ "where esempio_filati_id = :filatoId)", nativeQuery=true)
	public Iterable<Uncinetto> findUncinettoNotForFilato(@Param("filatoId") Long id);
	


	public List<Uncinetto> findByMisura(int misura);
}

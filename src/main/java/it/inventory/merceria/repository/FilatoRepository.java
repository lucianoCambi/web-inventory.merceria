package it.inventory.merceria.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.inventory.merceria.model.Filato;
import it.inventory.merceria.model.Uncinetto;


public interface FilatoRepository extends CrudRepository<Filato, Long> {

	public boolean existsByNomeAndPeso(String nome,int peso);
	
	@Query(value="select * "
			+ "from filato "
			+ "where filato.id not in "
			+ "(select esempio_filati_id "
			+ "from filato_uncinetti_adatti "
			+ "where uncinetti_adatti_id = :uncinettoId)", nativeQuery=true)
	public Iterable<Filato> findFilatiNotForUncinetto(@Param("uncinettoId") Long id);
//
//	@Query(value="select * "
//			+"from filato  "
//			+"where filato.nome = :filatoNome", nativeQuery=true)
//	public Iterable<Filato> findByNome(@Param("filatoNome")String nome);

	
	public List<Filato> findByNome(String nome);
	
//	@Query(value="delete * "
//			+ "from filato_uncinetti_adatti "
//			+ "where esempio_filati_id = :filatoId", nativeQuery=true)
//	public void eliminaCollegamenti(@Param("filatoId")Long findById);
}

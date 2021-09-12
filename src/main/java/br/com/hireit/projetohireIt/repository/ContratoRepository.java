package br.com.hireit.projetohireIt.repository;

import br.com.hireit.projetohireIt.tables.ContratosTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContratoRepository extends JpaRepository<ContratosTable, Integer> {

    @Query(value = "SELECT * FROM contrato WHERE fk_demanda = ?1 and fk_oferta = ?2", nativeQuery = true)
    List<ContratosTable> findContratoByDemandaAndOferta(int fkDemanda, int fkOferta);

}

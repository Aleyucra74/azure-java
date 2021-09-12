package br.com.hireit.projetohireIt.repository;

import br.com.hireit.projetohireIt.tables.TecnologiasTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TecnologiaRepository extends JpaRepository<TecnologiasTable, Integer> {

}

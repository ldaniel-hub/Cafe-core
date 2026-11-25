package br.edu.graosapi.repository;

import br.edu.graosapi.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    boolean existsByProdutorId(Long produtorId);
}

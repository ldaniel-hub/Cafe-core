package br.edu.graosapi.service;

import br.edu.graosapi.dto.ProdutorRequest;
import br.edu.graosapi.exception.ResourceNotFoundException;
import br.edu.graosapi.model.Produtor;
import br.edu.graosapi.repository.LoteRepository;
import br.edu.graosapi.repository.ProdutorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutorService {
    private final ProdutorRepository produtorRepository;
    private final LoteRepository loteRepository;

    public ProdutorService(ProdutorRepository produtorRepository, LoteRepository loteRepository) {
        this.produtorRepository = produtorRepository;
        this.loteRepository = loteRepository;
    }

    @Transactional(readOnly = true)
    public List<Produtor> listar() {
        return produtorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Produtor buscarPorId(Long id) {
        return produtorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produtor não encontrado: " + id));
    }

    public Produtor cadastrar(Produtor produtor) {
        return produtorRepository.save(produtor);
    }

    public Produtor atualizar(Long id, ProdutorRequest request) {
        Produtor produtor = buscarPorId(id);
        produtor.setNome(request.nome());
        produtor.setEmail(request.email());
        produtor.setMunicipio(request.municipio());
        return produtorRepository.save(produtor);
    }

    public void remover(Long id) {
        Produtor produtor = buscarPorId(id);
        if (loteRepository.existsByProdutorId(id)) {
            throw new IllegalArgumentException("Não é possível remover um produtor que possui lotes");
        }
        produtorRepository.delete(produtor);
    }
}

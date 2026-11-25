package br.edu.graosapi.service;

import br.edu.graosapi.dto.LoteRequest;
import br.edu.graosapi.dto.MovimentacaoRequest;
import br.edu.graosapi.dto.TipoMovimentacao;
import br.edu.graosapi.exception.ResourceNotFoundException;
import br.edu.graosapi.model.Lote;
import br.edu.graosapi.model.Produtor;
import br.edu.graosapi.repository.LoteRepository;
import br.edu.graosapi.repository.ProdutorRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoteService {
    private final LoteRepository loteRepository;
    private final ProdutorRepository produtorRepository;

    public LoteService(LoteRepository loteRepository, ProdutorRepository produtorRepository) {
        this.loteRepository = loteRepository;
        this.produtorRepository = produtorRepository;
    }

    @Transactional(readOnly = true)
    public List<Lote> listar() {
        return loteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Lote buscarPorId(Long id) {
        return loteRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado: " + id));
    }

    public Lote cadastrar(LoteRequest request) {
        Lote lote = new Lote();
        aplicarDados(lote, request);
        lote.setQuantidadeKg(BigDecimal.ZERO);
        return loteRepository.save(lote);
    }

    public Lote atualizar(Long id, LoteRequest request) {
        Lote lote = buscarPorId(id);
        aplicarDados(lote, request);
        return loteRepository.save(lote);
    }

    public Lote movimentar(Long id, MovimentacaoRequest request) {
        Lote lote = buscarPorId(id);
        BigDecimal quantidade = request.quantidadeKg();
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A quantidade movimentada deve ser positiva");
        }

        if (request.tipo() == TipoMovimentacao.ENTRADA) {
            lote.setQuantidadeKg(lote.getQuantidadeKg().add(quantidade));
        } else if (request.tipo() == TipoMovimentacao.SAIDA) {
            if (lote.getQuantidadeKg().compareTo(quantidade) < 0) {
                throw new IllegalArgumentException("Estoque insuficiente no lote");
            }
            lote.setQuantidadeKg(lote.getQuantidadeKg().subtract(quantidade));
        } else {
            throw new IllegalArgumentException("Tipo de movimentação inválido");
        }
        return loteRepository.save(lote);
    }

    public void remover(Long id) {
        Lote lote = buscarPorId(id);
        if (lote.getQuantidadeKg().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Só é possível remover lotes sem estoque");
        }
        loteRepository.delete(lote);
    }

    private void aplicarDados(Lote lote, LoteRequest request) {
        Produtor produtor = produtorRepository.findById(request.produtorId())
            .orElseThrow(() -> new ResourceNotFoundException("Produtor não encontrado: " + request.produtorId()));
        lote.setCodigo(request.codigo());
        lote.setVariedade(request.variedade());
        lote.setProdutor(produtor);
    }
}

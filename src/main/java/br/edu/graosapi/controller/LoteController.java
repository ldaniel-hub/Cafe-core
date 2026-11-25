package br.edu.graosapi.controller;

import br.edu.graosapi.dto.LoteRequest;
import br.edu.graosapi.dto.LoteResponse;
import br.edu.graosapi.dto.MovimentacaoRequest;
import br.edu.graosapi.model.Lote;
import br.edu.graosapi.service.LoteService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {
    private final LoteService service;

    public LoteController(LoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<LoteResponse> listar() {
        return service.listar().stream().map(this::converter).toList();
    }

    @GetMapping("/{id}")
    public LoteResponse buscar(@PathVariable Long id) {
        return converter(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<LoteResponse> cadastrar(@Valid @RequestBody LoteRequest request) {
        Lote salvo = service.cadastrar(request);
        return ResponseEntity.created(URI.create("/api/lotes/" + salvo.getId())).body(converter(salvo));
    }

    @PutMapping("/{id}")
    public LoteResponse atualizar(@PathVariable Long id, @Valid @RequestBody LoteRequest request) {
        return converter(service.atualizar(id, request));
    }

    @PostMapping("/{id}/movimentacoes")
    public LoteResponse movimentar(@PathVariable Long id, @Valid @RequestBody MovimentacaoRequest request) {
        return converter(service.movimentar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    private LoteResponse converter(Lote lote) {
        return new LoteResponse(lote.getId(), lote.getCodigo(), lote.getVariedade(),
            lote.getQuantidadeKg(), lote.getProdutor().getId());
    }
}

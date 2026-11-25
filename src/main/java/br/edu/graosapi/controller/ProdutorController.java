package br.edu.graosapi.controller;

import br.edu.graosapi.dto.ProdutorRequest;
import br.edu.graosapi.dto.ProdutorResponse;
import br.edu.graosapi.model.Produtor;
import br.edu.graosapi.service.ProdutorService;
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
@RequestMapping("/api/produtores")
public class ProdutorController {
    private final ProdutorService service;

    public ProdutorController(ProdutorService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProdutorResponse> listar() {
        return service.listar().stream().map(this::converter).toList();
    }

    @GetMapping("/{id}")
    public ProdutorResponse buscar(@PathVariable Long id) {
        return converter(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutorResponse> cadastrar(@Valid @RequestBody ProdutorRequest request) {
        Produtor salvo = service.cadastrar(criarEntidade(request));
        return ResponseEntity.created(URI.create("/api/produtores/" + salvo.getId())).body(converter(salvo));
    }

    @PutMapping("/{id}")
    public ProdutorResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutorRequest request) {
        return converter(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }

    private Produtor criarEntidade(ProdutorRequest request) {
        Produtor produtor = new Produtor();
        produtor.setNome(request.nome());
        produtor.setEmail(request.email());
        produtor.setMunicipio(request.municipio());
        return produtor;
    }

    private ProdutorResponse converter(Produtor produtor) {
        return new ProdutorResponse(produtor.getId(), produtor.getNome(), produtor.getEmail(), produtor.getMunicipio());
    }
}

package br.edu.graosapi.service;

import br.edu.graosapi.exception.ResourceNotFoundException;
import br.edu.graosapi.model.Produtor;
import br.edu.graosapi.repository.LoteRepository;
import br.edu.graosapi.repository.ProdutorRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutorServiceTest {
    @Mock ProdutorRepository produtorRepository;
    @Mock LoteRepository loteRepository;
    @InjectMocks ProdutorService service;

    @Test
    void deveBuscarProdutorPorId() {
        Produtor produtor = new Produtor();
        produtor.setNome("Maria");
        when(produtorRepository.findById(1L)).thenReturn(Optional.of(produtor));

        assertEquals("Maria", service.buscarPorId(1L).getNome());
        verify(produtorRepository).findById(1L);
    }

    @Test
    void deveRetornarNaoEncontradoParaIdInexistente() {
        when(produtorRepository.findById(9L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(9L));
    }
}

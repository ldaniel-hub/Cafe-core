package br.edu.graosapi.service;

import br.edu.graosapi.dto.MovimentacaoRequest;
import br.edu.graosapi.dto.TipoMovimentacao;
import br.edu.graosapi.model.Lote;
import br.edu.graosapi.repository.LoteRepository;
import br.edu.graosapi.repository.ProdutorRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoteServiceTest {
    @Mock LoteRepository loteRepository;
    @Mock ProdutorRepository produtorRepository;
    @InjectMocks LoteService service;

    @Test
    void deveRegistrarEntradaESaidaDeEstoque() {
        Lote lote = new Lote();
        lote.setQuantidadeKg(new BigDecimal("100.000"));
        when(loteRepository.findById(1L)).thenReturn(Optional.of(lote));
        when(loteRepository.save(any(Lote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        service.movimentar(1L, new MovimentacaoRequest(TipoMovimentacao.ENTRADA, new BigDecimal("25.000")));
        Lote resultado = service.movimentar(1L, new MovimentacaoRequest(TipoMovimentacao.SAIDA, new BigDecimal("40.000")));

        assertEquals(0, new BigDecimal("85.000").compareTo(resultado.getQuantidadeKg()));
        verify(loteRepository, org.mockito.Mockito.times(2)).save(lote);
    }

    @Test
    void naoDevePermitirSaidaMaiorQueEstoque() {
        Lote lote = new Lote();
        lote.setQuantidadeKg(new BigDecimal("10.000"));
        when(loteRepository.findById(1L)).thenReturn(Optional.of(lote));

        assertThrows(IllegalArgumentException.class, () ->
            service.movimentar(1L, new MovimentacaoRequest(TipoMovimentacao.SAIDA, new BigDecimal("11.000"))));
        verify(loteRepository, never()).save(any(Lote.class));
    }

    @Test
    void naoDevePermitirMovimentacaoNulaOuNegativa() {
        Lote lote = new Lote();
        when(loteRepository.findById(1L)).thenReturn(Optional.of(lote));

        assertThrows(IllegalArgumentException.class, () ->
            service.movimentar(1L, new MovimentacaoRequest(TipoMovimentacao.ENTRADA, BigDecimal.ZERO)));
        verify(loteRepository, never()).save(any(Lote.class));
    }
}

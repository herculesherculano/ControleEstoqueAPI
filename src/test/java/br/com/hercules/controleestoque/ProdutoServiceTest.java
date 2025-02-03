package br.com.hercules.controleestoque;

import br.com.hercules.controleestoque.model.Produto;
import br.com.hercules.controleestoque.repository.ProdutoRepository;
import br.com.hercules.controleestoque.service.ProdutoService;
import br.com.hercules.controleestoque.service.impl.ProdutoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarProduto(){
        //Dado (Given)
        Produto produto = new Produto(1L, "Violao", 10, 700);
        when(produtoRepository.save(produto)).thenReturn(produto);

        //Quando(When)
        Produto resultado = produtoService.createProduto(produto);

        //Então(Then)
        assertThat(resultado).isNotNull();
        assertThat(resultado.getDescricao()).isEqualTo("Violao");
        assertThat(resultado.getQuantidade()).isEqualTo(10);
        assertThat(resultado.getPreco()).isEqualTo(700);
        verify(produtoRepository, times(1)).save(produto);

    }

    @Test
    void deveBuscarProdutoPorId(){
        //Given
        Produto produto = new Produto(2L,"Guitarra",5,1500);
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(produto));

        //When
        Produto resultado = produtoService.produtoGetById(2L);

        //Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(2);
        assertThat(resultado.getDescricao()).isEqualTo("Guitarra");
        assertThat(resultado.getQuantidade()).isEqualTo(5);
        assertThat(resultado.getPreco()).isEqualTo(1500);

    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExistir(){
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException excecao = assertThrows(RuntimeException.class, () -> produtoService.produtoGetById(99L));

        assertThat(excecao.getMessage()).isEqualTo("Produto não encontrado!");
    }

    @Test
    void deveExcluirProdutoPorId(){
        //Given
        when(produtoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(produtoRepository).deleteById(1L);

        //when
        produtoService.deletarProduto(1L);

        //then
        verify(produtoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarProdutoInexistente(){
        //Given
        when(produtoRepository.existsById(99L)).thenReturn(false);

        //when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> produtoService.deletarProduto(99L));

        //then
        assertThat(exception.getMessage()).isEqualTo("Produto não encontrado!");

    }

    @Test
    void deveAtualizarProduto(){
        //Given
        Long id = 1L;
        Produto produtoExistente = new Produto(id, "Violao", 10, 1300);
        Produto produtoAtualizado = new Produto(id, "Guitarra", 5, 1500);
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produtoExistente));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoAtualizado);


        //when
        Produto resultado = produtoService.alterarProduto(id,produtoAtualizado);

        //then
        assertThat(resultado.getDescricao()).isEqualTo("Guitarra");
        assertThat(resultado.getQuantidade()).isEqualTo(5);
        assertThat(resultado.getPreco()).isEqualTo(1500);
        verify(produtoRepository, times(1)).save(produtoExistente);
    }


}

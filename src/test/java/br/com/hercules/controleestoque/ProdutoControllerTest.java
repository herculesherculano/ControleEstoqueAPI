package br.com.hercules.controleestoque;

import br.com.hercules.controleestoque.controller.ProdutoController;
import br.com.hercules.controleestoque.exception.ResourceNotFoundException;
import br.com.hercules.controleestoque.model.Produto;
import br.com.hercules.controleestoque.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    private Produto produto;

    @BeforeEach
    void setUp(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        produto = new Produto(1L, "Produto Teste", 10, 100);
    }

    @Test
    void deveRetornarProdutoQuandoBuscarPorId(){

        //Given
        when(produtoService.produtoGetById(1L)).thenReturn(produto);

        //when
        ResponseEntity<Produto> response = produtoController.getProdutoById(1L);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produto, response.getBody());
    }

    @Test
    void deveRetornarExcecaoQuandoBuscarPorIdInexistente(){

        //Given
        when(produtoService.produtoGetById(999L)).thenThrow(new ResourceNotFoundException("Produto não encontrado!"));

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                ()-> produtoController.getProdutoById(999L));

        //then
        assertEquals("Produto não encontrado!", exception.getMessage());

        verify(produtoService, times(1)).produtoGetById(999L);
    }

    @Test
    void deveRetornarProdutosPorNome(){
        //Given
        String descricao="Guitarra";
        Produto produto1 = new Produto(2L,"Guitarra Gibson",10,10000);
        Produto produto2 =new Produto(3L,"Guitarra Fender",10,15000);
        List<Produto> produtos;
        produtos = List.of(produto1, produto2);
        when(produtoService.produtoGetByDescricao(descricao)).thenReturn(produtos);

        //when
        ResponseEntity<List<Produto>> response = produtoController.getProdutosByDescricao(descricao);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2,response.getBody().size());
        assertTrue(response.getBody().contains(produto1));
        assertTrue(response.getBody().contains(produto2));

    }

    @Test
    void deveCriarProdutoComSucesso(){

        //Given
        when(produtoService.createProduto(any(Produto.class))).thenReturn(produto);

        //when

        ResponseEntity<Produto> response = produtoController.saveProduto(produto);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(produto, response.getBody());
        assertNotNull(response.getHeaders().getLocation());
        assertTrue(response.getHeaders().getLocation().toString().contains("/1"));
    }

    @Test
    void deveAtualizarProdutoComSucesso(){

        //Giver
        when(produtoService.atualizarProduto(1L, produto)).thenReturn(produto);

        //when
        ResponseEntity<Produto> response = produtoController.atualizarProduto(1L, produto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(produto.getId(), response.getBody().getId());
        assertEquals(produto.getDescricao(), response.getBody().getDescricao());
        assertEquals(produto.getQuantidade(), response.getBody().getQuantidade());
        assertEquals(produto.getPreco(), response.getBody().getPreco());

        verify(produtoService, times(1)).atualizarProduto(1L, produto);

    }

    @Test
    void deveRetornarExcecaoQuandoAtualizarProdutoInexistente(){

        //Given
        when(produtoService.atualizarProduto(999L, produto)).thenThrow(new ResourceNotFoundException("Produto não encontrado!"));

        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                ()-> produtoController.atualizarProduto(999L, produto));

        //then
        assertEquals("Produto não encontrado!", exception.getMessage());

        verify(produtoService, times(1)).atualizarProduto(999L, produto);
    }

    @Test
    void deveDeletarProdutoComSucesso(){

        ResponseEntity<Produto> response = produtoController.deletarProduto(1L);

        assertEquals(HttpStatus.NO_CONTENT,response.getStatusCode());
        verify(produtoService,times(1)).deletarProduto(1L);
    }

    @Test
    void deveRetornarExcecaoQuandoDeletarPorIdInexistente(){

        doThrow(new ResourceNotFoundException("Produto não encontrado!"))
                .when(produtoService).deletarProduto(999L);
        //when
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                ()-> produtoController.deletarProduto(999L));

        //then
        assertEquals("Produto não encontrado!", exception.getMessage());

        verify(produtoService, times(1)).deletarProduto(999L);
    }

}

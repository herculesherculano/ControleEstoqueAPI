package br.com.hercules.controleestoque.controller;

import br.com.hercules.controleestoque.model.Produto;
import br.com.hercules.controleestoque.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.Servlet;
import jdk.jfr.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "Endpoints para gerenciar produtos")
public class ProdutoController {

    private final ProdutoService produtoService;


    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista de produtos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Produtos",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Produto.class)))
    })
    @GetMapping
    public ResponseEntity<List<Produto>> getProdutos(){
        var listaProdutos = produtoService.findAllProdutos();
        return ResponseEntity.ok(listaProdutos);
    }
    @Operation(summary = "Buscar um produto pelo ID", description = "Retorna os detalhes de um produto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado!",
                content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado!")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(Long id){
        var produto = produtoService.produtoGetById(id);
        return ResponseEntity.ok(produto);
    }

    @Operation(summary = "Buscar produtos por descrição", description = "Retorna uma lista de produtos a partir de sua descrição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de Produtos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class)))
    })
    @GetMapping("/descricao")
    public ResponseEntity<List<Produto>>getProdutosByDescricao(@RequestParam String descricao){
        var listaProdutosDescricao = produtoService.produtoGetByDescricao(descricao);
        return ResponseEntity.ok(listaProdutosDescricao);
    }

    @Operation(summary = "Criar um novo produto", description = "Cria um novo produto e retorna os dados do produto criado")
    @ApiResponses(value = {
            @ApiResponse(responseCode ="201", description = "Produto criado com sucesso",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping
    public ResponseEntity<Produto> saveProduto(@RequestBody Produto produtoToCreate){
        var produtoCreated = produtoService.createProduto(produtoToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(produtoCreated);
    }

    @Operation(summary = "Atualizar produto", description = "Atualiza um produto existentes e retorna os detalhes do produto atualizado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado!"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto){
        return ResponseEntity.ok(produtoService.atualizarProduto(id, produto));
    }

    @Operation(summary = "Deletar produto", description = "Deleta um produto a partir de sua ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Produto.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Produto> deletarProduto(@PathVariable Long id){
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}

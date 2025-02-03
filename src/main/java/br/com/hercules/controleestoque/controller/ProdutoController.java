package br.com.hercules.controleestoque.controller;

import br.com.hercules.controleestoque.model.Produto;
import br.com.hercules.controleestoque.service.ProdutoService;
import jakarta.servlet.Servlet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;


    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getProdutos(){
        var listaProdutos = produtoService.findAllProdutos();
        return ResponseEntity.ok(listaProdutos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoById(Long id){
        var produto = produtoService.produtoGetById(id);
        return ResponseEntity.ok(produto);
    }

    @PostMapping
    public ResponseEntity<Produto> saveProduto(@RequestBody Produto produtoToCreate){
        var produtoCreated = produtoService.createProduto(produtoToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoCreated.getId())
                .toUri();
        return ResponseEntity.created(location).body(produtoCreated);
    }

    @PutMapping
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto produto){
        return ResponseEntity.ok(produtoService.alterarProduto(id, produto));
    }

    @DeleteMapping
    public ResponseEntity<Produto> deletarProduto(@PathVariable Long id){
        produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}

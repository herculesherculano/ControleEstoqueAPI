package br.com.hercules.controleestoque.service.impl;

import br.com.hercules.controleestoque.model.Produto;
import br.com.hercules.controleestoque.repository.ProdutoRepository;
import br.com.hercules.controleestoque.service.ProdutoService;

import java.util.List;
import java.util.Optional;

public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto produtoGetById(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado!"));
    }

    @Override
    public List<Produto> findAllProdutos() {
        return produtoRepository.findAll();
    }

    @Override
    public Produto createProduto(Produto produtoToCreate) {
        return produtoRepository.save(produtoToCreate);
    }

    @Override
    public List<Produto> produtoGetByDescricao(String descricao) {
        return produtoRepository.findByDescricaoContainingIgnoreCase(descricao);
    }

    @Override
    public Produto alterarProduto(Long id, Produto produtoAtualizado) {
        Produto produto = produtoGetById(id);
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setQuantidade(produtoAtualizado.getQuantidade());
        produto.setPreco(produtoAtualizado.getPreco());
        return produtoRepository.save(produto);

    }

    @Override
    public void deleteProduto(Long id) {
        produtoRepository.deleteById(id);
    }
}

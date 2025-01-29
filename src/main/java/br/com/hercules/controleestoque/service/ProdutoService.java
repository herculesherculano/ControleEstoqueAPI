package br.com.hercules.controleestoque.service;

import br.com.hercules.controleestoque.model.Produto;

import java.util.List;

public interface ProdutoService {


    public Produto produtoGetById(Long id);

    public List<Produto> findAllProdutos();

    public Produto createProduto(Produto produtoToCreate);

    public List<Produto> produtoGetByDescricao(String descricao);

    public Produto alterarProduto(Long id, Produto produtoAtualizado);

    public void deleteProduto(Long id);



}

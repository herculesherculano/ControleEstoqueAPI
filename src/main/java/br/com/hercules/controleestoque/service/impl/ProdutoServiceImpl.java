package br.com.hercules.controleestoque.service.impl;

import br.com.hercules.controleestoque.model.Produto;
import br.com.hercules.controleestoque.repository.ProdutoRepository;
import br.com.hercules.controleestoque.service.ProdutoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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
    public Produto atualizarProduto(Long id, Produto produtoAtualizado) {
        Produto produtoExistente = produtoRepository.findById(id).orElseThrow( () -> new RuntimeException("Produto não encontrado!"));
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        return produtoRepository.save(produtoExistente);

    }

    @Override
    public void deletarProduto(Long id) {
        if(!produtoRepository.existsById(id)){
            throw new RuntimeException("Produto não encontrado!");
        }
        produtoRepository.deleteById(id);
    }
}

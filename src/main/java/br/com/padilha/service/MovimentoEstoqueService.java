package br.com.padilha.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.padilha.model.MovimentoEstoque;
import br.com.padilha.model.Produto;
import br.com.padilha.model.enums.TipoMovimentacao;
import br.com.padilha.respository.MovimentoEstoqueRepository;
import br.com.padilha.respository.ProdutoRepository;

@Service
public class MovimentoEstoqueService {

	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	public boolean estoqueValidado(MovimentoEstoque movimentoEstoque) {
		Optional<Produto> produto = produtoRepository.findById(movimentoEstoque.getProduto().getId());
		movimentoEstoque.setProduto(produto.get());
		Integer quantidadeVenda = movimentoEstoque.getQuantidadeVenda();
		Integer quantidadeEstoque = produto.get().getQuantidadeEstoque();
		
		return (quantidadeVenda < quantidadeEstoque);
	}
	
	public MovimentoEstoque registrarMovimentacao(MovimentoEstoque movimentoEstoque) {
		MovimentoEstoque movimentoEfetivado = movimentoEstoqueRepository.save(movimentoEstoque);
		atualizarEstoque(movimentoEstoque);
		
		return movimentoEfetivado;
	}
	
	private void atualizarEstoque(MovimentoEstoque movimentoEstoque) {
		Produto produto = movimentoEstoque.getProduto();
		Integer saldoAtual = produto.getQuantidadeEstoque();
		Integer quantidadeMovimentacao = movimentoEstoque.getQuantidadeVenda();
		TipoMovimentacao tipoMovimentacao = movimentoEstoque.getTipoMovimentacao();
		
		if (tipoMovimentacao.equals(TipoMovimentacao.ENTRADA)) {
			produto.setQuantidadeEstoque(saldoAtual + quantidadeMovimentacao);  
		} else {
			produto.setQuantidadeEstoque(saldoAtual - quantidadeMovimentacao);
		}

		produtoRepository.save(produto);
	}
}
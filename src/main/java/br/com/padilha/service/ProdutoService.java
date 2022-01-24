package br.com.padilha.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.padilha.dto.ProdutoLucroDTO;
import br.com.padilha.dto.ProdutoTipoDTO;
import br.com.padilha.model.MovimentoEstoque;
import br.com.padilha.model.Produto;
import br.com.padilha.model.enums.TipoProduto;
import br.com.padilha.respository.MovimentoEstoqueRepository;
import br.com.padilha.respository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private MovimentoEstoqueRepository movimentoEstoqueRepository;
	
	// Salva se for novo ou atualiza se já existir o registro
	public Produto salvar(Produto produto) {
		return produtoRepository.save(produto);
	}
	
	public List<Produto> listarProduto(){
		return produtoRepository.findAll();
	}
	
	public Optional<Produto> buscarProduto(Long id){
		return produtoRepository.findById(id);
	}
	
	public void removerProduto(Long id) {
		produtoRepository.deleteById(id);
	}
	
	public List<ProdutoTipoDTO> listarProdutoTipo(TipoProduto tipoProduto){
		return produtoRepository.findProdutoTipo(tipoProduto).stream().
				map(this::converterProdutoTipoDTO).collect(Collectors.toList());
	}
	
	private ProdutoTipoDTO converterProdutoTipoDTO(Produto produto){
		ProdutoTipoDTO produtoDTO = new ProdutoTipoDTO();
		produtoDTO.setCodigo(produto.getCodigo());
		produtoDTO.setDescricao(produto.getDescricao());
		produtoDTO.setTipoProduto(produto.getTipoProduto());
		produtoDTO.setQuantidadeDisponivel(produto.getQuantidadeEstoque());
		produtoDTO.setQuantidadeSaida(quantidadeVendas(produto));

		return produtoDTO;
	}
	
	private int quantidadeVendas(Produto produto) {
		List<MovimentoEstoque> movimentacoesVenda = movimentoEstoqueRepository.findVendasProduto(produto);
		return movimentacoesVenda.stream().map(movimento -> movimento.getQuantidadeVenda()).
				collect(Collectors.summingInt(Integer::intValue));
	}
	
	public ProdutoLucroDTO calcularLucroProduto(Long idProduto) {
		Optional<Produto> produto = buscarProduto(idProduto);
		return converterProdutoLucroDTO(produto.get());
	}

	private double valorLucroVendas(Produto produto) {
		List<MovimentoEstoque> movimentacoesVenda = movimentoEstoqueRepository.findVendasProduto(produto);
		double valorFornecedorProduto = produto.getValorFornecedor();
		double lucro = 0.0;

		for (MovimentoEstoque movimentacao : movimentacoesVenda) {
			lucro += (movimentacao.getValorVenda() - valorFornecedorProduto)*movimentacao.getQuantidadeVenda();
		}

		return lucro;
	}
	
	private ProdutoLucroDTO converterProdutoLucroDTO(Produto produto) {
		ProdutoLucroDTO produtoLucroDTO = new ProdutoLucroDTO();
		produtoLucroDTO.setQuantidadeTotalSaida(quantidadeVendas(produto));
		produtoLucroDTO.setLucro(valorLucroVendas(produto));
		return produtoLucroDTO;
	}
	
}

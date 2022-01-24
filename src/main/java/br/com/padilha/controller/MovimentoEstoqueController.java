package br.com.padilha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.padilha.model.MovimentoEstoque;
import br.com.padilha.model.enums.TipoMovimentacao;
import br.com.padilha.service.MovimentoEstoqueService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/movimento")
public class MovimentoEstoqueController {
	
	@Autowired
	private MovimentoEstoqueService movimentoEstoqueService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Registrar uma transação de venda ou entrada de produtos")
	public MovimentoEstoque registrarMovimentacao(@RequestBody MovimentoEstoque movimentoEstoque ) {
		if (movimentoEstoque.getTipoMovimentacao() == TipoMovimentacao.SAIDA &&
				!movimentoEstoqueService.estoqueValidado(movimentoEstoque)) {
			throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Venda não efetivada, Produto não tem estoque suficiente.");
		}
		return movimentoEstoqueService.registrarMovimentacao(movimentoEstoque);
	}
	
}

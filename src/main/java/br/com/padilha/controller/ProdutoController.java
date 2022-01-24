package br.com.padilha.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.padilha.dto.ProdutoLucroDTO;
import br.com.padilha.dto.ProdutoTipoDTO;
import br.com.padilha.model.Produto;
import br.com.padilha.model.enums.TipoProduto;
import br.com.padilha.service.ProdutoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
    @Autowired
    private ModelMapper modelMapper;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Salvar um produto")
	@ApiResponses({
		@ApiResponse(code = 201, message = "Produto criado com sucesso."),
		@ApiResponse(code = 401, message = "Falha na criação.")
	})
	public Produto salvar(@RequestBody Produto produto) {
		return produtoService.salvar(produto);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<Produto> listarProduto(){
		Iterable<Produto> listaProdutos = produtoService.listarProduto();
		List<Produto> produtos = new ArrayList<Produto>();
		for (Produto produto : listaProdutos) {
			long id = produto.getId();
			Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).buscarProduto(id)).withSelfRel();
			produto.add(link);
			produtos.add(produto);
		}

		return produtos;
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public Produto buscarProduto(@PathVariable("id") long id) {
		Produto produto = produtoService.buscarProduto(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).listarProduto()).withRel("Lista de Produtos");
		produto.add(link);
		return produto;
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void removerProduto(@PathVariable("id") long id) {
		produtoService.buscarProduto(id)
			.map(produto -> {
				produtoService.removerProduto(produto.getId());
				return Void.TYPE;
			}).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
	}
	
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarProduto(@PathVariable("id") Long id, @RequestBody Produto produto){
        produtoService.buscarProduto(id)
                .map(produtoBase -> {
                    modelMapper.map(produto, produtoBase);
                    produtoService.salvar(produtoBase);
                    return Void.TYPE;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado."));
    }
	
	@GetMapping("/listarPeloTipo/{tipoProduto}")
	@ResponseStatus(HttpStatus.OK)
	public List<ProdutoTipoDTO> listarProdutoTipo(@PathVariable("tipoProduto") TipoProduto tipoProduto){
		return produtoService.listarProdutoTipo(tipoProduto);
	}
	
	@GetMapping("/lucro/{idProduto}")
	@ResponseStatus(HttpStatus.OK)
	public ProdutoLucroDTO calcularLucroProduto(@PathVariable("idProduto") Long idProduto){
		return produtoService.calcularLucroProduto(idProduto);
	}
	
}

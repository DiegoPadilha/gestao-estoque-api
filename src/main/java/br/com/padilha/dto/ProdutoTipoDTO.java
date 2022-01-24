package br.com.padilha.dto;

import br.com.padilha.model.enums.TipoProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoTipoDTO {
	
	private String codigo;
	private String descricao;
	private TipoProduto tipoProduto;
	private int quantidadeSaida;
	private int quantidadeDisponivel;

}

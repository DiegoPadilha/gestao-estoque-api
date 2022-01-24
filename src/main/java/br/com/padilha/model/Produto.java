package br.com.padilha.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.padilha.model.enums.TipoProduto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Produto extends RepresentationModel<Produto> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5282157802720597627L;

	@Id
	@GeneratedValue
	@JsonIgnore
	private Long id;
	
	@Column(name = "codigo", nullable  = false)
	private String codigo;
	
	@Column(name = "descricao", nullable = false)
	private String descricao;
	
	@Column(name = "tipoProduto", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto;
	
	@Column(name = "valorFornecedor", nullable = false)
	private Double valorFornecedor;
	
	@ApiModelProperty(value = "Quantidade em estoque")
	@Column(name = "quantidadeEstoque", nullable = true)
	private Integer quantidadeEstoque;
	
	@JsonIgnore
	@OneToMany(mappedBy = "produto")
	private List<MovimentoEstoque> movimentosEstoque = new ArrayList<>();

}

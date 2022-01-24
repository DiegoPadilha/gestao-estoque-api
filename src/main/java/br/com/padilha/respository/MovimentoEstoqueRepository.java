package br.com.padilha.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.padilha.model.MovimentoEstoque;
import br.com.padilha.model.Produto;

@Repository
public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {

	@Query("SELECT me FROM MovimentoEstoque me WHERE me.produto = ?1 and me.tipoMovimentacao = br.com.padilha.model.enums.TipoMovimentacao.SAIDA") 
	List<MovimentoEstoque> findVendasProduto(Produto produto);
	
}

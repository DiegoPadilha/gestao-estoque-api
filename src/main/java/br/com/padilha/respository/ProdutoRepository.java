package br.com.padilha.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.padilha.model.Produto;
import br.com.padilha.model.enums.TipoProduto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query("SELECT p FROM Produto p WHERE p.tipoProduto = ?1") 
	List<Produto> findProdutoTipo(TipoProduto tipoProduto);
	
}
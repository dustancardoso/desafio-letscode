package com.desafioletscode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafioletscode.model.Jogador;
import com.desafioletscode.model.JogadorRanking;

@Repository
public interface JogadorRepository extends JpaRepository<Jogador, Long>{
	
	public Optional<Jogador> findByLogin(String login);

	List<JogadorRanking> findAllProjectedBy(Sort sort);
}

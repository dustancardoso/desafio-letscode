package com.desafioletscode.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafioletscode.model.RodadaJogo;

@Repository
public interface RodadaJogoRepository extends JpaRepository<RodadaJogo, Long>{
	
	public List<RodadaJogo> findByLoginJogador(String loginJogador);
	
	@Query(value = "SELECT r FROM RodadaJogo r WHERE r.loginJogador = :loginJogador AND r.acerto is null")
	public List<RodadaJogo> findRodadasAbertasByLoginJogador(@Param("loginJogador") String loginJogador);
	
}

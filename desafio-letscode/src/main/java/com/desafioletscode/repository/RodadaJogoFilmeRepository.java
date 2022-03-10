package com.desafioletscode.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desafioletscode.model.RodadaJogoFilme;

@Repository
public interface RodadaJogoFilmeRepository extends JpaRepository<RodadaJogoFilme, Long>{
	
}

package com.desafioletscode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.desafioletscode.model.Jogador;
import com.desafioletscode.service.JogadorService;

@SpringBootApplication
public class DesafioLetscodeApplication implements CommandLineRunner {
	

	
	public static void main(String[] args) {
		
		SpringApplication.run(DesafioLetscodeApplication.class, args);
		
	}
	
	@Autowired
	JogadorService jogadorService;
	
	@Override
	public void run(String... args) throws Exception {
		
		try {
			jogadorService.cadastrarJogador(new Jogador("ADMIN", "admin", "admin"));		
		} catch (Exception e) {
			//Tentativa de criação sem tratamento de erro
		}
		try {
			jogadorService.cadastrarJogador(new Jogador("ADMIN2", "admin2", "admin2"));		
		} catch (Exception e) {
			//Tentativa de criação sem tratamento de erro
		}
		
	}

}

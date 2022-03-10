package com.desafioletscode.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.desafioletscode.erros.ErroInternoException;
import com.desafioletscode.erros.MensagensApi;
import com.desafioletscode.model.Jogador;
import com.desafioletscode.model.JogadorRanking;
import com.desafioletscode.service.JogadorService;

@RestController
@RequestMapping("/apijogo/jogador")
public class JogadorRestController {
	@Autowired
	JogadorService jogadorService;
	
	@PostMapping("/cadastrarJogador")
	@ResponseBody
	public ResponseEntity<MensagensApi> cadastrarJogador(@RequestBody @Valid Jogador jogador) throws ErroInternoException {
			jogadorService.cadastrarJogador(jogador);
			return MensagensApi.getSucesso("Jogador login '"+jogador.getLogin()+"' cadastrado com sucesso.");
	}
	
	@GetMapping("/listarRankingJogadores")
	@ResponseBody
	public ResponseEntity<List<JogadorRanking>> listarRanking() throws ErroInternoException {
	    return  ResponseEntity.ok(jogadorService.listarRanking());
	}
	
}

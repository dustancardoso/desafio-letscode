package com.desafioletscode.service;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.desafioletscode.erros.ErroInternoException;
import com.desafioletscode.model.Jogador;
import com.desafioletscode.model.JogadorRanking;
import com.desafioletscode.repository.JogadorRepository;

@Service
public class JogadorService {
	
	@Autowired
	JogadorRepository jogadorRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
		
	public List<JogadorRanking> listarRanking() throws ErroInternoException{
		try {
			return jogadorRepository.findAllProjectedBy(Sort.by(Sort.Direction.DESC, "pontosAcumulados"));
		} catch (Exception e) {
			throw new ErroInternoException("Erro ao listar jogadores: "+e.getMessage());
		}		
	}
	
	public Jogador cadastrarJogador(Jogador jogador) throws ErroInternoException{
		try {
			if(jogadorRepository.findByLogin(jogador.getLogin()).isPresent())
			throw new ErroInternoException("Já existe jogador com o login escolhido. Escolha outro login");		
			jogador.setSenha(passwordEncoder.encode(jogador.getSenha()));
			jogador.setPontosAcumulados(null);//Evita fraude nos pontos acumulados
			return jogadorRepository.save(jogador);
		}catch(ErroInternoException e) {
			throw e;
		}catch(Exception e) {
			throw new ErroInternoException(e.getMessage());
		}
	}
}
package com.desafioletscode.service;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.desafioletscode.erros.ErroInternoException;
import com.desafioletscode.model.RodadaJogoFilme;
import com.desafioletscode.model.Jogador;
import com.desafioletscode.model.RodadaJogo;
import com.desafioletscode.repository.JogadorRepository;
import com.desafioletscode.repository.RodadaJogoRepository;

@Service
public class RodadaJogoService {
	
	@Autowired
	RodadaJogoRepository rodadaJogoRepository;
	
	@Autowired
	JogadorRepository jogadorRepository;
	
	@Autowired
	ConsumerIMDBService consumerIMDBService;

	private int NUMERO_MAXIMO_ERROS=3;
	
	public RodadaJogo gerarRodada() 
			throws ErroInternoException {
			try {
				RodadaJogo rodada = new RodadaJogo();
				rodada.setDataPartida(new Date());
				for(RodadaJogoFilme filme : consumerIMDBService.obterFilmesDistintos())
					rodada.addFilme(filme);
				return rodada;
			}catch(Exception e) {
				throw new ErroInternoException(e.getMessage());
			}
	}
	
	private boolean verificaAcerto(List<RodadaJogoFilme> filmesDaRodada, RodadaJogoFilme filmeEscolhido) {
		boolean acertou = true;
		for(RodadaJogoFilme filme : filmesDaRodada) {
			if(!filme.equals(filmeEscolhido) 
					&& filme.calcularNota()>filmeEscolhido.calcularNota())
					acertou = false;
		}	
		return acertou;
	}
	
	@Transactional(rollbackOn=Exception.class)
	public RodadaJogo salvarRodadaEmAberto(RodadaJogo rodada, String loginJogador) throws ErroInternoException {
		try {
			rodada.setLoginJogador(loginJogador);
			return rodadaJogoRepository.save(rodada);
		} catch (Exception e) {
			throw new ErroInternoException("Erro ao cadastrar a rodada.");
		}		
	}
	@Transactional(rollbackOn=Exception.class)
	public RodadaJogo salvarResultadoRodada(RodadaJogo rodada, RodadaJogoFilme filmeEscolhido) throws ErroInternoException {
		try {
			boolean acertou = verificaAcerto(rodada.getFilmesRodada(), filmeEscolhido);
			rodada.setAcerto(acertou);
			rodada.setIdFilmeEscolhido(filmeEscolhido.getImdbID());
			rodada.setPontos(acertou?1:0);
			atualizaPontosAcumuladosJogador(rodada);
			return rodadaJogoRepository.save(rodada);
			
		} catch (ErroInternoException e) {
			throw e;
		} catch (Exception e) {
			throw new ErroInternoException("Erro ao cadastrar a rodada.");
		}		
	}
	
	@Transactional(rollbackOn=Exception.class)
	private void atualizaPontosAcumuladosJogador(RodadaJogo rodada) throws ErroInternoException {
		try {
			Optional<Jogador> jogadorRodada = jogadorRepository.findByLogin(rodada.getLoginJogador());
			Integer pontosAcumulados = jogadorRodada.get().getPontosAcumulados();
			pontosAcumulados = (pontosAcumulados==null)? rodada.getPontos() :
									pontosAcumulados + rodada.getPontos();
			jogadorRodada.get().setPontosAcumulados(pontosAcumulados);
			jogadorRepository.save(jogadorRodada.get());		
		} catch (Exception e) {
			throw new ErroInternoException("Erro ao atualizar dados do jogador");			
		}
	}
	
	private int countErrosJogador(List<RodadaJogo> rodadasUsuario) {
		int quantidadeErros = 0;
		for(RodadaJogo rodada: rodadasUsuario)
			if(!rodada.getAcerto())
				quantidadeErros++;
		return quantidadeErros;
	}
	
	public boolean validarFilmesParaJogador(Collection<RodadaJogoFilme> filmesCandidatos, String login) 
			throws ErroInternoException {
	    try {
			if(filmesCandidatos.isEmpty()) return false;
	    	List<RodadaJogo> rodadasUsuario = rodadaJogoRepository.findByLoginJogador(login);
	    	if(rodadasUsuario.isEmpty()) return true; //Somente chega se 
	    	if(countErrosJogador(rodadasUsuario)>= this.NUMERO_MAXIMO_ERROS)
	    		throw new ErroInternoException("Jogador já atingiu número máximo de erros("+NUMERO_MAXIMO_ERROS+")");
    		
	    	boolean algumaRodadaContemTodos = false;
    		for(RodadaJogo rodada: rodadasUsuario) 
    			if(rodada.containsAll(filmesCandidatos)) {
    				algumaRodadaContemTodos = true;
    				break;
    			};	    
		    return !algumaRodadaContemTodos;
	    }catch(ErroInternoException e) {
	    	throw e;
	    }
	    catch(Exception e) {
	    	throw new ErroInternoException("Não foi possível validar o par de filmes para o usuário");
	    }
	}
	
	public List<RodadaJogo> listarTodasRodadas() throws ErroInternoException{
		try {
			return rodadaJogoRepository.findAll();	
		} catch (Exception e) {
			throw new ErroInternoException("Não foi possível obter lista de rodadas");
		}			
	}
	
	public List<RodadaJogo> buscarRodadaEmAberto(String loginJogador) throws ErroInternoException{
		try {
			return rodadaJogoRepository.findRodadasAbertasByLoginJogador(loginJogador);	
		} catch (Exception e) {
			throw new ErroInternoException("Não foi possível obter lista de rodadas do Jogador");
		}			
	}
	
	public String obterLoginUsuario(HttpServletRequest request) throws ErroInternoException {
		try {
			Principal principal = request.getUserPrincipal();
		    return principal.getName();
		}catch (Exception e) {
			throw new ErroInternoException("Não foi possível identificar o usuário a partir da requisição");
		}
	}
	
	

}

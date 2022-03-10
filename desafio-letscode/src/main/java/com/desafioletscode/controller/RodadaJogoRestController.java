package com.desafioletscode.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;

import com.desafioletscode.erros.ErroInternoException;
import com.desafioletscode.erros.MensagensApi;
import com.desafioletscode.model.RodadaJogo;
import com.desafioletscode.model.RodadaJogoFilme;
import com.desafioletscode.model.SelecaoFilme;
import com.desafioletscode.repository.RodadaJogoFilmeRepository;
import com.desafioletscode.security.ConfiguracaoSegurancaWeb;
import com.desafioletscode.service.ConsumerIMDBService;
import com.desafioletscode.service.RodadaJogoService;

import io.netty.handler.codec.http.HttpRequest;

@RestController
@RequestMapping("/apijogo/rodadaJogo")
public class RodadaJogoRestController {
	
	@Autowired
	RodadaJogoService rodadaJogoService;
	
	@Autowired
	TokenStore tokenStore;
	
	@Value("${security.oauth2.client.client-id}")
	private String idCliente;
	
	private RodadaJogo rodadaEmAberto;
	
	/**
	 * Retorna os filmes da rodada sem dados de rating e votos, 
	 * evitando que o jogador já tenha a resposta
	 * @return
	 */	
	private List<RodadaJogoFilme> obterFilmesRodadaParaApresentacao(){
		List<RodadaJogoFilme> filmesParaApresentacao = new ArrayList<RodadaJogoFilme>();
		for(RodadaJogoFilme filme: rodadaEmAberto.getFilmesRodada()) {
			filmesParaApresentacao.add(new RodadaJogoFilme(filme.getImdbID(), filme.getTitle(), 
					filme.getYear(), filme.getPoster(), filme.getCountry(), filme.getGenre()));
		}
		return filmesParaApresentacao;
	}
	
	/**
	 * Método para solicitar uma nova rodada do Jogo
	 * Caso a rodada não seja respondida, retornará sempre a mesma rodada.
	 * @return
	 * @throws ErroInternoException
	 */	
	@GetMapping("/obterNovaRodada")
	@ResponseBody
	public ResponseEntity<Object> obterNovaRodada(HttpServletRequest request) throws ErroInternoException {
		List<RodadaJogo> rodadasEmAbertoJogador =  rodadaJogoService.buscarRodadaEmAberto(rodadaJogoService.obterLoginUsuario(request));
		this.rodadaEmAberto = new RodadaJogo();
		if(rodadasEmAbertoJogador.isEmpty()) {
			while(!rodadaJogoService.validarFilmesParaJogador(rodadaEmAberto.getFilmesRodada(), rodadaJogoService.obterLoginUsuario(request))) {
				rodadaEmAberto =  rodadaJogoService.gerarRodada();				
			}
		}else {
			rodadaEmAberto = rodadasEmAbertoJogador.iterator().next();				
		}		
		rodadaJogoService.salvarRodadaEmAberto(rodadaEmAberto, rodadaJogoService.obterLoginUsuario(request));
		
		return  ResponseEntity.ok(obterFilmesRodadaParaApresentacao());
	}
	
	/**
	 * Valida resposta do usuário, retornando RodadaJogo com acerto ou erro
	 * Para filmes com imdbRating N/A ou imdbVotes N/A é considerada nota zero
	 * @see RodadaJogoFilme#calcularNota()
	 * @param filmes
	 * @return
	 * @throws ErroInternoException 
	 */
	@PostMapping("/submeterResposta")
	@ResponseBody
	public ResponseEntity<MensagensApi> submeterResposta(@RequestBody List<RodadaJogoFilme> filmes, HttpServletRequest request) throws ErroInternoException {
		SelecaoFilme selecaoFilme = new SelecaoFilme();	
		List<RodadaJogo> rodadasEmAberto =  rodadaJogoService.buscarRodadaEmAberto(rodadaJogoService.obterLoginUsuario(request));
		if(rodadasEmAberto.isEmpty())
			return MensagensApi.getErroNegocio("Nenhuma rodada válida. Solicite nova rodada do jogo");
		else 
			this.rodadaEmAberto = rodadasEmAberto.iterator().next();
		
		verificarSelecaoDeFilme(filmes, selecaoFilme);
		
		Optional<RodadaJogoFilme> filmeEscolhido = this.rodadaEmAberto.buscarFilme(selecaoFilme.getIdIMDB());
		if(filmeEscolhido.isEmpty())
			return MensagensApi.getErroNegocio("Filme escolhido não pertence a rodada atual. Verifique.");
		
		rodadaJogoService.salvarResultadoRodada(rodadaEmAberto, filmeEscolhido.get());
		
		return MensagensApi.getSucesso("Jogador "+(this.rodadaEmAberto.getAcerto()?"acertou" : "errou"));

	}

	private void verificarSelecaoDeFilme(List<RodadaJogoFilme> filmes, SelecaoFilme selecaoFilme) 
			throws ErroInternoException {
		try {
			for(RodadaJogoFilme filme : filmes) {
				if(filme.isSelected()) {
					selecaoFilme.setQuantidadeSelecionada(selecaoFilme.getQuantidadeSelecionada()+1);
					selecaoFilme.setIdIMDB(Optional.ofNullable(filme.getImdbID()));
				}				
			}
			if(!selecaoFilme.isSelecaoUnicaCompleta())
				throw new ErroInternoException("É necessário selecionar um dos filmes (somente um). Utilize o campo 'selected'");

		}catch (ErroInternoException e) {
			throw e;			
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new ErroInternoException("Erro na solitação. Verifique formato da resposta.");			
		}
	}	


	@GetMapping("/sairDoJogo")
	public ResponseEntity<MensagensApi> sairDoJogo(HttpServletRequest request) throws ErroInternoException{
		if(!rodadaJogoService.buscarRodadaEmAberto(rodadaJogoService.obterLoginUsuario(request)).isEmpty())
			return MensagensApi.getErroNegocio("É necessário responder a última rodada para sair do jogo.");
		for(OAuth2AccessToken tokenUsuario : tokenStore.findTokensByClientIdAndUserName(idCliente, rodadaJogoService.obterLoginUsuario(request)))
			tokenStore.removeAccessToken(tokenUsuario);
		return MensagensApi.getSucesso("Saída do jogo realizada com sucesso.");			
	}
	

	
	
	
}

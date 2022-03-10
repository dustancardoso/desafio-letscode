package com.desafioletscode.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import com.desafioletscode.erros.ErroInternoException;
import com.desafioletscode.model.RodadaJogoFilme;

import reactor.core.publisher.Mono;


@Service
public class ConsumerIMDBService{
	
	private static String BASE_URL = "https://www.omdbapi.com/";
	private static String API_KEY = "33c5f3b1";
	
	private static WebClient gerarClientIMDB() {
		return WebClient.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	private static UriBuilder gerarUriIMDB() {
		DefaultUriBuilderFactory uri = new DefaultUriBuilderFactory(BASE_URL);
		return uri.builder().queryParam("apikey", API_KEY);
	}
	/**
	 * Método para retornar os dados do filme na API IMDB
	 * @param imdbID
	 * @return
	 */
	public RodadaJogoFilme obterDadosRodadaJogoFilme(String imdbID) {
		RodadaJogoFilme filme = null;
		try {
			Mono<RodadaJogoFilme> monoFilme = gerarClientIMDB().method(HttpMethod.GET)
					.uri(gerarUriIMDB().queryParam("i", imdbID).build())
					.retrieve().bodyToMono(RodadaJogoFilme.class);
			
			filme  = monoFilme.block();
		}catch(Exception e) {
			return RodadaJogoFilme.getFilmeErro(Boolean.FALSE, "Unexpected error during access to IMBD");
		}
		
		return filme;		

	}
	private String formatarIdIMDB(int id) {
		String idStr = Integer.valueOf(id).toString();
		while(idStr.length()<7)
			idStr = "0"+idStr;
		idStr = "tt"+idStr;
		return idStr;
	}
	private int extrairIdIMDB(String idStr) {
		String str = idStr.replace("tt", ""); 
		return Integer.valueOf(str);
		
	}
	
	
	public RodadaJogoFilme obterFilmeAleatorio(Integer exceto) throws ErroInternoException {
		try {
			RodadaJogoFilme filme = RodadaJogoFilme.getFilmeErro(Boolean.FALSE, " ");
			int tentativas = 1;			
			// Habilitar somente tipo "movie"??
			while((!filme.getResponse() || filme.calcularNota()==0)	&& tentativas < 30) {			
					Random r = new Random();
					//Numero de ID observado nos limites mais altos
					int candidatoId = r.nextInt(9909998); 
					while(exceto!=null && candidatoId == exceto.intValue())
						candidatoId = r.nextInt(9909998); 
					filme =obterDadosRodadaJogoFilme(formatarIdIMDB(candidatoId));
			}
			return filme;
		}catch(Exception e) {
			throw new ErroInternoException("Não foi possível obter filme no serviço IMDB");
		}
	}
	
	public List<RodadaJogoFilme> obterFilmesDistintos() throws ErroInternoException{
		List<RodadaJogoFilme> filmesDaRodada = new ArrayList<RodadaJogoFilme>();
		RodadaJogoFilme filme1 = obterFilmeAleatorio(null);
		RodadaJogoFilme filme2 = obterFilmeAleatorio(extrairIdIMDB(filme1.getImdbID()));
			filmesDaRodada.add(filme1);
			filmesDaRodada.add(filme2);
		return filmesDaRodada;		
	}
	


}

package com.desafioletscode.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.desafioletscode.controller.JogadorRestController;
import com.desafioletscode.erros.ErroInternoException;
import com.desafioletscode.erros.MensagensApi;
import com.desafioletscode.model.Jogador;
import com.desafioletscode.model.JogadorRanking;
import com.desafioletscode.service.JogadorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(JogadorRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "test")
public class JogadorRestControllerTest{
	

	@MockBean
	JogadorService jogadorService;
	
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
	ObjectMapper mapper;	

	@Test
	public void testCadastrarJogador_Sucesso() throws Exception {
		Jogador jogador = new Jogador();
		jogador.setNome("Jogador Teste");
		jogador.setLogin("jogadorTeste");
		jogador.setSenha("senhaTeste");
		
		when(this.jogadorService.cadastrarJogador(jogador)).thenReturn(jogador);
		
		mockMvc.perform(post("/apijogo/jogador/cadastrarJogador")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(jogador)))
	            .andExpect(status().is(MensagensApi.getMensagemSucesso().getStatus()));
		
	}
	
	@Test
	public void testCadastrarJogador_ErroInterno() throws Exception {
		
		Jogador jogador = new Jogador();
		jogador.setNome("Jogador Teste");
		jogador.setLogin("jogadorTeste");
		jogador.setSenha("senhaTeste");
		
		when(this.jogadorService.cadastrarJogador(any(Jogador.class))).thenThrow(ErroInternoException.class);
		
		mockMvc.perform(post("/apijogo/jogador/cadastrarJogador")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(jogador)))
	            .andExpect(status().is(MensagensApi.getMensagemErroInterno().getStatus()));		
	}
	
	@Test
	public void testCadastrarJogador_ErroNegocio() throws Exception {
		
		Jogador jogador = new Jogador();
		jogador.setNome("Jogador Teste");
		jogador.setLogin("");
		jogador.setSenha("");
		
		when(this.jogadorService.cadastrarJogador(any(Jogador.class))).thenThrow(ErroInternoException.class);
		
		mockMvc.perform(post("/apijogo/jogador/cadastrarJogador")
				.contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(jogador)))
	            .andExpect(status().is(MensagensApi.getMensagemErroNegocio().getStatus()));		
	}
	
	@Test
	public void testListarRanking_Sucesso() throws Exception {
		List<JogadorRanking> lista = new ArrayList<JogadorRanking>();
		
		when(this.jogadorService.listarRanking()).thenReturn(lista);
		
		mockMvc.perform(get("/apijogo/jogador/listarRankingJogadores"))		
				.andExpect(status().is(MensagensApi.getMensagemSucesso().getStatus()));	
	}
	
	@Test
	public void testListarRanking_ErroInterno() throws Exception {
		
		when(this.jogadorService.listarRanking()).thenThrow(ErroInternoException.class);
		
		mockMvc.perform(get("/apijogo/jogador/listarRankingJogadores"))		
				.andExpect(status().is(MensagensApi.getErroInterno().getStatusCodeValue()));	
	}


}

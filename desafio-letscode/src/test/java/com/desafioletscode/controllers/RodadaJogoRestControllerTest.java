package com.desafioletscode.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.desafioletscode.controller.RodadaJogoRestController;
import com.desafioletscode.erros.MensagensApi;
import com.desafioletscode.model.RodadaJogo;
import com.desafioletscode.service.JogadorService;
import com.desafioletscode.service.RodadaJogoService;
import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(RodadaJogoRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "test")
public class RodadaJogoRestControllerTest{
	

	@MockBean
	RodadaJogoService rodadaJogoService;
	
	@MockBean
	JogadorService jogadorService;
	
	@MockBean
	TokenStore tokenStore;
	

	@Autowired
    private MockMvc mockMvc;
    
    @Autowired
	ObjectMapper mapper;
    
	@Spy
	private List<RodadaJogo> rodadas = new ArrayList<RodadaJogo>();
	

	@Test
	public void testObterNovaRodada_JaExiste() throws Exception {
		this.rodadas = new ArrayList<RodadaJogo>();		
		RodadaJogo rodada = new RodadaJogo();
		this.rodadas.add(rodada);
		
		String login = new String("login");
		
		when(this.rodadaJogoService.obterLoginUsuario(any(HttpServletRequest.class))).thenReturn(login);
				
		when(this.rodadaJogoService.buscarRodadaEmAberto(login)).thenReturn(rodadas);
		
		when(this.rodadaJogoService.salvarRodadaEmAberto(rodada, login)).thenReturn(rodada);
		
		mockMvc.perform(get("/apijogo/rodadaJogo/obterNovaRodada"))		
		.andExpect(status().is(MensagensApi.getMensagemSucesso().getStatus()));	
		
	}
	@Test
	public void testObterNovaRodada_NaoExiste() throws Exception {
		
		this.rodadas = new ArrayList<RodadaJogo>();		
		
		RodadaJogo rodadaTemporaria = new RodadaJogo();
		
		RodadaJogo novaRodadaGerada = new RodadaJogo();
		
		String login = new String("login");
		
		when(this.rodadaJogoService.obterLoginUsuario(any(HttpServletRequest.class))).thenReturn(login);
		
		when(this.rodadaJogoService.buscarRodadaEmAberto(login)).thenReturn(rodadas);
		
		when(this.rodadaJogoService.salvarRodadaEmAberto(rodadaTemporaria, login)).thenReturn(rodadaTemporaria);
		
		when(this.rodadaJogoService.validarFilmesParaJogador(rodadaTemporaria.getFilmesRodada(), login)).thenReturn(false);
		
		when(this.rodadaJogoService.gerarRodada()).thenReturn(novaRodadaGerada);
		
		when(this.rodadaJogoService.validarFilmesParaJogador(novaRodadaGerada.getFilmesRodada(), login)).thenReturn(true);
	
		when(this.rodadaJogoService.salvarRodadaEmAberto(novaRodadaGerada, login)).thenReturn(novaRodadaGerada);
		
		mockMvc.perform(get("/apijogo/rodadaJogo/obterNovaRodada"))		
		.andExpect(status().is(MensagensApi.getMensagemSucesso().getStatus()));	
		
	}



}

package com.desafioletscode.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import com.desafioletscode.model.Jogador;

@Controller
@RequestMapping("/jogador")
public class JogadorViewController {
	
	private static WebClient gerarClientWebInterno() {
		return WebClient.builder()
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	private static UriBuilder gerarUriInterno() {
		DefaultUriBuilderFactory uri = new DefaultUriBuilderFactory();
		return uri.builder();
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public ModelAndView cadastrarJogador(Jogador jogador) {
		return new ModelAndView("CadastroJogador");
	    
	}
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public ModelAndView salvarJogador(@Valid Jogador jogador, BindingResult result
			, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return cadastrarJogador(jogador);
		}
		
		ModelAndView mv = new ModelAndView("redirect:/jogador/novo");
		attributes.addFlashAttribute("mensagem", "Jogador salvo com sucesso.");
		return mv;
	}
	@RequestMapping("/logout")
	public ModelAndView logout() {
	    return new ModelAndView("logout");
	}
	@RequestMapping("/login")
	public ModelAndView login() {
	    return new ModelAndView("index");
	}
	
	@RequestMapping("/erro")
	public ModelAndView erro() {
	    return new ModelAndView("erro_autenticacao");
	}

}

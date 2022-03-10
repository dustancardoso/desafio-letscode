package com.desafioletscode.erros;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerApi {

	@ExceptionHandler(ErroInternoException.class)
	public ResponseEntity<MensagensApi> erroInterno(ErroInternoException e, HttpServletRequest request){
		return ResponseEntity.status(MensagensApi.getMensagemErroInterno().getHttpStatus())
				.body(MensagensApi.getMensagemErroInterno().addDetail(e.getMessage()));
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MensagensApi> erroValidacao(MethodArgumentNotValidException e, HttpServletRequest request){
		MensagensApi erroPadronizado = MensagensApi.getMensagemErroNegocio();
		String mensagem = new String();
		for(FieldError erro : e.getFieldErrors()) {
			mensagem = mensagem.concat(erro.getDefaultMessage()+"; ");
			erroPadronizado.adicionarMensagemCampo(erro.getField(), erro.getDefaultMessage());
		}
		erroPadronizado.addDetail(mensagem);
		erroPadronizado.setType(e.getClass().descriptorString());
		return ResponseEntity.status(erroPadronizado.getHttpStatus()).body(erroPadronizado);	
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<MensagensApi> erroFormato(HttpMessageNotReadableException e, HttpServletRequest request){
		MensagensApi erroPadronizado = MensagensApi.getMensagemErroInterno();
		erroPadronizado.setType(e.getClass().descriptorString());
		erroPadronizado.setDetail("Não foi possível processar a solicitação. Verifique formato dos dados");
		return ResponseEntity.status(erroPadronizado.getHttpStatus()).body(erroPadronizado);
	}

}

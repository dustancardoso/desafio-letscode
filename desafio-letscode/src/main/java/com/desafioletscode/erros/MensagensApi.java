package com.desafioletscode.erros;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Erro no padrão RFC 7807
 * @author dustan
 *
 */
public class MensagensApi {
	
	private int status;
	
	private String title;
	
	private String detail;
	
	private String type;
	
	private String instance;
	
	private Date timestamp;
	
	class FieldMessage{
		String name;
		String message;
		
		public FieldMessage(String name, String message) {
			this.name = name;
			this.message = message;			
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
	
	private List<FieldMessage> fields = new ArrayList<FieldMessage>();
	
	public void adicionarMensagemCampo(String name, String message) {
		this.fields.add(new FieldMessage(name, message));
	}
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public MensagensApi addDetail(String detail) {
		this.detail = detail;
		return this;
	}
	
	public MensagensApi addTitle(String title) {
		this.title = title;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}
	
	public HttpStatus getHttpStatus() {
		return HttpStatus.valueOf(this.status)==null ?
				HttpStatus.NOT_FOUND : 
				HttpStatus.valueOf(this.status);
	}
	
	public static MensagensApi getMensagemErroInterno() {
		MensagensApi novoErro = new MensagensApi();
		novoErro.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		novoErro.setTitle("Erro de processamento interno");
		novoErro.setTimestamp(new Date());
		novoErro.setDetail("Ocorreu um erro interno. Tente novamente mais tarde ou entre em contato");
		return novoErro;
	}
	public static MensagensApi getMensagemErroNegocio() {
		MensagensApi novoErro = new MensagensApi();
		novoErro.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
		novoErro.setTitle("Solicitação inválida");
		novoErro.setTimestamp(new Date());
		return novoErro;
	}
	
	public static MensagensApi getMensagemSucesso() {
		MensagensApi novoSucesso= new MensagensApi();
		novoSucesso.setStatus(HttpStatus.OK.value());
		novoSucesso.setTitle("Solicitação processada com sucesso");
		novoSucesso.setTimestamp(new Date());
		return novoSucesso;
	}
	
	public static ResponseEntity<MensagensApi> getErroInterno() {
		MensagensApi novoErro = getMensagemErroInterno();
		return ResponseEntity.status(novoErro.getHttpStatus()).body(novoErro);			
	}
	
	
	public static ResponseEntity<MensagensApi> getErroInterno(String mensagemDetalhada) {
		MensagensApi novoErro = getMensagemErroInterno();
		novoErro.setDetail(mensagemDetalhada);
		return ResponseEntity.status(novoErro.getHttpStatus()).body(novoErro);
			
	}
	
	public static ResponseEntity<MensagensApi> getErroNegocio(String mensagemDetalhada) {
		MensagensApi novoErro = getMensagemErroNegocio();
		novoErro.setDetail(mensagemDetalhada);
		return ResponseEntity.status(novoErro.getHttpStatus()).body(novoErro);			
	}
	
	public static ResponseEntity<MensagensApi> getSucesso(String mensagemDetalhada) {
		MensagensApi novoSucesso= getMensagemSucesso();
		novoSucesso.setDetail(mensagemDetalhada);
		return ResponseEntity.status(novoSucesso.getHttpStatus()).body(novoSucesso);				
	}


	public List<FieldMessage> getFields() {
		return fields;
	}


	public void setFields(List<FieldMessage> fields) {
		this.fields = fields;
	}


	public Date getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}

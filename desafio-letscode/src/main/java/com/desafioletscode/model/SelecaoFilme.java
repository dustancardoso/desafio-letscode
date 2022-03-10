package com.desafioletscode.model;

import java.util.Optional;

public class SelecaoFilme {
	
	private int quantidadeSelecionada = 0;
	
	private Optional<String> idIMDB = Optional.empty();

	public int getQuantidadeSelecionada() {
		return quantidadeSelecionada;
	}

	public void setQuantidadeSelecionada(int quantidadeSelecionada) {
		this.quantidadeSelecionada = quantidadeSelecionada;
	}

	public Optional<String> getIdIMDB() {
		return idIMDB;
	}

	public void setIdIMDB(Optional<String> idIMDB) {
		this.idIMDB = idIMDB;
	}
	
	public boolean isSelecaoUnicaCompleta() {
		return getQuantidadeSelecionada() == 1 &&
				idIMDB.isPresent();
	}

}

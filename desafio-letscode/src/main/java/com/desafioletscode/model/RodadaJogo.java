package com.desafioletscode.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class RodadaJogo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String loginJogador;
	
	private Date dataPartida;
	
	private String idFilmeEscolhido;
	
	private Boolean acerto;
	
	private Integer pontos;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "rodadaJogo", cascade = CascadeType.ALL) 
	private List<RodadaJogoFilme> filmesRodada = new ArrayList<RodadaJogoFilme>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Date getDataPartida() {
		return dataPartida;
	}

	public void setDataPartida(Date dataPartida) {
		this.dataPartida = dataPartida;
	}

	public String getIdFilmeEscolhido() {
		return idFilmeEscolhido;
	}

	public void setIdFilmeEscolhido(String idFilmeEscolhido) {
		this.idFilmeEscolhido = idFilmeEscolhido;
	}

	public Boolean getAcerto() {
		return acerto;
	}

	public void setAcerto(Boolean acerto) {
		this.acerto = acerto;
	}

	public Integer getPontos() {
		return pontos;
	}

	public void setPontos(Integer pontos) {
		this.pontos = pontos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RodadaJogo other = (RodadaJogo) obj;
		return Objects.equals(id, other.id);
	}
	public String getLoginJogador() {
		return loginJogador;
	}
	public void setLoginJogador(String loginJogador) {
		this.loginJogador = loginJogador;
	}
	
	public boolean contains(RodadaJogoFilme filme) {
		for (RodadaJogoFilme filmeRodada : this.filmesRodada) {
			if(filmeRodada.getImdbID().equalsIgnoreCase(filme.getImdbID()))
				return true;
		}
		return false;
	}
	
	public boolean containsAll(Collection<RodadaJogoFilme> filmes) {
		boolean containsAll = true;
		for(RodadaJogoFilme filme: filmes) {
			if(!this.contains(filme))
				containsAll = false;
		}
		return containsAll;
	}
	
	public Optional<RodadaJogoFilme> buscarFilme(Optional<String> imdbID) {
		if(imdbID.isPresent())
			for (RodadaJogoFilme filmeRodada : this.filmesRodada) {
				if(filmeRodada.getImdbID().equalsIgnoreCase(imdbID.get()))
					return Optional.of(filmeRodada);
			}
		return Optional.empty();
	}
	
	public void addFilme(RodadaJogoFilme filme) {
		filme.setRodadaJogo(this);
		this.filmesRodada.add(filme);
	}

	public List<RodadaJogoFilme> getFilmesRodada() {
		return filmesRodada;
	}

	public void setFilmesRodada(List<RodadaJogoFilme> filmesRodada) {
		this.filmesRodada = filmesRodada;
	}


}

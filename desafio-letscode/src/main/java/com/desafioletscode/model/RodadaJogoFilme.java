package com.desafioletscode.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class RodadaJogoFilme{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private RodadaJogo rodadaJogo;

	@JsonProperty("imdbID")
	private String imdbID;
	
	@JsonProperty("Title")
	private String title;
	
	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("imdbRating")
	private String imdbRating;
	
	@JsonProperty("imdbVotes")
	private String imdbVotes;
	
	@JsonProperty("Poster")
	private String poster;
	
	@JsonProperty("Response")
	private Boolean response;
	
	@JsonProperty("Year")
	private int year;
	
	@JsonProperty("Error")
	private String error;
	
	@JsonProperty("Genre")
	private String genre;
	
	@JsonProperty("Country")
	private String country;
		
	@JsonProperty("seriesID")
	private String seriesID;
	
	private boolean selected = false;
	
	
	public RodadaJogoFilme() {
		super();
	}
	
	public RodadaJogoFilme(String imdbID2, String title2, int year2, String poster2, String country, String genre) {
		this.setImdbID(imdbID2);
		this.setTitle(title2);
		this.setYear(year2);
		this.setCountry(country);
		this.setGenre(genre);
		this.setPoster(poster2);
		
	}
	
	public static RodadaJogoFilme getFilmeErro(Boolean response, String error) {
		RodadaJogoFilme filme = new RodadaJogoFilme();
		filme.setError(error);
		filme.setResponse(response);
		return filme;		
	}

	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	@JsonProperty("imdbID")
	public String getImdbID() {
		return imdbID;
	}
	
	@JsonProperty("imdbID")
	public void setImdbID(String imdbID) {
		this.imdbID = imdbID;
	}
	
	@JsonProperty("Title")
	public String getTitle() {
		return this.title;
	}
	
	@JsonProperty("Title")
	public void setTitle(String title) {
		this.title = title;
	}
	
	@JsonProperty("imdbRating")
	public String getImdbRating() {
		return imdbRating;
	}
	
	@JsonProperty("imdbRating")
	public void setImdbRating(String imdbRating) {
		this.imdbRating = imdbRating;
	}
	
	@JsonProperty("imdbVotes")
	public String getImdbVotes() {
		return imdbVotes;
	}
	
	@JsonProperty("imdbVotes")
	public void setImdbVotes(String imdbVotes) {
		this.imdbVotes = imdbVotes;
	}
	
	@JsonProperty("Poster")
	public String getPoster() {
		return this.poster;
	}
	
	@JsonProperty("Poster")
	public void setPoster(String poster) {
		this.poster = poster;
	}
	
	@JsonProperty("Response")
	public Boolean getResponse() {
		return this.response;
	}
	
	@JsonProperty("Response")
	public void setResponse(Boolean response) {
		this.response = response;
	}
	
	@JsonProperty("Type")
	public String getType() {
		return type;
	}
	
	@JsonProperty("Type")
	public void setType(String type) {
		this.type = type;
	}
	
	@JsonProperty("Year")
	public int getYear() {
		return year;
	}
	
	@JsonProperty("Year")
	public void setYear(int year) {
		this.year = year;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	public double calcularNota() {
		double nota = 0;
		double votos = 0;
		try{
			nota = Double.parseDouble(this.imdbRating);
			votos = Double.parseDouble(this.getImdbVotes().replace(",", ""));
		}catch (NumberFormatException e) {
			return 0;
		}
		return nota*votos;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(imdbID);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RodadaJogoFilme other = (RodadaJogoFilme) obj;
		return Objects.equals(imdbID, other.getImdbID());
	}
	
	@JsonProperty("Genre")
	public String getGenre() {
		return genre;
	}
	
	@JsonProperty("Genre")
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	@JsonProperty("Country")
	public String getCountry() {
		return country;
	}
	
	@JsonProperty("Country")
	public void setCountry(String country) {
		this.country = country;
	}
	
	@JsonProperty("seriesID")
	public String getSeriesID() {
		return seriesID;
	}
	
	@JsonProperty("seriesID")
	public void setSeriesID(String seriesID) {
		this.seriesID = seriesID;
	}


	public void setRodadaJogo(RodadaJogo rodadaJogo) {
		this.rodadaJogo = rodadaJogo;
	}
	

}

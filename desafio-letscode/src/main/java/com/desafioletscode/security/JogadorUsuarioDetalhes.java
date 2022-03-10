package com.desafioletscode.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.desafioletscode.model.Jogador;


@SuppressWarnings("serial")
public class JogadorUsuarioDetalhes implements UserDetails{
	
	private final Jogador jogador;
	
	public JogadorUsuarioDetalhes(Jogador jogador) {
		super();
		this.jogador = (jogador!=null)? jogador: new Jogador();
	}
	
	public JogadorUsuarioDetalhes() {
		super();
		this.jogador = new Jogador();
	}	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<>();
	}

	@Override
	public String getPassword() {
		return jogador.getSenha();
	}

	@Override
	public String getUsername() {
		return jogador.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}

package com.desafioletscode.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import com.desafioletscode.model.Jogador;
import com.desafioletscode.repository.JogadorRepository;

@Service
public class JogadorUsuarioDetalhesService implements UserDetailsService {
	
	@Autowired
	JogadorRepository jogadorRepository;
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
	    return new Pbkdf2PasswordEncoder("letsseed@#", 2);
	}

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Optional<Jogador> jogadorOptional = jogadorRepository.findByLogin(login);
		if(jogadorOptional.isEmpty())
			throw new UsernameNotFoundException("Jogador '"+login+"' não encontrado");
		else
			return new JogadorUsuarioDetalhes(jogadorOptional.get());
	}

}

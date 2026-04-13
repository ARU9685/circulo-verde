package com.circuloverde.circulo_verde.service;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        System.out.println(">>> loadUserByUsername: " + nombre);
        Usuario usuario = usuarioRepository.findByNombre(nombre);
        if (usuario == null) {
            System.out.println(">>> Usuario NO encontrado en BD: " + nombre);
            throw new UsernameNotFoundException("Usuario no encontrado: " + nombre);
        }
        System.out.println(">>> Usuario encontrado: " + nombre);
        return User.withUsername(usuario.getNombre())
                .password(usuario.getContrasenia())   // ya viene hasheado de BD
                .roles("USER")
                .build();
    }

    public Usuario registrar(Usuario usuario) {
        // Hashear contraseña antes de guardar
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        Usuario guardado = usuarioRepository.save(usuario);
        System.out.println(">>> Usuario guardado en BD con id: " + guardado.getId());
        return guardado;
    }

    public Usuario buscarPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre);
    }
}

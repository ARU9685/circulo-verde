package com.circuloverde.circulo_verde.config;

import com.circuloverde.circulo_verde.model.Usuario;
import com.circuloverde.circulo_verde.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Auth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;

    public Auth2LoginSuccessHandler(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        HttpSession session = request.getSession();
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario != null) {
            // Ya existe — entrar directamente
            session.setAttribute("usuarioHuerto", usuario);
            response.sendRedirect("/panel");
        } else {
            // Primera vez — pedir que complete el perfil
            session.setAttribute("googleEmail", email);
            session.setAttribute("googleNombre", (String) oauth2User.getAttribute("name"));
            response.sendRedirect("/completar-perfil");
        }
    }
}

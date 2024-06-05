package com.apostassa.infra.servlet;

import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.security.TokenJWT;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebFilter(servletNames = {
					//USUARIO
					"AlterarSenhaUserServlet",
					"DadosPessoaisUserServlet",
					"PaginaInicialUserServlet",

					//USUARIO-PERFIL-PARTICIPANTE
					"PerfilParticipanteUserServlet",
					})
public class AutenticacaoUserFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletReq, ServletResponse servletResp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletReq;
		HttpServletResponse resp = (HttpServletResponse) servletResp;
		
		Cookie[] cookies = req.getCookies();
		boolean tokenEncontrado = false;
		
		if (cookies != null) {
			for(Cookie cookie : cookies) {
				if (cookie.getName().equals("token")) {
					try {
						String stringToken = cookie.getValue();
						Map<String, String> allClaims = new TokenJWT().pegarTodosAtributos(stringToken);
						
						String usuarioCpf = allClaims.get("subject");
						String stringRole = allClaims.get("role");
						String email = allClaims.get("email");

						if (stringRole.contains("Null claim")) {
							stringRole = null;
						}
						
						req.setAttribute("usuarioCpf", usuarioCpf);
						req.setAttribute("role", stringRole);
						req.setAttribute("email", email);
					} catch (AutenticacaoException e) {
						resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						resp.getWriter().write(e.getMessage());
						removerCookie(resp, "token");
						return;
					}
					tokenEncontrado = true;
					break;
				}
			}
		}
		
		if (!tokenEncontrado) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.getWriter().write("Você precisa estar autenticado para acessar essa página");
			return;
		}
		
		chain.doFilter(servletReq, servletResp);
		
	}

	private void removerCookie(HttpServletResponse resp, String nome) {
		Cookie cookie = new Cookie(nome, "");
		cookie.setMaxAge(0);
		resp.addCookie(cookie);
	}

}

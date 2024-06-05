package com.apostassa.infra.servlet.usuario;

import com.apostassa.aplicacao.usecase.usuario.AutenticarUsuario;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.gateway.usuario.RepositorioDeUsuarioUserComJdbcPostgres;
import com.apostassa.infra.gateway.usuario.UsuarioUserWebAdapter;
import com.apostassa.infra.security.TokenJWT;
import com.apostassa.infra.servlet.usuario.dto.AutenticarUsuarioDTO;
import com.apostassa.infra.util.JacksonUtil;
import com.apostassa.infra.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.Map;

@WebServlet("/usuario/autenticar")
public class AutenticarUsuarioUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ProvedorConexaoJDBC provedorConexaoJDBC;

	private RepositorioDeUsuarioUserComJdbcPostgres repositorio;

	private UsuarioUserWebAdapter adapter;

	private UsuarioMapper usuarioMapper;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.provedorConexaoJDBC = InicializadorConexao.executa(request);
		this.repositorio = new RepositorioDeUsuarioUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
		this.adapter = new UsuarioUserWebAdapter();
		this.usuarioMapper =  Mappers.getMapper(UsuarioMapper.class);
		super.service(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String requestBody = Util.pegarJsonCorpoDaRequisicao(req);
			AutenticarUsuarioDTO autenticarUsuarioDTO = (AutenticarUsuarioDTO) JacksonUtil.deserializar(requestBody, AutenticarUsuarioDTO.class);
			Util.validar(autenticarUsuarioDTO);
			Usuario usuario = usuarioMapper.autenticarUsuarioDTOParaUsuario(autenticarUsuarioDTO);

			AutenticarUsuario autenticarUsuario = new AutenticarUsuario(provedorConexaoJDBC, repositorio, adapter, new TokenJWT());
			Map<String, String> respostaAutenticarUsuario = autenticarUsuario.executa(usuario);

			removerTodosOsCookiesNomeadosComoToken(req, resp);

			resp.getWriter().write(respostaAutenticarUsuario.get("jsonToken"));
			resp.setStatus(HttpServletResponse.SC_OK);
			criarCookie(resp, respostaAutenticarUsuario);
		} catch (AutenticacaoException | ValidacaoException e) {
			resp.getWriter().write(e.getMessage());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao gerar json de resposta");
		}
	}

	private static void criarCookie(HttpServletResponse resp, Map<String, String> respostaAutenticarUsuario) {
		Cookie cookie = new Cookie("token", respostaAutenticarUsuario.get("token"));
		int dezDiasEmSegundos = 86400 * 5; // 86400 segundos em um dia
		cookie.setMaxAge(dezDiasEmSegundos);
		cookie.setPath("/");
		resp.addCookie(cookie);
	}

	private void removerTodosOsCookiesNomeadosComoToken(HttpServletRequest req, HttpServletResponse resp) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName() == "token") {
					removerCookie(resp, "token");
				}
			}
		}
	}

	private void removerCookie(HttpServletResponse resp, String nome) {
		Cookie cookie = new Cookie(nome, "");
		cookie.setMaxAge(0);
		resp.addCookie(cookie);
	}
}

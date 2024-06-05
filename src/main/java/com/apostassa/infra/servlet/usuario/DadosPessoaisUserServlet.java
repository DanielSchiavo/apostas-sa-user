package com.apostassa.infra.servlet.usuario;

import com.apostassa.aplicacao.usecase.usuario.PegarDadosDoUsuarioPessoais;
import com.apostassa.aplicacao.usecase.usuario.UsuarioAlteraSeusDadosPessoais;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.gateway.usuario.RepositorioDeUsuarioUserComJdbcPostgres;
import com.apostassa.infra.gateway.usuario.UsuarioUserWebAdapter;
import com.apostassa.infra.servlet.usuario.dto.UsuarioAlteraSeusDadosPessoaisDTO;
import com.apostassa.infra.util.JacksonUtil;
import com.apostassa.infra.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;

import java.io.IOException;

@WebServlet("/usuario/dados-pessoais")
public class DadosPessoaisUserServlet extends HttpServlet {

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
		this.usuarioMapper = Mappers.getMapper(UsuarioMapper.class);
		super.service(request, response);
	}
    
    @Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String usuarioId = req.getAttribute("usuarioId").toString();

			PegarDadosDoUsuarioPessoais pegarDadosPessoais = new PegarDadosDoUsuarioPessoais(provedorConexaoJDBC, repositorio, adapter);
			String respostaPegarDadosDoUsuarioPessoais = pegarDadosPessoais.executa(usuarioId);

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(respostaPegarDadosDoUsuarioPessoais);
		} catch (ValidacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
		}
	}
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String requestBody = Util.pegarJsonCorpoDaRequisicao(req);
			String usuarioCpf = req.getAttribute("usuarioCpf").toString();

			UsuarioAlteraSeusDadosPessoaisDTO usuarioAlteraSeusDadosPessoaisDTO = (UsuarioAlteraSeusDadosPessoaisDTO) JacksonUtil.deserializar(requestBody, UsuarioAlteraSeusDadosPessoaisDTO.class);
			Util.validar(usuarioAlteraSeusDadosPessoaisDTO);
			Usuario usuario = usuarioMapper.usuarioAlteraSeusDadosPessoaisDTOParaUsuario(usuarioAlteraSeusDadosPessoaisDTO);

			UsuarioAlteraSeusDadosPessoais usuarioAlteraSeusDadosPessoais = new UsuarioAlteraSeusDadosPessoais(provedorConexaoJDBC, repositorio, adapter);
			String respostaUsuarioAlteraSeusDadosPessoais = usuarioAlteraSeusDadosPessoais.executa(usuario, usuarioCpf);

			resp.setStatus(HttpServletResponse.SC_OK);
			resp.getWriter().write(respostaUsuarioAlteraSeusDadosPessoais);
		} catch (ValidacaoException | AlterarUsuarioException | AutenticacaoException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().write(e.getMessage());
		}
    }
}

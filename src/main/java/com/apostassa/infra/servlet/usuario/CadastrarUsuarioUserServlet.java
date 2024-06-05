package com.apostassa.infra.servlet.usuario;

import com.apostassa.aplicacao.usecase.usuario.CadastrarUsuario;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.gateway.usuario.RepositorioDeUsuarioUserComJdbcPostgres;
import com.apostassa.infra.gateway.usuario.UsuarioUserWebAdapter;
import com.apostassa.infra.servlet.usuario.dto.CadastrarUsuarioDTO;
import com.apostassa.infra.util.JacksonUtil;
import com.apostassa.infra.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.Map;

@WebServlet("/usuario/cadastro")
public class CadastrarUsuarioUserServlet extends HttpServlet {

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
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String requestBody = Util.pegarJsonCorpoDaRequisicao(req);

			CadastrarUsuarioDTO cadastrarUsuarioDTO = (CadastrarUsuarioDTO) JacksonUtil.deserializar(requestBody, CadastrarUsuarioDTO.class);
			Util.validar(cadastrarUsuarioDTO);
			Usuario usuario = usuarioMapper.cadastrarUsuarioDTOParaUsuario(cadastrarUsuarioDTO);

			CadastrarUsuario cadastrarUsuario = new CadastrarUsuario(provedorConexaoJDBC, repositorio, adapter);
			Map<String, String> respostaCadastrarUsuario = cadastrarUsuario.executa(usuario);

			resp.getWriter().write(respostaCadastrarUsuario.get("mensagem"));
			resp.setStatus(HttpServletResponse.SC_CREATED);
		} catch (ValidacaoException e) {
			e.printStackTrace();
			resp.getWriter().write(e.getMessage());
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}

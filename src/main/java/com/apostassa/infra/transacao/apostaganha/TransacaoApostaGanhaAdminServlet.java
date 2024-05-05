package com.apostassa.infra.transacao.apostaganha;

import com.apostassa.aplicacao.transacao.apostaganha.ExecutarTransacaoApostaGanha;
import com.apostassa.aplicacao.transacao.apostaganha.ExecutarTransacaoApostaGanhaDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.usuario.RepositorioDeUsuarioUserComJdbcPostgres;
import com.apostassa.infra.util.JacksonUtil;
import com.apostassa.infra.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TransacaoApostaGanhaAdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProvedorConexaoJDBC provedorConexaoJDBC;

    private RepositorioDeTransacaoApostaGanhaComJdbcPostgres repositorio;

    private TransacaoApostaGanhaWebAdapter adapter;

    private RepositorioDeUsuarioUserComJdbcPostgres repositorioDeUsuario;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.provedorConexaoJDBC = InicializadorConexao.executa(request);
        this.repositorio = new RepositorioDeTransacaoApostaGanhaComJdbcPostgres(provedorConexaoJDBC.getConexao());
        this.adapter = new TransacaoApostaGanhaWebAdapter();
        this.repositorioDeUsuario = new RepositorioDeUsuarioUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
        super.service(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String usuarioId = req.getAttribute("usuarioId").toString();
            String corpoDaRequisicao = Util.pegarJsonCorpoDaRequisicao(req);
            var executarTransacaoApostaGanhaDTO = (ExecutarTransacaoApostaGanhaDTO) JacksonUtil.deserializar(corpoDaRequisicao, ExecutarTransacaoApostaGanhaDTO.class);

            var executarTransacaoApostaGanha = new ExecutarTransacaoApostaGanha(provedorConexaoJDBC, repositorio, adapter, repositorioDeUsuario);
            String respostaExecutarTransacaoApostaGanha = executarTransacaoApostaGanha.executa(executarTransacaoApostaGanhaDTO, usuarioId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(respostaExecutarTransacaoApostaGanha);
        } catch (ValidacaoException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }
}

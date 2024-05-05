package com.apostassa.infra.transacao.saque;

import com.apostassa.aplicacao.transacao.saque.ExecutarTransacaoSaque;
import com.apostassa.aplicacao.transacao.saque.ExecutarTransacaoSaqueDTO;
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

public class TransacaoSaqueUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProvedorConexaoJDBC provedorConexaoJDBC;

    private RepositorioDeTransacaoSaqueComJdbcPostgres repositorio;

    private TransacaoSaqueUserWebAdapter adapter;

    private RepositorioDeUsuarioUserComJdbcPostgres repositorioDeUsuario;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.provedorConexaoJDBC = InicializadorConexao.executa(request);
        this.repositorio = new RepositorioDeTransacaoSaqueComJdbcPostgres(provedorConexaoJDBC.getConexao());
        this.adapter = new TransacaoSaqueUserWebAdapter();
        this.repositorioDeUsuario = new RepositorioDeUsuarioUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
        super.service(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String usuarioId = req.getAttribute("usuarioId").toString();
            String corpoDaRequisicao = Util.pegarJsonCorpoDaRequisicao(req);
            var executarTransacaoSaqueDTO = (ExecutarTransacaoSaqueDTO) JacksonUtil.deserializar(corpoDaRequisicao, ExecutarTransacaoSaqueDTO.class);

            var executarTransacaoSaque = new ExecutarTransacaoSaque(provedorConexaoJDBC, repositorio, adapter, repositorioDeUsuario);
            String respostaExecutarTransacaoSaque = executarTransacaoSaque.executa(executarTransacaoSaqueDTO, usuarioId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(respostaExecutarTransacaoSaque);
        } catch (ValidacaoException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }
}

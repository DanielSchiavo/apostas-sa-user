package com.apostassa.infra.transacao.deposito;

import com.apostassa.aplicacao.transacao.deposito.ExecutarTransacaoDeposito;
import com.apostassa.aplicacao.transacao.deposito.ExecutarTransacaoDepositoDTO;
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

public class TransacaoDepositoUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProvedorConexaoJDBC provedorConexaoJDBC;

    private RepositorioDeTransacaoDepositoComJdbcPostgres repositorio;

    private TransacaoDepositoUserWebAdapter adapter;

    private RepositorioDeUsuarioUserComJdbcPostgres repositorioDeUsuario;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.provedorConexaoJDBC = InicializadorConexao.executa(request);
        this.repositorio = new RepositorioDeTransacaoDepositoComJdbcPostgres(provedorConexaoJDBC.getConexao());
        this.adapter = new TransacaoDepositoUserWebAdapter();
        this.repositorioDeUsuario = new RepositorioDeUsuarioUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
        super.service(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String usuarioId = req.getAttribute("usuarioId").toString();
            String corpoDaRequisicao = Util.pegarJsonCorpoDaRequisicao(req);
            var executarTransacaoDepositoDTO = (ExecutarTransacaoDepositoDTO) JacksonUtil.deserializar(corpoDaRequisicao, ExecutarTransacaoDepositoDTO.class);

            var executarTransacaoDeposito = new ExecutarTransacaoDeposito(provedorConexaoJDBC, repositorio, adapter, repositorioDeUsuario);
            String respostaExecutarTransacaoApostaGanha = executarTransacaoDeposito.executa(executarTransacaoDepositoDTO, usuarioId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(respostaExecutarTransacaoApostaGanha);
        } catch (ValidacaoException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }
}

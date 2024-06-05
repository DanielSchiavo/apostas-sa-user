package com.apostassa.infra.servlet.transacao.deposito;

import com.apostassa.aplicacao.usecase.transacao.deposito.ExecutarTransacaoDeposito;
import com.apostassa.aplicacao.usecase.transacao.deposito.ExecutarTransacaoDepositoDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.transacao.TransacaoDeposito;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.gateway.transacao.deposito.RepositorioDeTransacaoDepositoComJdbcPostgres;
import com.apostassa.infra.gateway.transacao.deposito.TransacaoDepositoUserWebAdapter;
import com.apostassa.infra.gateway.usuario.RepositorioDeUsuarioUserComJdbcPostgres;
import com.apostassa.infra.util.JacksonUtil;
import com.apostassa.infra.util.Util;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mapstruct.factory.Mappers;

import java.io.IOException;

@WebServlet("/transacao/deposito")
public class TransacaoDepositoUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProvedorConexaoJDBC provedorConexaoJDBC;

    private RepositorioDeTransacaoDepositoComJdbcPostgres repositorio;

    private TransacaoDepositoUserWebAdapter adapter;

    private RepositorioDeUsuarioUserComJdbcPostgres repositorioDeUsuario;

    private TransacaoDepositoMapper transacaoDepositoMapper;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.provedorConexaoJDBC = InicializadorConexao.executa(request);
        this.repositorio = new RepositorioDeTransacaoDepositoComJdbcPostgres(provedorConexaoJDBC.getConexao());
        this.adapter = new TransacaoDepositoUserWebAdapter();
        this.repositorioDeUsuario = new RepositorioDeUsuarioUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
        this.transacaoDepositoMapper = Mappers.getMapper(TransacaoDepositoMapper.class);
        super.service(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String usuarioId = req.getAttribute("usuarioId").toString();
            String corpoDaRequisicao = Util.pegarJsonCorpoDaRequisicao(req);
            var executarTransacaoDepositoDTO = (ExecutarTransacaoDepositoDTO) JacksonUtil.deserializar(corpoDaRequisicao, ExecutarTransacaoDepositoDTO.class);
            TransacaoDeposito transacaoDeposito = transacaoDepositoMapper.formatarExecutarTransacaoDepositoDTOParaTransacaoDeposito(executarTransacaoDepositoDTO);

            var executarTransacaoDeposito = new ExecutarTransacaoDeposito(provedorConexaoJDBC, repositorio, adapter, repositorioDeUsuario);
            String respostaExecutarTransacaoApostaGanha = executarTransacaoDeposito.executa(transacaoDeposito, usuarioId);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(respostaExecutarTransacaoApostaGanha);
        } catch (ValidacaoException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }
}

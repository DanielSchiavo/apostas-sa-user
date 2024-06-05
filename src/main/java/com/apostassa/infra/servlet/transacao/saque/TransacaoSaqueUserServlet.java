package com.apostassa.infra.servlet.transacao.saque;

import com.apostassa.aplicacao.usecase.transacao.saque.ExecutarTransacaoSaque;
import com.apostassa.aplicacao.usecase.transacao.saque.ExecutarTransacaoSaqueDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.transacao.TransacaoSaque;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.gateway.transacao.saque.RepositorioDeTransacaoSaqueComJdbcPostgres;
import com.apostassa.infra.gateway.transacao.saque.TransacaoSaqueUserWebAdapter;
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

@WebServlet("/transacao/saque")
public class TransacaoSaqueUserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProvedorConexaoJDBC provedorConexaoJDBC;

    private RepositorioDeTransacaoSaqueComJdbcPostgres repositorio;

    private TransacaoSaqueUserWebAdapter adapter;

    private RepositorioDeUsuarioUserComJdbcPostgres repositorioDeUsuario;

    private TransacaoSaqueMapper transacaoSaqueMapper = Mappers.getMapper(TransacaoSaqueMapper.class);

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
            String usuarioCpf = req.getAttribute("usuarioCpf").toString();
            String corpoDaRequisicao = Util.pegarJsonCorpoDaRequisicao(req);

            var executarTransacaoSaqueDTO = (ExecutarTransacaoSaqueDTO) JacksonUtil.deserializar(corpoDaRequisicao, ExecutarTransacaoSaqueDTO.class);
            TransacaoSaque transacaoSaque = transacaoSaqueMapper.executarTransacaoSaqueDTOParaTransacaoSaque(executarTransacaoSaqueDTO);

            var executarTransacaoSaque = new ExecutarTransacaoSaque(provedorConexaoJDBC, repositorio, adapter, repositorioDeUsuario);
            String respostaExecutarTransacaoSaque = executarTransacaoSaque.executa(transacaoSaque, usuarioCpf);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(respostaExecutarTransacaoSaque);
        } catch (ValidacaoException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }
}

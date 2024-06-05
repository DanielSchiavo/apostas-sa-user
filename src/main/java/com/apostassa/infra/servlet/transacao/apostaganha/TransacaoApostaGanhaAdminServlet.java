package com.apostassa.infra.servlet.transacao.apostaganha;

import com.apostassa.aplicacao.usecase.transacao.apostaganha.ExecutarTransacaoApostaGanha;
import com.apostassa.aplicacao.usecase.transacao.apostaganha.ExecutarTransacaoApostaGanhaDTO;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.transacao.TransacaoApostaGanha;
import com.apostassa.infra.db.InicializadorConexao;
import com.apostassa.infra.db.ProvedorConexaoJDBC;
import com.apostassa.infra.gateway.transacao.apostaganha.RepositorioDeTransacaoApostaGanhaComJdbcPostgres;
import com.apostassa.infra.gateway.transacao.apostaganha.TransacaoApostaGanhaWebAdapter;
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

@WebServlet("/transacao/aposta-ganha")
public class TransacaoApostaGanhaAdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProvedorConexaoJDBC provedorConexaoJDBC;

    private RepositorioDeTransacaoApostaGanhaComJdbcPostgres repositorio;

    private TransacaoApostaGanhaWebAdapter adapter;

    private RepositorioDeUsuarioUserComJdbcPostgres repositorioDeUsuario;

    private TransacaoApostaGanhaMapper transacaoApostaGanhaMapper;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.provedorConexaoJDBC = InicializadorConexao.executa(request);
        this.repositorio = new RepositorioDeTransacaoApostaGanhaComJdbcPostgres(provedorConexaoJDBC.getConexao());
        this.adapter = new TransacaoApostaGanhaWebAdapter();
        this.repositorioDeUsuario = new RepositorioDeUsuarioUserComJdbcPostgres(provedorConexaoJDBC.getConexao());
        this.transacaoApostaGanhaMapper = Mappers.getMapper(TransacaoApostaGanhaMapper.class);
        super.service(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String usuarioId = req.getAttribute("usuarioId").toString();
            String corpoDaRequisicao = Util.pegarJsonCorpoDaRequisicao(req);
            var executarTransacaoApostaGanhaDTO = (ExecutarTransacaoApostaGanhaDTO) JacksonUtil.deserializar(corpoDaRequisicao, ExecutarTransacaoApostaGanhaDTO.class);
            TransacaoApostaGanha transacaoApostaGanha = transacaoApostaGanhaMapper.formatarExecutarTransacaoApostaGanhaDTOParaTransacaoApostaGanha(executarTransacaoApostaGanhaDTO);

            var executarTransacaoApostaGanha = new ExecutarTransacaoApostaGanha(provedorConexaoJDBC, repositorio, adapter, repositorioDeUsuario);
            String respostaExecutarTransacaoApostaGanha = executarTransacaoApostaGanha.executa(transacaoApostaGanha, usuarioId);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(respostaExecutarTransacaoApostaGanha);
        } catch (ValidacaoException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(e.getMessage());
        }
    }
}

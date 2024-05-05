package com.apostassa.aplicacao.usuario;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.usuario.Usuario;


public class PegarDadosDoUsuarioPessoais {

    private final ProvedorConexao provedorConexao;

    private final RepositorioDeUsuarioUser repositorioDeUsuario;

    private final UsuarioUserPresenter presenter;

    public PegarDadosDoUsuarioPessoais(ProvedorConexao provedorConexao, RepositorioDeUsuarioUser repositorioDeUsuario, UsuarioUserPresenter presenter) {
        this.provedorConexao = provedorConexao;
        this.repositorioDeUsuario = repositorioDeUsuario;
        this.presenter = presenter;
    }

    public String executa(String usuarioId) throws ValidacaoException {
        try {
            Usuario usuario = repositorioDeUsuario.pegarDadosDoUsuarioPessoais(usuarioId);

            return presenter.respostaPegarDadosDoUsuarioPessoais(usuario);
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}

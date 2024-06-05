package com.apostassa.aplicacao.usecase.usuario;

import com.apostassa.aplicacao.ProvedorConexao;
import com.apostassa.aplicacao.gateway.usuario.UsuarioUserPresenter;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
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

    public String executa(String usuarioCpf) throws ValidacaoException {
        try {
            Usuario usuario = repositorioDeUsuario.pegarDadosDoUsuarioPessoais(usuarioCpf);

            return presenter.respostaPegarDadosDoUsuarioPessoais(usuario);
        } finally {
            provedorConexao.fecharConexao();
        }
    }
}

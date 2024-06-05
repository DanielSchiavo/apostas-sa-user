package com.apostassa.aplicacao.gateway.usuario;

import com.apostassa.dominio.usuario.Usuario;

import java.util.Map;

public interface UsuarioUserPresenter {

    public Map<String, String> respostaAutenticarUsuario(String token);

    public Map<String, String> respostaCadastrarUsuario(Usuario usuario);

    public String respostaDeletarUsuario();

    public String respostaUsuarioAlteraSeusDadosPessoais(Usuario usuario);

    public String respostaUsuarioAlteraSuaSenha();

    public String respostaPegarDadosDoUsuarioPaginaInicial(Usuario usuario);

    public String respostaPegarDadosDoUsuarioPessoais(Usuario usuario);
}

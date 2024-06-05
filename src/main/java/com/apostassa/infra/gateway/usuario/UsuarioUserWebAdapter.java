package com.apostassa.infra.gateway.usuario;

import com.apostassa.aplicacao.gateway.usuario.UsuarioUserPresenter;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.infra.util.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class UsuarioUserWebAdapter implements UsuarioUserPresenter {

    @Override
    public Map<String, String> respostaAutenticarUsuario(String token) {
        Map<String, String> jsonMap = new HashMap<>();
        try {
            Map<String, String> jsonToken = new HashMap<>();
            jsonToken.put("token", token);
            String json = new ObjectMapper().writeValueAsString(jsonToken);

            jsonMap.put("jsonToken", json);
            jsonMap.put("token", token);
            return jsonMap;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> respostaCadastrarUsuario(Usuario usuario) {
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("mensagem", "Cadastrado com sucesso!");
        return jsonMap;
    }

    @Override
    public String respostaDeletarUsuario() {
        return "";
    }

    @Override
    public String respostaUsuarioAlteraSeusDadosPessoais(Usuario usuario) {
        return "Dados pessoais alterado com sucesso!";
    }

    @Override
    public String respostaUsuarioAlteraSuaSenha() {
        return "Senha alterada com sucesso!";
    }

    @Override
    public String respostaPegarDadosDoUsuarioPaginaInicial(Usuario usuario) {
        return JacksonUtil.serializadorNotNullAndNotEmpty(usuario);
    }

    @Override
    public String respostaPegarDadosDoUsuarioPessoais(Usuario usuario) {
        return JacksonUtil.serializadorNotNull(usuario);
    }
}

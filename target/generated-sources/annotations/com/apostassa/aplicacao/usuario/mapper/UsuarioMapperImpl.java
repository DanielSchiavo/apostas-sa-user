package com.apostassa.aplicacao.usuario.mapper;

import com.apostassa.aplicacao.usuario.AutenticarUsuarioDTO;
import com.apostassa.aplicacao.usuario.CadastrarUsuarioDTO;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSeuPerfilDeJogadorDTO;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSeusDadosPessoaisDTO;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSuaSenhaDTO;
import com.apostassa.aplicacao.usuario.UsuarioPaginaInicialDTO;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.perfiljogador.PerfilJogador;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-27T21:14:15-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioPaginaInicialDTO formatarUsuarioParaUsuarioPaginaInicialDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioPaginaInicialDTO usuarioPaginaInicialDTO = new UsuarioPaginaInicialDTO();

        usuarioPaginaInicialDTO.setFoto( usuarioPerfilJogadorFoto( usuario ) );
        usuarioPaginaInicialDTO.setNome( usuario.getNome() );
        if ( usuario.getSaldo() != null ) {
            usuarioPaginaInicialDTO.setSaldo( usuario.getSaldo().toString() );
        }

        return usuarioPaginaInicialDTO;
    }

    @Override
    public Usuario formatarAutenticarUsuarioDTOParaUsuario(AutenticarUsuarioDTO autenticarUsuarioDTO) {
        if ( autenticarUsuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setEmail( autenticarUsuarioDTO.getEmail() );
        usuario.setSenha( autenticarUsuarioDTO.getSenha() );

        return usuario;
    }

    @Override
    public Usuario formatarCadastrarUsuarioDTOParaUsuario(CadastrarUsuarioDTO cadastrarUsuarioDTO) {
        if ( cadastrarUsuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( cadastrarUsuarioDTO.getId() );
        usuario.setCpf( cadastrarUsuarioDTO.getCpf() );
        usuario.setNome( cadastrarUsuarioDTO.getNome() );
        usuario.setSobrenome( cadastrarUsuarioDTO.getSobrenome() );
        usuario.setEmail( cadastrarUsuarioDTO.getEmail() );
        usuario.setCelular( cadastrarUsuarioDTO.getCelular() );
        usuario.setSenha( cadastrarUsuarioDTO.getSenha() );
        usuario.setDataNascimento( cadastrarUsuarioDTO.getDataNascimento() );
        usuario.setDataCriacaoConta( cadastrarUsuarioDTO.getDataCriacaoConta() );
        usuario.setSaldo( cadastrarUsuarioDTO.getSaldo() );

        return usuario;
    }

    @Override
    public PerfilJogador formatarUsuarioAlteraSeuPerfilDeJogadorDTOParaUsuario(UsuarioAlteraSeuPerfilDeJogadorDTO usuarioAlteraSeuPerfilDeJogadorDTO) {
        if ( usuarioAlteraSeuPerfilDeJogadorDTO == null ) {
            return null;
        }

        PerfilJogador perfilJogador = new PerfilJogador();

        perfilJogador.setFoto( usuarioAlteraSeuPerfilDeJogadorDTO.getFoto() );
        perfilJogador.setInstagram( usuarioAlteraSeuPerfilDeJogadorDTO.getInstagram() );
        perfilJogador.setFacebook( usuarioAlteraSeuPerfilDeJogadorDTO.getFacebook() );
        perfilJogador.setTwitter( usuarioAlteraSeuPerfilDeJogadorDTO.getTwitter() );
        perfilJogador.setFrase( usuarioAlteraSeuPerfilDeJogadorDTO.getFrase() );

        return perfilJogador;
    }

    @Override
    public Usuario formatarUsuarioAlteraSeusDadosPessoaisDTOParaUsuario(UsuarioAlteraSeusDadosPessoaisDTO usuarioAlteraSeusDadosPessoaisDTO) {
        if ( usuarioAlteraSeusDadosPessoaisDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setCpf( usuarioAlteraSeusDadosPessoaisDTO.getCpf() );
        usuario.setRg( usuarioAlteraSeusDadosPessoaisDTO.getRg() );
        usuario.setNome( usuarioAlteraSeusDadosPessoaisDTO.getNome() );
        usuario.setSobrenome( usuarioAlteraSeusDadosPessoaisDTO.getSobrenome() );
        usuario.setEmail( usuarioAlteraSeusDadosPessoaisDTO.getEmail() );
        usuario.setCelular( usuarioAlteraSeusDadosPessoaisDTO.getCelular() );
        usuario.setDataNascimento( usuarioAlteraSeusDadosPessoaisDTO.getDataNascimento() );

        return usuario;
    }

    @Override
    public Usuario formatarUsuarioAlteraSuaSenhaDTOParaUsuario(UsuarioAlteraSuaSenhaDTO usuarioAlteraSuaSenhaDTO) {
        if ( usuarioAlteraSuaSenhaDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setSenha( usuarioAlteraSuaSenhaDTO.getSenhaAtual() );

        return usuario;
    }

    private String usuarioPerfilJogadorFoto(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }
        PerfilJogador perfilJogador = usuario.getPerfilJogador();
        if ( perfilJogador == null ) {
            return null;
        }
        String foto = perfilJogador.getFoto();
        if ( foto == null ) {
            return null;
        }
        return foto;
    }
}

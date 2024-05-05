package com.apostassa.aplicacao.usuario.mapper;

import com.apostassa.aplicacao.usuario.AutenticarUsuarioDTO;
import com.apostassa.aplicacao.usuario.CadastrarUsuarioDTO;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSeusDadosPessoaisDTO;
import com.apostassa.aplicacao.usuario.UsuarioAlteraSuaSenhaDTO;
import com.apostassa.dominio.usuario.Usuario;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-01T21:59:34-0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
public class UsuarioMapperImpl implements UsuarioMapper {

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
}

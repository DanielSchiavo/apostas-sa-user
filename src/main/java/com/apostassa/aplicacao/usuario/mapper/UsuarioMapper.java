package com.apostassa.aplicacao.usuario.mapper;


import com.apostassa.aplicacao.usuario.*;
import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.dominio.usuario.perfiljogador.PerfilJogador;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(builder = @Builder(disableBuilder = true), unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {
	
	@Mapping(source = "perfilJogador.foto", target = "foto")
	UsuarioPaginaInicialDTO formatarUsuarioParaUsuarioPaginaInicialDTO(Usuario usuario);
	
	Usuario formatarAutenticarUsuarioDTOParaUsuario(AutenticarUsuarioDTO autenticarUsuarioDTO);
	
	Usuario formatarCadastrarUsuarioDTOParaUsuario(CadastrarUsuarioDTO cadastrarUsuarioDTO);
	
	PerfilJogador formatarUsuarioAlteraSeuPerfilDeJogadorDTOParaUsuario(UsuarioAlteraSeuPerfilDeJogadorDTO usuarioAlteraSeuPerfilDeJogadorDTO);
	
	Usuario formatarUsuarioAlteraSeusDadosPessoaisDTOParaUsuario(UsuarioAlteraSeusDadosPessoaisDTO usuarioAlteraSeusDadosPessoaisDTO);
	
	@Mapping(source = "senhaAtual", target = "senha")
	Usuario formatarUsuarioAlteraSuaSenhaDTOParaUsuario(UsuarioAlteraSuaSenhaDTO usuarioAlteraSuaSenhaDTO);
	
}

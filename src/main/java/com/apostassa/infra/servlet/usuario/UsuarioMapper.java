package com.apostassa.infra.servlet.usuario;


import com.apostassa.dominio.usuario.Usuario;
import com.apostassa.infra.servlet.usuario.dto.UsuarioAlterarSenhaDTO;
import com.apostassa.infra.servlet.usuario.dto.AutenticarUsuarioDTO;
import com.apostassa.infra.servlet.usuario.dto.CadastrarUsuarioDTO;
import com.apostassa.infra.servlet.usuario.dto.UsuarioAlteraSeusDadosPessoaisDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(builder = @Builder(disableBuilder = true), unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {
	
	Usuario autenticarUsuarioDTOParaUsuario(AutenticarUsuarioDTO autenticarUsuarioDTO);
	
	Usuario cadastrarUsuarioDTOParaUsuario(CadastrarUsuarioDTO cadastrarUsuarioDTO);
	
	Usuario usuarioAlteraSeusDadosPessoaisDTOParaUsuario(UsuarioAlteraSeusDadosPessoaisDTO usuarioAlteraSeusDadosPessoaisDTO);
	
	@Mapping(source = "senhaAtual", target = "senha")
	Usuario usuarioAlteraSuaSenhaDTOParaUsuario(UsuarioAlterarSenhaDTO usuarioAlteraSuaSenhaDTO);
	
}

package com.apostassa.infra.gateway.usuario;

import com.apostassa.aplicacao.gateway.usuario.RepositorioDeUsuarioUser;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.*;
import com.apostassa.dominio.usuario.exceptions.AlterarUsuarioException;
import com.apostassa.dominio.usuario.exceptions.AutenticacaoException;
import com.apostassa.dominio.usuario.exceptions.UsuarioNaoEncontradoException;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;

import java.math.BigDecimal;
import java.sql.*;


public class RepositorioDeUsuarioUserComJdbcPostgres implements RepositorioDeUsuarioUser {

	private final Connection connection;
	
	public RepositorioDeUsuarioUserComJdbcPostgres(Connection connection) {
		this.connection = connection;
	}
	
	@Override
	public Usuario autenticarUsuario(String emailUsuario) throws AutenticacaoException, ValidacaoException {
		String sql = "SELECT cpf, email, senha FROM usuarios WHERE email = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, emailUsuario);
			try (ResultSet rs = ps.executeQuery()) {
				boolean encontrou = rs.next();
				if (!encontrou) {
					throw new AutenticacaoException();
				}

				String cpf = rs.getString("cpf");
				String email = rs.getString("email");
				String senha = rs.getString("senha");

				Usuario.UsuarioBuilder usuarioBuilder = Usuario.builder().email(email).senha(senha);
				
				sql = "SELECT role FROM usuarios_roles WHERE usuario_cpf = ?";
				try (PreparedStatement ps2 = connection.prepareStatement(sql)) {
					ps2.setString(1, cpf);
					try (ResultSet rs2 = ps2.executeQuery()) {
						boolean encontrouRole = rs2.next();
						if (encontrouRole) {
							String nomeRole = rs2.getString("role");
							RoleUsuario role = new RoleUsuario();
							role.setRole(NomeRole.valueOf(nomeRole));
							
							usuarioBuilder.role(role);
						}
					}
				}
				
				return usuarioBuilder.build();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void cadastrarUsuario(Usuario usuario) throws ValidacaoException {
		String sql = """
				INSERT INTO usuarios
				(id, cpf, nome, sobrenome, email, ddd, numero_celular, senha, data_nascimento, data_criacao_conta, saldo)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, usuario.getCpf());
			ps.setString(2, usuario.getNome());
			ps.setString(3, usuario.getSobrenome());
			ps.setString(4, usuario.getEmail());
			ps.setString(5, usuario.getCelular().getDdd());
			ps.setString(6, usuario.getCelular().getNumero());
			ps.setString(7, usuario.getSenha());
			ps.setDate(8, Date.valueOf(usuario.getDataNascimento()));
			ps.setTimestamp(9, Timestamp.valueOf(usuario.getDataCriacaoConta()));
			ps.setBigDecimal(10, usuario.getSaldo());
			ps.execute();
		} catch (SQLException e) {
			String mensagem = "Erro ao cadastrar usuário";
			switch (e.getSQLState()) {
			case "23505": // Código de erro para violação de restrição única
				if (e.getMessage().contains("usuarios_cpf_key")) {
					mensagem = "CPF já cadastrado";
				} else if (e.getMessage().contains("usuarios_email_key")) {
					mensagem = "E-mail já cadastrado";
				} else if (e.getMessage().contains("usuarios_numero_celular_key")) {
					mensagem = "Número de celular já cadastrado";
				}
				break;
			default:
				break;
			}
			throw new ValidacaoException(mensagem);
		}
	}

	@Override
	public Usuario pegarDadosDoUsuarioPaginaInicial(String usuarioCpf) throws ValidacaoException {
		try(connection) {
			String sql = "SELECT nome, foto, saldo FROM usuarios WHERE cpf = ?";
			try(PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, usuarioCpf);
				try(ResultSet rs = ps.executeQuery()) {
					boolean encontrou = rs.next();
					if (!encontrou) {
						throw new UsuarioNaoEncontradoException(usuarioCpf);
					}
					
					String nome = rs.getString("nome");
					String foto = rs.getString("foto");
					BigDecimal saldo = rs.getBigDecimal("saldo");
					
					Usuario usuario = Usuario.builder()
											 .nome(nome)
											 .perfilJogador(PerfilParticipante.builder().foto(foto).build())
											 .saldo(saldo)
											 .build();
					return usuario;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Usuario pegarDadosDoUsuarioPessoais(String usuarioCpf) throws ValidacaoException {
		try(connection) {
			String sql = "SELECT rg, nome, sobrenome, email, ddd, numero_celular, data_nascimento, confirmou_identidade FROM usuarios WHERE cpf = ?";
			try(PreparedStatement ps = connection.prepareStatement(sql)) {
				ps.setString(1, usuarioCpf);
				try(ResultSet rs = ps.executeQuery()) {
					boolean encontrou = rs.next();
					if (!encontrou) {
						throw new UsuarioNaoEncontradoException(usuarioCpf);
					}
					
					String rg = rs.getString("rg");
					String nome = rs.getString("nome");
					String sobrenome = rs.getString("sobrenome");
					String email = rs.getString("email");
					Celular telefone = new Celular(rs.getString("ddd"), rs.getString("numero_celular"));
					boolean confirmouIdentidade = rs.getBoolean("confirmou_identidade");
					
					Usuario.UsuarioBuilder usuarioBuilder = Usuario.builder()
												.cpf(usuarioCpf)
												.nome(nome)
												.sobrenome(sobrenome)
												.email(email)
												.celular(telefone)
												.confirmouIdentidade(confirmouIdentidade);
					
					if (rg != null) {
						System.out.println("Aqui " + rg);
						usuarioBuilder.rg(new RG(rg));
					}
					
					return usuarioBuilder.build();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public BigDecimal pegarSaldoPorUsuarioCpf(String usuarioId) {
		return null;
	}

	@Override
	public void usuarioAlteraSeusDadosPessoais(Usuario usuario) throws AlterarUsuarioException {
		String sql = gerarSqlUsuarioAlteraSeusDadosPessoais(usuario);

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			int index = 1;
			if (usuario.getCpf() != null) {
				ps.setString(index, usuario.getCpf());
				index++;
			}
			if (usuario.getRg() != null) {
				ps.setString(index, usuario.getRg().getNumero());
				index++;
			}
			if (usuario.getNome() != null) {
				ps.setString(index, usuario.getNome());
				index++;
			}
			if (usuario.getSobrenome() != null) {
				ps.setString(index, usuario.getSobrenome());
				index++;
			}
			if (usuario.getEmail() != null) {
				ps.setString(index, usuario.getEmail());
				index++;
			}
			if (usuario.getCelular() != null) {
				if (usuario.getCelular().getDdd() != null) {
					ps.setString(index, usuario.getCelular().getDdd());
					index++;
				}
				if (usuario.getCelular().getNumero() != null) {
					ps.setString(index, usuario.getCelular().getNumero());
					index++;
				}
			}
			if (usuario.getDataNascimento() != null) {
				ps.setDate(index, Date.valueOf(usuario.getDataNascimento()));
				index++;
			}
			ps.setString(index, usuario.getCpf());
			int executeUpdate = ps.executeUpdate();

			if (executeUpdate == 0) {
				throw new AlterarUsuarioException("Não foi possivel alterar os dados do perfil de jogador");
			}
		} catch (SQLException e) {
			String mensagem = "Erro ao alterar usuário";
			e.printStackTrace();
			switch (e.getSQLState()) {
				case "23505": // Código de erro para violação de restrição única
					if (e.getMessage().contains("usuarios_cpf_key")) {
						mensagem = "CPF já cadastrado";
					} else if (e.getMessage().contains("usuarios_email_key")) {
						mensagem = "E-mail já cadastrado";
					} else if (e.getMessage().contains("usuarios_numero_celular_key")) {
						mensagem = "Número de celular já cadastrado";
					}
					break;
				default:
					break;
			}
			throw new AlterarUsuarioException(mensagem);
		}
	}

	@Override
	public void usuarioAlteraSuaSenha(String novaSenha, String email) {
		String sql = "UPDATE usuarios SET senha = ? WHERE email = ?";
		try (PreparedStatement updatePs = connection.prepareStatement(sql)) {
			updatePs.setString(1, novaSenha);
			updatePs.setString(2, email);
			int executeUpdate = updatePs.executeUpdate();

			if (executeUpdate == 0) {
				throw new RuntimeException("Não foi possível alterar a senha do usuário");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean verificarSeUsuarioJaConfirmouIdentidade(String usuarioId) throws UsuarioNaoEncontradoException {
		String sql = "SELECT confirmou_identidade FROM usuarios WHERE id = ?";
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setString(1, usuarioId);
			try (ResultSet rs = ps.executeQuery()) {
				boolean encontrou = rs.next();
				if (!encontrou) {
					throw new UsuarioNaoEncontradoException(usuarioId);
				}

				return rs.getBoolean("confirmou_identidade");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void novoSaldo(BigDecimal novoSaldo) {

	}

	private String gerarSqlUsuarioAlteraSeusDadosPessoais(Usuario usuario) {
        StringBuilder sql = new StringBuilder("UPDATE usuarios SET ");
        boolean first = true;
		if (usuario.getCpf() != null) {
            sql.append("cpf = ?");
            first = false;
		}
		if (usuario.getRg() != null) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("rg = ?");
            first = false;
		}
        if (usuario.getNome() != null) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("nome = ?");
            first = false;
        }
        if (usuario.getSobrenome() != null) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("sobrenome = ?");
            first = false;
        }
        if (usuario.getEmail() != null) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("email = ?");
            first = false;
        }
        if (usuario.getCelular() != null) {
        	if (usuario.getCelular().getDdd() != null) {
        		if (!first) {
        			sql.append(", ");
        		}
        		sql.append("ddd = ?");
        		first = false;
        	}
        	if (usuario.getCelular().getNumero() != null) {
        		if (!first) {
        			sql.append(", ");
        		}
        		sql.append("numero_celular = ?");
        		first = false;
        	}
        }
        if (usuario.getDataNascimento() != null) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("data_nascimento = ?");
            first = false;
        }
        
        sql.append(" WHERE id = ?");
        return sql.toString();
    }
	

}

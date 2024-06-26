package com.apostassa.infra.gateway.usuario.perfilparticipante;

import com.apostassa.aplicacao.gateway.usuario.perfilparticipante.RepositorioDePerfilParticipanteUser;
import com.apostassa.dominio.ValidacaoException;
import com.apostassa.dominio.usuario.perfilparticipante.AlterarPerfilParticipanteException;
import com.apostassa.dominio.usuario.perfilparticipante.PerfilParticipante;

import java.sql.*;

public class RepositorioDePerfilParticipanteUserComJdbcPostgres implements RepositorioDePerfilParticipanteUser {

    private final Connection connection;

    public RepositorioDePerfilParticipanteUserComJdbcPostgres(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void criarPerfilParticipante(PerfilParticipante perfilParticipante) throws ValidacaoException {
        String sql = """
				INSERT INTO usuarios_perfis_participantes
				(usuario_cpf, data_e_hora_criacao, ativo)
				VALUES (?, ?, ?)""";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, perfilParticipante.getUsuarioCpf());
            ps.setTimestamp(2, Timestamp.valueOf(perfilParticipante.getDataCriacao()));
            ps.setBoolean(3, perfilParticipante.getAtivo());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            String mensagem = "Erro ao criar perfil de participante";
            throw new ValidacaoException(mensagem);
        }
    }

    @Override
    public void alterarPerfilParticipante(PerfilParticipante perfilParticipante) throws AlterarPerfilParticipanteException {
        String sql = gerarSqlUsuarioAlteraSeuPerfilDeParticipante(perfilParticipante);
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int index = 1;
            if (perfilParticipante.getFoto() != null) {
                ps.setString(index, perfilParticipante.getFoto());
                index++;
            }
            if (perfilParticipante.getInstagram() != null) {
                ps.setString(index, perfilParticipante.getInstagram());
                index++;
            }
            if (perfilParticipante.getFacebook() != null) {
                ps.setString(index, perfilParticipante.getFacebook());
                index++;
            }
            if (perfilParticipante.getTwitter() != null) {
                ps.setString(index, perfilParticipante.getTwitter());
                index++;
            }
            if (perfilParticipante.getFrase() != null) {
                ps.setString(index, perfilParticipante.getFrase());
                index++;
            }
            ps.setString(index, perfilParticipante.getUsuarioCpf().toString());
            int executeUpdate = ps.executeUpdate();

            if (executeUpdate == 0) {
                throw new AlterarPerfilParticipanteException("Não foi possivel alterar os dados do perfil de participante");
            }
        } catch (SQLException e) {
            throw new AlterarPerfilParticipanteException(e.getMessage());
        }
    }

    @Override
    public PerfilParticipante pegarDadosPerfilParticipante(String usuarioCpf) throws ValidacaoException {
        try(connection){
            String sql = "SELECT foto, instagram, facebook, twitter, frase, ativo FROM usuarios_perfis_participantes WHERE usuario_cpf = ?";
            try(PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, usuarioCpf);
                try(ResultSet rs = ps.executeQuery()) {
                    boolean encontrou = rs.next();
                    if (!encontrou) {
                        throw new ValidacaoException(usuarioCpf);
                    }

                    String foto = rs.getString("foto");
                    String instagram = rs.getString("instagram");
                    String facebook = rs.getString("facebook");
                    String twitter = rs.getString("twitter");
                    String frase = rs.getString("frase");
                    Boolean ativo = rs.getBoolean("ativo");

                    PerfilParticipante perfilJogador = PerfilParticipante.builder()
                            .foto(foto)
                            .instagram(instagram)
                            .facebook(facebook)
                            .twitter(twitter)
                            .frase(frase)
                            .ativo(ativo).build();

                    return perfilJogador;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String gerarSqlUsuarioAlteraSeuPerfilDeParticipante(PerfilParticipante perfilParticipante) {
        StringBuilder sql = new StringBuilder("UPDATE usuarios_perfis_participantes SET ");
        boolean first = true;
        if (perfilParticipante.getFoto() != null) {
            sql.append("foto = ?");
            first = false;
        }
        if (perfilParticipante.getInstagram() != null) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("instagram = ?");
            first = false;
        }
        if (perfilParticipante.getFacebook() != null) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("facebook = ?");
            first = false;
        }
        if (perfilParticipante.getTwitter() != null) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("twitter = ?");
            first = false;
        }
        if (perfilParticipante.getFrase() != null) {
            if (!first) {
                sql.append(", ");
            }
            sql.append("frase = ?");
            first = false;
        }

        sql.append(" WHERE usuario_cpf = ?");
        return sql.toString();
    }
}

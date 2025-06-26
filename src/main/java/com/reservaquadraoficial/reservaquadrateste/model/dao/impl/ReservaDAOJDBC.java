package com.reservaquadraoficial.reservaquadrateste.model.dao.impl;
import com.reservaquadraoficial.reservaquadrateste.db.DB;
import com.reservaquadraoficial.reservaquadrateste.model.dao.ReservaDAO;
import com.reservaquadraoficial.reservaquadrateste.model.entities.Reserva;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOJDBC implements ReservaDAO {
    private Connection conn;

    private static final LocalTime[] HORARIOS = {
            LocalTime.of(7, 0), LocalTime.of(9, 0),
            LocalTime.of(9, 0), LocalTime.of(11, 0),
            LocalTime.of(11, 0), LocalTime.of(13, 0),
            LocalTime.of(13, 0), LocalTime.of(15, 0),
            LocalTime.of(15, 0), LocalTime.of(17, 0),
            LocalTime.of(17, 0), LocalTime.of(19, 0),
            LocalTime.of(19, 0), LocalTime.of(21, 0),
            LocalTime.of(21, 0), LocalTime.of(22, 0)
    };

    public ReservaDAOJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void inserir(Reserva reserva) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "INSERT INTO reservas (nome_pessoa, esporte, data, hora_inicio, hora_fim) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);

            st.setString(1, reserva.getNomePessoa());
            st.setString(2, reserva.getEsporte());
            st.setDate(3, Date.valueOf(reserva.getData()));
            st.setTime(4, Time.valueOf(reserva.getHoraInicio()));
            st.setTime(5, Time.valueOf(reserva.getHoraFim()));

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    reserva.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir reserva", e);
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void atualizar(Reserva reserva) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(
                    "UPDATE reservas SET nome_pessoa = ?, esporte = ?, data = ?, " +
                            "hora_inicio = ?, hora_fim = ? WHERE id = ?");

            st.setString(1, reserva.getNomePessoa());
            st.setString(2, reserva.getEsporte());
            st.setDate(3, Date.valueOf(reserva.getData()));
            st.setTime(4, Time.valueOf(reserva.getHoraInicio()));
            st.setTime(5, Time.valueOf(reserva.getHoraFim()));
            st.setInt(6, reserva.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar reserva", e);
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deletar(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM reservas WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar reserva", e);
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Reserva buscarPorId(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM reservas WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instanciarReserva(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reserva por ID", e);
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Reserva> buscarTodos() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM reservas ORDER BY data, hora_inicio");
            rs = st.executeQuery();

            List<Reserva> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(instanciarReserva(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar todas as reservas", e);
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Reserva> buscarPorData(LocalDate data) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM reservas WHERE data = ? ORDER BY hora_inicio");
            st.setDate(1, Date.valueOf(data));
            rs = st.executeQuery();

            List<Reserva> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(instanciarReserva(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas por data", e);
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Reserva> buscarPorEsporte(String esporte) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM reservas WHERE esporte LIKE ? ORDER BY data, hora_inicio");
            st.setString(1, "%" + esporte + "%");
            rs = st.executeQuery();

            List<Reserva> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(instanciarReserva(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas por esporte", e);
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Reserva> buscarPorNome(String nome) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT * FROM reservas WHERE nome_pessoa LIKE ? ORDER BY data, hora_inicio");
            st.setString(1, "%" + nome + "%");
            rs = st.executeQuery();

            List<Reserva> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(instanciarReserva(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar reservas por nome", e);
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<LocalTime[]> getHorariosDisponiveis(LocalDate data) {
        List<LocalTime[]> disponiveis = new ArrayList<>();
        List<Reserva> reservas = buscarPorData(data);

        // Converter para lista de horários já reservados
        List<LocalTime> horariosReservados = new ArrayList<>();
        for (Reserva r : reservas) {
            horariosReservados.add(r.getHoraInicio());
        }

        // Verificar cada bloco de horário
        for (int i = 0; i < HORARIOS.length; i += 2) {
            LocalTime inicio = HORARIOS[i];

            if (!horariosReservados.contains(inicio)) {
                disponiveis.add(new LocalTime[]{inicio, HORARIOS[i+1]});
            }
        }

        return disponiveis;
    }

    @Override
    public boolean existeConflito(LocalDate data, LocalTime inicio, LocalTime fim, Integer idExcluir) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE data = ? AND " +
                "((hora_inicio < ? AND hora_fim > ?) OR " +
                "(hora_inicio < ? AND hora_fim > ?) OR " +
                "(hora_inicio >= ? AND hora_fim <= ?))";

        if (idExcluir != null) {
            sql += " AND id != ?";
        }

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setDate(1, Date.valueOf(data));
            st.setTime(2, Time.valueOf(fim));
            st.setTime(3, Time.valueOf(inicio));
            st.setTime(4, Time.valueOf(fim));
            st.setTime(5, Time.valueOf(inicio));
            st.setTime(6, Time.valueOf(inicio));
            st.setTime(7, Time.valueOf(fim));

            if (idExcluir != null) {
                st.setInt(8, idExcluir);
            }

            try (ResultSet rs = st.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar conflito de horário", e);
        }
    }

    private Reserva instanciarReserva(ResultSet rs) throws SQLException {
        Reserva reserva = new Reserva();
        reserva.setId(rs.getInt("id"));
        reserva.setNomePessoa(rs.getString("nome_pessoa"));
        reserva.setEsporte(rs.getString("esporte"));
        reserva.setData(rs.getDate("data").toLocalDate());
        reserva.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
        reserva.setHoraFim(rs.getTime("hora_fim").toLocalTime());
        return reserva;
    }
}

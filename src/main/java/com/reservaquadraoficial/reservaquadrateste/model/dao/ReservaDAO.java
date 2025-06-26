package com.reservaquadraoficial.reservaquadrateste.model.dao;

import com.reservaquadraoficial.reservaquadrateste.model.entities.Reserva;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservaDAO {
    void inserir(Reserva reserva);
    void atualizar(Reserva reserva);
    void deletar(Integer id);
    Reserva buscarPorId(Integer id);
    List<Reserva> buscarTodos();
    List<Reserva> buscarPorData(LocalDate data);
    List<Reserva> buscarPorEsporte(String esporte);
    List<Reserva> buscarPorNome(String nome);
    List<LocalTime[]> getHorariosDisponiveis(LocalDate data);
    boolean existeConflito(LocalDate data, LocalTime inicio, LocalTime fim, Integer idExcluir);
}

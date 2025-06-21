package com.reservaquadraoficial.reservaquadrateste.model.entities;

import java.time.LocalDate;
import java.time.LocalTime;


public class Reserva {
    private Integer id;
    private String nomePessoa;
    private String esporte;
    private LocalDate data;
    private LocalTime horaInicio;
    private LocalTime horaFim;

    public Reserva() {}

    public Reserva(String nomePessoa, String esporte, LocalDate data, LocalTime horaInicio, LocalTime horaFim) {
        this.nomePessoa = nomePessoa;
        this.esporte = esporte;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getEsporte() {
        return esporte;
    }

    public void setEsporte(String esporte) {
        this.esporte = esporte;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s às %s-%s)",
                nomePessoa, esporte, data, horaInicio, horaFim);
    }
}

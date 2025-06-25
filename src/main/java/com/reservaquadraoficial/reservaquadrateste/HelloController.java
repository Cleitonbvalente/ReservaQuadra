package com.reservaquadraoficial.reservaquadrateste;

import com.reservaquadraoficial.reservaquadrateste.HelloApplication;
import javafx.fxml.FXML;

import java.io.IOException;

public class HelloController {
    @FXML
    public void onCadastrarReservaClicked() {
        try {
            HelloApplication.criarTela("cadastrar-reserva-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBuscarReservaClicked() {
        try {
            HelloApplication.criarTela("buscar-reserva-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onEditarReservaClicked() {
        try {
            HelloApplication.criarTela("editar-reserva-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onExcluirReservaClicked() {
        try {
            HelloApplication.criarTela("excluir-reserva-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
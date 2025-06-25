package com.reservaquadraoficial.reservaquadrateste;


import com.reservaquadraoficial.reservaquadrateste.model.dao.DaoFactory;
import com.reservaquadraoficial.reservaquadrateste.model.dao.ReservaDAO;
import com.reservaquadraoficial.reservaquadrateste.model.entities.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.*;
        import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class CadastrarHorarioController {

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> cbHorario;
    @FXML private TextField txtNome;
    @FXML private ComboBox<String> cbEsporte;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    private final ReservaDAO dao = DaoFactory.createReservaDAO();

    @FXML
    public void initialize() {
        // Configurar ComboBox de esportes
        cbEsporte.getItems().addAll("Futebol", "Vôlei", "Basquete", "Handebol", "Tênis", "Outro");

        // Configurar horários fixos de 2 em 2 horas (7-9, 9-11, etc.)
        for (int i = 7; i < 22; i += 2) {
            String horaFim = (i + 2) > 22 ? "22:00" : String.format("%02d:00", i + 2);
            cbHorario.getItems().add(String.format("%02d:00 - %02d:00", i, Integer.parseInt(horaFim.split(":")[0])));
        }
    }

    @FXML
    private void onBtnSalvar() {
        try {
            if (validarCampos()) {
                Reserva reserva = criarReserva();
                dao.inserir(reserva);
                fecharJanela();
                mostrarAlerta("Sucesso", "Horário reservado com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao reservar horário: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onBtnCancelar() {
        fecharJanela();
    }

    private boolean validarCampos() {
        if (datePicker.getValue() == null) {
            mostrarAlerta("Aviso", "Selecione uma data válida", Alert.AlertType.WARNING);
            return false;
        }
        if (cbHorario.getValue() == null) {
            mostrarAlerta("Aviso", "Selecione um horário disponível", Alert.AlertType.WARNING);
            return false;
        }
        if (txtNome.getText().isEmpty()) {
            mostrarAlerta("Aviso", "Informe o nome do responsável", Alert.AlertType.WARNING);
            return false;
        }
        if (cbEsporte.getValue() == null) {
            mostrarAlerta("Aviso", "Selecione um esporte", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private Reserva criarReserva() {
        String[] horarios = cbHorario.getValue().split(" - ");
        return new Reserva(
                txtNome.getText(),
                cbEsporte.getValue(),
                datePicker.getValue(),
                LocalTime.parse(horarios[0]),
                LocalTime.parse(horarios[1])
        );
    }

    private void fecharJanela() {
        ((Stage) btnSalvar.getScene().getWindow()).close();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

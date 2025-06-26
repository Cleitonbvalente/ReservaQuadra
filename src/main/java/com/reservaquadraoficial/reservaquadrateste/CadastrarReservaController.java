package com.reservaquadraoficial.reservaquadrateste;

import com.reservaquadraoficial.reservaquadrateste.model.dao.DaoFactory;
import com.reservaquadraoficial.reservaquadrateste.model.dao.ReservaDAO;
import com.reservaquadraoficial.reservaquadrateste.model.entities.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class CadastrarReservaController {

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
            cbHorario.getItems().add(String.format("%02d:00 - %s", i, horaFim));
        }
    }

    @FXML
    private void onBtnSalvar() {
        try {
            if (validarCampos()) {
                String[] horarios = cbHorario.getValue().split(" - ");
                LocalTime inicio = LocalTime.parse(horarios[0]);
                LocalTime fim = LocalTime.parse(horarios[1]);

                if (dao.existeConflito(datePicker.getValue(), inicio, fim, null)) {
                    mostrarAlerta("Conflito", "Já existe uma reserva neste horário", Alert.AlertType.WARNING);
                    return;
                }

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
        StringBuilder erros = new StringBuilder();

        if (datePicker.getValue() == null) {
            erros.append("• Selecione uma data válida\n");
        } else if (datePicker.getValue().isBefore(LocalDate.now())) {
            erros.append("• Data não pode ser no passado\n");
        }

        if (cbHorario.getValue() == null) {
            erros.append("• Selecione um horário disponível\n");
        }

        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            erros.append("• Informe o nome do responsável\n");
        }

        if (cbEsporte.getValue() == null) {
            erros.append("• Selecione um esporte\n");
        }

        if (erros.length() > 0) {
            mostrarAlerta("Aviso", "Corrija os seguintes erros:\n\n" + erros.toString(),
                    Alert.AlertType.WARNING);
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

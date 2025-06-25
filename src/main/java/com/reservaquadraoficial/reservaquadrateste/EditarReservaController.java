package com.reservaquadraoficial.reservaquadrateste;

import com.reservaquadraoficial.reservaquadrateste.model.dao.DaoFactory;
import com.reservaquadraoficial.reservaquadrateste.model.dao.ReservaDAO;
import com.reservaquadraoficial.reservaquadrateste.model.entities.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.*;
        import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;

public class EditarReservaController {

    @FXML private TextField txtId;
    @FXML private TextField txtNome;
    @FXML private ComboBox<String> cbEsporte;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> cbHorario;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    private Reserva reservaSelecionada;
    private final ReservaDAO dao = DaoFactory.createReservaDAO();

    @FXML
    public void initialize() {
        // Configurar ComboBox de esportes
        cbEsporte.getItems().addAll("Futebol", "Vôlei", "Basquete", "Handebol", "Tênis");

        // Configurar horários fixos de 2 em 2 horas
        for (int i = 7; i < 22; i += 2) {
            String horaFim = (i + 2) > 22 ? "22:00" : String.format("%02d:00", i + 2);
            cbHorario.getItems().add(String.format("%02d:00 - %s", i, horaFim));
        }

        // Desabilitar campo ID (somente leitura)
        txtId.setDisable(true);
    }

    public void setReservaSelecionada(Reserva reserva) {
        this.reservaSelecionada = reserva;
        carregarDadosReserva();
    }

    private void carregarDadosReserva() {
        if (reservaSelecionada != null) {
            txtId.setText(reservaSelecionada.getId().toString());
            txtNome.setText(reservaSelecionada.getNomePessoa());
            cbEsporte.setValue(reservaSelecionada.getEsporte());
            datePicker.setValue(reservaSelecionada.getData());

            // Formatar horário para o ComboBox
            String horarioFormatado = String.format("%02d:%02d - %02d:%02d",
                    reservaSelecionada.getHoraInicio().getHour(),
                    reservaSelecionada.getHoraInicio().getMinute(),
                    reservaSelecionada.getHoraFim().getHour(),
                    reservaSelecionada.getHoraFim().getMinute());

            cbHorario.setValue(horarioFormatado);
        }
    }

    @FXML
    private void onBtnSalvar() {
        try {
            if (validarCampos()) {
                atualizarReserva();
                dao.atualizar(reservaSelecionada);
                fecharJanela();
                mostrarAlerta("Sucesso", "Reserva atualizada com sucesso!", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao atualizar reserva: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onBtnCancelar() {
        fecharJanela();
    }

    private boolean validarCampos() {
        if (txtNome.getText().isEmpty()) {
            mostrarAlerta("Aviso", "Informe o nome do responsável", Alert.AlertType.WARNING);
            return false;
        }
        if (cbEsporte.getValue() == null) {
            mostrarAlerta("Aviso", "Selecione um esporte", Alert.AlertType.WARNING);
            return false;
        }
        if (datePicker.getValue() == null) {
            mostrarAlerta("Aviso", "Selecione uma data válida", Alert.AlertType.WARNING);
            return false;
        }
        if (cbHorario.getValue() == null) {
            mostrarAlerta("Aviso", "Selecione um horário disponível", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void atualizarReserva() {
        reservaSelecionada.setNomePessoa(txtNome.getText());
        reservaSelecionada.setEsporte(cbEsporte.getValue());
        reservaSelecionada.setData(datePicker.getValue());

        // Extrair horário do formato "HH:mm - HH:mm"
        String[] horarios = cbHorario.getValue().split(" - ");
        reservaSelecionada.setHoraInicio(LocalTime.parse(horarios[0]));
        reservaSelecionada.setHoraFim(LocalTime.parse(horarios[1]));
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

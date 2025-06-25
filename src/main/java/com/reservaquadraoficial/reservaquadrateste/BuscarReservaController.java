package com.reservaquadraoficial.reservaquadrateste;

import com.reservaquadraoficial.reservaquadrateste.model.dao.DaoFactory;
import com.reservaquadraoficial.reservaquadrateste.model.dao.ReservaDAO;
import com.reservaquadraoficial.reservaquadrateste.model.entities.Reserva;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class BuscarReservaController {

    @FXML private DatePicker datePicker;
    @FXML private TextField txtNome;
    @FXML private ComboBox<String> cbEsporte;
    @FXML private TableView<Reserva> tableView;
    @FXML private TableColumn<Reserva, String> colNome;
    @FXML private TableColumn<Reserva, String> colEsporte;
    @FXML private TableColumn<Reserva, LocalDate> colData;
    @FXML private TableColumn<Reserva, String> colHorario;
    @FXML private Button btnBuscar;
    @FXML private Button btnLimpar;

    private final ReservaDAO dao = DaoFactory.createReservaDAO();

    @FXML
    public void initialize() {
        // Configurar colunas da tabela
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomePessoa"));
        colEsporte.setCellValueFactory(new PropertyValueFactory<>("esporte"));
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colHorario.setCellValueFactory(cellData -> {
            Reserva reserva = cellData.getValue();
            return javafx.beans.binding.Bindings.concat(
                    reserva.getHoraInicio().toString(), " - ", reserva.getHoraFim().toString()
            );
        });

        // Configurar ComboBox de esportes
        cbEsporte.getItems().addAll("Futebol", "Vôlei", "Basquete", "Handebol", "Tênis", "Todos");
        cbEsporte.getSelectionModel().selectLast();

        // Carregar todas as reservas inicialmente
        atualizarTabela(dao.buscarTodos());
    }

    @FXML
    private void onBtnBuscar() {
        try {
            LocalDate data = datePicker.getValue();
            String nome = txtNome.getText().trim();
            String esporte = cbEsporte.getValue();

            List<Reserva> reservas;

            if (data != null) {
                // Busca por data específica
                reservas = dao.buscarPorData(data);
            } else if (!nome.isEmpty()) {
                // Busca por nome
                reservas = dao.buscarPorNome(nome);
            } else if (!esporte.equals("Todos")) {
                // Busca por esporte
                reservas = dao.buscarPorEsporte(esporte);
            } else {
                // Mostrar todas as reservas
                reservas = dao.buscarTodos();
            }

            atualizarTabela(reservas);

        } catch (Exception e) {
            mostrarAlerta("Erro", "Erro ao buscar reservas: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void onBtnLimpar() {
        datePicker.setValue(null);
        txtNome.clear();
        cbEsporte.getSelectionModel().selectLast();
        atualizarTabela(dao.buscarTodos());
    }

    private void atualizarTabela(List<Reserva> reservas) {
        tableView.setItems(FXCollections.observableArrayList(reservas));
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    @FXML
    private void onEditarReserva() {
        Reserva reservaSelecionada = tableView.getSelectionModel().getSelectedItem();
        if (reservaSelecionada != null) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        HelloApplication.class.getResource("editar-reserva-view.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(loader.load()));

                EditarReservaController controller = loader.getController();
                controller.setReservaSelecionada(reservaSelecionada);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                // Atualizar tabela após edição
                atualizarTabela(dao.buscarTodos());

            } catch (IOException e) {
                mostrarAlerta("Erro", "Não foi possível abrir a tela de edição", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Aviso", "Selecione uma reserva para editar", Alert.AlertType.WARNING);
        }
    }
}

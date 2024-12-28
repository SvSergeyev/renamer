package tech.sergeyev.renamer.api;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tech.sergeyev.renamer.service.RenameService;

import java.io.File;
import java.util.List;

public class MainController {
    private static final String RED = "#ff0000";
    private static final String BLACK = "#000000";

    public MainController() {
        this.service = new RenameService();
    }

    private final RenameService service;

    @FXML
    private Label mintrudFilesLabel;

    @FXML
    private Label expertFilesLabel;

    @FXML
    private List<File> mintrudFiles;

    @FXML
    private List<File> expertFiles;

    @FXML
    private TextArea logTextArea;

    @FXML
    // Выгрузка - откуда берем имя
    // Выписка - куда вставляем
    protected void chooseMultipleMintrudFiles() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файлы выписки");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Файл PDF", "*.pdf"));
        var stage = (Stage) mintrudFilesLabel.getScene().getWindow();
        mintrudFiles = fileChooser.showOpenMultipleDialog(stage);
        if (mintrudFiles != null && !mintrudFiles.isEmpty()) {
            mintrudFilesLabel.setText("Выбрано файлов: " + mintrudFiles.size());
            mintrudFilesLabel.setTextFill(Paint.valueOf(BLACK));
        }
    }

    @FXML
    // Выгрузка - откуда берем имя
    // Выписка - куда вставляем
    protected void chooseMultipleExpertFiles() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите файлы выгрузки");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Файл SOUT", "*.sout"));
        var stage = (Stage) expertFilesLabel.getScene().getWindow();
        expertFiles = fileChooser.showOpenMultipleDialog(stage);
        if (expertFiles != null && !expertFiles.isEmpty()) {
            expertFilesLabel.setText("Выбрано файлов: " + expertFiles.size());
            expertFilesLabel.setTextFill(Paint.valueOf(BLACK));
        }
    }

    @FXML
    protected void onRenameButtonClick() {
        var result = service.rename(mintrudFiles, expertFiles);
        for (var message : result.getMessages()) {
            logTextArea.appendText(message + "\n");
        }
    }
}
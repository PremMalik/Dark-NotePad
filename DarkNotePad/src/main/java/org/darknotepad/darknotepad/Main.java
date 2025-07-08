package org.darknotepad.darknotepad;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    private TextArea textArea;
    private File currentFile = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dark Notepad");

        textArea = new TextArea();
        textArea.setWrapText(true);  // Word wrap
        textArea.getStyleClass().add("text-area");

        // Menu Bar
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAsItem = new MenuItem("Save As");

        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem);
        menuBar.getMenus().addAll(fileMenu);

        // Actions
        newItem.setOnAction(e -> newFile());
        openItem.setOnAction(e -> openFile(primaryStage));
        saveItem.setOnAction(e -> saveFile(primaryStage));
        saveAsItem.setOnAction(e -> saveAsFile(primaryStage));

        // Keyboard Shortcuts
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));

        BorderPane layout = new BorderPane();
        layout.setTop(menuBar);
        layout.setCenter(textArea);

        Scene scene = new Scene(layout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void newFile() {
        textArea.clear();
        currentFile = null;
    }

    private void openFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            currentFile = file;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                textArea.setText(sb.toString());
            } catch (IOException ex) {
                showError("Error opening file.");
            }
        }
    }

    private void saveFile(Stage stage) {
        if (currentFile != null) {
            writeFile(currentFile);
        } else {
            saveAsFile(stage);
        }
    }

    private void saveAsFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            currentFile = file;
            writeFile(file);
        }
    }

    private void writeFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(textArea.getText());
        } catch (IOException ex) {
            showError("Error saving file.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

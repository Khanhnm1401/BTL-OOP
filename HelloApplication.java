package com.example.demo1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {

    private Stage window;
    private Scene loginScene, registerScene, forgotPasswordScene, mainScene;
    private Map<String, String> users = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        loadUsersFromFile();

        window = primaryStage;
        window.setTitle("Sổ Thu Chi");

        // Màn hình đăng nhập
        GridPane loginGrid = createFormPane();
        TextField loginUsername = createTextField("Tên đăng nhập");
        PasswordField loginPassword = createPasswordField("Mật khẩu");
        Label loginMessage = new Label();

        loginGrid.add(createLabel("Đăng nhập"), 0, 0, 2, 1);
        loginGrid.add(new Label("Tên đăng nhập:"), 0, 1);
        loginGrid.add(loginUsername, 1, 1);
        loginGrid.add(new Label("Mật khẩu:"), 0, 2);
        loginGrid.add(loginPassword, 1, 2);
        loginGrid.add(createButton("Đăng nhập", e -> handleLogin(loginUsername, loginPassword, loginMessage)), 1, 3);
        loginGrid.add(createButton("Đăng ký", e -> switchScene(registerScene)), 0, 4);
        loginGrid.add(createButton("Quên mật khẩu", e -> switchScene(forgotPasswordScene)), 1, 4);
        loginGrid.add(loginMessage, 0, 5, 2, 1);

        loginScene = new Scene(loginGrid, 400, 300);

        // Màn hình đăng ký
        GridPane registerGrid = createFormPane();
        TextField registerUsername = createTextField("Tên đăng nhập");
        PasswordField registerPassword = createPasswordField("Mật khẩu");
        PasswordField confirmPassword = createPasswordField("Xác nhận mật khẩu");
        Label registerMessage = new Label();

        registerGrid.add(createLabel("Đăng ký"), 0, 0, 2, 1);
        registerGrid.add(new Label("Tên đăng nhập:"), 0, 1);
        registerGrid.add(registerUsername, 1, 1);
        registerGrid.add(new Label("Mật khẩu:"), 0, 2);
        registerGrid.add(registerPassword, 1, 2);
        registerGrid.add(new Label("Xác nhận mật khẩu:"), 0, 3);
        registerGrid.add(confirmPassword, 1, 3);
        registerGrid.add(createButton("Đăng ký", e -> handleRegister(registerUsername, registerPassword, confirmPassword, registerMessage)), 1, 4);
        registerGrid.add(createButton("Quay lại Đăng nhập", e -> switchScene(loginScene)), 1, 5);
        registerGrid.add(registerMessage, 0, 6, 2, 1);

        registerScene = new Scene(registerGrid, 400, 300);

        // Màn hình quên mật khẩu
        GridPane forgotPasswordGrid = createFormPane();
        TextField forgotUsername = createTextField("Tên đăng nhập");
        Label forgotPasswordMessage = new Label();

        forgotPasswordGrid.add(createLabel("Quên mật khẩu"), 0, 0, 2, 1);
        forgotPasswordGrid.add(new Label("Tên đăng nhập:"), 0, 1);
        forgotPasswordGrid.add(forgotUsername, 1, 1);
        forgotPasswordGrid.add(createButton("Lấy lại mật khẩu", e -> handleForgotPassword(forgotUsername, forgotPasswordMessage)), 1, 2);
        forgotPasswordGrid.add(forgotPasswordMessage, 0, 3, 2, 1);
        forgotPasswordGrid.add(createButton("Quay lại Đăng nhập", e -> switchScene(loginScene)), 1, 4);

        forgotPasswordScene = new Scene(forgotPasswordGrid, 400, 300);

        // Màn hình chính sau khi đăng nhập
        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(createLabel("Chào mừng bạn đến với Sổ Thu Chi!"), createButton("Đăng xuất", e -> switchScene(loginScene)));

        mainScene = new Scene(mainLayout, 400, 300);

        window.setScene(loginScene);
        window.show();
    }

    private void handleLogin(TextField usernameField, PasswordField passwordField, Label messageLabel) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (users.containsKey(username) && users.get(username).equals(password)) {
            messageLabel.setText("Đăng nhập thành công!");
            switchScene(mainScene);
        } else {
            messageLabel.setText("Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    }

    private void handleRegister(TextField usernameField, PasswordField passwordField, PasswordField confirmPasswordField, Label messageLabel) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPasswordText = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Tên đăng nhập và mật khẩu không được để trống!");
        } else if (!password.equals(confirmPasswordText)) {
            messageLabel.setText("Mật khẩu không khớp!");
        } else if (users.containsKey(username)) {
            messageLabel.setText("Tên đăng nhập đã tồn tại!");
        } else {
            users.put(username, password);
            saveUserToFile(username, password);
            messageLabel.setText("Đăng ký thành công!");
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        }
    }

    private void handleForgotPassword(TextField usernameField, Label messageLabel) {
        String username = usernameField.getText().trim();
        if (users.containsKey(username)) {
            messageLabel.setText("Mật khẩu của bạn là: " + users.get(username));
        } else {
            messageLabel.setText("Tên đăng nhập không tồn tại!");
        }
    }

    private void switchScene(Scene scene) {
        window.setScene(scene);
    }

    private GridPane createFormPane() {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(20, 20, 20, 20));
        pane.setVgap(10);
        pane.setHgap(10);
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    private TextField createTextField(String promptText) {
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        return textField;
    }

    private PasswordField createPasswordField(String promptText) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(promptText);
        return passwordField;
    }

    private Label createLabel(String text) {
        return new Label(text);
    }

    private Button createButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        return button;
    }

    private void loadUsersFromFile() {
        File file = new File("users.txt");
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) users.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            System.out.println("Không thể tải tài khoản: " + e.getMessage());
        }
    }

    private void saveUserToFile(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt", true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Không thể lưu tài khoản: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

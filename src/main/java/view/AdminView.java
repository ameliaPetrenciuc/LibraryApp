package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import model.Role;
import model.User;
import model.builder.UserBuilder;

import java.util.List;

public class AdminView {
    private TableView<User> userTableView;
    private TextField usernameTextField;
    private TextField passwordTextField;
    private ComboBox<String> rolesBox=new ComboBox<>();
    private Button createUserButton;
    private Button deleteUserButton;
    private Button pdfButton;

    public AdminView(Stage primaryStage) {
        primaryStage.setTitle("Admin Panel");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        initTableView(gridPane);
        initActionButtons(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane) {
        userTableView = new TableView<>();

        TableColumn<User, String> userColumn = new TableColumn<>("Username");
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userColumn.setMinWidth(200);

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(user -> {
            List<Role> roles = user.getValue().getRoles();
            String roleNames = roles.stream().map(Role::getRole).reduce((a, b) -> a + ", " + b).orElse("No Roles");
            return new SimpleStringProperty(roleNames);
        });

        userTableView.getColumns().addAll(userColumn, roleColumn);

        VBox tableContainer = new VBox(userTableView);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setPadding(new Insets(20));

        gridPane.add(tableContainer, 0, 1, 3, 1);
    }

    private void initActionButtons(GridPane gridPane) {
        Label usernameLabel = new Label("Username:");
        gridPane.add(usernameLabel, 0, 2);
        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 1, 2);

        Label passwordLabel = new Label("Password:");
        gridPane.add(passwordLabel, 2, 2);
        passwordTextField = new TextField();
        gridPane.add(passwordTextField, 3, 2);

        Label roleLabel = new Label("Role:");
        gridPane.add(roleLabel, 0, 3);
//        rolesBox = new ComboBox<>();
        rolesBox.getItems().addAll("Administrator", "Employee", "Customer");
        gridPane.add(rolesBox, 1, 3);

        createUserButton = new Button("Create User");
        gridPane.add(createUserButton, 0, 4);

        deleteUserButton = new Button("Delete User");
        gridPane.add(deleteUserButton, 1, 4);

        pdfButton = new Button("Generate PDF");
        gridPane.add(pdfButton, 2, 4);
    }

    public void addCreateUserButtonListener(EventHandler<ActionEvent> listener) {
        createUserButton.setOnAction(listener);
    }

    public void addDeleteUserButtonListener(EventHandler<ActionEvent> listener) {
        deleteUserButton.setOnAction(listener);
    }

    public void addPdfButtonListener(EventHandler<ActionEvent> listener) {
        pdfButton.setOnAction(listener);
    }

//    public String getUsername() {
//        return usernameTextField.getText();
//    }
//
//    public String getPassword() {
//        return passwordTextField.getText();
//    }

    public String getSelectedRole() {
        return rolesBox.getValue();
    }

    public TableView<User> getUserTableView() {
        return userTableView;
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public void setListOfUsers(List<User> data) {
        // Transformă lista într-o ObservableList
        ObservableList<User> observableUserList = FXCollections.observableList(data);

        // Setează datele în tabelul utilizatorilor
        userTableView.setItems(observableUserList);

        // Reîmprospătează tabelul pentru a reflecta noile date
        userTableView.refresh();
    }

    public User getNewEmployee() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        return new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .build();
    }

//    public User getSelectedEmployee(List<User> data) {
//        ObservableList<User> observableUserList = FXCollections.observableList(data);
//        return observableUserList.getSelectionModel().getSelectedItem();
//    }

    public void setUsersInTable(ObservableList<User> users) {
        userTableView.setItems(users);
    }
    public Long getRoleBox() {
        String selectedRole = rolesBox.getSelectionModel().getSelectedItem();
        if (selectedRole != null) {
            switch (selectedRole) {
                case "Administrator":
                    return 1L;
                case "Employee":
                    return 2L;
                case "Customer":
                    return 3L;
                default:
                    return null;
            }
        }
        return null;
    }

}


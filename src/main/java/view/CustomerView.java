package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

import view.model.BookDTO;



import java.util.List;

public class CustomerView {
    private TableView bookTableView;
    private final ObservableList<BookDTO> booksObservableList;
    private TextField quantityTextField;
    private Label quantityLabel;
    private Button buyButton;

    public CustomerView(Stage primaryStage, List<BookDTO> bookDTO){
        primaryStage.setTitle("Library");

        GridPane gridPane=new GridPane();
        initializedGridPage(gridPane);

        Scene scene=new Scene(gridPane, 720,480);
        primaryStage.setScene(scene);

        booksObservableList= FXCollections.observableArrayList(bookDTO);
        initTableView(gridPane);
        initSaveOptions(gridPane);
        primaryStage.show();
    }

    private void initializedGridPage(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25,25,25,25));
    }

    private void initTableView(GridPane gridPane){
        bookTableView=new TableView<BookDTO>();

        bookTableView.setPlaceholder(new Label("No books to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<BookDTO, String> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        bookTableView.getColumns().addAll(titleColumn, authorColumn, priceColumn, stockColumn);
        bookTableView.setItems(booksObservableList);

        gridPane.add(bookTableView,0,0,5,1);
    }

    private void initSaveOptions(GridPane gridPane){
        quantityLabel=new Label("Quantity");
        gridPane.add(quantityLabel,1,1);
        quantityTextField=new TextField();
        gridPane.add(quantityTextField,2,1);

        buyButton=new Button("Buy");
        gridPane.add(buyButton,3,1);

    }

    public void addBuyButtonListener(EventHandler<ActionEvent> buyButtonListener){
        buyButton.setOnAction(buyButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }


    public Long getQuantity(){
        return Long.valueOf(quantityTextField.getText());
    }

    public TableView getBookTableView(){
        return bookTableView;
    }

}

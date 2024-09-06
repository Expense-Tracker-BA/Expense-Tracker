package Frontend;

import Backend.Business_Layer.Expense;
import Backend.Service_Layer.ResponseT;
import Backend.Service_Layer.Service_Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ExpenseTrackerController {
    @FXML
    private Button clear_button;
    @FXML
    private Label dateRangeLabel;

    @FXML
    private TextField lowerDateRangeText;

    @FXML
    private TextField upperDateRangeText;

    @FXML
    private Label dateRangeErrorMessage;

    @FXML
    private TableView<Expense> expenseTable;

    @FXML
    private TableColumn<Expense, Integer> IDColumn;

    @FXML
    private TableColumn<Expense, String> descriptionColumn;

    @FXML
    private TableColumn<Expense, Double> costColumn;

    @FXML
    private TableColumn<Expense, String> dateColumn;

    @FXML
    private TableColumn<Expense, String> categoryColumn;


    private ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the columns in the table
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpense_date_string()));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));


        expenseTable.setItems(expenseList);
        ExtractAll();

    }

    private void ExtractAll()
    {
        ResponseT<List<Expense>> expensesResponse = Service_Controller.GetInstance().ExtractAll();
        expenseList.setAll(expensesResponse.Value);
    }


    @FXML
    protected void onDateRangeShowButtonClick() {
        String lowerDate = lowerDateRangeText.getText();
        String upperDate = upperDateRangeText.getText();

        ResponseT<List<Expense>> expensesResponse = Service_Controller.GetInstance().ExtractInDateRange(lowerDate, upperDate);

        if (expensesResponse.ErrorOccured()) {
            dateRangeErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            dateRangeErrorMessage.setText("");
            // Populate the table with the returned expenses
            List<Expense> expenses = expensesResponse.Value;
            expenseList.setAll(expenses);  // Updates the TableView with the new data
            clear_button.setVisible(true);
        }

    }

    @FXML
    public void Clear_filters() {
        clear_button.setVisible(false);
        ExtractAll();
    }
}

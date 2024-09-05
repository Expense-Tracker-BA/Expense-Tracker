package Frontend;

import Backend.Expense;
import Backend.Service_Layer.ResponseT;
import Backend.Service_Layer.Service_Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ExpenseTrackerController {
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
    private TableColumn<Expense, String> descriptionColumn;

    @FXML
    private TableColumn<Expense, Double> costColumn;

    @FXML
    private TableColumn<Expense, LocalDate> dateColumn;

    @FXML
    private TableColumn<Expense, String> categoryColumn;

    private ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the columns in the table
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("expense_date"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Initially empty table
        expenseTable.setItems(expenseList);
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
        }
    }
}

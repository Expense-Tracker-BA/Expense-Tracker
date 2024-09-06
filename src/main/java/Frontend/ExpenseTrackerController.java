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
    private Label totalCostLabel;
    @FXML
    private Button desc_filter_button;
    @FXML
    private TextField description_filter_text;
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
        ResponseT<Double> total_cost=Service_Controller.GetInstance().Get_Curr_Sum();
        String formattedCost = String.format("%.3f", total_cost.Value);
        this.totalCostLabel.setText("Total Cost: $"+formattedCost);

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
        description_filter_text.setDisable(false);
        description_filter_text.setText("");
        desc_filter_button.setDisable(false);

        ExtractAll();
    }
    @FXML
    public void on_desc_filter_Click( ) {
        String description = description_filter_text.getText();
        ResponseT<List<Expense>> expensesResponse = Service_Controller.GetInstance().ExtractByDescription(description);

        if (expensesResponse.ErrorOccured()) {
            dateRangeErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            dateRangeErrorMessage.setText("");
            List<Expense> expenses = expensesResponse.Value;
            expenseList.setAll(expenses);
            clear_button.setVisible(true);
            description_filter_text.setDisable(true);
            desc_filter_button.setDisable(true);
            ResponseT<Double> total_cost=Service_Controller.GetInstance().Get_Curr_Sum();
            String formattedCost = String.format("%.3f", total_cost.Value);
            this.totalCostLabel.setText("Total Cost: $"+formattedCost);
        }
    }
}

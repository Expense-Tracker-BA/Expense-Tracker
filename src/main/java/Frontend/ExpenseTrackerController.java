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


    public TextField Expense_ID_to_remove;
    public Button Remove_expense_button;
    public Label remove_expense_ErrorMessage;
    @FXML
    private Label desc_filter_ErrorMessage;
    @FXML
    private Label insert_expense_ErrorMessage;
    @FXML
    private TextField new_expense_date;
    @FXML
    private TextField new_expense_desc;
    @FXML
    private TextField new_expense_cost;
    @FXML
    private ChoiceBox<String> categorychoicebox;
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
        ObservableList<String> categories = FXCollections.observableArrayList(
                "Clothes", "Transport", "Food", "Electronics"
        );
        categorychoicebox.setItems(categories);

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
            desc_filter_ErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            desc_filter_ErrorMessage.setText("");
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

    public void on_insert_expense_Click(ActionEvent actionEvent) {
        String description = new_expense_desc.getText();
        String Date= new_expense_date.getText();
        Double cost=Double.parseDouble(new_expense_cost.getText());
        String category=categorychoicebox.getValue();
        ResponseT<String> expensesResponse = Service_Controller.GetInstance().AddExpense(Date,description,cost,category);

        if (expensesResponse.ErrorOccured()) {
            insert_expense_ErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            insert_expense_ErrorMessage.setText("");
            ExtractAll();
        }
    }

    public void on_remove_expense_Click() {
        String ID = Expense_ID_to_remove.getText();



        ResponseT<String> expensesResponse = Service_Controller.GetInstance().RemoveExpense(ID);

        if (expensesResponse.ErrorOccured()) {
            remove_expense_ErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            remove_expense_ErrorMessage.setText("");
            ExtractAll();
        }
    }
}

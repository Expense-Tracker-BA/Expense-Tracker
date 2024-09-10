package Frontend;

import Backend.Business_Layer.Expense;
import Backend.Service_Layer.ResponseT;
import Backend.Service_Layer.Service_Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ExpenseTrackerController {


    public TextField Expense_ID_to_remove;
    public Button Remove_expense_button;
    public Label remove_expense_ErrorMessage;
    public TextField expense_to_update_id;
    public Label expense_to_update_date_label;
    public TextField expense_to_update_date_text;
    public Label expense_to_update_Description_label;
    public TextField expense_to_update_Description_text;
    public Label expense_to_update_Cost_label;
    public TextField expense_to_update_cost_text;
    public Label expense_to_update_category_label;
    public ChoiceBox<String> categorychoicebox_update_expense;
    public Button update_expense_button;
    public Label update_expense_ErrorMessage;
    public Button show_expense_to_update_button;
    public Label show_expense_to_update_message;
    public Button date_filter_button;
    public Label costRangeLabel;
    public TextField lowerCostRangeText;
    public TextField upperCostRangeText;
    public Button cost_filter_button;
    public Label costRangeErrorMessage;
    public Label CategoryLabel;
    public Button category_filter_button;
    public Label categoryErrorMessage;
    public ListView<CheckBox> categorySelectListView;
    public PieChart expensePieChart;
    public VBox percentageLabels;

    private ObservableList<CheckBox> checkBoxItems = FXCollections.observableArrayList();
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

    @FXML
    private BarChart<String, Number> expenseBarChart;

    @FXML
    private CategoryAxis dateAxis;

    @FXML
    private NumberAxis priceAxis;

    private ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the columns in the table

        IDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getExpense_date_string()));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        dateColumn.setComparator(dateComparator);
        expenseTable.setItems(expenseList);
        ExtractAll();
        ObservableList<String> categories = FXCollections.observableArrayList(
                "Clothes", "Transport", "Food", "Electronics"
        );
        categorychoicebox.setItems(categories);
        categorychoicebox_update_expense.setItems(categories);

        checkBoxItems.addAll(
                new CheckBox("Clothes"),
                new CheckBox("Transport"),
                new CheckBox("Food"),
                new CheckBox("Electronics")
        );

        categorySelectListView.setItems(checkBoxItems);
        // Customize the ListView to properly handle CheckBoxes
        categorySelectListView.setCellFactory(new Callback<ListView<CheckBox>, ListCell<CheckBox>>() {
            @Override
            public ListCell<CheckBox> call(ListView<CheckBox> param) {
                return new ListCell<CheckBox>() {
                    @Override
                    protected void updateItem(CheckBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setGraphic(item);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }



    private static Comparator<String> dateComparator = (date1, date2) -> {

        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate d1 = LocalDate.parse(date1, DATE_FORMATTER);
        LocalDate d2 = LocalDate.parse(date2, DATE_FORMATTER);
        return d1.compareTo(d2);
    };


    private void ExtractAll()
    {
        ResponseT<List<Expense>> expensesResponse = Service_Controller.GetInstance().ExtractAll();
        expenseList.setAll(expensesResponse.Value);
        ResponseT<Double> total_cost=Service_Controller.GetInstance().Get_Curr_Sum();
        String formattedCost = String.format("%.3f", total_cost.Value);
        this.totalCostLabel.setText("Total Cost: $"+formattedCost);
        hide_update_expense_fields();
        update_pie_chart(expensesResponse.Value);
        update_bar_chart(expensesResponse.Value);
    }

    private void update_bar_chart(List<Expense> expenses) {
        String cost = "Cost";
        String Date = "Date";
        //Getting the data into the dictionary:
        Map<String, Double> dateToTotalPriceMap = new HashMap<>();
        for(Expense e : expenses){
            if(!dateToTotalPriceMap.containsKey(e.getExpense_date_string())){
                dateToTotalPriceMap.put(e.getExpense_date_string(), e.getCost());
            }
            else {
                double temp = dateToTotalPriceMap.get(e.getExpense_date_string());
                dateToTotalPriceMap.replace(e.getExpense_date_string(), temp, temp + e.getCost());
            }
        }

        //Sorting the data from the lowest date first:
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        Map<String, Double> sortedMap = new LinkedHashMap<>();
        dateToTotalPriceMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> {
                    // Parse the date strings to LocalDate objects
                    LocalDate date1 = LocalDate.parse(entry1.getKey(), formatter);
                    LocalDate date2 = LocalDate.parse(entry2.getKey(), formatter);
                    // Compare dates
                    return date1.compareTo(date2);
                })
                .forEachOrdered(entry -> sortedMap.put(entry.getKey(), entry.getValue()));

        //Creating the bar chart with the updated data:
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");

        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(),entry.getValue()));
        }

        expenseBarChart.getData().clear();  // Clear old data
        expenseBarChart.getData().add(series);  // Add new data
    }

    private void update_pie_chart(List<Expense> expenses) {
        String Clothes="Clothes";
        String Food="Food";
        String Transport="Transport";
        String Electronics="Electronics";
        double Clothes_count=0;
        double Food_count=0;
        double Transport_count=0;
        double Electronics_count=0;
        double total=expenses.size();
        for(Expense e:expenses)
        {
            switch (e.getCategory()){
                case "Clothes": Clothes_count=Clothes_count+1;break;
                case "Food": Food_count=Food_count+1; break;
                case "Transport": Transport_count=Transport_count+1; break;
                case "Electronics": Electronics_count=Electronics_count+1;break;
            }
        }
        percentageLabels.getChildren().clear(); // this clears the percentage label
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        if(Clothes_count!=0)
        {
            pieChartData.add( new PieChart.Data("Clothes", (Clothes_count/total)*100));
            String formattedpercent = String.format("%.1f", (Clothes_count/total)*100); // to show only 1 digit after the zero point
            Label clothes_label = new Label("Clothes: "+formattedpercent+"%");
            percentageLabels.getChildren().add(clothes_label);

        }
        if(Food_count!=0)
        {
            pieChartData.add( new PieChart.Data("Food", (Food_count/total)*100));
            String formattedpercent = String.format("%.1f", (Food_count/total)*100); // to show only 1 digit after the zero point
            Label Food_label = new Label("Food: "+formattedpercent+"%");
            percentageLabels.getChildren().add(Food_label);
        }
        if(Transport_count!=0)
        {
            pieChartData.add( new PieChart.Data("Transport", (Transport_count/total)*100));
            String formattedpercent = String.format("%.1f", (Transport_count/total)*100); // to show only 1 digit after the zero point
            Label Transport_label = new Label("Transport: "+formattedpercent+"%");
            percentageLabels.getChildren().add(Transport_label);
        }
        if(Electronics_count!=0)
        {
            pieChartData.add( new PieChart.Data("Electronics", (Electronics_count/total)*100));
            String formattedpercent = String.format("%.1f", (Electronics_count/total)*100); // to show only 1 digit after the zero point
            Label Electronics_label = new Label("Electronics: "+formattedpercent+"%");
            percentageLabels.getChildren().add(Electronics_label);
        }

        expensePieChart.setData(pieChartData);
    }

    private void hide_update_expense_fields() {
        expense_to_update_date_label.setVisible(false);
        expense_to_update_date_text.setVisible(false);
        expense_to_update_category_label.setVisible(false);
        categorychoicebox_update_expense.setVisible(false);
        update_expense_button.setVisible(false);
        update_expense_ErrorMessage.setVisible(false);
        expense_to_update_Cost_label.setVisible(false);
        expense_to_update_cost_text.setVisible(false);
        expense_to_update_Description_label.setVisible(false);
        expense_to_update_Description_text.setVisible(false);
        show_expense_to_update_message.setVisible(true);
        show_expense_to_update_button.setVisible(true);
        expense_to_update_id.setText("");
    }


    @FXML
    protected void onDateRangeShowButtonClick() {
        String lowerDate = lowerDateRangeText.getText();
        String upperDate = upperDateRangeText.getText();

        ResponseT<List<Expense>> expensesResponse = Service_Controller.GetInstance().ExtractInDateRange(lowerDate, upperDate);

        if (expensesResponse.ErrorOccured()) {
            dateRangeErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            clear_all_messages();
            // Populate the table with the returned expenses
            List<Expense> expenses = expensesResponse.Value;
            expenseList.setAll(expenses);  // Updates the TableView with the new data
            update_pie_chart(expenses);
            update_bar_chart(expenses);
            clear_button.setVisible(true);
            lowerDateRangeText.setDisable(true);
            upperDateRangeText.setDisable(true);
            date_filter_button.setDisable(true);
            ResponseT<Double> total_cost=Service_Controller.GetInstance().Get_Curr_Sum();
            String formattedCost = String.format("%.3f", total_cost.Value);
            this.totalCostLabel.setText("Total Cost: $"+formattedCost);
            hide_update_expense_fields();
        }
    }

    private void clear_all_messages() {
        show_expense_to_update_message.setText("");
        update_expense_ErrorMessage.setText("");
        remove_expense_ErrorMessage.setText("");
        dateRangeErrorMessage.setText("");
        insert_expense_ErrorMessage.setText("");
        desc_filter_ErrorMessage.setText("");
        costRangeErrorMessage.setText("");
        categoryErrorMessage.setText("");
    }

    @FXML
    public void Clear_filters() {
        clear_button.setVisible(false);
        description_filter_text.setDisable(false);
        description_filter_text.setText("");
        desc_filter_button.setDisable(false);
        lowerDateRangeText.setDisable(false);
        upperDateRangeText.setDisable(false);
        date_filter_button.setDisable(false);
        lowerCostRangeText.setDisable(false);
        upperCostRangeText.setDisable(false);
        cost_filter_button.setDisable(false);
        categorySelectListView.getItems().forEach(checkBox -> {
            checkBox.setDisable(false);
        });
        category_filter_button.setDisable(false);
        ExtractAll();
    }
    @FXML
    public void on_desc_filter_Click( ) {
        String description = description_filter_text.getText();
        ResponseT<List<Expense>> expensesResponse = Service_Controller.GetInstance().ExtractByDescription(description);

        if (expensesResponse.ErrorOccured()) {
            desc_filter_ErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            clear_all_messages();
            List<Expense> expenses = expensesResponse.Value;
            expenseList.setAll(expenses);
            update_pie_chart(expenses);
            update_bar_chart(expenses);
            clear_button.setVisible(true);
            description_filter_text.setDisable(true);
            desc_filter_button.setDisable(true);
            ResponseT<Double> total_cost=Service_Controller.GetInstance().Get_Curr_Sum();
            String formattedCost = String.format("%.3f", total_cost.Value);
            this.totalCostLabel.setText("Total Cost: $"+formattedCost);
            hide_update_expense_fields();
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
            clear_all_messages();
            ExtractAll();
            hide_update_expense_fields();
        }
    }

    public void on_remove_expense_Click() {
        String ID = Expense_ID_to_remove.getText();



        ResponseT<String> expensesResponse = Service_Controller.GetInstance().RemoveExpense(ID);

        if (expensesResponse.ErrorOccured()) {
            remove_expense_ErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            clear_all_messages();
            ExtractAll();
            hide_update_expense_fields();
        }
    }

    public void on_update_expense_Click() {
        ResponseT<String> expensesResponse = Service_Controller.GetInstance().UpdateExpense(Integer.parseInt(expense_to_update_id.getText()), expense_to_update_Description_text.getText(), expense_to_update_cost_text.getText(), expense_to_update_date_text.getText(), categorychoicebox_update_expense.getValue());

        if (expensesResponse.ErrorOccured()) {
            update_expense_ErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            clear_all_messages();
            ExtractAll();
            hide_update_expense_fields();
        }
    }

    public void on_show_expense_to_update_Click( ) {
        String ID=expense_to_update_id.getText();
        ResponseT<Expense> expensesResponse = Service_Controller.GetInstance().ExtractByID(ID);

        if (expensesResponse.ErrorOccured()) {
            show_expense_to_update_message.setText(expensesResponse.ErrorMessage);
        } else {

            show_expense_to_update_message.setVisible(false);
            show_expense_to_update_button.setVisible(false);

            expense_to_update_date_label.setVisible(true);
            expense_to_update_date_text.setVisible(true);
            expense_to_update_category_label.setVisible(true);
            categorychoicebox_update_expense.setVisible(true);
            update_expense_button.setVisible(true);
            update_expense_ErrorMessage.setVisible(true);
            expense_to_update_Cost_label.setVisible(true);
            expense_to_update_cost_text.setVisible(true);
            expense_to_update_Description_label.setVisible(true);
            expense_to_update_Description_text.setVisible(true);
            expense_to_update_date_text.setText(expensesResponse.Value.getExpense_date_string());
            categorychoicebox_update_expense.setValue(expensesResponse.Value.getCategory());
            expense_to_update_cost_text.setText(Double.toString(expensesResponse.Value.getCost()));
            expense_to_update_Description_text.setText(expensesResponse.Value.getDescription());
        }

    }

    public void onCostRangeShowButtonClick() {
        String lowerCost = lowerCostRangeText.getText();
        String upperCost = upperCostRangeText.getText();

        ResponseT<List<Expense>> expensesResponse = Service_Controller.GetInstance().ExtractByCost(lowerCost, upperCost);

        if (expensesResponse.ErrorOccured()) {
            costRangeErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            clear_all_messages();
            // Populate the table with the returned expenses
            List<Expense> expenses = expensesResponse.Value;
            expenseList.setAll(expenses);  // Updates the TableView with the new data
            update_pie_chart(expenses);
            update_bar_chart(expenses);
            clear_button.setVisible(true);
            lowerCostRangeText.setDisable(true);
            upperCostRangeText.setDisable(true);
            cost_filter_button.setDisable(true);
            ResponseT<Double> total_cost=Service_Controller.GetInstance().Get_Curr_Sum();
            String formattedCost = String.format("%.3f", total_cost.Value);
            this.totalCostLabel.setText("Total Cost: $"+formattedCost);
            hide_update_expense_fields();
        }
    }

    public void onCategoryShowButtonClick() {
        AtomicInteger allBoxesChecked_counter = new AtomicInteger(0);
        List<String> selectedCategories = new ArrayList<>();
        categorySelectListView.getItems().forEach(checkBox -> {
            if (checkBox.isSelected()) {
                selectedCategories.add(checkBox.getText());
            }
        });

        ResponseT<List<Expense>> expensesResponse = Service_Controller.GetInstance().ExtractByCategory(selectedCategories);

        if (expensesResponse.ErrorOccured()) {
            categoryErrorMessage.setText(expensesResponse.ErrorMessage);
        } else {
            clear_all_messages();
            // Populate the table with the returned expenses
            List<Expense> expenses = expensesResponse.Value;
            update_pie_chart(expenses);
            update_bar_chart(expenses);
            expenseList.setAll(expenses);  // Updates the TableView with the new data
            clear_button.setVisible(true);
            categorySelectListView.getItems().forEach(checkBox -> {
                if (checkBox.isSelected()) {
                    allBoxesChecked_counter.incrementAndGet();
                }
                else{
                    checkBox.setDisable(true);
                }
            });
//            if(allBoxesChecked_counter.get() == categorySelectListView.getItems().size()){
//                category_filter_button.setDisable(true);
//            }
            ResponseT<Double> total_cost=Service_Controller.GetInstance().Get_Curr_Sum();
            String formattedCost = String.format("%.3f", total_cost.Value);
            this.totalCostLabel.setText("Total Cost: $"+formattedCost);
            hide_update_expense_fields();
        }
    }
}

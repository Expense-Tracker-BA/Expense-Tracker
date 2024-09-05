package com.example.expensetracker;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExpenseTrackerController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import control.MatrixCalculator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Matrix;

public class ShowScreenController implements Initializable {
	int showWorkRow = 0;
	int showWorkCol = 0;
	int showAnswerRow = 0;
	int showAnswerCol = 0;

	String[][] answer;
	String[][] showWork;

	@FXML
	private GridPane gridOne;
	@FXML
	private GridPane gridPaneTwo;

	public ShowScreenController() {
		gridOne = new GridPane();
		gridPaneTwo = new GridPane();
	}

	public void resetControls(int row, int col, int row2, int col2) {
		showWorkRow = row;
		showWorkCol = col;

		showAnswerRow = row2;
		showAnswerCol = col2;

		answer = new String[row][col];

		showWork = new String[row][col];
	}

	public void setFirstGrid() {
		if (showWork != null) {
			gridOne.getChildren().clear();
			for (int i = 0; i < showWorkRow; i++) {
				for (int j = 0; j < showWorkCol; j++) {
					TextField field = new TextField();
					field.setText(showWork[i][j]);
					field.setPrefWidth(field.getText().length() * 14);

					field.getStyleClass().add("gridTextField2");
					gridOne.add(field, j, i);
				}
			}
		}

		for (int i = 0; i < showAnswerRow; i++) {
			for (int j = 0; j < showAnswerCol; j++) {
				if (answer != null) {
					TextField field = new TextField();
					System.out.println("Is field null: " + field);
					System.out.println("Is answer null: " + answer[i][j]);
					if (answer[i][j] != null) {
						field.setText(answer[i][j]);
						field.setPrefWidth(field.getText().length() * 14);
						field.getStyleClass().add("gridTextField2");
						gridPaneTwo.add(field, j, i);
					}

				}

			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	public String[][] getAnswer() {
		return answer;
	}

	public void setAnswer(String[][] answer) {
		this.answer = answer;
	}

	public String[][] getShowWork() {
		return showWork;
	}

	public void setShowWork(String[][] showWork) {
		this.showWork = showWork;
	}

}

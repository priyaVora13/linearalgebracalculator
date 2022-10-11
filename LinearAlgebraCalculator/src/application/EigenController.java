package application;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import control.EigenCalculator;
import control.MatrixCalculator;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Matrix;
import model.Vector;
import model.VectorOrientation;

public class EigenController implements Initializable {
	double currentLambda = Integer.MAX_VALUE;
	EigenCalculator eigenCal = new EigenCalculator();
	MatrixCalculator cal = new MatrixCalculator();
	@FXML
	private Button Back;
	@FXML
	private ScrollPane scrollMatrixTwo;

	Map<String, TextField> mapTextField;
	Map<String, TextField> mapTextField2;
	Map<String, TextField> resultMap;

	@FXML
	AnchorPane secondMatrixAnchor;
	@FXML
	private Button calculateButton;
	@FXML
	private Button showWorkButton;
	@FXML
	private GridPane gridFirstMatrix;
	@FXML
	private GridPane gridSecondMatrix;

	@FXML
	private TextField lambdaField;
	@FXML
	private Label lambdaLabel;

	@FXML
	private Label showWorkLabel;

	@FXML
	private TextField firstRowSizeField;
	@FXML
	private TextField secondRowSizeField;
	@FXML
	private TextField firstColSizeField;
	@FXML
	private TextField secondColSizeField;
	@FXML
	private Label operationTypeLabel;
	@FXML
	private ChoiceBox<String> operationChoiceBox;
	@FXML
	private Button dimension1;
	@FXML
	private Button dimension2;
	@FXML
	private Button setButton;
	@FXML
	private Pane firstMatrix;
	@FXML
	private Pane secondMatrix;

	public EigenController() {
		resetGridTextFields();// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		scrollMatrixTwo = new ScrollPane();
		secondMatrixAnchor = new AnchorPane();
		secondMatrix = new Pane();
		setButton.setDisable(true);
		calculateButton.setDisable(true);
		operationChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				gridFirstMatrix.getChildren().clear();
				gridSecondMatrix.getChildren().clear();
				showWorkButton.setVisible(true);
				Integer indexSelected = operationChoiceBox.getSelectionModel().getSelectedIndex();
				System.out.println(operationChoiceBox.getItems().get(indexSelected));
				operationTypeLabel.setText(operationChoiceBox.getItems().get(indexSelected));

				if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
						.equals("Is EigenValue?")) {
					System.out.println("Print!");

					dimension2.setDisable(true);

					secondRowSizeField.setText("0");
					secondColSizeField.setText("0");

					secondRowSizeField.setDisable(true);
					secondColSizeField.setDisable(true);

					setButton.setDisable(false);
					calculateButton.setDisable(false);

					lambdaLabel.setVisible(true);
					lambdaField.setVisible(true);
					lambdaField.clear();
					lambdaField.setPromptText("Enter λ Value Here");

				} else if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
						.equals("Is EigenVector?(Corresponding)")) {

					lambdaLabel.setVisible(false);
					lambdaField.setVisible(false);

					dimension2.setDisable(false);

					secondRowSizeField.clear();
					secondColSizeField.clear();

					secondRowSizeField.setDisable(false);
					secondColSizeField.setDisable(false);

					setButton.setDisable(false);
					calculateButton.setDisable(false);

				}

				setButton.setDisable(false);
				calculateButton.setDisable(false);
			}
		});

		showWorkButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				System.out.println("Show Work!");
				if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
						.equals("Is EigenValue?")) {
					try {
						Desktop.getDesktop().open(new java.io.File(System.getProperty("user.home") + "/Desktop"
								+ "\\MatrixShowWork" + "\\IsEigenValue.txt"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
						.equals("Is EigenVector?(Corresponding)")) {
					try {
						Desktop.getDesktop().open(new java.io.File(System.getProperty("user.home") + "/Desktop"
								+ "\\MatrixShowWork" + "\\IsEigenVector.txt"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
	}

	public void resetGridTextFields() {
		mapTextField = new HashMap<>();
		mapTextField2 = new HashMap<>();
		resultMap = new HashMap<>();
		showWorkButton = new Button();
		showWorkLabel = new Label();

		operationChoiceBox = new ChoiceBox<String>();
		operationTypeLabel = new Label();
		calculateButton = new Button();
		setButton = new Button();
		gridFirstMatrix = new GridPane();
		gridSecondMatrix = new GridPane();

		firstMatrix = new Pane();
		secondMatrix = new Pane();
		firstRowSizeField = new TextField();
		firstColSizeField = new TextField();
		secondRowSizeField = new TextField();
		secondColSizeField = new TextField();
		dimension1 = new Button();
		dimension2 = new Button();
		lambdaField = new TextField();
		lambdaLabel = new Label();

	}

	@FXML
	private void calculateButtonAction(ActionEvent event) throws NumberFormatException, IOException {
		System.out.println("Calculated!");

		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		String secondRowSize = secondRowSizeField.getText().trim();
		String secondColSize = secondColSizeField.getText().trim();
		String lambdaValue = lambdaField.getText().trim();
		System.out.println("Lambda Value: " + lambdaValue);

		if ((operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
				.equals("Is EigenValue?"))) {
			lambdaLabel.setVisible(true);
			lambdaField.setVisible(true);
			if (lambdaValue == null || lambdaValue.isEmpty()) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Lambda Value must not be left empty.");
				alert.setContentText("Please set the value to prooceed!");

				alert.showAndWait();
			} else {

				currentLambda = Double.parseDouble(lambdaValue);

				matrixOperation(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize),
						Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));

			}
		} else if ((operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
				.equals("Is EigenVector?(Corresponding)"))) {
			matrixOperation(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize),
					Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));
		}

		showWorkLabel.setText("Click Far Right Button");
		showWorkLabel.setVisible(true);
	}

	@FXML
	private void setAction() {
		showWorkLabel.setVisible(false);
		gridFirstMatrix.getChildren().clear();
		gridSecondMatrix.getChildren().clear();
		int operationIndexSelected = operationChoiceBox.getSelectionModel().getSelectedIndex();
		String operationSelected = operationChoiceBox.getItems().get(operationIndexSelected);
		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		String secondRowSize = secondRowSizeField.getText().trim();
		String secondColSize = secondColSizeField.getText().trim();

		System.out.println(firstRowSize);
		System.out.println(firstColSize);
		System.out.println(secondRowSize);
		System.out.println(secondColSize);

		operationHelper(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize), Integer.parseInt(secondRowSize),
				Integer.parseInt(secondColSize), firstRowSize, firstColSize, secondRowSize, secondColSize);

		if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
				.equals("Is EigenValue?")) {
			System.out.println("Print!");

			dimension2.setDisable(true);

			secondRowSizeField.setText("0");
			secondColSizeField.setText("0");

			secondRowSizeField.setDisable(true);
			secondColSizeField.setDisable(true);

			setButton.setDisable(false);
			calculateButton.setDisable(false);

		}

	}

	private String generateMapCellName(int row, int col) {
		return "Cell_" + row + "_" + col;
	}

	public void matrixOperation(int row1, int col1, int row2, int col2) throws IOException {
		// I need to get the data from hashmap
		if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
				.equals("Is EigenValue?")) {
			double[][] dataOne = printHashMap(mapTextField, row1, col1);
			// double[][] dataTwo = printHashMap(mapTextField2, row2, col2);
			// Matrix b = new Matrix("SecondMatrix", row2, col2);
			// b.setCurrentMatrix(dataTwo);

			Matrix resultMatrix = new Matrix();

			Matrix a = new Matrix("FirstMatrix", row1, col1);
			a.setCurrentMatrix(dataOne);

			a.printMatrix();

			eigenCal.isEigenValue(a, currentLambda);

		} else if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
				.equals("Is EigenVector?(Corresponding)")) {
			double[][] dataOne = printHashMap(mapTextField, row1, col1);
			double[][] dataTwo = printHashMap(mapTextField2, row2, col2);

			Matrix resultMatrix = new Matrix();

			Matrix a = new Matrix("FirstMatrix", row1, col1);
			a.setCurrentMatrix(dataOne);

			Matrix givenVector = new Matrix("SecondMatrix", row2, col2);
			givenVector.setCurrentMatrix(dataTwo);
			a.printMatrix();

			eigenCal.isEigenVector(givenVector, a);
		}

	}

	public double[][] printHashMap(Map<String, TextField> map, int row, int col) {
		double[][] dataArray = new double[row][col];
		System.out.println("Prints Hashmap: ");
		for (int currentRow = 0; currentRow < row; currentRow++) {
			for (int currentCol = 0; currentCol < col; currentCol++) {
				System.out.print(currentRow);
				System.out.print(",");
				System.out.print(currentCol);
				System.out.print(" ");

				String getValueFrom = "Cell_" + currentRow + "_" + currentCol;
				if (map.get(getValueFrom).getText().trim().contains("/")) {
					// System.out.println("The value is a fraction...");
					// System.out.println("VALUE: " + map.get(getValueFrom).getText());

				} else {
					// System.out.println("The value is not a fraction");
					// System.out.println("VALUE: " + map.get(getValueFrom).getText());
					// System.out.println("????????????????????????????? ; " +
					// map.get(getValueFrom).getText().trim());
					dataArray[currentRow][currentCol] = Double.parseDouble(map.get(getValueFrom).getText().trim());
				}

			}
			System.out.println(" ");
		}
		return dataArray;
	}

	public void operationHelper(int row1Size, int col1Size, int row2Size, int col2Size, String firstRowSize,
			String firstColSize, String secondRowSize, String secondColSize) {
		gridFirstMatrix.getChildren().clear();
		gridSecondMatrix.getChildren().clear();
		for (int i = 0; i < row1Size; i++) {
			for (int j = 0; j < col1Size; j++) {
				String name = generateMapCellName(i, j);
				TextField field = new TextField();
				field.getStyleClass().add("gridTextField");
				gridFirstMatrix.add(field, j, i);
				mapTextField.put(name, field);
			}
		}

		for (int i = 0; i < row2Size; i++) {
			for (int j = 0; j < col2Size; j++) {
				String name = generateMapCellName(i, j);
				TextField field = new TextField();
				field.getStyleClass().add("gridTextField2");
				gridSecondMatrix.add(field, j, i);
				mapTextField2.put(name, field);
			}
		}

		if (firstRowSize.isEmpty() && firstColSize.isEmpty() && secondRowSize.isEmpty() && secondColSize.isEmpty()) {
			dimension1.setDisable(true);
			dimension2.setDisable(true);
		} else if (firstRowSize.isEmpty() || firstColSize.isEmpty() || secondRowSize.isEmpty()
				|| secondColSize.isEmpty()) {
			dimension1.setDisable(true);
			dimension2.setDisable(true);
		} else {
			dimension1.setDisable(false);
			dimension2.setDisable(false);

			String dimensionLabelOne = firstRowSize + "X" + firstColSize;
			String dimensionLabelTwo = secondRowSize + "X" + secondColSize;

			if (Integer.parseInt(firstRowSize) > 6 || Integer.parseInt(firstColSize) > 6
					|| Integer.parseInt(secondColSize) > 6 || Integer.parseInt(secondRowSize) > 6) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Dimensions are larger than 6X6");
				alert.setContentText("Click on Dimension button to calculate!");

				alert.showAndWait();

			} else {
				dimension1.setText(dimensionLabelOne);
				dimension2.setText(dimensionLabelTwo);
			}

		}
	}

	@FXML
	private void backAction(ActionEvent e) {
		System.out.println("Back!");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/CalculatorScreen.fxml"));
		Parent root;
		try {
			root = loader.load();
			CalculatorScreen controller = loader.getController();

			// call method from controller to set Data
			Stage stage = new Stage();
			stage.setTitle("Linear Algebra Calculator");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
			// Hide this current window (if this is what you want)
			((Node) (e.getSource())).getScene().getWindow().hide();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}

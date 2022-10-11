package application;

import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import control.MatrixCalculator;
import control.VectorCalculator;
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

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Matrix;
import model.Vector;
import model.VectorOrientation;

public class CalculatorScreen implements Initializable {
	MatrixCalculator cal = new MatrixCalculator();
	VectorCalculator vcal = new VectorCalculator();
	Map<String, TextField> mapTextField;
	Map<String, TextField> mapTextField2;
	Map<String, TextField> resultMap;

	int resultRow = 0;
	int resultCol = 0;

	@FXML
	private Button calculateButton;
	@FXML
	private Button showWorkButton;
	@FXML
	private GridPane gridFirstMatrix;
	@FXML
	private GridPane gridSecondMatrix;

	@FXML
	private GridPane resultGrid;
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

	public CalculatorScreen() {
		resetGridTextFields();
	}

	public void resetGridTextFields() {
		mapTextField = new HashMap<>();
		mapTextField2 = new HashMap<>();
		resultMap = new HashMap<>();
		showWorkButton = new Button();

		operationChoiceBox = new ChoiceBox<String>();
		operationTypeLabel = new Label();
		calculateButton = new Button();
		setButton = new Button();
		gridFirstMatrix = new GridPane();
		gridSecondMatrix = new GridPane();
		resultGrid = new GridPane();
		firstMatrix = new Pane();
		secondMatrix = new Pane();
		firstRowSizeField = new TextField();
		firstColSizeField = new TextField();
		secondRowSizeField = new TextField();
		secondColSizeField = new TextField();
		dimension1 = new Button();
		dimension2 = new Button();

	}

	private String generateMapCellName(int row, int col) {
		return "Cell_" + row + "_" + col;
	}

	public String doubleToFraction(double doubleVal) {
		double negligibleRatio = 0.01;
		String val = "";

		for (int i = 1;; i++) {
			double tem = doubleVal / (1D / i);
			if (Math.abs(tem - Math.round(tem)) < negligibleRatio) {

				val = Math.round(tem) + "/" + i;

				System.out.println(Math.round(tem) + "/" + i);
				break;

			}
		}
		return val;
	}

	public void addOperation() {
		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		int row1Size = Integer.parseInt(firstRowSize);
		int col1Size = Integer.parseInt(firstColSize);

		String secondRowSize = secondRowSizeField.getText().trim();
		String secondColSize = secondColSizeField.getText().trim();

		int row2Size = Integer.parseInt(secondRowSize);
		int col2Size = Integer.parseInt(secondColSize);

		if (row1Size == row2Size && col1Size == col2Size) {
			operationHelper(row1Size, col1Size, row2Size, col2Size, firstRowSize, firstColSize, secondRowSize,
					secondColSize);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Dimensions must be the same for both matrices!");
			alert.setContentText("Please Reset!");

			alert.showAndWait();

		}
	}

	public void dotProductOperation() {
		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		int row1Size = Integer.parseInt(firstRowSize);
		int col1Size = Integer.parseInt(firstColSize);

		String secondRowSize = secondRowSizeField.getText().trim();
		String secondColSize = secondColSizeField.getText().trim();

		int row2Size = Integer.parseInt(secondRowSize);
		int col2Size = Integer.parseInt(secondColSize);

		// set it to be vertical
		if (col1Size == 1 && col2Size == 1) {
			if (row1Size == row2Size && col1Size == col2Size) {
				operationHelper(row1Size, col1Size, row2Size, col2Size, firstRowSize, firstColSize, secondRowSize,
						secondColSize);
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Dimensions must be the same for both matrices!");
				alert.setContentText("Please Reset!");

				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("For Vertical Vectors : Column size must be one");
			alert.setContentText("Please Reset!");

			alert.showAndWait();

		}
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

			List<String> validPositionList = new ArrayList<String>();
			List<String> validPositionListTwo = new ArrayList<String>();

			validPositionList = getValidPositions(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize));
			System.out.println(" ");
			validPositionListTwo = getValidPositions(Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));
		}
	}

	public void multiplicationOperation() {
		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		int row1Size = Integer.parseInt(firstRowSize);
		int col1Size = Integer.parseInt(firstColSize);

		String secondRowSize = secondRowSizeField.getText().trim();
		String secondColSize = secondColSizeField.getText().trim();

		int row2Size = Integer.parseInt(secondRowSize);
		int col2Size = Integer.parseInt(secondColSize);

		if (col1Size == row2Size) {
			operationHelper(row1Size, col1Size, row2Size, col2Size, firstRowSize, firstColSize, secondRowSize,
					secondColSize);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("The column of first matrix must match the row of second matrix!");
			alert.setContentText("Please Reset!");

			alert.showAndWait();

		}
	}

	public void inverseOperation() {
		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		int row1Size = Integer.parseInt(firstRowSize);
		int col1Size = Integer.parseInt(firstColSize);

		operationHelper(row1Size, col1Size, 0, 0, firstRowSize, firstColSize, "", "");
	}

	public void tranposeOperation() {
		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		int row1Size = Integer.parseInt(firstRowSize);
		int col1Size = Integer.parseInt(firstColSize);

		operationHelper(row1Size, col1Size, 0, 0, firstRowSize, firstColSize, "", "");
	}

	public void determinantAndInverseOperationSettings() {
		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		int row1Size = Integer.parseInt(firstRowSize);
		int col1Size = Integer.parseInt(firstColSize);

		operationHelper(row1Size, col1Size, 0, 0, firstRowSize, firstColSize, "", "");
	}

	@FXML
	private void setAction() {

		mapTextField = new HashMap<>();
		mapTextField2 = new HashMap<>();
		resultMap = new HashMap<>();
		resultGrid.getChildren().clear();
		gridFirstMatrix.getChildren().clear();
		gridSecondMatrix.getChildren().clear();
		int operationIndexSelected = operationChoiceBox.getSelectionModel().getSelectedIndex();
		String operationSelected = operationChoiceBox.getItems().get(operationIndexSelected);
		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		String secondRowSize = secondRowSizeField.getText().trim();
		String secondColSize = secondColSizeField.getText().trim();
		if (firstRowSizeField.getText().trim().isEmpty() || firstColSizeField.getText().trim().isEmpty()
				|| secondRowSizeField.getText().trim().isEmpty() || secondColSizeField.getText().trim().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("You must reset the row and col dimensions!");
			alert.setContentText(" ");

			alert.showAndWait();

			if (operationSelected.equals("Matrice Transpose")) {
				dimension2.setDisable(true);
				gridSecondMatrix.setDisable(true);
				secondColSizeField.setText("0");
				secondRowSizeField.setText("0");
				secondRowSizeField.setDisable(true);
				secondColSizeField.setDisable(true);

			}

		} else {

			if (Integer.parseInt(firstRowSize) < 7 && Integer.parseInt(firstColSize) < 7
					&& Integer.parseInt(secondRowSize) < 7 && Integer.parseInt(secondColSize) < 7) {
				if (operationSelected.equals("Add Matrices") || operationSelected.equals("Subtract Matrices")) {

					addOperation();

				} else if (operationSelected.equals("Vector Dot Product")) {
					dotProductOperation();
				} else if (operationSelected.equals("Multiply Matrices")) {
					multiplicationOperation();
				} else if (operationSelected.equals("Determinant") || operationSelected.equals("Inverse Matrix")
						|| operationSelected.equals("Matrix Row Operation")
						|| operationSelected.equals("Matrice Transpose") || operationSelected.equals("Vector Magnitude")
						|| operationSelected.equals("Vector Unit Vector")) {
					dimension2.setDisable(true);
					gridSecondMatrix.setDisable(true);
					secondColSizeField.setText("0");
					secondRowSizeField.setText("0");
					secondRowSizeField.setDisable(true);
					secondColSizeField.setDisable(true);
					dimension1.setDisable(false);
					dimension1.setText(firstRowSize + "X" + firstColSize);
					determinantAndInverseOperationSettings();
				} else {
					System.out.println("User has not selected any operation...");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText("Must select an operation type to continue!");
					alert.setContentText("Select operation type in the dropdown.");
					alert.showAndWait();
				}
			} else {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("This Matrix Calculator only supports 6X6 Matrix");
				alert.setContentText("Sorry for the inconvience.");

				alert.showAndWait();
			}
		}
	}

	public List<String> getValidPositions(int rowSize, int colSize) {
		List<String> validPositionList = new ArrayList<String>();
		for (int row = 0; row < rowSize; row++) {
			for (int col = 0; col < colSize; col++) {
				String validPosition = row + "," + col + "";
				validPositionList.add(validPosition.trim());
				System.out.print(row);
				System.out.print(",");
				System.out.print(col);
				System.out.print(" ");
			}
			System.out.println(" ");
		}
		return validPositionList;
	}

	private void activelySetDimensions(String firstRowSize, String firstColSize, String secondRowSize,
			String secondColSize) {
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

			if (Integer.parseInt(firstRowSize) > 7 || Integer.parseInt(firstColSize) > 7
					|| Integer.parseInt(secondColSize) > 7 || Integer.parseInt(secondRowSize) > 7) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Information Dialog");
				alert.setHeaderText("Dimensions are larger than 6X6");
				alert.setContentText("Click on Dimension button to calculate!");

				alert.showAndWait();
				dimension1.setText(dimensionLabelOne);
				dimension2.setText(dimensionLabelTwo);
			}
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
					System.out.println("The value is a fraction...");
					System.out.println("VALUE: " + map.get(getValueFrom).getText());

				} else {
					System.out.println("The value is not a fraction");
					System.out.println("VALUE: " + map.get(getValueFrom).getText());
					System.out.println("????????????????????????????? ; " + map.get(getValueFrom).getText().trim());
					dataArray[currentRow][currentCol] = Double.parseDouble(map.get(getValueFrom).getText().trim());
				}

			}
			System.out.println(" ");
		}
		return dataArray;
	}

	public void matrixOperation(int row1, int col1, int row2, int col2) throws IOException {
		// I need to get the data from hashmap
		double[][] dataOne = printHashMap(mapTextField, row1, col1);
		double[][] dataTwo = printHashMap(mapTextField2, row2, col2);
		double determinantValue = Integer.MAX_VALUE;
		double dotProduct = Integer.MAX_VALUE;
		double magnitude = Integer.MAX_VALUE;
		Vector unitVector = new Vector();
		Matrix resultMatrix = new Matrix();

		Matrix a = new Matrix("FirstMatrix", row1, col1);
		a.setCurrentMatrix(dataOne);
		Matrix b = new Matrix("SecondMatrix", row2, col2);
		b.setCurrentMatrix(dataTwo);

		if (operationTypeLabel.getText().trim().equals("Inverse Matrix")) {
			double[][] dataCurrent = cal.inverse(dataOne);
			if (dataCurrent != null) {
				resultMatrix.setCurrentMatrix(dataCurrent);
				System.out.println("Print out: Inverse: ");
				resultMatrix.printMatrix();
			} else {
				resultMatrix = null;
			}
		} else if (operationTypeLabel.getText().equals("Add Matrices")) {
			resultMatrix = cal.addMatrices(a, b);
		} else if (operationTypeLabel.getText().equals("Subtract Matrices")) {
			resultMatrix = cal.subtractMatrices(a, b);
		} else if (operationTypeLabel.getText().equals("Multiply Matrices")) {
			resultMatrix = cal.multipyMatrices(a, b);
			System.out.println("Row: " + resultMatrix.getRow());
			System.out.println("Col: " + resultMatrix.getColumn());
		} else if (operationTypeLabel.getText().equals("Matrice Transpose")) {
			double[][] dataCurrent = MatrixCalculator.transpose(dataOne);
			resultMatrix.setCurrentMatrix(dataCurrent);
			System.out.println("Print out: Tranpose: ");
			dimension2.setDisable(true);
			gridSecondMatrix.setDisable(true);
			secondColSizeField.setText("0");
			secondRowSizeField.setText("0");
			secondRowSizeField.setDisable(true);
			secondColSizeField.setDisable(true);

			resultMatrix.printMatrix();
		} else if (operationTypeLabel.getText().equals("Determinant")) {
			dimension2.setDisable(true);
			gridSecondMatrix.setDisable(true);
			secondColSizeField.setText("0");
			secondRowSizeField.setText("0");
			secondRowSizeField.setDisable(true);
			secondColSizeField.setDisable(true);
			determinantValue = cal.determinant(dataOne);
			System.out.println("Determinant Value: " + determinantValue);

		} else if (operationTypeLabel.getText().equals("Matrix Row Operation")) {
			dimension2.setDisable(true);
			gridSecondMatrix.setDisable(true);
			secondColSizeField.setText("0");
			secondRowSizeField.setText("0");
			secondRowSizeField.setDisable(true);
			secondColSizeField.setDisable(true);
			double[][] dataArray = cal.rref(dataOne);
			resultMatrix.setCurrentMatrix(dataArray);
			System.out.println("Print out: RREF");
			resultMatrix.printMatrix();

		} else if (operationTypeLabel.getText().trim().equals("Vector Dot Product")) {
			int sizeA = dataOne.length * dataOne[0].length;
			int sizeB = dataTwo.length * dataTwo[0].length;
			double[] A = vcal.grabDataArray(sizeA, dataOne);
			double[] B = vcal.grabDataArray(sizeB, dataTwo);

			dotProduct = vcal.dotProduct(A, B);
			System.out.println(dotProduct);
		} else if (operationTypeLabel.getText().trim().equals("Vector Magnitude")) {
			Vector currentVector = new Vector();
			currentVector.setCurrentVector(dataOne);
			magnitude = vcal.magnitude(currentVector);
			System.out.println("Magnitude: " + magnitude);
		} else if (operationTypeLabel.getText().trim().equals("Vector Unit Vector")) {
			Vector currentVector = new Vector("A", row1, col1, VectorOrientation.VERTICAL);
			currentVector.setCurrentVector(dataOne);
			unitVector = vcal.unitVector(currentVector);
		}

		if (operationTypeLabel.getText().equals("Determinant")) {
			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < 1; j++) {
					String name = generateMapCellName(i, j);
					TextField field = new TextField();
					field.setText("Determinant: " + determinantValue);
					field.setPrefWidth(field.getText().length() * 14);
					field.getStyleClass().add("gridResultTextField");
					resultGrid.add(field, j, i);
					resultMap.put(name, field);
					System.out.println(resultMap.isEmpty());
				}
			}
		} else if (operationTypeLabel.getText().equals("Inverse Matrix")) {
			if (resultMatrix != null) {
				for (int i = 0; i < resultMatrix.getCurrentMatrix().length; i++) {
					for (int j = 0; j < resultMatrix.getCurrentMatrix()[0].length; j++) {
						String name = generateMapCellName(i, j);
						TextField field = new TextField();

						double value = resultMatrix.getCurrentMatrix()[i][j];
						String valueString = value + "";
						double check = 1.0 / 0;
						if (!valueString.contains("NaN")) {
							System.out.println("Value does not contain NaN");
							System.out.println(resultMatrix.getCurrentMatrix()[i][j]);
							String positionValue = doubleToFraction(resultMatrix.getCurrentMatrix()[i][j]);
							field.setText(positionValue);
							field.setPrefWidth(field.getText().length() * 14);
							field.getStyleClass().add("gridResultTextField");
							resultGrid.add(field, j, i);
							resultMap.put(name, field);
							System.out.println(resultMap.isEmpty());
						} else {
							String positionValue = resultMatrix.getCurrentMatrix()[i][j] + "";
							field.setText("n/a");
							field.setPrefWidth(field.getText().length() * 20);
							field.getStyleClass().add("gridResultTextField");
							resultGrid.add(field, j, i);
							resultMap.put(name, field);
							System.out.println(resultMap.isEmpty());
						}

					}
				}
			} else {
				String name = generateMapCellName(0, 0);
				TextField field = new TextField();
				field.setText("Inverse doesnt not exist.");
				field.setPrefWidth(field.getText().length() * 14);
				field.getStyleClass().add("gridResultTextField");
				resultGrid.add(field, 0, 0);
			}

		} else if (operationTypeLabel.getText().equals("Matrix Row Operation")) {
			for (int i = 0; i < resultMatrix.getCurrentMatrix().length; i++) {
				for (int j = 0; j < resultMatrix.getCurrentMatrix()[0].length; j++) {
					String name = generateMapCellName(i, j);
					TextField field = new TextField();

					String positionValue = doubleToFraction(resultMatrix.getCurrentMatrix()[i][j]);
					field.setText(positionValue);
					field.setPrefWidth(field.getText().length() * 14);
					field.getStyleClass().add("gridResultTextField");
					resultGrid.add(field, j, i);
					resultMap.put(name, field);
					System.out.println(resultMap.isEmpty());
				}
			}
		} else if (operationTypeLabel.getText().equals("Matrice Transpose")) {
			for (int i = 0; i < resultMatrix.getCurrentMatrix().length; i++) {
				for (int j = 0; j < resultMatrix.getCurrentMatrix()[0].length; j++) {
					String name = generateMapCellName(i, j);
					TextField field = new TextField();

					String positionValue = doubleToFraction(resultMatrix.getCurrentMatrix()[i][j]);

					field.setText(positionValue);
					field.setPrefWidth(field.getText().length() * 14);
					field.getStyleClass().add("gridResultTextField");
					resultGrid.add(field, j, i);
					resultMap.put(name, field);
					System.out.println(resultMap.isEmpty());
				}
			}
		} else if (operationTypeLabel.getText().equals("Vector Dot Product")) {
			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < 1; j++) {
					String name = generateMapCellName(i, j);
					TextField field = new TextField();
					field.setText("Dot Product: " + dotProduct);
					field.setPrefWidth(field.getText().length() * 14);
					field.getStyleClass().add("gridResultTextField");
					resultGrid.add(field, j, i);
					resultMap.put(name, field);
					System.out.println(resultMap.isEmpty());
				}
			}

		} else if (operationTypeLabel.getText().equals("Vector Magnitude")) {
			for (int i = 0; i < 1; i++) {
				for (int j = 0; j < 1; j++) {
					String name = generateMapCellName(i, j);
					TextField field = new TextField();
					field.setText("Magnitude: " + magnitude);
					field.setPrefWidth(field.getText().length() * 14);
					field.getStyleClass().add("gridResultTextField");
					resultGrid.add(field, j, i);
					resultMap.put(name, field);
					System.out.println(resultMap.isEmpty());
				}
			}

		} else if (operationTypeLabel.getText().equals("Vector Unit Vector")) {
			for (int i = 0; i < unitVector.getCurrentVector().length; i++) {
				for (int j = 0; j < unitVector.getCurrentVector()[i].length; j++) {
					String name = generateMapCellName(i, j);
					TextField field = new TextField();
					field.setText("" + unitVector.getCurrentVector()[i][j]);
					field.setPrefWidth(field.getText().length() * 14);
					field.getStyleClass().add("gridResultTextField");
					resultGrid.add(field, j, i);
					resultMap.put(name, field);
					System.out.println(resultMap.isEmpty());
				}
			}

		}

		else {
			resultMatrix.printMatrix();

			for (int i = 0; i < resultMatrix.getRow(); i++) {
				for (int j = 0; j < resultMatrix.getColumn(); j++) {
					String name = generateMapCellName(i, j);
					TextField field = new TextField();
					field.setText("" + resultMatrix.getCurrentMatrix()[i][j]);
					field.setPrefWidth(field.getText().length() * 14);
					field.getStyleClass().add("gridResultTextField");
					resultGrid.add(field, j, i);
					resultMap.put(name, field);
					System.out.println(resultMap.isEmpty());
				}
			}
		}
	}

	@FXML
	private void calculateButtonAction(ActionEvent event) throws NumberFormatException, IOException {

		if (firstRowSizeField.getText().trim().isEmpty() || firstColSizeField.getText().trim().isEmpty()
				|| secondRowSizeField.getText().trim().isEmpty() || secondColSizeField.getText().trim().isEmpty()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("The row and column is empty.");
			alert.setContentText("Please set them to prooceed!");

			alert.showAndWait();
		} else {
			System.out.println("Calculating...");
			calculateButton.setDisable(false);
			String firstRowSize = firstRowSizeField.getText().trim();
			String firstColSize = firstColSizeField.getText().trim();
			String secondRowSize = secondRowSizeField.getText().trim();
			String secondColSize = secondColSizeField.getText().trim();

			activelySetDimensions(firstRowSize, firstColSize, secondRowSize, secondColSize);

			if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
					.equals("Add Matrices")) {
				matrixOperation(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize),
						Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));

			} else if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
					.equals("Subtract Matrices")) {
				matrixOperation(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize),
						Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));

			} else if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
					.equals("Vector Dot Product")) {
				matrixOperation(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize),
						Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));

			} else if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
					.equals("Vector Magnitude")) {
				matrixOperation(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize),
						Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));
			} else if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
					.equals("Vector Unit Vector")) {
				matrixOperation(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize),
						Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));
			}

			else if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
					.equals("Multiply Matrices")) {
				matrixOperation(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize),
						Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));
			} else if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
					.equals("Determinant")
					|| operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Inverse Matrix")
					|| operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Matrix Row Operation")
					|| operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Matrice Transpose")) {
				matrixOperation(Integer.parseInt(firstRowSize), Integer.parseInt(firstColSize),
						Integer.parseInt(secondRowSize), Integer.parseInt(secondColSize));
				dimension2.setDisable(true);
				gridSecondMatrix.setDisable(true);
				secondColSizeField.setText("0");
				secondRowSizeField.setText("0");
				secondRowSizeField.setDisable(true);
				secondColSizeField.setDisable(true);
			}

			firstRowSizeField.clear();
			firstColSizeField.clear();
			if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
					.equals("Determinant")
					|| operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Inverse Matrix")
					|| operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Matrix Row Operation")
					|| operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Vector Magnitude")
					|| operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Vector Unit Vector")
					|| operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Matrice Transpose")) {
				secondRowSizeField.setText("0");
				secondColSizeField.setText("0");
			} else {
				secondRowSizeField.clear();
				secondColSizeField.clear();
			}

		}

	}

	@FXML
	private void dimension1Action(ActionEvent event) {
		String firstRowSize = firstRowSizeField.getText().trim();
		String firstColSize = firstColSizeField.getText().trim();
		String secondRowSize = secondRowSizeField.getText().trim();
		String secondColSize = secondColSizeField.getText().trim();

	}

	public void loadEigenScreen(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/EigenCalculationScreen.fxml"));
		Parent root;
		try {
			root = loader.load();
			EigenController controller = loader.getController();

			// call method from controller to set Data
			Stage stage = new Stage();
			stage.setTitle("EigenValue");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			scene.getStylesheets().add(Main.class.getResource("eigen.css.css").toExternalForm());
			// Hide this current window (if this is what you want)
			((Node) (event.getSource())).getScene().getWindow().hide();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void load(int row, int col, int row2, int col2) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ShowWorkScreen.fxml"));

		Parent root;
		try {
			root = loader.load();
			ShowScreenController controller = loader.getController();
			controller.resetControls(row, col, row2, col2);

			if (operationTypeLabel.getText().trim().equals("Add Matrices")) {
				controller.setAnswer(cal.getAnswerAddition());
				controller.setShowWork(cal.getShowWorkAddition());
			} else if (operationTypeLabel.getText().trim().equals("Subtract Matrices")) {
				controller.setAnswer(cal.getAnswerSubtraction());
				controller.setShowWork(cal.getShowWorkSubtraction());
			} else if (operationTypeLabel.getText().trim().equals("Multiply Matrices")) {
				controller.setAnswer(cal.getAnswerMultiplication());
				controller.setShowWork(cal.getShowWorkMultiplication());
			} else if (operationTypeLabel.getText().trim().equals("Vector Dot Product")) {
				String[][] newPlaceHolder = new String[3][3];
				String[][] newPlaceHolder2 = new String[1][1];

				newPlaceHolder[0][0] = vcal.getDotProductShowWork();
				newPlaceHolder[1][0] = vcal.getDotProductAnswer();
				newPlaceHolder[2][0] = vcal.getDotProduct();

				controller.setShowWork(newPlaceHolder);
			} else if (operationTypeLabel.getText().trim().equals("Vector Magnitude")) {
				String[][] values = new String[4][4];
				values[0][0] = vcal.getOriginalWorkMagnitude();
				values[1][0] = vcal.getSquaredWorkMagnitude();
				values[2][0] = vcal.getCurrentSquareRootValue();
				String value = vcal.getCurrentSquareRootValue().trim();
				values[3][0] = "" + Math.sqrt(Double.parseDouble(value.substring(3, value.length())));

				controller.setShowWork(values);
			}

			controller.setFirstGrid();
			// call method from controller to set Data
			Stage stage = new Stage();
			stage.setTitle("Show Work");
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
			scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
			// Hide this current window (if this is what you want)
			// // ((Node)(event.getSource())).getScene().getWindow().hide();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dimension1.setDisable(true);
		dimension2.setDisable(true);
		setButton.setDisable(true);
		calculateButton.setDisable(true);

		operationChoiceBox.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				showWorkButton.setVisible(true);
				Integer indexSelected = operationChoiceBox.getSelectionModel().getSelectedIndex();
				System.out.println(operationChoiceBox.getItems().get(indexSelected));
				operationTypeLabel.setText(operationChoiceBox.getItems().get(indexSelected));
				if (operationChoiceBox.getItems().get(indexSelected).equals("Determinant")
						|| operationChoiceBox.getItems().get(indexSelected).equals("Inverse Matrix")
						|| operationChoiceBox.getItems().get(indexSelected).equals("Matrix Row Operation")
						|| operationChoiceBox.getItems().get(indexSelected).equals("Matrice Transpose")
						|| operationChoiceBox.getItems().get(indexSelected).equals("Vector Magnitude")
						|| operationChoiceBox.getItems().get(indexSelected).equals("Vector Unit Vector")) {

					if (operationChoiceBox.getItems().get(indexSelected).equals("Matrice Transpose")) {
						showWorkButton.setVisible(false);
					}
					dimension2.setDisable(true);
					gridSecondMatrix.setDisable(true);
					secondColSizeField.setText("0");
					secondRowSizeField.setText("0");
					secondRowSizeField.setDisable(true);
					secondColSizeField.setDisable(true);

				} else if (operationChoiceBox.getItems().get(indexSelected).equals("Eigen Calculations")) {
					loadEigenScreen(e);
				} else {
					gridSecondMatrix.setDisable(false);
					secondRowSizeField.setDisable(false);
					secondColSizeField.setDisable(false);
					secondColSizeField.clear();
					secondRowSizeField.clear();
				}
				setButton.setDisable(false);
				calculateButton.setDisable(false);
				mapTextField = new HashMap<>();
				mapTextField2 = new HashMap<>();
				resultMap = new HashMap<>();
				resultGrid.getChildren().clear();
				gridFirstMatrix.getChildren().clear();
				gridSecondMatrix.getChildren().clear();

			}
		});

		showWorkButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

				if (operationTypeLabel.getText().trim().equals("Operation Type: (Select to Proceed)")) {
					System.out.println("Please select an operation type..");
				} else {
					if (operationChoiceBox.getItems().get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Add Matrices")) {
						String[][] showWork = cal.getShowWorkAddition();
						String[][] showAnswer = cal.getAnswerAddition();

						for (int i = 0; i < showWork.length; i++) {
							for (int j = 0; j < showWork[i].length; j++) {
								System.out.print(showWork[i][j] + " ");
							}
							System.out.println(" ");
						}
						System.out.println(" ");
						for (int i = 0; i < showAnswer.length; i++) {
							for (int j = 0; j < showAnswer[i].length; j++) {
								System.out.print(showAnswer[i][j] + " ");
							}
							System.out.println(" ");
						}

						load(showWork.length, showWork[0].length, showAnswer.length, showAnswer[0].length);

					} else if (operationChoiceBox.getItems()
							.get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Subtract Matrices")) {
						String[][] showWork = cal.getShowWorkSubtraction();
						String[][] showAnswer = cal.getAnswerSubtraction();

						for (int i = 0; i < showWork.length; i++) {
							for (int j = 0; j < showWork[i].length; j++) {
								System.out.print(showWork[i][j] + " ");
							}
							System.out.println(" ");
						}
						System.out.println(" ");
						for (int i = 0; i < showAnswer.length; i++) {
							for (int j = 0; j < showAnswer[i].length; j++) {
								System.out.print(showAnswer[i][j] + " ");
							}
							System.out.println(" ");
						}

						load(showWork.length, showWork[0].length, showAnswer.length, showAnswer[0].length);
					} else if (operationChoiceBox.getItems()
							.get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Multiply Matrices")) {
						String[][] showWork = cal.getShowWorkMultiplication();
						String[][] showAnswer = cal.getAnswerMultiplication();

						for (int i = 0; i < showWork.length; i++) {
							for (int j = 0; j < showWork[i].length; j++) {
								System.out.print(showWork[i][j] + " ");
							}
							System.out.println(" ");
						}
						load(showWork.length, showWork[0].length, showAnswer.length, showAnswer[0].length);

					} else if (operationChoiceBox.getItems()
							.get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Vector Dot Product")) {
						System.out.println("Vector Dot Product Work: " + vcal.getDotProductShowWork());
						load(3, 1, 3, 1);
					} else if (operationChoiceBox.getItems()
							.get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Vector Magnitude")) {
						load(4, 1, 4, 1);
					} else if (operationChoiceBox.getItems()
							.get(operationChoiceBox.getSelectionModel().getSelectedIndex())
							.equals("Matrix Row Operation")) {
						try {
							Desktop.getDesktop().open(new java.io.File(System.getProperty("user.home") + "/Desktop"
									+ "\\MatrixShowWork" + "\\ShowWork.txt"));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (operationChoiceBox.getItems()
							.get(operationChoiceBox.getSelectionModel().getSelectedIndex()).equals("Determinant")) {
						try {
							Desktop.getDesktop().open(new java.io.File(System.getProperty("user.home") + "/Desktop"
									+ "\\MatrixShowWork" + "\\Determinant.txt"));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else if (operationChoiceBox.getItems()
							.get(operationChoiceBox.getSelectionModel().getSelectedIndex()).equals("Inverse Matrix")) {
						try {
							Desktop.getDesktop().open(new java.io.File(System.getProperty("user.home") + "/Desktop"
									+ "\\MatrixShowWork" + "\\+InverseMatrix.txt"));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}

			}
		});

	}

}

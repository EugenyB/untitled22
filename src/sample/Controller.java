package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.MyModel;
import model.Person;

import java.util.Optional;

public class Controller {
    @FXML private Button addOrEditButton;
    @FXML private RadioButton editRadio;
    @FXML private RadioButton createRadio;
    @FXML private TextField nameField;
    @FXML private TextField ageField;
    @FXML private TextField salaryField;
    @FXML private TableView<Person> table;
    @FXML private TableColumn<Person, Integer> idColumn;
    @FXML private TableColumn<Person, String> nameColumn;
    @FXML private TableColumn<Person, Integer> ageColumn;
    @FXML private TableColumn<Person, Double> salaryColumn;

    boolean edit = false;

    private MyModel model;

    @FXML
    public void initialize() {
        model = new MyModel();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

        fillData();

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                showSelectedItem(newValue);
        });

    }

    private void showSelectedItem(Person person) {
        if (isEdit()) {
            nameField.setText(person.getName());
            ageField.setText(String.valueOf(person.getAge()));
            salaryField.setText(String.valueOf(person.getSalary()));
        }
    }

    private void fillData() {
        table.setItems(FXCollections.observableArrayList(model.findAllPeople()));
    }

    public void addPerson() {
        try {
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            double salary = Double.parseDouble(salaryField.getText());
            model.createPerson(name, age, salary);
            nameField.clear();
            ageField.clear();
            salaryField.clear();
            fillData();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Неправильный формат данных");
            alert.setTitle("Ошибка");
            alert.show();
        }
    }

    public void editPerson() {
        try {
            Person person = table.getSelectionModel().getSelectedItem();
            person.setName(nameField.getText());
            person.setAge(Integer.parseInt(ageField.getText()));
            person.setSalary(Double.parseDouble(salaryField.getText()));
            model.editPerson(person);
            edit = false;
            fillData();
            edit = true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Неправильный формат данных");
            alert.setTitle("Ошибка");
            alert.show();
        }
    }

    public void deletePerson() {
        Person person = table.getSelectionModel().getSelectedItem();
        if (person==null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                String.format("Удалить запись: %s %d %f ?", person.getName(), person.getAge(), person.getSalary()),
                ButtonType.YES, ButtonType.NO
        );
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (ButtonType.YES.equals(buttonType.get())) {
            model.deletePerson(person);
            fillData();
        }
    }

    public void selectMode() {
        if (createRadio.isSelected()) {
            addOrEditButton.setText("Создать");
            nameField.clear();
            ageField.clear();
            salaryField.clear();
            edit = false;

        } else {
            addOrEditButton.setText("Изменить");
            edit = true;
        }
    }

    private boolean isEdit() {
        return editRadio.isSelected();
    }

    public void addOrEditPerson(ActionEvent actionEvent) {
        if (isEdit()) {
            editPerson();
        } else {
            addPerson();
        }
    }
}

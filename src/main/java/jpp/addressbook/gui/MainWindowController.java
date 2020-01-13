package jpp.addressbook.gui;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import jpp.addressbook.*;

import javax.management.relation.RoleUnresolved;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;


public class MainWindowController {


    @FXML
    private TableView<Contact> tableView;
    @FXML
    private TableColumn<Contact, Salutation> salutationColumn;
    @FXML
    private TableColumn<Contact, String> firstNameColumn;
    @FXML
    private TableColumn<Contact, String> lastNameColumn;
    @FXML
    private MenuItem imports;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField streetAddress;
    @FXML private TextField zipCode;
    @FXML private TextField city;
    @FXML private TextField eMail;
    @FXML private TextField phone;
    @FXML private TextField lastNamePrefixFilter;
    @FXML private TextField birthdayFilter;
    @FXML private TextField zipCodeFilter;
    @FXML private DatePicker birthday;
    @FXML private ChoiceBox<Salutation> salutation;
    @FXML private VBox niceBox;
    @FXML private VBox TextFilterBox;
    @FXML private Button acceptButton;
    @FXML private Button changeButton;
    @FXML private Button newButton;
    @FXML private Button declineButton;
    @FXML private Button deleteButton;
    @FXML private Button undoFilterButton;
    @FXML private CheckBox prefixCheckBox;
    @FXML private CheckBox birthdayCheckBox;
    @FXML private CheckBox zipCodeCheckBox;
    @FXML private RadioButton andButton;
    @FXML private RadioButton orButton;




    private AddressBook addressBook = new CAddressBook();
    private AddressBookUtil addressBookUtil = new CAddressBookUtil(addressBook);
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();


    private ObservableSet<TextField> selctedTextFields = FXCollections.observableSet();
    private ObservableSet<CheckBox> selectedCheckBoxes = FXCollections.observableSet();
    private ObservableSet<CheckBox> unselectedCheckBoxes = FXCollections.observableSet();
    private IntegerBinding numCheckBoxesSelected = Bindings.size(selectedCheckBoxes);

    private final int maxNumSelected =  1;


    public AddressBookApplication addressBookApplication;

    public void setMain(AddressBookApplication addressBookApplication) {
        this.addressBookApplication = addressBookApplication;
    }



    //import csv contacts
    public void handleImport(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV-File");
        File selectedFile = fileChooser.showOpenDialog(null);
        String input ="";
        if (selectedFile == null){
            createEmptyAlert("No file selected");
        } else{
        try {
            Path path = Paths.get(selectedFile.getPath());
            BufferedReader rd = Files.newBufferedReader(path, UTF_8);
            String line = rd.readLine();
            while(line != null ){
                input += "\n" + line;
                line = rd.readLine();
            }

        } catch (IOException e) {
             createEmptyAlert("Import did not have the corrects format.");
        }



        try{
            addressBook.importContacts(input);
            //createEmptyAlert(addressBook.setErrorStream(errorStream));
        } catch (RuntimeException e){
            createEmptyAlert("Import is invalid");
        }

        tableView.getItems().clear();
        tableView.setItems(getContact(addressBook.getContacts()));

        }
    }

    private void saveFile(String content, File file){
        try {
            FileWriter fileWriter;

            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException ex) {
            createEmptyAlert("Can`t write on file");
        }

    }

    //export csv contacts
    public void export(){
        FileChooser fileChooser = new FileChooser();


        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("CSV File", "*.csv");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File file = fileChooser.showSaveDialog(null);

        if (file != null){
            saveFile(addressBook.exportAllContacts(), file);
        } else {
            createEmptyAlert("File selection aborted");
        }

    }
        public void createEmptyAlert(String str){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Error", ButtonType.CLOSE);
            alert.setContentText(str);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.CLOSE){
                alert.close();
            }

        }


        public void handleeMail(ActionEvent event) {
            if ( addressBook.getContacts().isEmpty() ){
                createEmptyAlert( "No data too handle.");

            } else{
                final Stage dialog = new Stage();
                Window owner =  ((Node) event.getTarget()).getScene().getWindow();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(owner);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Shares of e-mail providers in all contacts:" + "\n" + addressBookUtil.getMailProviderShare().toString()));
                Scene dialogScene = new Scene(dialogVbox, 900, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        }

        public void handleAge(ActionEvent event){
            if (addressBook.getContacts().isEmpty()){
                createEmptyAlert( "No data to handle");
            } else{
                final Stage dialog = new Stage();
                Window owner =  ((Node) event.getTarget()).getScene().getWindow();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(owner);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("The average age of all contacts is, " + addressBookUtil.getAverageAgeAt(LocalDate.now())));
                Scene dialogScene = new Scene(dialogVbox, 900, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        }

        public void handleSalutation(ActionEvent event){
            if (addressBook.getContacts().isEmpty()){
                createEmptyAlert( "No data to handle");
            } else{
                final Stage dialog = new Stage();
                Window owner =  ((Node) event.getTarget()).getScene().getWindow();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(owner);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Average number of female:  " + addressBookUtil.getSalutationShare(Salutation.FRAU) + "\n Average number of male: " + addressBookUtil.getSalutationShare(Salutation.HERR)));
                Scene dialogScene = new Scene(dialogVbox, 900, 200);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        }


    // show contact details
    public void showContactDetails(){
        Contact contact = tableView.getSelectionModel().getSelectedItem();
        if (contact != null){
        salutation.setValue(contact.getSalutation());
        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
        birthday.setValue(contact.getBirthday());
        streetAddress.setText(contact.getStreetAddress());
        zipCode.setText(String.valueOf(contact.getZipCode()));
        city.setText(contact.getCity());
        if (!contact.getEMail().isPresent()){
            eMail.setText("");
        }else{
            eMail.setText(contact.getEMail().get());
        }
        if (!contact.getPhone().isPresent()){
            phone.setText("");
        }else{
            phone.setText(String.valueOf(contact.getPhone().get()));
        }
        }

    }

    //change contact details
    public void enableChangeContact(){
        if (tableView.getSelectionModel().getSelectedItem() == null){
            createEmptyAlert("You need to pick a contact.");
        } else{

        for (Node node : niceBox.getChildren()){
            node.setDisable(false);
        }

        acceptButton.setVisible(true);
        newButton.setVisible(false);
        declineButton.setVisible(true);
        deleteButton.setVisible(false);
        }

    }

    //add new contact
    public void addNewContact(){
        for (Node node : niceBox.getChildren()){
            node.setDisable(false);
        }
        clearSelection();
        changeButton.setVisible(false);
        acceptButton.setVisible(true);
        declineButton.setVisible(true);
        deleteButton.setVisible(false);

    }


    // decline new || change Conatct
    public void decline(){
        clearSelection();
        for (Node node : niceBox.getChildren()){
            node.setDisable(true);
        }

        if (newButton.isVisible()){
            changeButton.setVisible(true);
            acceptButton.setVisible(false);
            declineButton.setVisible(false);
            deleteButton.setVisible(true);
        } else {
            newButton.setVisible(true);
            acceptButton.setVisible(false);
            declineButton.setVisible(false);
            deleteButton.setVisible(true);
        }
    }


    //decide which filter
    public void decideFilter(){
        if (tableView.getItems().isEmpty()){
            createEmptyAlert("AddressBook is empty");
            undoFilterButton.setVisible(true);
        } else{


        if (andButton.isSelected()|| orButton.isSelected()){
            applyMultipleFilter();
        } else {
            applyFilter();

        }
        }
    }


    // apply filter
    public void applyFilter() {
//        Set<Contact> contacts = addressBookUtil.filter(addressBookUtil.lastNamePrefixFilter(lastNamePrefixFilter.getText()));
//       tableView.setItems(getContact(contacts));

        FilteredList<Contact> filteredList = new FilteredList<>(contacts, e -> true);


        if (birthdayCheckBox.isSelected()) {
            if (birthdayFilter.getText().isEmpty()) {
                createEmptyAlert("Search is empty");
            } else {
                filteredList.setPredicate(addressBookUtil.birthYearFilter(Year.parse(birthdayFilter.getText())));
                setTableView(filteredList);
            }
            } else if (prefixCheckBox.isSelected()) {
                if (lastNamePrefixFilter.getText().isEmpty()) {
                    createEmptyAlert("Search is empty");
                } else {
                    filteredList.setPredicate(addressBookUtil.lastNamePrefixFilter(lastNamePrefixFilter.getText()));
                    setTableView(filteredList);
                }
            } else if (zipCodeCheckBox.isSelected()) {
                if (zipCodeFilter.getText().isEmpty()) {
                    createEmptyAlert("Search is empty");
                } else {
                    filteredList.setPredicate(addressBookUtil.zipCodeFilter(Integer.parseInt(zipCodeFilter.getText())));
                    setTableView(filteredList);
                }
            }


    }

    public void applyMultipleFilter(){
        FilteredList<Contact> filteredList = new FilteredList<>(contacts, e -> true);

        if (andButton.isSelected()){

        int i = selctedTextFields.size();

        switch (i){
            case 0:
                createEmptyAlert("Select a Filter");
                break;
            case 1:
                applyFilter();
                break;
            case 2:
                if (selctedTextFields.contains(birthdayFilter) && selctedTextFields.contains(lastNamePrefixFilter)){

                    filteredList.setPredicate(addressBookUtil.birthYearFilter(Year.parse(birthdayFilter.getText())).and(addressBookUtil.lastNamePrefixFilter(lastNamePrefixFilter.getText())));
                    setTableView(filteredList);
                    break;
                } else if (selctedTextFields.contains(birthdayFilter) && selctedTextFields.contains(zipCodeFilter)){

                    filteredList.setPredicate(addressBookUtil.birthYearFilter(Year.parse(birthdayFilter.getText())).and(addressBookUtil.zipCodeFilter(Integer.parseInt(zipCodeFilter.getText()))));
                    setTableView(filteredList);
                    break;
                } else if (selctedTextFields.contains(lastNamePrefixFilter) && selctedTextFields.contains(zipCodeFilter)){

                    filteredList.setPredicate(addressBookUtil.zipCodeFilter(Integer.parseInt(zipCodeFilter.getText())).and(addressBookUtil.lastNamePrefixFilter(lastNamePrefixFilter.getText())));
                    setTableView(filteredList);
                    break;
                }
            case 3:
                filteredList.setPredicate(addressBookUtil.zipCodeFilter(Integer.parseInt(zipCodeFilter.getText())).and(addressBookUtil.lastNamePrefixFilter(lastNamePrefixFilter.getText())).and(addressBookUtil.birthYearFilter(Year.parse(birthdayFilter.getText()))));
                setTableView(filteredList);
                break;
        }
        } else if (orButton.isSelected()){
            int i = selctedTextFields.size();

            switch (i){
                case 0:
                    createEmptyAlert("Select a Filter");
                    break;
                case 1:
                    applyFilter();
                    break;
                case 2:
                    if (selctedTextFields.contains(birthdayFilter) && selctedTextFields.contains(lastNamePrefixFilter)){

                        filteredList.setPredicate(addressBookUtil.birthYearFilter(Year.parse(birthdayFilter.getText())).or(addressBookUtil.lastNamePrefixFilter(lastNamePrefixFilter.getText())));
                        setTableView(filteredList);
                        break;
                    } else if (selctedTextFields.contains(birthdayFilter) && selctedTextFields.contains(zipCodeFilter)){

                        filteredList.setPredicate(addressBookUtil.birthYearFilter(Year.parse(birthdayFilter.getText())).or(addressBookUtil.zipCodeFilter(Integer.parseInt(zipCodeFilter.getText()))));
                        setTableView(filteredList);
                        break;
                    } else if (selctedTextFields.contains(lastNamePrefixFilter) && selctedTextFields.contains(zipCodeFilter)){

                        filteredList.setPredicate(addressBookUtil.zipCodeFilter(Integer.parseInt(zipCodeFilter.getText())).or(addressBookUtil.lastNamePrefixFilter(lastNamePrefixFilter.getText())));
                        setTableView(filteredList);
                        break;
                    }
                case 3:
                    filteredList.setPredicate(addressBookUtil.zipCodeFilter(Integer.parseInt(zipCodeFilter.getText())).or(addressBookUtil.lastNamePrefixFilter(lastNamePrefixFilter.getText())).or(addressBookUtil.birthYearFilter(Year.parse(birthdayFilter.getText()))));
                    setTableView(filteredList);
                    break;
            }


        }


    }

    public void setPrefixTextField() {
        if (prefixCheckBox.isSelected()) {
            lastNamePrefixFilter.setDisable(false);
            selctedTextFields.add(lastNamePrefixFilter);
            andButton.setDisable(true);
            orButton.setDisable(true);
        } else {
            lastNamePrefixFilter.setDisable(true);
            selctedTextFields.remove(lastNamePrefixFilter);
            andButton.setDisable(false);
            orButton.setDisable(false);
        }
    }

    public void setBirthdayTextField() {

        if (birthdayCheckBox.isSelected()) {
            birthdayFilter.setDisable(false);
            selctedTextFields.add(birthdayFilter);
            andButton.setDisable(true);
            orButton.setDisable(true);
        } else {
            birthdayFilter.setDisable(true);
            selctedTextFields.remove(birthdayFilter);
            andButton.setDisable(false);
            orButton.setDisable(false);
        }

        System.out.println(selctedTextFields.size());
    }

    public void setZipCodeTextField(){

        if (zipCodeCheckBox.isSelected()){
            zipCodeFilter.setDisable(false);
            selctedTextFields.add(zipCodeFilter);
            andButton.setDisable(true);
            orButton.setDisable(true);
        } else{
            zipCodeFilter.setDisable(true);
            selctedTextFields.remove(zipCodeFilter);
            andButton.setDisable(false);
            orButton.setDisable(false);
        }

    }

    public void setTableView(FilteredList<Contact> filteredList){
        SortedList<Contact> sortedList = new SortedList<>(filteredList);

        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
        undoFilterButton.setVisible(true);

    }


    private void configureCheckBox(CheckBox checkBox) {


        if (checkBox.isSelected()) {
            selectedCheckBoxes.add(checkBox);
        } else {
            unselectedCheckBoxes.add(checkBox);
        }

        checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                unselectedCheckBoxes.remove(checkBox);
                selectedCheckBoxes.add(checkBox);
            } else {
                selectedCheckBoxes.remove(checkBox);
                unselectedCheckBoxes.add(checkBox);
            }

        });


    }

    public void undoFilter(){
        tableView.setItems(getContact(addressBook.getContacts()));
        tableView.getItems().clear();
        tableView.setItems(getContact(addressBook.getContacts()));
        undoFilterButton.setVisible(false);
        andButton.setDisable(false);
        orButton.setDisable(false);
        orButton.setSelected(false);
        andButton.setSelected(false);
        zipCodeCheckBox.setSelected(false);
        birthdayCheckBox.setSelected(false);
        prefixCheckBox.setSelected(false);
        lastNamePrefixFilter.setDisable(true);
        birthdayFilter.setDisable(true);
        zipCodeFilter.setDisable(true);
        lastNamePrefixFilter.clear();
        birthdayFilter.clear();
        zipCodeFilter.clear();



    }
    public void changeContact(){
        if (changeButton.isVisible()){
            if (isValid()){



                Contact contact = tableView.getSelectionModel().getSelectedItem();

                contact.setSalutation(salutation.getValue());
                contact.setFirstName(firstName.getText());
                contact.setLastName(lastName.getText());
                contact.setBirthday(birthday.getValue());
                contact.setStreetAddress(streetAddress.getText());
                contact.setZipCode(Integer.parseInt(zipCode.getText()));
                contact.setCity(city.getText());
                if (!eMail.getText().isEmpty()){
                    contact.setEMail(eMail.getText());
                } else{
                    contact.setEMail(null);
                }
                if (!phone.getText().isEmpty()){
                    contact.setPhone(Long.valueOf(phone.getText()));
                } else{
                    contact.setPhone(0L);
                }

                newButton.setVisible(true);
                acceptButton.setVisible(false);
                declineButton.setVisible(false);
                deleteButton.setVisible(true);
                tableView.getItems().clear();
                tableView.setItems(getContact(addressBook.getContacts()));

                for (Node node : niceBox.getChildren()){
                    node.setDisable(true);
                }
                clearSelection();

            }

        }else if (newButton.isVisible()){
            if (isValid()){
                System.out.println("lol");
                if (phone.getText().isEmpty() && !eMail.getText().isEmpty()){
                    Contact contact = new CContact(addressBook.getSmallestUnusedId(), salutation.getValue(), firstName.getText(), lastName.getText(), birthday.getValue(), streetAddress.getText(), Integer.parseInt(zipCode.getText()), city.getText(), Optional.empty(), Optional.of(eMail.getText()));
                    addressBook.addContact(contact);
                } else if (eMail.getText().isEmpty() && !phone.getText().isEmpty()){
                    Contact contact = new CContact(addressBook.getSmallestUnusedId(), salutation.getValue(), firstName.getText(), lastName.getText(), birthday.getValue(), streetAddress.getText(), Integer.parseInt(zipCode.getText()), city.getText(), Optional.of(Long.parseLong(phone.getText())), Optional.empty());
                    addressBook.addContact(contact);
                }else if (phone.getText().isEmpty() && eMail.getText().isEmpty()){
                    Contact contact = new CContact(addressBook.getSmallestUnusedId(), salutation.getValue(), firstName.getText(), lastName.getText(), birthday.getValue(), streetAddress.getText(), Integer.parseInt(zipCode.getText()), city.getText(), Optional.empty(),Optional.empty());
                    addressBook.addContact(contact);
                }


                acceptButton.setVisible(false);
                changeButton.setVisible(true);
                declineButton.setVisible(false);
                deleteButton.setVisible(true);
                tableView.getItems().clear();
                tableView.setItems(getContact(addressBook.getContacts()));

                for (Node node : niceBox.getChildren()){
                    node.setDisable(true);
                }
                clearSelection();
            }
        }
    }

    //delete Contact
    public void deleteContact(){
        Contact contact = tableView.getSelectionModel().getSelectedItem();
        addressBook.removeContact(contact.getId());
        tableView.getItems().clear();
        tableView.setItems(getContact(addressBook.getContacts()));
        clearSelection();
    }


    //initialize
    public void initialize(){
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("firstName"));
        salutationColumn.setCellValueFactory(new PropertyValueFactory<Contact, Salutation>("salutation"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("lastName"));

        salutation.getItems().add(Salutation.FRAU);
        salutation.getItems().add(Salutation.HERR);
        salutation.setValue(Salutation.FRAU);


        zipCodeFilter.setDisable(true);
        birthdayFilter.setDisable(true);
        lastNamePrefixFilter.setDisable(true);

        undoFilterButton.setVisible(false);
        declineButton.setVisible(false);
        acceptButton.setVisible(false);

        for (Node node :niceBox.getChildren()){
            node.setDisable(true);
        }

        if (!addressBook.getContacts().isEmpty()){
            tableView.setItems(getContact(addressBook.getContacts()));
        }


            configureCheckBox(birthdayCheckBox);
            configureCheckBox(zipCodeCheckBox);
            configureCheckBox(prefixCheckBox);


            numCheckBoxesSelected.addListener((obs, oldSelectedCount, newSelectedCount) -> {
                if (!orButton.isSelected() && !andButton.isSelected()) {
                    if (newSelectedCount.intValue() >= maxNumSelected) {
                        unselectedCheckBoxes.forEach(cb -> cb.setDisable(true));

                    } else {
                        unselectedCheckBoxes.forEach(cb -> cb.setDisable(false));

                    }
                }
            });



    }

    public ObservableList<Contact> getContact( Set<Contact> contactSet){


        for ( Contact contact : contactSet) {
            contacts.add(contact);

        }
        return contacts;
    }

    public Boolean isValid(){
        int count = 0;

        //überprüft first name
        if (firstName.getText().isEmpty()){
            createEmptyAlert("First Name is invalid");
        } else{
            count++;
        }

        //überprüft last name
        if (lastName.getText().isEmpty()){
            createEmptyAlert("Last Name is invalid");
        } else{
            count++;
        }

        //überprüft birthdate
        if (birthday.getValue() == null || isThisDateValid(String.valueOf(birthday.getValue()), "dd.MM.yyyy")){
            createEmptyAlert("Birthday is invalid");
        } else {
            count++;
        }

        //überprüft street address
        if (streetAddress.getText().isEmpty()){
            createEmptyAlert("street Address is invalid");
        } else {
            count++;
        }

        // überprüft zipCode
        if (zipCode.getText().isEmpty() || !isNumeric(zipCode.getText()) || !isNatrual(zipCode.getText())){
            createEmptyAlert("Zip code is invalid");

        } else {
            count++;
        }

        //überprüft city
        if (city.getText().isEmpty()){
            createEmptyAlert("City is invalid");
        }else{
            count++;
        }

        //überprüft phone
        if (phone.getText().isEmpty()){
            count++;
        } else if (!isNumeric(zipCode.getText()) || !isNatrual(zipCode.getText())){
            createEmptyAlert("Phone is invalid");
        } else{
            count++;
        }

        //überprüft email
        if (eMail.getText().isEmpty()){
            count++;
        } else if (!validEmail(eMail.getText())){
            createEmptyAlert("Email is invalid");
        } else {
            count++;
        }

        //überprüfe count
        if (count == 8){
            return true;
        } else {
            return false;
        }
    }


    // tet if email is valid
    public boolean validEmail(String str){
        if (str.matches("\\S+@\\S+\\.\\S+")){
            return true;
        }
        return false;
    }

    //is natrual
    public boolean isNatrual(String str){
        int nr = Integer.parseInt(str);
        if (nr > 0){
            return true;
        } else {
            return false;
        }
    }

    //test if numeric
    public boolean isNumeric(String str){
        for ( char c : str.toCharArray()) {
            if (!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    }

    // test if date is valid
    public boolean isThisDateValid(String dateToValidate, String dateFormat){
        if(dateToValidate == null){
            return false;
        }

        if (!dateToValidate.matches("[0-9][0-9].[0-1][0-9].[0-9][0-9][0-9][0-9]")){
            return false;
        }


        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);


        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void clearSelection(){
        salutation.setValue(Salutation.FRAU);
        firstName.clear();
        lastName.clear();
        birthday.setValue(LocalDate.now());
        streetAddress.clear();
        zipCode.clear();
        city.clear();
        eMail.clear();
        phone.clear();
    }


}





package lk.ijse.supermarket.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.supermarket.db.DBConnection;


import lk.ijse.supermarket.view.tblmodel.CustomerTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class AddCustomerFormController implements Initializable {
    public AnchorPane root;
    public JFXButton btnAddNewCustomer;
    public JFXTextField txtCustomerID;
    public JFXComboBox cmbCustomerType;
    public JFXTextField txtCustomerName;
    public JFXTextField txtAddress;
    public JFXTextField txtCity;
    public JFXTextField txtProvince;
    public JFXTextField txtContact;
    public JFXButton btnSaveCustomer;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colCustID;
    public TableColumn colCustType;
    public TableColumn colCustName;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colProvince;
    public TableColumn colContact;
    public JFXButton btnDeleteCustomer;
    public JFXButton btnUpdateCustomer;
    public ImageView imgGoCustomerOrder;
    private boolean addNew;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cmbCustomerType.setItems( FXCollections.observableArrayList("Gold","Sliver","Platinum"));

        tblCustomer.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tblCustomer.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("customerType"));
        tblCustomer.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblCustomer.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        tblCustomer.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("city"));
        tblCustomer.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("province"));
        tblCustomer.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("contact"));


        loadAllCustomers();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {


            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM newValue) {

                if (newValue == null){
                    addNew = true;
                    clearTextFields();
                    return;
                }

                txtCustomerID.setText(newValue.getCustomerID());
                cmbCustomerType.setValue(newValue.getCustomerType());
                txtCustomerName.setText(newValue.getCustomerName());
                txtAddress.setText(newValue.getCustomerAddress());
                txtCity.setText(newValue.getCity());
                txtProvince.setText(newValue.getProvince());
                txtContact.setText(newValue.getContact() + "");

                addNew = false;

            }
        });
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView){
            ImageView icon = (ImageView) mouseEvent.getSource();

            Parent root = null;

            switch(icon.getId()){
                case "imgGoCustomerOrder":
                    root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/supermarket/view/CustomerPlaceOrderForm.fxml"));
                    break;

            }

            if (root != null){
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.root.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.setResizable(true);
                primaryStage.sizeToScene();
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();

            }
        }

    }

    public void btnAddNewCustomerOnAction(ActionEvent actionEvent) {
        txtCustomerID.requestFocus();
        addNew = true;





    }



    public void cmbCustomerTypeOnAction(ActionEvent actionEvent) {
    }

    public void btnSaveCustomerOnAction(ActionEvent actionEvent) {
        if (addNew){

            try {

                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?,?,?,?)");

                if (Pattern.compile( "^[A-Z]{1}[0-9]{1,5}" ).matcher( txtCustomerID.getText( ) ).matches( )) {
                        if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtCustomerName.getText( ) ).matches( )) {
                            if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtAddress.getText( ) ).matches( )) {
                                if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtCity.getText( ) ).matches( )) {
                                    if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtProvince.getText( ) ).matches( )) {
                                        if (Pattern.compile( "^[0-9]{1,}[0-9]{1,}$" ).matcher( txtContact.getText( ) ).matches( )) {


                                             pstm.setObject(1, txtCustomerID.getText());
                                             pstm.setObject(2, cmbCustomerType.getValue().toString());
                                             pstm.setObject(3, txtCustomerName.getText());
                                             pstm.setObject(4, txtAddress.getText());
                                             pstm.setObject(5, txtCity.getText());
                                             pstm.setObject(6, txtProvince.getText());
                                             pstm.setObject(7, Integer.parseInt(txtContact.getText()));

                                            int affectedRows = pstm.executeUpdate();

                                            if (affectedRows > 0){
                                                loadAllCustomers();
                                             }else{
                                                 new Alert(Alert.AlertType.ERROR, "Failed to add the customer", ButtonType.OK).show();
                                             }
                                        }else {
                                            txtContact.setFocusColor( Paint.valueOf( "red" ) );
                                            txtContact.requestFocus( );
                                        }
                                    }else {
                                        txtProvince.setFocusColor( Paint.valueOf( "red" ) );
                                        txtProvince.requestFocus( );
                                    }
                                }else {
                                    txtCity.setFocusColor( Paint.valueOf( "red" ) );
                                    txtCity.requestFocus( );
                                }
                            }else {
                                txtAddress.setFocusColor( Paint.valueOf( "red" ) );
                                txtAddress.requestFocus( );
                            }
                        }else {
                            txtCustomerName.setFocusColor( Paint.valueOf( "red" ) );
                            txtCustomerName.requestFocus( );
                        }

                }else {
                    txtCustomerID.setFocusColor( Paint.valueOf( "red" ) );
                    txtCustomerID.requestFocus( );
                }

                                        } catch (Exception ex) {
                Logger.getLogger(AddCustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
            }


        }else{



        }
    }

    private void clearTextFields(){
        txtCustomerID.setText("");
        cmbCustomerType.setValue("");
        txtCustomerName.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtProvince.setText("");
        txtContact.setText("");

    }

    private void loadAllCustomers() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Statement stm = connection.createStatement();

            ResultSet rst = stm.executeQuery("SELECT * FROM Customer");

            ArrayList<CustomerTM> alCustomers= new ArrayList<>();

            while (rst.next()){

                CustomerTM customer = new CustomerTM(rst.getString(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getString(4),
                        rst.getString(5),
                        rst.getString(6),
                        rst.getInt(7));

                alCustomers.add(customer);

            }

            ObservableList<CustomerTM> olCustomers = FXCollections.observableArrayList(alCustomers);

            tblCustomer.setItems(olCustomers);

        } catch (Exception ex) {
            Logger.getLogger(AddCustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }






    public void btnDeleteCustomerOnAction(ActionEvent actionEvent) {

        Alert confirmAlert = new Alert(Alert.AlertType.WARNING, "Are you sure whether you want to delete the customer?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.get() == ButtonType.YES) {

            String customerID = tblCustomer.getSelectionModel().getSelectedItem().getCustomerID();

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement("DELETE FROM Customer WHERE customerID=?");

                pstm.setObject(1, customerID);

               int affectedRows = pstm.executeUpdate();

               if (affectedRows>0) {
                    loadAllCustomers();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Failed to delete the customer", ButtonType.OK);
                    a.show();
                }
            } catch (Exception ex) {
                Logger.getLogger(AddCustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }

    public void btnUpdateCustomerOnAction(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement pstm = connection.prepareStatement("UPDATE Customer SET customerType=?, customerName=?, customerAddress=?, city=?,  province=?, contact=? WHERE customerID=?");


            pstm.setObject(1, cmbCustomerType.getValue().toString());
            pstm.setObject(2, txtCustomerName.getText());
            pstm.setObject(3, txtAddress.getText());
            pstm.setObject(4, txtCity.getText());
            pstm.setObject(5, txtProvince.getText());
            pstm.setObject(6, Integer.parseInt(txtContact.getText()));
            pstm.setObject(7, txtCustomerID.getText());

            int affectedRows = pstm.executeUpdate();

            if (affectedRows > 0){
                loadAllCustomers();
            }else{
                new Alert(Alert.AlertType.ERROR, "Failed to update the Customer", ButtonType.OK).show();
            }
        } catch (Exception ex) {
            Logger.getLogger(AddCustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.model.Product;
import lk.ijse.supermarket.view.tblmodel.ProductTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class AddItemFormController implements Initializable {
    public AnchorPane root;
    public JFXButton btnAddNewProduct;
    public JFXTextField txtProductID;
    public JFXTextField txtProductName;
    public JFXTextField txtDescription;
    public JFXTextField txtSpecification;
    public JFXTextField txtDisplayName;
    public JFXTextField txtAvailableBrands;
    public JFXComboBox cmbAvailability;
    public JFXComboBox cmbActiveState;
    public JFXButton btnSaveProduct;
    public TableView<ProductTM> tblProduct;
    public JFXButton btndeleteProduct;
    public JFXButton btnUpdateProduct;
    public ImageView imgAdminHome;
    private boolean addNew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cmbAvailability.setItems( FXCollections.observableArrayList("Able","Disable","Temporary Disable"));
        cmbActiveState.setItems(FXCollections.observableArrayList("Active","In Active"));

        tblProduct.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("productID"));
        tblProduct.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("productName"));
        tblProduct.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblProduct.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("specification"));
        tblProduct.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("displayName"));
        tblProduct.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("availability"));
        tblProduct.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("activeState"));
        tblProduct.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("availableBrand"));


        loadAllProducts();

        tblProduct.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProductTM>() {




            @Override
            public void changed(ObservableValue<? extends ProductTM> observable, ProductTM oldValue, ProductTM newValue) {


                if (newValue == null){
                    addNew = true;
                    clearTextFields();
                    return;
                }

                txtProductID.setText(newValue.getProductID());
                txtProductName.setText(newValue.getProductName());
                txtDescription.setText(newValue.getDescription());
                txtSpecification.setText(newValue.getSpecification());
                txtDisplayName.setText(newValue.getDisplayName());
                cmbAvailability.setValue(newValue.getAvailability());
                cmbActiveState.setValue(newValue.getActiveState());
                txtAvailableBrands.setText(newValue.getAvailableBrand()+"");


                addNew = false;

            }
        });
    }

    private void clearTextFields(){
        txtProductID.setText("");
        txtProductName.setText("");
        txtDescription.setText("");
        txtSpecification.setText("");
        txtDisplayName.setText("");
        cmbAvailability.setValue("");
        cmbActiveState.setValue("");
        txtAvailableBrands.setText("");

    }
    public void btnUpdateProductOnAction(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement pstm = connection.prepareStatement("UPDATE Product SET productName=?, description=?, specification=?, displayName=?,  availability=?, activeState=? availableBrand=? WHERE productID=?");




            pstm.setObject(1,  txtProductName.getText());
            pstm.setObject(2, txtDescription.getText());
            pstm.setObject(3, txtSpecification.getText());
            pstm.setObject(4, txtDisplayName.getText());
            pstm.setObject(5, cmbAvailability.getValue().toString());
            pstm.setObject(6, cmbActiveState.getValue().toString());
            pstm.setObject(7, txtAvailableBrands.getText());
            pstm.setObject(8, txtProductID.getText());


            int affectedRows = pstm.executeUpdate();

            if (affectedRows > 0){
                loadAllProducts();
            }else{
                new Alert(Alert.AlertType.ERROR, "Failed to update the Product", ButtonType.OK).show();
            }
        } catch (Exception ex) {
            Logger.getLogger(AddItemFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }




    public void btnDeleteProductOnAction(ActionEvent actionEvent) {
        Alert confirmAlert = new Alert(Alert.AlertType.WARNING, "Are you sure whether you want to delete the product?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.get() == ButtonType.YES) {

            String productID = tblProduct.getSelectionModel().getSelectedItem().getProductID();

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement("DELETE FROM Product WHERE productID=?");

                pstm.setObject(1, productID);

                int affectedRows = pstm.executeUpdate();

                if (affectedRows>0) {
                    loadAllProducts();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Failed to delete the product", ButtonType.OK);
                    a.show();
                }
            } catch (Exception ex) {
                Logger.getLogger(AddItemFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void btnSaveProductOnAction(ActionEvent actionEvent) {
        if (addNew){

            try {

                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement("INSERT INTO Product VALUES (?,?,?,?,?,?,?,?)");

                if (Pattern.compile( "^(P)[0-9]{1,5}$" ).matcher( txtProductID.getText( ) ).matches( )) {
                if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtProductName.getText( ) ).matches( )) {
                    if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtDescription.getText( ) ).matches( )) {
                        if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtSpecification.getText( ) ).matches( )) {
                            if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtDisplayName.getText( ) ).matches( )) {
                                if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtAvailableBrands.getText( ) ).matches( )) {

                pstm.setObject(1, txtProductID.getText());
                pstm.setObject(2, txtProductName.getText());
                pstm.setObject(3, txtDescription.getText());
                pstm.setObject(4, txtSpecification.getText());
                pstm.setObject(5, txtDisplayName.getText());
                pstm.setObject(6, cmbAvailability.getValue().toString());
                pstm.setObject(7, cmbActiveState.getValue().toString());
                pstm.setObject(8,  txtAvailableBrands.getText());


                int affectedRows = pstm.executeUpdate();

                if (affectedRows > 0){
                    loadAllProducts();
                }else{
                    new Alert(Alert.AlertType.ERROR, "Failed to add the product", ButtonType.OK).show();
                }
                                }else {
                                    txtAvailableBrands.setFocusColor( Paint.valueOf( "red" ) );
                                    txtAvailableBrands.requestFocus();
                                }
                            }else {
                                txtDisplayName.setFocusColor( Paint.valueOf( "red" ) );
                                txtDisplayName.requestFocus();
                            }
                        }else {
                            txtSpecification.setFocusColor( Paint.valueOf( "red" ) );
                            txtSpecification.requestFocus();
                        }
                    }else {
                        txtDescription.setFocusColor( Paint.valueOf( "red" ) );
                        txtDescription.requestFocus();
                    }
                }else {
                    txtProductName.setFocusColor( Paint.valueOf( "red" ) );
                    txtProductName.requestFocus();
                }
                }else {
                    txtProductID.setFocusColor( Paint.valueOf( "red" ) );
                    txtProductID.requestFocus();
                }
            } catch (Exception ex) {
                Logger.getLogger(AddCustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
            }



        }else{



        }
    }

    public void btnAddNewProductOnAction(ActionEvent actionEvent) {
        txtProductID.requestFocus();
        addNew = true;
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView){
            ImageView icon = (ImageView) mouseEvent.getSource();

            Parent root = null;

            switch(icon.getId()){
                case "imgAdminHome":
                    root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/supermarket/view/AdminForm.fxml"));
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

    private void loadAllProducts() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Statement stm = connection.createStatement();

            ResultSet rst = stm.executeQuery("SELECT * FROM Product");

            ArrayList<ProductTM> alProducts= new ArrayList<>();

            while (rst.next()){

                ProductTM product = new ProductTM(rst.getString(1),
                        rst.getString(2),
                        rst.getString(3),
                        rst.getString(4),
                        rst.getString(5),
                        rst.getString(6),
                        rst.getString(7),
                        rst.getString(8));

                alProducts.add(product);

            }

            ObservableList<ProductTM> olProducts = FXCollections.observableArrayList(alProducts);

            tblProduct.setItems(olProducts);

        } catch (Exception ex) {
            Logger.getLogger(AddItemFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}

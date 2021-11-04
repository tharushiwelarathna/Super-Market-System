package lk.ijse.supermarket.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.supermarket.AppInitializer;
import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.model.Product;
import lk.ijse.supermarket.view.tblmodel.BatchTM;
import lk.ijse.supermarket.view.tblmodel.CustomerTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class AddBatchFormController implements Initializable {
    public AnchorPane root;
    public JFXTextField txtPropertyID;
    public JFXTextField txtBatch;
    public JFXTextField txtPrice;
    public JFXTextField txtDiscount;
    public JFXComboBox cmbActiveState;
    public JFXButton btnSaveBatch;
    public TableView<BatchTM> tblProperty;
    public JFXButton btnDeleteBatch;
    public JFXButton btnUpdateBatch;
    public JFXComboBox<String> cmbProductID;
    public JFXTextField txtQty;
    public JFXButton btnAddNewBatch;
    public Label lblTime;
    public Label lblDate;
    public JFXCheckBox setDiscount;
    public ImageView imgAdminHome;
    public Label lblProductName;
    private boolean addNew;
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connection = DBConnection.getInstance().getConnection();
            loadAllData();
        } catch (SQLException ex) {
            Logger.getLogger(lk.ijse.supermarket.controller.AddBatchFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cmbProductID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                String productID = observable.getValue();
                if (productID == null) {
                    lblProductName.setText("");

                    return;
                }

                try {
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Product WHERE ProductID=?");
                    pstm.setObject(1, productID);
                    ResultSet rst = pstm.executeQuery();
                    if (rst.next()) {
                        String productName= rst.getString(2);
                        lblProductName.setText(productName);
                    }


                } catch (SQLException ex) {
                    Logger.getLogger(lk.ijse.supermarket.controller.AddBatchFormController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        cmbActiveState.setItems( FXCollections.observableArrayList("Able","Disable"));
        
        tblProperty.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("propertyID"));
        tblProperty.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("batch"));
        tblProperty.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("price"));
        tblProperty.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("discount"));
        tblProperty.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("activeState"));
        tblProperty.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblProperty.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("productID"));


        loadAllBatchs();
        generateDateTime();


        txtDiscount.setDisable ( true );

        tblProperty.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BatchTM>() {


            @Override
            public void changed(ObservableValue<? extends BatchTM> observable, BatchTM oldValue, BatchTM newValue) {

                if (newValue == null){
                    addNew = true;
                    clearTextFields();
                    return;
                }



                txtBatch.setText ( newValue.getBatch () );
                txtPrice.setText ( String.valueOf ( newValue.getPrice () ) );
                setDiscount.setSelected ( newValue.isDiscountState () );
                txtDiscount.setText ( String.valueOf ( newValue.getDiscount () ) );
                cmbActiveState.setValue(newValue.getActiveState());
                txtQty.setText ( String.valueOf ( newValue.getQuantity() ) );
                cmbProductID.setValue (newValue.getProductID () );


                addNew = false;

            }
        });


    }

    private void loadAllData() throws SQLException {

        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM Product");
        cmbProductID.getItems().removeAll(cmbProductID.getItems());
        while (rst.next()) {
            String productID = rst.getString(1);
            cmbProductID.getItems().add(productID);
        }


    }




    public void generateDateTime() {
        lblDate.setText( LocalDate.now().toString());

        Timeline timeline = new Timeline( new KeyFrame( Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "hh:mm:ss a");
            lblTime.setText( LocalDateTime.now().format( formatter));
        }), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount( Animation.INDEFINITE);
        timeline.play();
    }


    public void btnAddNewBatchOnAction(ActionEvent actionEvent) {
        txtPropertyID.requestFocus();
        addNew = true;
    }



    public void btnUpdateBatchOnAction(ActionEvent actionEvent) {

        try {
            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement pstm = connection.prepareStatement("UPDATE Batch SET batch=?,price=?,discountState=?,discount=?,activeState=?,quantity=?,systemDate=?,productID=? WHERE propertyID=?");
            SimpleDateFormat formatter=new SimpleDateFormat ( "dd/MM/yyyy HH:mm" );
            Date date=new Date ( );

            pstm.setObject(1, txtBatch.getText());
            pstm.setObject(2, BigDecimal.valueOf(Long.parseLong(txtPrice.getText())));
            pstm.setObject(3, setDiscount.isSelected());
            pstm.setObject(4, BigDecimal.valueOf(Long.parseLong(txtDiscount.getText())));
            pstm.setObject(5, cmbActiveState.getValue().toString());
            pstm.setObject(6, Integer.parseInt(txtQty.getText()));
            pstm.setObject(7, formatter.format(date));
            pstm.setObject(8, cmbProductID.getValue().toString());
            pstm.setObject(9, txtPropertyID.getText());

            int affectedRows = pstm.executeUpdate();

            if (affectedRows > 0){
                loadAllBatchs();
            }else{
                new Alert(Alert.AlertType.ERROR, "Failed to update the item", ButtonType.OK).show();
            }
        } catch (Exception ex) {
            Logger.getLogger(AddBatchFormController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    public void setDiscountOnAction ( ActionEvent actionEvent ) {
        if (setDiscount.isSelected ()){
            txtDiscount.setDisable ( false );
            txtDiscount.setText ( "0" );
        }else {
            txtDiscount.setDisable ( true );
        }
    }

    public void btnDeleteBatchOnAction(ActionEvent actionEvent) {
        Alert confirmAlert = new Alert(Alert.AlertType.WARNING, "Are you sure whether you want to delete the item?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.get() == ButtonType.YES) {

            String propertyID = tblProperty.getSelectionModel().getSelectedItem().getPropertyID();

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement("DELETE FROM Batch WHERE propertyID=?");

                pstm.setObject(1, propertyID);

                int affectedRows = pstm.executeUpdate();

                if (affectedRows>0) {
                    loadAllBatchs();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Failed to delete the item", ButtonType.OK);
                    a.show();
                }
            } catch (Exception ex) {
                Logger.getLogger(AddBatchFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void btnSaveBatchOnAction(ActionEvent actionEvent) {
        if (addNew){

            try {

                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement("INSERT INTO Batch VALUES (?,?,?,?,?,?,?,?,?)");

                if (Pattern.compile( "^(I)[0-9]{1,5}" ).matcher( txtPropertyID.getText( ) ).matches( )) {
                    if (Pattern.compile( "^[0-9]{1,9}$" ).matcher( txtBatch.getText( ) ).matches( )) {
                    if (Pattern.compile( "^[0-9]{1,9}$" ).matcher( txtPrice.getText( ) ).matches( )) {
                        if (Pattern.compile( "^[0-9]{1,9}$" ).matcher( txtDiscount.getText( ) ).matches( )) {
                            if (Pattern.compile( "^[0-9]{1,5}$" ).matcher( txtQty.getText( ) ).matches( )) {

                SimpleDateFormat formatter=new SimpleDateFormat ( "dd/MM/yyyy HH:mm" );
                Date date=new Date ( );

                pstm.setObject(1, txtPropertyID.getText());
                pstm.setObject(2, txtBatch.getText());
                pstm.setObject(3, BigDecimal.valueOf(Long.parseLong(txtPrice.getText())));
                pstm.setObject(4, setDiscount.isSelected());
                pstm.setObject(5, BigDecimal.valueOf(Long.parseLong(txtDiscount.getText())));
                pstm.setObject(6, cmbActiveState.getValue().toString());
                pstm.setObject(7, Integer.parseInt(txtQty.getText()));
                pstm.setObject(8, formatter.format(date));
                pstm.setObject(9, cmbProductID.getValue().toString());
              

                int affectedRows = pstm.executeUpdate();

                if (affectedRows > 0){
                    loadAllBatchs();
                }else{
                    new Alert(Alert.AlertType.ERROR, "Failed to add the batch", ButtonType.OK).show();
                }
                            }else {
                                txtQty.setFocusColor( Paint.valueOf( "red" ) );
                                txtQty.requestFocus();
                            }
                        }else {
                            txtDiscount.setFocusColor( Paint.valueOf( "red" ) );
                            txtDiscount.requestFocus();
                        }
                    }else {
                        txtPrice.setFocusColor( Paint.valueOf( "red" ) );
                        txtPrice.requestFocus();
                    }
                }else {
                    txtBatch.setFocusColor( Paint.valueOf( "red" ) );
                    txtBatch.requestFocus();
                }
                }else {
                    txtPropertyID.setFocusColor( Paint.valueOf( "red" ) );
                    txtPropertyID.requestFocus();
                }
            } catch (Exception ex) {
                Logger.getLogger(AddBatchFormController.class.getName()).log(Level.SEVERE, null, ex);
            }


        }else{ }

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

    public void clearTextFields(){
        txtPropertyID.setText ( "");
        txtBatch.setText ( "");
        txtPrice.setText ("" );
        txtDiscount.setText ("" );
        cmbActiveState.setValue("");
        txtQty.setText ("" );
        cmbProductID.setValue (null );
    }

    private void loadAllBatchs() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Statement stm = connection.createStatement();

            ResultSet rst = stm.executeQuery("SELECT * FROM Batch");

            ArrayList<BatchTM> alBatchs= new ArrayList<>();

            while (rst.next()){

                BatchTM batch = new BatchTM(rst.getString ( 1 ),
                                            rst.getString(2),
                                            rst.getBigDecimal ( 3 ),
                                            rst.getBoolean ( 4 ),
                                             rst.getBigDecimal ( 5 ),
                                            rst.getString ( 6 ),
                                            rst.getInt ( 7 ),
                                            rst.getString ( 8 ),
                                            rst.getString ( 9 ));

                alBatchs.add(batch);

            }

            ObservableList<BatchTM> olBatchs = FXCollections.observableArrayList(alBatchs);

            tblProperty.setItems(olBatchs);

        } catch (Exception ex) {
            Logger.getLogger(AddBatchFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}

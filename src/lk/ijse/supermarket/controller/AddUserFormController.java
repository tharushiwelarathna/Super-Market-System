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
import lk.ijse.supermarket.AppInitializer;
import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.view.tblmodel.CustomerTM;
import lk.ijse.supermarket.view.tblmodel.UserTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class AddUserFormController implements Initializable {
    public AnchorPane root;
    public JFXButton btnAddNewUser;
    public JFXTextField txtUserID;
    public JFXTextField txtUserName;
    public JFXTextField txtPassword;
    public JFXComboBox cmbUserType;
    public JFXComboBox cmbActiveState;
    public JFXButton btnSaveUser;
    public TableView<UserTM> tblUser;
    public JFXButton btnDeleteUser;
    public JFXButton btnUpdateUser;
    public ImageView imgAdminHome;
    private boolean addNew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cmbUserType.setItems( FXCollections.observableArrayList("Admin","Chasier"));
        cmbActiveState.setItems(FXCollections.observableArrayList("Able","Disable"));

        tblUser.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("userID"));
        tblUser.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("userName"));
        tblUser.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("password"));
        tblUser.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("activeState"));
        tblUser.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("userType"));



        loadAllUsers();

        tblUser.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserTM>() {


            @Override
            public void changed(ObservableValue<? extends UserTM> observable, UserTM oldValue, UserTM newValue) {

                if (newValue == null){
                    addNew = true;
                    clearTextFields();
                    return;
                }

                txtUserID.setText ( String.valueOf ( newValue.getUserID () ) );
                txtUserName.setText(newValue.getUserName());
                txtPassword.setText(newValue.getPassword());
                cmbActiveState.setValue(newValue.getActiveState());
                cmbUserType.setValue(newValue.getUserType()+"");

                addNew = false;

            }
        });
    }
    public void btnUpdateUserOnAction(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement pstm = connection.prepareStatement("UPDATE User SET userName=?, password=?, activeState=?,userType=? WHERE userID=?" );


            if (Pattern.compile( "^[A-z]{1,}$" ).matcher( txtUserName.getText( ) ).matches( )) {
                if (Pattern.compile( "^[0-9]{1,5}$" ).matcher( txtPassword.getText( ) ).matches( )) {


            pstm.setObject(1, txtUserName.getText());
            pstm.setObject(2, txtPassword.getText());
            pstm.setObject(3, cmbActiveState.getValue().toString());
            pstm.setObject(4, cmbUserType.getValue().toString());
            pstm.setObject(5, Integer.parseInt(txtUserID.getText()));

            int affectedRows = pstm.executeUpdate();

            if (affectedRows > 0){
                loadAllUsers();
            }else{
                new Alert(Alert.AlertType.ERROR, "Failed to update the User", ButtonType.OK).show();
            }

                }else {
                    txtPassword.setFocusColor( Paint.valueOf( "red" ) );
                    txtPassword.requestFocus();
                }
            }else {
                txtUserName.setFocusColor( Paint.valueOf( "red" ) );
                txtUserName.requestFocus();
            }
        } catch (Exception ex) {
            Logger.getLogger(AddUserFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void btnDeleteUserOnAction(ActionEvent actionEvent) {

        Alert confirmAlert = new Alert(Alert.AlertType.WARNING, "Are you sure whether you want to delete the user?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.get() == ButtonType.YES) {

            int userID =  tblUser.getSelectionModel().getSelectedItem().getUserID();

            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement("DELETE FROM User WHERE userID=?");

                pstm.setObject(1, userID );

                int affectedRows = pstm.executeUpdate();

                if (affectedRows>0) {
                    loadAllUsers();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Failed to delete the user", ButtonType.OK);
                    a.show();
                }
            } catch (Exception ex) {
                Logger.getLogger(AddCustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void btnSaveUserOnAction(ActionEvent actionEvent) {
        if (addNew){

            try {

                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement("INSERT INTO User VALUES (?,?,?,?,?)");


                pstm.setObject(1, Integer.parseInt(txtUserID.getText()));
                pstm.setObject(2, txtUserName.getText());
                pstm.setObject(3, txtPassword.getText());
                pstm.setObject(4, cmbActiveState.getValue().toString());
                pstm.setObject(5, cmbUserType.getValue().toString());



                int affectedRows = pstm.executeUpdate();

                if (affectedRows > 0){
                    loadAllUsers();
                }else{
                    new Alert(Alert.AlertType.ERROR, "Failed to add the user", ButtonType.OK).show();
                }
            } catch (Exception ex) {
                Logger.getLogger(AddUserFormController.class.getName()).log(Level.SEVERE, null, ex);
            }


        }else{



        }
    }

    public void btnAddNewUserOnAction(ActionEvent actionEvent) {
        txtUserID.requestFocus();
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
    private void clearTextFields(){
        txtUserID.setText ( "" );
        txtUserName.setText ( "" );
        txtPassword.setText ( "" );
        cmbActiveState.setValue("");
       cmbUserType.setValue("");

    }

    private void loadAllUsers() {
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            Statement stm = connection.createStatement();

            ResultSet rst = stm.executeQuery("SELECT * FROM User");

            ArrayList<UserTM> alUsers= new ArrayList<>();

            while (rst.next()){

                UserTM user = new UserTM(rst.getInt ( 1 ),
                        rst.getString ( 2 ),
                        rst.getString ( 3 ),
                        rst.getString ( 4 ),
                        rst.getString ( 5 ));

                alUsers.add(user);

            }

            ObservableList<UserTM> olUsers = FXCollections.observableArrayList(alUsers);

            tblUser.setItems(olUsers);

        } catch (Exception ex) {
            Logger.getLogger(AddUserFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}

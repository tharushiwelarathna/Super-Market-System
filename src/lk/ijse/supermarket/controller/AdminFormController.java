package lk.ijse.supermarket.controller;

import javafx.animation.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.supermarket.AppInitializer;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdminFormController implements Initializable {
    public AnchorPane root;
    public Label lblMenu;
    public Label lblDescription;
    public ImageView imgProduct;
    public ImageView imgCashier;
    public Label lblDate;
    public Label lblTime;
    public ImageView imgBatch;
    public ImageView imgOrders;
    public AnchorPane root1;

    public void initialize(URL url, ResourceBundle rb) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();


        generateDateTime();
    }

    public void navigate(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView){
            ImageView icon = (ImageView) mouseEvent.getSource();

            Parent root = null;

            switch(icon.getId()){
                case "imgCashier":
                    root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/supermarket/view/AddUserForm.fxml"));
                    break;
                case "imgProduct":
                    root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/supermarket/view/AddItemForm.fxml"));
                    break;
                case "imgBatch":
                    root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/supermarket/view/AddBatchForm.fxml"));
                    break;
                case "imgOrders":
                    root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/supermarket/view/ManageOrderForm.fxml"));
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
    public void generateDateTime() {
        lblDate.setText( LocalDate.now().toString());

        Timeline timeline = new Timeline( new KeyFrame( Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "hh:mm:ss a");
            lblTime.setText( LocalDateTime.now().format( formatter));
        }), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount( Animation.INDEFINITE);
        timeline.play();
    }

    public void playMouseEnterAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView){
            ImageView icon = (ImageView) mouseEvent.getSource();

            switch(icon.getId()){
                case "imgCashier":
                    lblMenu.setText("Manage Users");
                    lblDescription.setText("Click to add, edit, delete, search or view users");
                    break;
                case "imgProduct":
                    lblMenu.setText("Manage Products");
                    lblDescription.setText("Click to add, edit, delete, search or view products");
                    break;
                case "imgBatch":
                    lblMenu.setText("Manage Items");
                    lblDescription.setText("Click to add, edit, delete, search or view products");
                    break;
                case "imgOrders":
                    lblMenu.setText("Orders");
                    lblDescription.setText("Click  here if you want to view customer orders ");
                    break;
            }

            ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void playMouseExitAnimation(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView){
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            lblMenu.setText("Welcome");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }

    public void navigateToHome(MouseEvent event) {
        AppInitializer.navigateToHome(root, (Stage) this.root.getScene().getWindow());
    }
}

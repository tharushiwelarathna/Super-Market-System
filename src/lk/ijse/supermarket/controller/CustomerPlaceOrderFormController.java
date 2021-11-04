package lk.ijse.supermarket.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import lk.ijse.supermarket.AppInitializer;
import lk.ijse.supermarket.db.DBConnection;
import lk.ijse.supermarket.view.tblmodel.OrderDetailTM;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerPlaceOrderFormController implements Initializable {
    public AnchorPane root;
    public JFXButton btnAddNewCustomer;
    public JFXDatePicker txtOrderDate;
    public JFXComboBox<String> cmbPropertyID;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQty;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtDiscount;
    public TableView <OrderDetailTM>tblOrderDetails;
    public Label lblTotal;
    public JFXTextField txtOrderID;
    public JFXTextField txtCustomerName;
    public JFXComboBox<String> cmbCustomerID;
    public ObservableList<OrderDetailTM> olOrderDetails;
    public JFXButton btnRemove;
    public Label lblUserID;
    public JFXComboBox<Integer> cmbUserID;
    public Label lblUserName;
    private Connection connection;
    private boolean update = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            connection = DBConnection.getInstance().getConnection();

            // Create a day cell factory
            Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            // Must call super
                            super.updateItem(item, empty);
                            LocalDate today = LocalDate.now();
                            setDisable(empty || item.compareTo(today) < 0);
                        }
                    };
                }
            };

            txtOrderDate.setDayCellFactory(dayCellFactory);
            loadAllData();
        } catch (SQLException ex) {
            Logger.getLogger(lk.ijse.supermarket.controller.CustomerPlaceOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            e.printStackTrace();
        }


        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                String customerID = observable.getValue();
                if (customerID == null) {
                    txtCustomerName.setText("");
                    return;
                }

                try {
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Customer WHERE customerID=?");
                    pstm.setObject(1, customerID);
                    ResultSet rst = pstm.executeQuery();

                    if (rst.next()) {
                        String customerName = rst.getString(3);
                        txtCustomerName.setText(customerName);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(lk.ijse.supermarket.controller.CustomerPlaceOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        cmbUserID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {

               Integer userID = observable.getValue();
                if (userID == null) {
                    lblUserName.setText("");
                    return;
                }

                try {
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM User WHERE userID=?");
                    pstm.setObject(1, userID);
                    ResultSet rst = pstm.executeQuery();

                    if (rst.next()) {
                        String userName = rst.getString(2);
                        lblUserName.setText(userName);
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(lk.ijse.supermarket.controller.CustomerPlaceOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        cmbPropertyID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                String propertyID = observable.getValue();

                if (propertyID  == null) {
                    txtUnitPrice.setText("");
                    txtQtyOnHand.setText("");
                    txtQty.setText("");
                    txtDiscount.setText("");
                    return;
                }

                try {
                    PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Batch WHERE propertyID  = ?");
                    pstm.setObject(1, propertyID);

                    ResultSet rst = pstm.executeQuery();

                    if (rst.next()) {

                        double price = rst.getDouble(3);
                        int quantity = rst.getInt(7);
                        double discount=rst.getDouble(5);


                        txtUnitPrice.setText(price + "");
                        txtQtyOnHand.setText(quantity + "");
                        txtDiscount.setText(discount + "");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(lk.ijse.supermarket.controller.CustomerPlaceOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("propertyID"));
        tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("discount"));
        tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

        olOrderDetails = FXCollections.observableArrayList();
        tblOrderDetails.setItems(olOrderDetails);



        tblOrderDetails.getItems().addListener(new ListChangeListener<OrderDetailTM>() {
            @Override
            public void onChanged(Change<? extends OrderDetailTM> c) {

                double total = 0.0;

                for (OrderDetailTM orderDetail : olOrderDetails) {
                    total += orderDetail.getTotal();
                }

                lblTotal.setText("Total : " + total);

            }
        });

        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderDetailTM>() {
            @Override
            public void changed(ObservableValue<? extends OrderDetailTM> observable, OrderDetailTM oldValue, OrderDetailTM newValue) {

                OrderDetailTM currentRow = observable.getValue();

                if (currentRow == null) {
                    cmbPropertyID.getSelectionModel().clearSelection();
                    update = false;
                    btnRemove.setDisable(true);
                    return;
                }

                update = true;
                String itemCode = currentRow.getPropertyID();
                btnRemove.setDisable(false);

                cmbPropertyID.getSelectionModel().select(itemCode);
                txtQty.setText(currentRow.getQty() + "");

            }
        });

        btnRemove.setDisable(true);


    }
    private void loadAllData() throws SQLException {

        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM Customer");
        cmbCustomerID.getItems().removeAll(cmbCustomerID.getItems());
        while (rst.next()) {
            String customerId = rst.getString(1);
            cmbCustomerID.getItems().add(customerId);
        }
        rst = stm.executeQuery("SELECT * FROM Batch");
        cmbPropertyID.getItems().removeAll(cmbPropertyID.getItems());
        while (rst.next()) {
            String propertyId = rst.getString(1);
            cmbPropertyID.getItems().add(propertyId);
        }
        rst=stm.executeQuery("SELECT * FROM User");
        cmbUserID.getItems().removeAll(cmbUserID.getItems());
        while (rst.next()){
            String userID =rst.getString(1);
            cmbUserID.getItems().add(Integer.valueOf(userID));
        }


    }

    public void btnCancelOnAction(ActionEvent actionEvent) {

    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        try {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO Orders VALUES (?,?,?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setObject(1, txtOrderID.getText());
            pstm.setObject(2, parseDate(txtOrderDate.getEditor().getText()));
          //  pstm.setObject(3, BigDecimal.valueOf ( Double.parseDouble ( lblTotal.getText ( ) )));
        //   pstm.setObject(3,Double.parseDouble(lblTotal.getText()));
            pstm.setObject(3, cmbCustomerID.getSelectionModel().getSelectedItem());
            pstm.setObject(4,cmbUserID.getValue());
           // pstm.setObject(4,Integer.parseInt(cmbUserID.getSelectionModel().getSelectedItem().toString()));


            int affectedRows = pstm.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                return;
            }

            pstm = connection.prepareStatement("INSERT INTO OrderDetail VALUES (?,?,?,?)");


            for (OrderDetailTM orderDetail : olOrderDetails) {

                pstm.setObject(1, orderDetail.getQty());
                pstm.setObject(2, orderDetail.getUnitPrice());
                pstm.setObject(3, txtOrderID.getText());
                pstm.setObject(4, orderDetail.getPropertyID());
                affectedRows = pstm.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    return;
                }
                int qtyOnHand = 0;

                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery("SELECT * FROM Batch WHERE propertyID='" + orderDetail.getPropertyID()+ "'");
                if (rst.next()) {
                    qtyOnHand = rst.getInt(7);
                }
                PreparedStatement pstm2 = connection.prepareStatement("UPDATE Batch SET quantity=? WHERE propertyID=?");
                pstm2.setObject(1, qtyOnHand - orderDetail.getQty());
                pstm2.setObject(2, orderDetail.getPropertyID());

                affectedRows = pstm2.executeUpdate();

                if (affectedRows == 0) {
                    connection.rollback();
                    return;
                }

            }

            connection.commit();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Order Placed", ButtonType.OK);
            alert.show();

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(lk.ijse.supermarket.controller.CustomerPlaceOrderFormController.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(lk.ijse.supermarket.controller.CustomerPlaceOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                Logger.getLogger(lk.ijse.supermarket.controller.CustomerPlaceOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void btnRemoveOnAction(ActionEvent actionEvent) {
        OrderDetailTM selectedRow = tblOrderDetails.getSelectionModel().getSelectedItem();
        olOrderDetails.remove(selectedRow);
    }

    public void btnAddNewCustomerOnAction(ActionEvent actionEvent) {
        InitUI("AddCustomerForm.fxml");
    }

    private void InitUI(String Location) {
        try {
            this.root.getChildren().clear();
            this.root.getChildren().add(FXMLLoader.load(this.getClass().getResource("/lk/ijse/supermarket/view/" + Location)));
        }catch (IOException ex){
            new Alert(Alert.AlertType.CONFIRMATION,ex.getMessage(), ButtonType.OK).show();

        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String propertyID = cmbPropertyID.getSelectionModel().getSelectedItem();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        double discount = Double.parseDouble( txtDiscount.getText( ) );

        if (!update) {
            for (OrderDetailTM orderDetail : olOrderDetails) {
                if (orderDetail.getPropertyID().equals(propertyID)) {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Please update the batch instead of adding", ButtonType.OK);
                    error.setHeaderText("Duplicate Entry Found");
                    error.setTitle("Duplicate Error");
                    error.show();
                    return;
                }
            }
        }

        OrderDetailTM orderDetail = new OrderDetailTM(
                propertyID,
                unitPrice,
                qty,
                discount,

                (qty * unitPrice)-(discount * qty));






        if (!update) {
            olOrderDetails.add(orderDetail);
            tblOrderDetails.setItems(olOrderDetails);
        } else {
            OrderDetailTM selectedRow = tblOrderDetails.getSelectionModel().getSelectedItem();
            int index = olOrderDetails.indexOf(selectedRow);
            olOrderDetails.set(index, orderDetail);
        }

        cmbPropertyID.getSelectionModel().clearSelection();
        cmbPropertyID.requestFocus();
    }



    private Date parseDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            return sdf.parse(date);
        } catch (ParseException ex) {

            Logger.getLogger(lk.ijse.supermarket.controller.CustomerPlaceOrderFormController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }


    public void navigateToHome(MouseEvent mouseEvent) {
        AppInitializer.navigateToHome(root, (Stage) this.root.getScene().getWindow());
    }

    public void navigateToCustomer(MouseEvent event) throws IOException {


    }
}

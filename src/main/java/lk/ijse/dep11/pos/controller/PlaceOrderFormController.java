package lk.ijse.dep11.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.dep11.pos.db.CustomerDataAccess;
import lk.ijse.dep11.pos.db.ItemDataAccess;
import lk.ijse.dep11.pos.tm.Customer;
import lk.ijse.dep11.pos.tm.Item;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PlaceOrderFormController {
    public AnchorPane root;
    public JFXTextField txtCustomerName;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXButton btnSave;
    public TableView tblOrderDetails;
    public JFXTextField txtUnitPrice;
    public JFXComboBox<Customer> cmbCustomerId;
    public JFXComboBox<Item> cmbItemCode;
    public JFXTextField txtQty;
    public Label lblId;
    public Label lblDate;
    public Label lblTotal;
    public JFXButton btnPlaceOrder;

    public void initialize() throws IOException {
        lblDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        newOrder();
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((ov, prev, cur) -> {
            if (cur != null) {
                txtCustomerName.setText(cur.getName());
                txtCustomerName.setDisable(false);
                txtCustomerName.setEditable(false);
            } else {
                txtCustomerName.clear();
                txtCustomerName.setDisable(true);
            }
        });
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((ov, prev, cur) -> {
            if (cur != null) {
                txtDescription.setText(cur.getDescription());
                txtQtyOnHand.setText(cur.getQty() + "");
                txtUnitPrice.setText(cur.getUnitPrice().toString());

                for (TextField txt : new TextField[]{txtDescription, txtQtyOnHand, txtUnitPrice}) {
                    txt.setDisable(false);
                    txt.setEditable(false);
                }
                txtQty.setDisable(cur.getQty() == 0);
            } else {
                for (TextField txt : new TextField[]{txtDescription, txtQtyOnHand, txtUnitPrice, txtQty}) {
                    txt.setDisable(true);
                    txt.clear();
                }
            }
        });
        txtQty.textProperty().addListener((ov, prevQty, curQty) -> {
            Item selectedItem = cmbItemCode.getSelectionModel().getSelectedItem();
//            btnSave.setDisable(true);
//            if (cur.matches("\\d+")){
//                if (Integer.parseInt(cur) <= selectedItem.getQty() && Integer.parseInt(cur) > 0){
//                    btnSave.setDisable(false);
//                }
//            }
            btnSave.setDisable(!(curQty.matches("\\d+") && Integer.parseInt(curQty) <= selectedItem.getQty()
                    && Integer.parseInt(curQty) > 0));
        });
    }

    private void newOrder() throws IOException {
        for (TextField txt : new TextField[]{txtCustomerName, txtDescription, txtQty, txtQtyOnHand, txtUnitPrice}) {
            txt.clear();
            txt.setDisable(true);
        }
        tblOrderDetails.getItems().clear();
        lblTotal.setText("TOTAL: Rs. 0.00");
        btnSave.setDisable(true);
        btnPlaceOrder.setDisable(true);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        try {
            cmbCustomerId.getItems().clear();
            cmbCustomerId.getItems().addAll(CustomerDataAccess.getAllCustomers());
            cmbItemCode.getItems().clear();
            cmbItemCode.getItems().addAll(ItemDataAccess.getAllItems());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to establish database connection, try later").show();
            e.printStackTrace();
            navigateToHome(null);
        }
        Platform.runLater(cmbCustomerId::requestFocus);
    }

    public void navigateToHome(MouseEvent mouseEvent) throws IOException {
        MainFormController.navigateToMain(root);
    }

    public void btnAdd_OnAction(ActionEvent actionEvent) {
    }

    public void txtQty_OnAction(ActionEvent actionEvent) {
    }

    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) {
    }
}

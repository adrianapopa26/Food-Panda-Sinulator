package presentationLayer;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class EmployeeGUI implements PropertyChangeListener {
    public TextArea text;
    private String orders="";
    public void propertyChange(PropertyChangeEvent evt) {
        this.setOrders((String) evt.getNewValue());
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders=this.getOrders()+orders;
    }

    public void initialize(){
        text.setText(""+ClientGUI.employee.getOrders());
    }

    public void logOff(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("logIn.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
}

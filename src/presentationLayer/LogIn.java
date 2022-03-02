package presentationLayer;

import businessLayer.DeliveryService;
import businessLayer.User;
import dataLayer.FilesWriter;
import dataLayer.Serializator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class LogIn {
    public TextField username;
    public PasswordField password;
    public ChoiceBox<String> function;
    private DeliveryService deliveryService=new DeliveryService();

    public void initialize(){;
        //deliveryService= Serializator.deserialize();
        deliveryService.getUsers().add(new User("adriana","1234","Administrator"));
        deliveryService.getUsers().add(new User("bia","0000","Employee"));
        deliveryService.getUsers().add(new User("doro","0000","Client"));
        deliveryService.getUsers().add(new User("miru","0000","Client"));
        ObservableList<String> box= FXCollections.observableArrayList();
        box.add("Administrator");
        box.add("Employee");
        box.add("Client");
        function.getItems().addAll(box);
    }

    public void showError(String s,String s1) {
        Alert.AlertType type = Alert.AlertType.ERROR;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText(s1);
        alert.getDialogPane().setHeaderText(s);
        alert.showAndWait();
    }

    public boolean validateAdministrator(String user,String password){
        for(User users: deliveryService.getUsers()){
            if(users.getFunction().equals("Administrator") && users.getUsername().equals(user) && users.getPassword().equals(password)){
                return true;
            }
        }
        showError("Invalid log in!","Try again!");
        return false;
    }

    public boolean validateEmployee(String user,String password){
        for(User users: deliveryService.getUsers()){
            if(users.getFunction().equals("Employee") && users.getUsername().equals(user) && users.getPassword().equals(password)){
                return true;
            }
        }
        showError("Invalid log in!","Try again!");
        return false;
    }

    public boolean validateClient(String user,String password){
        for(User users: deliveryService.getUsers()){
            if(users.getFunction().equals("Client") && users.getUsername().equals(user) && users.getPassword().equals(password)){
                return true;
            }
        }
        showError("Invalid log in!","Try again!");
        return false;
    }

    public void logInButton(ActionEvent event) throws IOException {
        String usernameText=username.getText();
        String passwordText=password.getText();
        String functionText=function.getValue();
        if(functionText.equals("Administrator")&&validateAdministrator(usernameText,passwordText)){
            Parent tableViewParent = FXMLLoader.load(getClass().getResource("administrator.fxml"));
            Scene tableViewScene = new Scene(tableViewParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }
        else{
            if(functionText.equals("Employee")){
                if(validateEmployee(usernameText,passwordText)){
                    Parent tableViewParent = FXMLLoader.load(getClass().getResource("employee.fxml"));
                    Scene tableViewScene = new Scene(tableViewParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(tableViewScene);
                    window.show();
                }
            }
            else{
                if(validateClient(usernameText,passwordText)){
                    Parent tableViewParent = FXMLLoader.load(getClass().getResource("client.fxml"));
                    Scene tableViewScene = new Scene(tableViewParent);
                    Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    window.setScene(tableViewScene);
                    window.show();
                }
            }
        }
    }

    public boolean validateClientForSign(String user){
        for(User users: deliveryService.getUsers()){
            if(users.getFunction().equals("Client") && users.getUsername().equals(user)){
                showError("Invalid sign up!","Try again!");
                return false;
            }
        }
        return true;
    }

    public void signUpButton(){
        String usernameText=username.getText();
        String passwordText=password.getText();
        String functionText=function.getValue();
        if(functionText.equals("Client")&&!usernameText.equals("") && !passwordText.equals("") &&validateClientForSign(usernameText)){
                deliveryService.getUsers().add(new User(usernameText,passwordText,"Client"));
                username.setText("");
                password.setText("");
                showError("Sign up successfull!","Please log in!");
                Serializator.serialize(deliveryService);
        }
        else{
            showError("You can not sign up!","Please select client!");
        }
    }

    public void exit() {
        Serializator.serialize(deliveryService);
        Stage stage = (Stage) username.getScene().getWindow(); stage.close();
    }
}

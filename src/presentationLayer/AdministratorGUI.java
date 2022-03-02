package presentationLayer;

import businessLayer.DeliveryService;
import businessLayer.MenuItem;
import dataLayer.Serializator;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class AdministratorGUI {
    public TextField title;
    public TextField rating;
    public TextField calories;
    public TextField protein;
    public TextField fat;
    public TextField sodium;
    public TextField price;
    public TableView<MenuItem> table;
    public TableColumn<MenuItem,String> titleC;
    public TableColumn<MenuItem,Double> ratingC;
    public TableColumn<MenuItem,Integer> caloriesC;
    public TableColumn<MenuItem,Integer> proteinC;
    public TableColumn<MenuItem,Integer> fatC;
    public TableColumn<MenuItem,Integer> sodiumC;
    public TableColumn<MenuItem,Integer> priceC;

    private ArrayList<MenuItem> composite=new ArrayList<>();
    private DeliveryService idsp = new DeliveryService();
    private DeliveryService deliveryService=new DeliveryService();
    ListChangeListener<MenuItem> multiSelection = changed -> composite.addAll(changed.getList());

    public void initialize(){
        deliveryService = Serializator.deserialize();
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().getSelectedItems().addListener(multiSelection);
        show();
    }

    public void showError(String s) {
        Alert.AlertType type = Alert.AlertType.ERROR;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText("Check the fields and try again!");
        alert.getDialogPane().setHeaderText(s);
        alert.showAndWait();
    }

    public void show(){
        ObservableList<MenuItem> menuItems= FXCollections.observableArrayList();
        menuItems.addAll(deliveryService.getMenu());
        titleC.setCellValueFactory(new PropertyValueFactory<>("Title"));
        ratingC.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        caloriesC.setCellValueFactory(new PropertyValueFactory<>("Calories"));
        proteinC.setCellValueFactory(new PropertyValueFactory<>("Protein"));
        fatC.setCellValueFactory(new PropertyValueFactory<>("Fat"));
        sodiumC.setCellValueFactory(new PropertyValueFactory<>("Sodium"));
        priceC.setCellValueFactory(new PropertyValueFactory<>("Price"));
        table.setItems(menuItems);
    }

    public void importProduct(){
        idsp.importProducts();
        deliveryService.setMenu(idsp.getMenu());
        show();
    }

    public boolean validateData(){
        if(!title.getText().equals("")){
            if(!rating.getText().equals("")){
                if(!calories.getText().equals("")){
                    if(!protein.getText().equals("")){
                        if(!fat.getText().equals("")){
                            if(!sodium.getText().equals("")){
                                if(!price.getText().equals("")){
                                    return true;
                                }
                                showError("Invalid price!");
                            }
                            showError("Invalid sodium!");
                        }
                        showError("Invalid fat!");
                    }
                    showError("Invalid protein!");
                }
                showError("Invalid calories!");
            }
            showError("Invalid rating!");
        }
        showError("Invalid title!");
        return false;
    }

    public void add(){
        if(validateData())
        {
            deliveryService.addMenuItem(new MenuItem(title.getText(),Double.parseDouble(rating.getText()),Integer.parseInt(calories.getText()),Integer.parseInt(protein.getText()),Integer.parseInt(fat.getText()),Integer.parseInt(sodium.getText()),Integer.parseInt(price.getText())));
            show();
        }

    }

    public void modify(){
        if(validateData()) {
            deliveryService.modifyMenuItem(new MenuItem(title.getText(), Double.parseDouble(rating.getText()), Integer.parseInt(calories.getText()), Integer.parseInt(protein.getText()), Integer.parseInt(fat.getText()), Integer.parseInt(sodium.getText()), Integer.parseInt(price.getText())));
            show();
        }
    }

    public void delete(){
        if(validateData()) {
            deliveryService.deleteMenuItem(new MenuItem(title.getText(), Double.parseDouble(rating.getText()), Integer.parseInt(calories.getText()), Integer.parseInt(protein.getText()), Integer.parseInt(fat.getText()), Integer.parseInt(sodium.getText()), Integer.parseInt(price.getText())));
            show();
        }
    }

    public void addComposite(){
        if(!title.getText().equals("")){
            composite.remove(0);
            deliveryService.createCompositeProduct(composite,title.getText());
            composite.clear();
            show();
        }
        else{
            showError("Insert title!");
        }
    }

    public void generateReports(){
        deliveryService.generateReports();
    }

    public void logOff(ActionEvent event) throws IOException {
        Serializator.serialize(deliveryService);
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("logIn.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
}

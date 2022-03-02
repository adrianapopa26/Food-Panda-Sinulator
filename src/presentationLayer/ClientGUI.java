package presentationLayer;

import businessLayer.DeliveryService;
import businessLayer.MenuItem;
import businessLayer.Order;
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
import javafx.stage.Stage;

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;

public class ClientGUI {
    public TextField title;
    public TextField ratingMin;
    public TextField ratingMax;
    public Spinner<Integer> caloriesMin;
    public Spinner<Integer> caloriesMax;
    public Spinner<Integer> proteinMin;
    public Spinner<Integer> proteinMax;
    public Spinner<Integer> fatMin;
    public Spinner<Integer> fatMax;
    public Spinner<Integer> sodiumMin;
    public Spinner<Integer> sodiumMax;
    public Spinner<Integer> priceMin;
    public Spinner<Integer> priceMax;
    public TextArea textOrder;
    public TableView<MenuItem> table;
    public TableColumn<MenuItem,String> titleC;
    public TableColumn<MenuItem,Double> ratingC;
    public TableColumn<MenuItem,Integer> caloriesC;
    public TableColumn<MenuItem,Integer> proteinC;
    public TableColumn<MenuItem,Integer> fatC;
    public TableColumn<MenuItem,Integer> sodiumC;
    public TableColumn<MenuItem,Integer> priceC;
    public static  EmployeeGUI employee=new EmployeeGUI();;
    private Integer clientID;
    private DeliveryService deliveryService=new DeliveryService();
    private ArrayList<MenuItem> order=new ArrayList<>();
    ListChangeListener<MenuItem> multiSelection = changed -> {
        order.addAll(changed.getList());
        for( MenuItem p : changed.getList())
            textOrder.appendText(p.getTitle()+"\n");};

    public void initialize(){
        Random random=new Random();
        clientID=random.nextInt(10)+1;
        deliveryService = Serializator.deserialize();
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.getSelectionModel().getSelectedItems().addListener(multiSelection);
        show(deliveryService.getMenu());
        sodiumMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        sodiumMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        proteinMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        proteinMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        caloriesMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        caloriesMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        fatMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        fatMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        priceMin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        priceMax.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000000));
        sodiumMax.setEditable(true);
        sodiumMin.setEditable(true);
        proteinMax.setEditable(true);
        proteinMin.setEditable(true);
        priceMax.setEditable(true);
        priceMin.setEditable(true);
        caloriesMax.setEditable(true);
        caloriesMin.setEditable(true);
        fatMin.setEditable(true);
        fatMax.setEditable(true);
        ratingMin.setText("0.0");
        ratingMax.setText("5.0");
    }

    public void show( ArrayList<MenuItem> menu){
        ObservableList<MenuItem> menuItems= FXCollections.observableArrayList();
        menuItems.addAll(menu);
        titleC.setCellValueFactory(new PropertyValueFactory<>("Title"));
        ratingC.setCellValueFactory(new PropertyValueFactory<>("Rating"));
        caloriesC.setCellValueFactory(new PropertyValueFactory<>("Calories"));
        proteinC.setCellValueFactory(new PropertyValueFactory<>("Protein"));
        fatC.setCellValueFactory(new PropertyValueFactory<>("Fat"));
        sodiumC.setCellValueFactory(new PropertyValueFactory<>("Sodium"));
        priceC.setCellValueFactory(new PropertyValueFactory<>("Price"));
        table.setItems(menuItems);
    }

    public void search(){
        ArrayList<MenuItem> sortedMenu = deliveryService.getMenu();
        if(!title.getText().equals("")){
            sortedMenu=deliveryService.searchKeyword(title.getText());
        }
        double maxRating=0.0,minRating=5.0;
        if(!ratingMin.getText().equals("")){
            minRating=Double.parseDouble(ratingMin.getText());
        }
        if(!ratingMax.getText().equals("")) {
            maxRating = Double.parseDouble(ratingMax.getText());
        }
        sortedMenu=deliveryService.sortRating(minRating,maxRating,sortedMenu);
        if(caloriesMax.getValue()!=0){
            sortedMenu=deliveryService.sortCalories(caloriesMin.getValue(),caloriesMax.getValue(),sortedMenu);
        }
        if(proteinMax.getValue()!=0){
            sortedMenu=deliveryService.sortProtein(proteinMin.getValue(),proteinMax.getValue(),sortedMenu);
        }
        if(fatMax.getValue()!=0){
            sortedMenu=deliveryService.sortFat(fatMin.getValue(),fatMax.getValue(),sortedMenu);
        }
        if(sodiumMax.getValue()!=0){
            sortedMenu=deliveryService.sortSodium(sodiumMin.getValue(),sodiumMax.getValue(),sortedMenu);
        }
        if(priceMax.getValue()!=0){
            sortedMenu=deliveryService.sortPrice(priceMin.getValue(),priceMax.getValue(),sortedMenu);
        }
        show(sortedMenu);
    }

    public void orderButton(){
        Order orders;
        if(deliveryService.getOrder().isEmpty()) {
             orders = new Order(1, clientID, LocalDate.now(), LocalTime.now());
        }
        else{
             orders = new Order(1+deliveryService.getOrder().size(), clientID, LocalDate.now(), LocalTime.now());
        }
        deliveryService.createOrder(orders,order);
        deliveryService.finishOrder(orders);
        textOrder.setText("Order has been registered!\n\n");
        order=new ArrayList<>();
        DeliveryService.support = new PropertyChangeSupport(this);
        deliveryService.addPropertyChangeListener(employee);
        deliveryService.setEmployee(DeliveryService.messageE);
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

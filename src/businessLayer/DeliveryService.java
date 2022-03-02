package businessLayer;

import dataLayer.FilesWriter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeliveryService implements IDeliveryServiceProcessing,Serializable {
    private ArrayList<MenuItem> menu ;
    private Map<Order, ArrayList<MenuItem>> orderMenu ;
    private ArrayList<Order> orders;
    private ArrayList<User> users;
    public static PropertyChangeSupport support;
    public static String messageE;
    private String print;

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setEmployee(String value){
        support.firePropertyChange("message",this.print,value);
        this.print=value;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<MenuItem> menu) {
        this.menu = menu;
    }

    public Map<Order, ArrayList<MenuItem>> getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(Map<Order, ArrayList<MenuItem>> orderMenu) {
        this.orderMenu = orderMenu;
    }

    public ArrayList<Order> getOrder() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public void wellFormed(){
        assert menu!=null;
        assert orderMenu!=null;
        assert orders!=null;
        assert users!=null;
        assert support!=null;
        assert messageE!=null;
        assert print!=null;
    }

    public DeliveryService() {
        this.menu = new ArrayList<>();
        this.orderMenu = new HashMap<>();
        this.orders = new ArrayList<>();
        this.users=new ArrayList<>();
        this.support = new PropertyChangeSupport(this);
        wellFormed();
    }

    private final Function<String, BaseProduct> mapToItem = (line) -> {
        String[] p = line.split(",");
        return new BaseProduct(p[0],Double.parseDouble(p[1]),Integer.parseInt(p[2]),Integer.parseInt(p[3]),Integer.parseInt(p[4]),Integer.parseInt(p[5]),Integer.parseInt(p[6]));
    };

    public <T> Predicate<T> distinctByKey(Function<? super T, String> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void importProducts() {
        List<MenuItem> input = new ArrayList<>();
        try{
            File inputFile = new File("products.csv");
            InputStream inputFileStream = new FileInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFileStream));
            input = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        menu = (ArrayList<MenuItem>) input.stream().filter(distinctByKey((MenuItem::getTitle))).distinct().collect(Collectors.toList());
    }

    public void addMenuItem(MenuItem menuItem){
        assert menuItem!=null;
        int preSize= menu.size();
        menu.add(menuItem);
        assert preSize+1==menu.size();
    }

    public void deleteMenuItem(MenuItem menuItem){
        assert menuItem != null;
        int preSize = menu.size();
        menu.removeIf(menuItem1 -> menuItem.getTitle().equals(menuItem1.getTitle()));
        assert preSize == menu.size()+1;
    }

    public void modifyMenuItem(MenuItem menuItem){
        assert menuItem != null;
        int postPrice = menuItem.getPrice();
        int prePrice=0;
        int i=0;
        for(MenuItem menuItem1: new ArrayList<>(menu)){
            if(menuItem.getTitle().equals(menuItem1.getTitle())){
                menu.set(i,menuItem);
                prePrice = menu.get(i).getPrice();
            }
            i++;
        }
        assert prePrice == postPrice;
    }

    public void createCompositeProduct(ArrayList<MenuItem> menuItems,String name){
        assert menuItems!=null;
        assert name!=null;
        int presize=menu.size();
        CompositeProduct compositeProduct=new CompositeProduct(name,menuItems);
        compositeProduct.computePrice();
        menu.add(compositeProduct);
        assert presize+1==menu.size();
    }

    public void createOrder(Order order, ArrayList<MenuItem> menuItem) {
        assert order != null;
        assert menuItem != null;
        int preSize = orderMenu.size();
        int preSize1 = orders.size();
        orderMenu.put(order, menuItem);
        orders.add(order);
        assert preSize+1==orderMenu.size();
        assert preSize1+1==orders.size();
    }

    public void finishOrder(Order order){
        StringBuilder sb = new StringBuilder();
        sb.append("NEW order created\n");
        sb.append("Order ID: ").append(order.getOrderID()).append("\n").append("Client ID: ").append(order.getClientID()).append("\n");
        sb.append("Date: ").append(order.getOrderDate().toString());
        sb.append("\nTime: ").append(order.getOrderTime().toString()).append("\nOrdered Items:\n");
        ArrayList<MenuItem> list = orderMenu.get(order);
        assert list!=null;
        for (MenuItem menuItem : list) {
            sb.append("Title: ").append(menuItem.getTitle()).append(", Rating: ").append(menuItem.getRating()).append(", Calories: ").append(menuItem.getCalories()).append(", Protein: ").append(menuItem.getProtein()).append(", Fat: ").append(menuItem.getFat()).append(", Sodium: ").append(menuItem.getSodium()).append(", Price: ").append(menuItem.getPrice()).append("\n");
        }
        sb.append("Price: ").append(computePrice(order)).append("\n\n");
        FilesWriter f =new FilesWriter();
        f.generateBill(sb.toString());
        messageE=sb.toString();
    }

    public int computePrice(Order order) {
        assert order != null ;
        int price=0;
        if(orders.contains(order)){
            ArrayList<MenuItem> orderedItems = orderMenu.get(order);
            for (MenuItem orderedItem : orderedItems) {
                price += orderedItem.getPrice();
            }
        }
        assert price>0;
        return price;
    }

    public void generateReports(){
        StringBuilder sb = new StringBuilder();
        sb.append("New report created!\n").append("Time interval of the orders:\n");
        orders.stream().filter(o -> o.getOrderTime().isAfter(LocalTime.of(0,30))&&o.getOrderTime().isBefore(LocalTime.of(23,59))).forEach(o -> {
                    sb.append("Order ID: ").append(o.getOrderID()).append("\n").append("Client ID: ").append(o.getClientID()).append("\n").append("Date: ").append(o.getOrderDate().toString()).append("\nTime: ").append(o.getOrderTime().toString()).append("\nOrdered Items:\n");
                    orderMenu.get(o).forEach(m-> sb.append("Title: ").append(m.getTitle()).append(", Rating: ").append(m.getRating()).append(", Calories: ").append(m.getCalories()).append(", Protein: ").append(m.getProtein()).append(", Fat: ").append(m.getFat()).append(", Sodium: ").append(m.getSodium()).append(", Price: ").append(m.getPrice()).append("\n"));
                    sb.append("Price: ").append(computePrice(o)).append("\n\n");
                });
        sb.append("Products ordered more than 2 times are:\n");
        ArrayList<MenuItem> aux=new ArrayList<>();
        AtomicInteger counter = new AtomicInteger();
        orders.forEach(o-> orderMenu.get(o).forEach(m -> {
            counter.set(0);
            orders.stream().filter(o1->o1.getOrderID()>o.getOrderID()).forEach(o1 ->orderMenu.get(o1).stream().filter(m1 -> m1.equals(m)).forEach(m1 -> counter.getAndIncrement()));
            if (counter.get() >= 3 &&!aux.contains(m)) {
                aux.add(m);
            }
        }));
        aux.forEach(m->sb.append("Title: ").append(m.getTitle()).append(", Rating: ").append(m.getRating()).append(", Calories: ").append(m.getCalories()).append(", Protein: ").append(m.getProtein()).append(", Fat: ").append(m.getFat()).append(", Sodium: ").append(m.getSodium()).append(", Price: ").append(m.getPrice()).append("\n"));
        sb.append("\n\nClients that ordered more than 2 times and price was greater than 200 are:\n");
        ArrayList<Integer> id=new ArrayList<>();
        orders.forEach(o->{
            counter.set(0);
            orders.stream().filter(o1->o1.getOrderID()>o.getOrderID()&&o.getClientID()==o1.getClientID()&&computePrice(o1)>200).forEach(o1-> counter.getAndIncrement());
            if(counter.get() >=2 && !id.contains(o.getClientID())){
                id.add(o.getClientID());
            }
        });
        id.forEach(i->sb.append(i).append("\n"));
        sb.append("\n\nThe products ordered on 21-05-2021:\n");
        ArrayList<String> name=new ArrayList<>();
        orders.stream().filter(o->o.getOrderDate().isEqual(LocalDate.of(2021,5,21))).forEach(o-> orderMenu.get(o).stream().filter(m->!name.contains(m.getTitle())).forEach(m->name.add(m.getTitle())));
        name.forEach(n->{
            sb.append(n);
            counter.set(1);
            orders.stream().filter(o->o.getOrderDate().isEqual(LocalDate.of(2021,5,21))).forEach(o->
                orderMenu.get(o).stream().filter(m->n.equals(m.getTitle())).forEach(m->counter.getAndIncrement()));
            sb.append(" ").append(counter).append(" times\n");
        });
        FilesWriter f =new FilesWriter();
        f.generateReports(sb.toString());
    }

    public ArrayList<MenuItem> searchKeyword(String keyword){
        assert keyword!=null;
        ArrayList<MenuItem> sortedMenu = new ArrayList<>();
        menu.stream().filter(s -> s.getTitle().contains(keyword)).forEach(sortedMenu::add);
        return sortedMenu;
    }

    public ArrayList<MenuItem> sortRating(double min,double max,ArrayList<MenuItem> myMenu){
        assert min>=0;
        assert max>=0;
        ArrayList<MenuItem> sortedMenu = new ArrayList<>();
        myMenu.stream().filter(s -> s.getRating()>=min&&s.getRating()<=max).forEach(sortedMenu::add);
        return sortedMenu;
    }

    public ArrayList<MenuItem> sortCalories(int min,int max,ArrayList<MenuItem> myMenu){
        assert min>=0;
        assert max>=0;
        ArrayList<MenuItem> sortedMenu = new ArrayList<>();
        myMenu.stream().filter(s -> s.getCalories()>=min&&s.getCalories()<=max).forEach(sortedMenu::add);
        return sortedMenu;
    }

    public ArrayList<MenuItem> sortProtein(int min,int max,ArrayList<MenuItem> myMenu){
        assert min>=0;
        assert max>=0;
        ArrayList<MenuItem> sortedMenu = new ArrayList<>();
        myMenu.stream().filter(s -> s.getProtein()>=min&&s.getProtein()<=max).forEach(sortedMenu::add);
        return sortedMenu;
    }

    public ArrayList<MenuItem> sortFat(int min,int max,ArrayList<MenuItem> myMenu){
        assert min>=0;
        assert max>=0;
        ArrayList<MenuItem> sortedMenu = new ArrayList<>();
        myMenu.stream().filter(s -> s.getFat()>=min&&s.getFat()<=max).forEach(sortedMenu::add);
        return sortedMenu;
    }

    public ArrayList<MenuItem> sortSodium(int min,int max,ArrayList<MenuItem> myMenu){
        assert min>=0;
        assert max>=0;
        ArrayList<MenuItem> sortedMenu = new ArrayList<>();
        myMenu.stream().filter(s -> s.getSodium()>=min&&s.getSodium()<=max).forEach(sortedMenu::add);
        return sortedMenu;
    }

    public ArrayList<MenuItem> sortPrice(int min,int max,ArrayList<MenuItem> myMenu){
        ArrayList<MenuItem> sortedMenu = new ArrayList<>();
        myMenu.stream().filter(s -> s.getPrice()>=min&&s.getPrice()<=max).forEach(sortedMenu::add);
        return sortedMenu;
    }

    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeObject(menu);
        stream.writeObject(orderMenu);
        stream.writeObject(orders);
        stream.writeObject(users);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        menu = (ArrayList<MenuItem>) stream.readObject();
        orderMenu = (Map<Order, ArrayList<MenuItem>>) stream.readObject();
        orders= (ArrayList<Order>) stream.readObject();
        users= (ArrayList<User>) stream.readObject();
    }
}
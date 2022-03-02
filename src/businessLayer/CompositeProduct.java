package businessLayer;

import java.util.ArrayList;

public class CompositeProduct extends MenuItem{
    private ArrayList<MenuItem> menuItems ;

    public CompositeProduct(String title, double rating, int calories, int protein, int fat, int sodium, int price) {
        super(title, rating, calories, protein, fat, sodium, price);
        menuItems=new ArrayList<>();
    }

    public CompositeProduct(String title,ArrayList<MenuItem> menuItem){
        super(title,0.0,0,0,0,0,0);
        menuItems=new ArrayList<>();
        menuItems=menuItem;
    }

    public int computePrice() {
        for (MenuItem menuItem : menuItems) {
            price += menuItem.getPrice();
            rating += menuItem.getRating();
            sodium += menuItem.getSodium();
            fat += menuItem.getFat();
            calories += menuItem.getCalories();
            protein += menuItem.getProtein();
        }
        rating=rating/menuItems.size();
        return price;
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}

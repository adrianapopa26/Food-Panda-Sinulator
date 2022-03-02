package businessLayer;

import java.util.ArrayList;

/**
 * Interface for DeliveryService Class
 */
public interface IDeliveryServiceProcessing {
    /**
     * @inv Invariant for DeliveryService
     */
    void wellFormed();

    /**
     * Imports the products from the .csv file and creates the Menu
     */
    void importProducts();

    /**
     * Insert a new item in the Menu
     * @pre item is not null
     * @post the size of Menu increases
     * @param menuItem item to be inserted
     */
    void addMenuItem(MenuItem menuItem);

    /**
     * Deletes an item from the Menu
     * @pre the item is not null
     * @post the size of Menu decreases
     * @param menuItem item to be deleted
     */
    void deleteMenuItem(MenuItem menuItem);

    /**
     * Modifies an item in the Menu
     * @pre the item is not null
     * @post the size of Menu decreases
     * @param menuItem item to be modified
     */
    void modifyMenuItem(MenuItem menuItem);

    /**
     * Create a new composite product and add it in the Menu
     * @pre the items array is not null
     * @pre the name is not null
     * @post the size of the menu increases
     * @param menuItems items forming the new product
     * @param name name of the product
     */
    void createCompositeProduct(ArrayList<MenuItem> menuItems, String name);

    /**
     * Create a new order
     * @pre order is not null
     * @pre  arraylist of products is not null
     * @post the size of order HashMap increases
     * @post the size of orders increases
     * @param order New order created
     * @param menuItem The products from the order
     */
    void createOrder(Order order, ArrayList<MenuItem> menuItem);

    /**
     * Finish the order and generate the bill
     * @pre orderMenu.get(order)is not null
     * @param order order to be finished
     */
    void finishOrder(Order order);

    /**
     * Compute the price of the order
     * @param order for which the price is computed
     * @return the price
     */
    int computePrice(Order order);

    /**
     * Generate the reports for the administrator
     */
    void generateReports();

    /**
     * Search for the word containing the keyword
     * @pre keyword is not null
     * @param keyword word to be searched
     * @return menu of items containing the keyword
     */
    ArrayList<MenuItem> searchKeyword(String keyword);

    /**
     * Search for items having the rating between the specified ranges
     * @pre min is not null
     * @pre max is not null
     * @param min minimum value
     * @param max maximum value
     * @param myMenu menu the search is executed for
     * @return the menu within the range
     */
    ArrayList<MenuItem> sortRating(double min,double max,ArrayList<MenuItem> myMenu);

    /**
     * Search for items having the calories between the specified ranges
     * @pre min is not null
     * @pre max is not null
     * @param min minimum value
     * @param max maximum value
     * @param myMenu menu the search is executed for
     * @return the menu within the range
     */
    ArrayList<MenuItem> sortCalories(int min,int max,ArrayList<MenuItem> myMenu);

    /**
     * Search for items having the protein between the specified ranges
     * @pre min is not null
     * @pre max is not null
     * @param min minimum value
     * @param max maximum value
     * @param myMenu menu the search is executed for
     * @return the menu within the range
     */
    ArrayList<MenuItem> sortProtein(int min,int max,ArrayList<MenuItem> myMenu);

    /**
     * Search for items having the fat between the specified ranges
     * @pre min is not null
     * @pre max is not null
     * @param min minimum value
     * @param max maximum value
     * @param myMenu menu the search is executed for
     * @return the menu within the range
     */
    ArrayList<MenuItem> sortFat(int min,int max,ArrayList<MenuItem> myMenu);

    /**
     * Search for items having the sodium between the specified ranges
     * @pre min is not null
     * @pre max is not null
     * @param min minimum value
     * @param max maximum value
     * @param myMenu menu the search is executed for
     * @return the menu within the range
     */
    ArrayList<MenuItem> sortSodium(int min,int max,ArrayList<MenuItem> myMenu);

    /**
     * Search for items having the price between the specified ranges
     * @pre min is not null
     * @pre max is not null
     * @param min minimum value
     * @param max maximum value
     * @param myMenu menu the search is executed for
     * @return the menu within the range
     */
    ArrayList<MenuItem> sortPrice(int min,int max,ArrayList<MenuItem> myMenu);
}

package businessLayer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Order implements Serializable {
    private int orderID;
    private int clientID;
    private LocalDate orderDate;
    private LocalTime orderTime;

    public Order(int orderID, int clientID, LocalDate orderDate, LocalTime orderTime) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalTime orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public int hashCode()
    {
        return 13 + 5 * orderID + 7 * clientID + 11 * orderDate.getDayOfMonth();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderID == order.orderID &&
                clientID == order.clientID &&
                Objects.equals(orderDate, order.orderDate) &&
                Objects.equals(orderTime, order.orderTime);
    }
}

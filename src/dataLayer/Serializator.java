package dataLayer;

import businessLayer.DeliveryService;
import java.io.*;

public class Serializator {
    public static void serialize(DeliveryService deliveryService) {
        try {
            FileOutputStream fileOut = new FileOutputStream("DeliveryService.txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(deliveryService);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static DeliveryService deserialize() {
        DeliveryService deliveryService=new DeliveryService();
        try {
            FileInputStream fileIn = new FileInputStream("DeliveryService.txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            deliveryService = (DeliveryService) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException|ClassNotFoundException i) {
            System.out.println("IDK FAIL!");
        }
        return deliveryService;
    }
}

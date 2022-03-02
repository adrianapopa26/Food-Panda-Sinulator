package dataLayer;
import java.io.FileWriter;
import java.io.IOException;

public class FilesWriter {
    public void generateBill(String s){
        try{
            FileWriter myWriter=new FileWriter("Bill.txt",true);
            myWriter.write(s);
            myWriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void generateReports(String s){
        try{
            FileWriter myWriter=new FileWriter("Reports.txt",true);
            myWriter.write(s);
            myWriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

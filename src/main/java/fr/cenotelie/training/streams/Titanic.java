package fr.cenotelie.training.streams;

import java.util.ArrayList;
import java.util.List;

public class Titanic {

    private List<Customer> customers;

    public Titanic() {
        customers = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        //TODO: Load data using BufferedReader and Scanner

    }

    public void head(int n) {
        //Display n first lines
    }

    public void tail(int n) {
        //Display n last lines
    }

    public List<Customer> getCustomers() {
        //return list of customers
        return null;
    }

    public static void main(String[] argv) {
        Titanic t = new Titanic();
        //TODO: Complete requests
    }

}

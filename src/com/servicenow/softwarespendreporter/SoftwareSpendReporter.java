package com.servicenow.softwarespendreporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SoftwareSpendReporter {
    public static void main(String[] args) {
        //Get path of CSV file, assuming that this file is valid
        String filePath = args[0];
        //Get the transactions by sorted the vendor's name
        ArrayList<Transaction> transactions = getTransactions(filePath);
        //Hashtable for spending report
        LinkedHashMap<String, TreeMap<String, Integer>> spendReport = getReport(transactions);
        //Print out report
        printReport(spendReport);
    }

    private static ArrayList<Transaction> getTransactions(String fp) {
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        BufferedReader reader = null;
        String line;

        try {
            reader = new BufferedReader(new FileReader(fp));
            //Skip first row since it's the title row
            reader.readLine();
            //Loop through, starting from the second line
            //Create a new Transaction object
            //Append that object to the transactions list
            while((line = reader.readLine()) != null) {
                //Read in each row
                //Each row will have 4 elements
                //[0] : transactionDate, [1] : name of vendor, [2] : name of product, [3] : amount of money
                String[] row = line.split(",");
                String trDate = row[0];
                String vd = row[1];
                String pd = row[2];
                int amt = Integer.parseInt(row[3]);
                //Create a new instance of transaction
                Transaction transaction = new Transaction(trDate, vd, pd, amt);
                //Append transaction to the list
                transactionsList.add(transaction);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //close reader when finished
            try {
                assert reader != null;
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //sort the transaction list and return it
        transactionsList.sort(Transaction.vendorNameComparator);
        return transactionsList;
    }

    private static LinkedHashMap<String, TreeMap<String, Integer>> getReport(ArrayList<Transaction> transactions) {
        LinkedHashMap<String, TreeMap<String, Integer>> report = new LinkedHashMap<>();

        for(Transaction t : transactions) {
            if(!report.containsKey(t.vendor)) {
                TreeMap<String, Integer> productAndAmount = new TreeMap<>();
                productAndAmount.put(t.product, t.amount);
                report.put(t.vendor, productAndAmount);
            }
            else {
                TreeMap<String, Integer> tmpProdAndAmount = report.get(t.vendor);
                //If there's already a product from this vendor, update the amount
                if(tmpProdAndAmount.containsKey(t.product)) {
                    int currProdAmount = tmpProdAndAmount.get(t.product);
                    tmpProdAndAmount.replace(t.product, currProdAmount + t.amount);
                }
                //If not, put the new product with its' amount cost in
                else {
                    tmpProdAndAmount.put(t.product, t.amount);
                }
                //Update this vendor info
                report.replace(t.vendor, tmpProdAndAmount);
            }
        }

        return report;
    }

    private static void printReport(LinkedHashMap<String, TreeMap<String, Integer>> report) {
        Locale usa = new Locale("en", "US");
        NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);
        dollarFormat.setMaximumFractionDigits(0);
        report.forEach((vendor, products) -> {
            int total = getTotal(products);
            System.out.println(vendor + " " + dollarFormat.format(total));
            products.forEach((prod, amt) -> System.out.println("  " + prod + " " + dollarFormat.format(amt)));
        });
    }

    private static int getTotal(TreeMap<String, Integer> products) {

        AtomicInteger total = new AtomicInteger();

        products.forEach((prod, amt) -> total.addAndGet(amt));
        return total.get();
    }
}

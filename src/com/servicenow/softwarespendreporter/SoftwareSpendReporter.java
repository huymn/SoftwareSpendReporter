package com.servicenow.softwarespendreporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class: SoftwareSpendReporter
 *
 * Print out report for software expenses
 *
 * Author: Huy Nguyen
 * Modified: 5/14/2021
 */
public class SoftwareSpendReporter {
    /**
     * Method: main
     * @param args - list of arguments, first entry should be file path
     */
    public static void main(String[] args) {
        //Check to make sure that there's a correct amount of arguments
        if(args.length != 1) {
            System.out.println("Incorrect number of arguments");
            System.out.println("Expected: 1, got: " + args.length);
            return;
        }
        //Get path of CSV file, assuming that this file is valid
        String filePath = args[0];
        SoftwareSpendReporter softwareSpendReporter = new SoftwareSpendReporter();
        softwareSpendReporter.printReport(filePath);

    }

    /**
     * Method: printReport
     * Generate report from csv file and print report
     * @param fp filepath of .csv file
     */
    public void printReport(String fp) {
        //Get the transactions by sorted the vendor's name
        ArrayList<Transaction> transactions = getTransactions(fp);
        //Hashtable for spending report
        LinkedHashMap<String, TreeMap<String, Integer>> spendReport = getReport(transactions);
        //Print out report
        print(spendReport);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    //////////////                   HELPER METHODS                        ////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Method: getTransactions
     *
     * Take in the path to the csv file
     * Read from the csv file and return a list of the transactions sorted by the vendor's name
     *
     * @param fp path to csv file
     * @return transaction list sorted by vendor's name
     */
    public ArrayList<Transaction> getTransactions(String fp) {
        if(fp == null) {
            throw new NullPointerException("file path is null");
        }
        //List of transactions
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        //Reader for reading from file
        BufferedReader reader = null;
        //Variable for storing each line read in from the csv file
        String line;

        try {
            //Initialized buffered reader for the file specified by the path
            reader = new BufferedReader(new FileReader(fp));
            //Skip first line since it's the titles line
            reader.readLine();
            //Loop through, starting from the second line
            //Create a new Transaction object
            //Append that object to the transactions list
            while((line = reader.readLine()) != null) {
                //Split by the commas for each entry of row
                String[] row = line.split(",");
                /*
                  Each row will have 4 elements
                  [0] : transaction date
                  [1] : name of vendor
                  [2] : name of product
                  [3] : amount of money
                 */
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
            //Print error if file reader is giving an exception
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

    /**
     * Method: getReport
     *
     * Take in list of transactions
     * Convert it to proper report information
     * @param transactions sorted list of transactions
     * @return proper report information
     */
    public LinkedHashMap<String, TreeMap<String, Integer>> getReport(ArrayList<Transaction> transactions) {
        //LinkedHashMap for report since we want to keep it in alphabetical order
        //The list of transactions will be sorted so when we iterate through transactions
        //We want to keep the order of which element we put in
        LinkedHashMap<String, TreeMap<String, Integer>> report = new LinkedHashMap<>();

        //Loop through each transaction in transactions
        for(Transaction transaction : transactions) {
            //If the vendor of this transaction is not in the hashmap, put a new one in
            if(!report.containsKey(transaction.getVendor())) {
                TreeMap<String, Integer> productAndAmount = new TreeMap<>();
                productAndAmount.put(transaction.getProduct(), transaction.getAmount());
                report.put(transaction.getVendor(), productAndAmount);
            }
            else {
                //Get the current product and amount of this vendor
                TreeMap<String, Integer> currProdAndAmount = report.get(transaction.getVendor());
                //If there's already a product from this vendor, update the amount
                if(currProdAndAmount.containsKey(transaction.getProduct())) {
                    int currProdAmount = currProdAndAmount.get(transaction.getProduct());
                    currProdAndAmount.replace(transaction.getProduct(), currProdAmount + transaction.getAmount());
                }
                //If not, put the new product with its' amount cost in
                else {
                    currProdAndAmount.put(transaction.getProduct(), transaction.getAmount());
                }
                //Update this vendor info
                report.replace(transaction.getVendor(), currProdAndAmount);
            }
        }
        //Return the report
        return report;
    }

    /**
     * Method: print
     *
     * Take in the report and print it in a two-level tree format
     * @param report report of the software expenses
     */
    public void print(LinkedHashMap<String, TreeMap<String, Integer>> report) {
        //Get the locale to display proper dollar format
        Locale usa = new Locale("en", "US");
        //Number format for printing out proper dollar amount
        NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(usa);
        //Set decimal to 0 so we don't print out cent, can be remove if we want to be more precise with the amount
        dollarFormat.setMaximumFractionDigits(0);
        //Print out each vendor and their products
        report.forEach((vendor, products) -> {
            int total = getTotal(products);
            System.out.println(vendor + " " + dollarFormat.format(total));
            //Print out products of this vendor, make sure to indent 2 spaces
            products.forEach((prod, amt) -> System.out.println("  " + prod + " " + dollarFormat.format(amt)));
        });
    }

    /**
     * Method: getTotal
     *
     * Get the total amount spend from buy product of a certain vendor
     * @param products products from certain vendor
     * @return total cost of all these products
     */
    public int getTotal(TreeMap<String, Integer> products) {
        //Use atomic integer so it can be used in a lambda expression
        AtomicInteger total = new AtomicInteger();
        //Calculate total amount
        products.forEach((product, amount) -> total.addAndGet(amount));
        //Return total amount
        return total.get();
    }
}

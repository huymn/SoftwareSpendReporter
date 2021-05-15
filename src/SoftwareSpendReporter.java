import java.util.*;
import java.io.*;

class SoftwareSpendReporter {
    public static void main(String[] args) {
        //Check for valid arguments
        if(args.length != 2) {
            System.out.println("Incorrect number of arguments");
            System.out.println("Example of correct command: java main /test.csv");
            return;
        }
        //Get path of CSV file, assuming that this file is valid
        String filePath = args[1];

        ArrayList<Transaction> transactions = getTransactions(filePath);

    }

    private static ArrayList<Transaction> getTransactions(String fp) {
        ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();



        return transactionsList;
    }
}

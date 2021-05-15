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
        BufferedReader reader = null;
        String line = "";

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
        transactionsList.sort(Transaction.getVendorNameComparator());
        return transactionsList;
    }
}

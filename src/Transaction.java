import java.util.Comparator;
import java.util.Locale;

public class Transaction {
    protected String transactionDate;
    protected String vendor;
    protected String product;
    protected int amount;

    public Transaction(String td, String v, String p, int a) {
        this.transactionDate = td;
        this.vendor = v;
        this.product = p;
        this.amount = a;
    }

    //Comparator so that transactions can be sorted
    public static Comparator<Transaction> vendorNameComparator = new Comparator<Transaction>() {
        @Override
        public int compare(Transaction t1, Transaction t2) {
            String vendor1 = t1.getVendor().toUpperCase();
            String vendor2 = t2.getVendor().toUpperCase();
            return vendor1.compareTo(vendor2);
        }
    };

    public static Comparator<Transaction> getVendorNameComparator() {
        return vendorNameComparator;
    }

    //Accessors
    public String getTransactionDate() {
        return this.transactionDate;
    }

    public String getVendor() {
        return this.vendor;
    }

    public String getProduct() {
        return this.product;
    }

    public int getAmount() {
        return this.amount;
    }
}

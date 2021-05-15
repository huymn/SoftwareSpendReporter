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

    //Accessors
    private String getTransactionDate() {
        return this.transactionDate;
    }

    private String getVendor() {
        return this.vendor;
    }

    private String getProduct() {
        return this.product;
    }

    private int getAmount() {
        return this.amount;
    }
}

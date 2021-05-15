package com.servicenow.softwarespendreporter;
import java.util.Comparator;

/**
 * Class: Transaction
 *
 * Contains information of a transaction that was made
 *
 * Author: Huy Nguyen
 * Modified: 5/14/2021
 *
 */
public class Transaction {
    private String transactionDate;
    private String vendor;
    private String product;
    private int amount;

    /**
     * Constructor
     * Initial all instance variables
     * @param transactionDate - when the purchase was made
     * @param vendor - the vendor that was paid in this transaction
     * @param product - software product that was purchase
     * @param amount - amount, in US dollars, that was spent in this transaction
     */
    public Transaction(String transactionDate, String vendor, String product, int amount) {
        this.transactionDate = transactionDate;
        this.vendor = vendor;
        this.product = product;
        this.amount = amount;
    }

    /**
     * Lambda expression for vendor name comparator for sorting
     */
    public static Comparator<Transaction> vendorNameComparator = (t1, t2) -> {
        String vendor1 = t1.vendor.toUpperCase();
        String vendor2 = t2.vendor.toUpperCase();
        return vendor1.compareTo(vendor2);
    };

    /**
     * Method: getTransactionDate
     * Getter for instance variable transactionDate
     * @return transactionDate
     */
    public String getTransactionDate() {
        return this.transactionDate;
    }

    /**
     * Method: getVendor
     * Getter for instance variable vendor
     * @return vendor
     */
    public String getVendor() {
        return this.vendor;
    }

    /**
     * Method: getProduct
     * Getter for instance variable product
     * @return product
     */
    public String getProduct() {
        return this.product;
    }

    /**
     * Method: getAmount
     * Getter for instance variable amount
     * @return amount
     */
    public int getAmount() {
        return this.amount;
    }
}

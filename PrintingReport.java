package auracafe;

import java.sql.Date;

public class PrintingReport {

    private String tokenID;
    private Date todayDate;
    private String item;
    private int quantity;
    private int amount;
    public String getTokenID() {
        return tokenID;
    }
    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }
    public Date getTodayDate() {
        return todayDate;
    }
    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }
    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }  
}

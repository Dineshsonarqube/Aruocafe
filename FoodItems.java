package auracafe;

public class FoodItems {
    private String item;
    private int amount;
    private int quantity;
    private int unitPrice;
    private boolean isOutOfStock;
    private int stock;
    private String id;

    public FoodItems() {
    }

    public FoodItems(String item, int unitPrice, boolean isOutOfStock, int stock, String id) {
        this.item = item;
        this.unitPrice = unitPrice;
        this.isOutOfStock = isOutOfStock;
        this.stock = stock;
        this.id = id;
    }

    public FoodItems(Builder builder) {
        this.item = builder.item;
        this.unitPrice = builder.unitPrice;
        this.amount = builder.amount;
        this.quantity = builder.quantity;
        this.isOutOfStock = builder.isOutOfStock;
        this.stock = builder.stock;
        this.id= builder.id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public boolean getIsOutOfStock() {
        return isOutOfStock;
    }

    public void setIsOutOfStock(boolean isOutOfStock) {
        this.isOutOfStock = isOutOfStock;
    }

    public int getStock(){
        return stock;
    }

    public void setStock(int stock){
        this.stock = stock;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public static class Builder {

        private String item;
        private int amount;
        private int quantity;
        private int unitPrice;
        private boolean isOutOfStock;
        private int stock;
        private String id;

        public Builder(String item) {
            this.item = item;
        }

        public FoodItems build() {
            FoodItems foodItem = new FoodItems(this);
            return foodItem;
        }

        public Builder isOutOfStock(boolean isOutOfStock) {
            this.isOutOfStock = isOutOfStock;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder unitPrice(int unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public Builder stock(int stock){
            this.stock =  stock;
            return this;
        }

        public Builder id(String id){
            this.id =  id;
            return this;
        }

    }

    public static Builder Builder(int newPrice) {
        return null;
    }

}
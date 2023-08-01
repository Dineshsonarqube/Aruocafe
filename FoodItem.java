package auracafe;

public class FoodItem {
    private String item;
    private int price;
    private Boolean isAvaialble;

    public FoodItem() {
    }

    public FoodItem(Builder builder) {
        this.item = builder.item;
        this.price = builder.price;
        this.isAvaialble = builder.isAvaialble;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getIsAvaialble() {
        return isAvaialble;
    }

    public void setIsAvaialble(Boolean isAvaialble) {
        this.isAvaialble = isAvaialble;
    }

    public static class Builder {

        private String item;
        private int price;
        private Boolean isAvaialble;

        public Builder(int price) {
            this.price = price;
        }

        public Builder isAvailable(boolean isAvaialble) {
            this.isAvaialble = isAvaialble;
            return this;
        }

        public Builder name(String item) {
            this.item = item;
            return this;
        }

        public FoodItem build() {
            FoodItem foodItem = new FoodItem(this);
            return foodItem;
        }
    }

    public static Builder Builder(int newPrice) {
        return null;
    }

}
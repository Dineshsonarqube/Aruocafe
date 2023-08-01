package auracafe;

import java.util.List;

public class Token {
    private int token;
    List<FoodItem> items;

    public Token() {
    }

    public Token(int token, List<FoodItem> items) {
        this.token = token;
        this.items = items;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public List<FoodItem> getItems() {
        return items;
    }

    public void setItems(List<FoodItem> items) {
        this.items = items;
    }

}

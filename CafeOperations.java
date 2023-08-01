package auracafe;

public interface CafeOperations {
    void openCafe();
    void madeStockAvailable(int index, String item);
    void getClosingCollection(boolean useByStream);
}

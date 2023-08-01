package auracafe;

public class AuraCafe {
    public static void main(String[] args) {
      
        CafeOperationImpl cafeOperationImpl = new CafeOperationImpl();
        cafeOperationImpl.openCafe();
        cafeOperationImpl.getClosingCollection(false);
        
    }
}


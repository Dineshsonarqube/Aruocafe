package auracafe;

import java.util.*;
import java.sql.*;

public class CafeOperationImpl implements CafeOperations, Runnable {

    private List<FoodItems> foodItems ;
    
    private Map<Integer, List<FoodItems>> tokenMap;
    Scanner commonScanner = new Scanner(System.in);
    static final String CONNECTION_URL = "jdbc:mysql://192.168.101.143:3306/cafe";
    static final String USER_NAME = "magiluser";
    static final String PASSWORD = "magiluser";
    static final String CLASSNAME_DB_CONNECT = "com.mysql.cj.jdbc.Driver";
    Connection con = null;
    int myChoice = 0;
    int myQuant = 0;

    public void commonDbConnection(){
        
       try{
        Class.forName(CLASSNAME_DB_CONNECT);
        con = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD);
       }catch(Exception e){
        System.out.println(e);
       }
    }

    private FoodItems addFoodItem(String item, int price, boolean isOutOfStock, int stockAvaialble, String uId) {
        return new FoodItems.Builder(item).unitPrice(price).isOutOfStock(isOutOfStock).stock(stockAvaialble).id(uId).build();
    }

    @Override
    public void openCafe() {
        boolean isUserNotClosedOrder = true;
        int tokenCount = 1;
        tokenMap = new HashMap<>();

        do {
            System.out.println(
                    "101 - Place your order\n102 - Add new item\n103 - Make a item available\n104 - Make a item not available\n105 - Close your order\n106 - Maximum item sold\n107 - Find a item is present in today's sales\nEnter your choice: ");
            int ownerChoice = commonScanner.nextInt();
            if (ownerChoice == 101) {
                displayItems();
                userChoices(tokenCount);
                displayToken();
                tokenCount++;
            } else if (ownerChoice == 102) {
                addFoodItems();
            } else if (ownerChoice == 103) {
                makeItemAvailable();
            } else if (ownerChoice == 104) {
                makeItemNotAvailable();
            } else if (ownerChoice == 105) {
                isUserNotClosedOrder = false;
            } else if (ownerChoice == 106) {
                getMaximumStockSold();
            } else if (ownerChoice == 107) {
                findToknowWhatSoldToday();
            }
        } while (isUserNotClosedOrder);
        commonScanner.close();

    }

    private void addFoodItems() {
        try (Scanner scn = new Scanner(System.in)) {
            System.out.println("Add a food item: ");
            String newItem = scn.nextLine();
            System.out.println("Enter the price: ");
            int newPrice = scn.nextInt();
            System.out.println("Enter the availability: ");
            boolean itemAvailable = scn.nextBoolean();
            System.out.println("Add the number of stocks ");
            int noOfStock = scn.nextInt();
            try {
                commonDbConnection();       
                if (!con.isClosed())
                    System.out.println("Mysql database connected successfully..");
                String insertQuery = "INSERT INTO DineshCafe(id, items, unitPrice, isAvailable, stockInHand)VALUES(?,?,?,?,?)";
                try (PreparedStatement pstmt = con.prepareStatement(insertQuery)){
                   
                    UUID uuid = UUID.randomUUID();
                    pstmt.setString(1, uuid.toString());
                    pstmt.setString(2, newItem);
                    pstmt.setInt(3, newPrice);
                    pstmt.setBoolean(4, itemAvailable);
                    pstmt.setInt(5, noOfStock);
                    pstmt.executeUpdate();
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }

            System.out.println("Item " + newItem.toUpperCase() + " added successfully..");
        }
        System.out.println();

    }

    private void makeItemAvailable() {
        Scanner scns = new Scanner(System.in);
        int quty =0;
        System.out.println("Enter the item code: ");
        int makeCode = scns.nextInt();
            System.out.println("Enter the quantity");
             quty = scns.nextInt();
            foodItems.get(makeCode).setStock(quty);
        if (foodItems.get(makeCode) != null)
            foodItems.get(makeCode).setIsOutOfStock(Boolean.TRUE);

            try {
            
                commonDbConnection();
                       
                String updateQuery = "UPDATE DineshCafe SET isAvailable=?, stockInHand=? where id= ?";
               try (PreparedStatement pstmt = con.prepareStatement(updateQuery)){
                pstmt.setBoolean(1, true); 
                pstmt.setInt(2, quty);
                pstmt.setString(3, foodItems.get(makeCode).getId());
                pstmt.executeUpdate();
               }
                con.close();
         scns.close();
         }catch (Exception e) {
            System.out.println(e);
        }
    }

    private void makeItemNotAvailable() {
        Scanner scns = new Scanner(System.in);
        String updateQuery = "UPDATE DineshCafe SET isAvailable=? where id= ?";
        System.out.println("Enter the item code: ");
        int makeCode = scns.nextInt();
        if (foodItems.get(makeCode) != null)
            foodItems.get(makeCode).setIsOutOfStock(Boolean.FALSE);
        
            try {           
                commonDbConnection();      
                
                try(PreparedStatement pstmt = con.prepareStatement(updateQuery)){
                pstmt.setBoolean(1, false); 
                pstmt.setString(2, foodItems.get(makeCode).getId());
                pstmt.executeUpdate();
                }
                con.close();
         }catch (Exception e) {
            System.out.println(e);
        }
    }

    private void displayItems() {
        foodItems = new ArrayList<>();
        System.out.println("Code \t Item \t Price \t Stocks In-hand ");
        System.out.println("---------------------------------------");
        try {
            
            ResultSet rs = null;
            commonDbConnection();
            String selectQuery  = "select * from dineshcafe"; 
            try(Statement p = con.createStatement()){
            rs = p.executeQuery(selectQuery);
            
            while(rs.next()){
                foodItems.add(addFoodItem(rs.getString("items"), rs.getInt("unitPrice"), 
                rs.getBoolean("isAvailable"), rs.getInt("stockInHand"), rs.getString("id")));
                
            }
            rs.close();
            con.close();
        }
        }catch (Exception e) {
            System.out.println(e);
        }
        for (int i = 0; i < foodItems.size(); i++) {
            if (foodItems.get(i).getIsOutOfStock() && foodItems.get(i).getStock() != 0)
                System.out.println(i + " \t" +
                        foodItems.get(i).getItem() + " \t     " + foodItems.get(i).getUnitPrice() + " \t"
                        + foodItems.get(i).getStock()
                // + (foodItems.get(i).getIsOutOfStock() ? " In Stock" : " Out of Stock")
                );
        }
    }

    private void userChoices(int tokenCount) {
        boolean userPromptedToExit = false;
        List<FoodItems> orderedItems = new ArrayList<>();
        do {
            System.out.println("\nEnter code: ");
            int userChoice = commonScanner.nextInt();
            myChoice = userChoice;
            if (userChoice == 100) {
                if (orderedItems.size() != 0) {
                    tokenMap.put(tokenCount, orderedItems);
                    try {
                        addNewToken(tokenCount, orderedItems);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                userPromptedToExit = true;
            } else if (userChoice <= foodItems.size() && foodItems.get(userChoice).getIsOutOfStock()) {

                System.out.print("selected item : " + foodItems.get(userChoice).getItem() + "\n"
                        + "Enter your quantity : ");
                int quantity = commonScanner.nextInt();
                myQuant = quantity;
                int total = quantity * foodItems.get(userChoice).getUnitPrice();
                int balanceStock = 0;
               
                try {
                    if (foodItems.get(userChoice).getStock() == 0) {
                        foodItems.get(userChoice).setIsOutOfStock(Boolean.FALSE);
                    } else {
                        balanceStock = foodItems.get(userChoice).getStock() - quantity;
                        foodItems.get(userChoice).setStock(balanceStock);
                           
                        commonDbConnection();
                    String updateQuery = "UPDATE DineshCafe SET stockInHand=? where id= ?";
                    try(PreparedStatement pstmt = con.prepareStatement(updateQuery)){
                    pstmt.setInt(1, balanceStock);
                    pstmt.setString(2, foodItems.get(userChoice).getId());
                    pstmt.executeUpdate();
                    }
                    con.close();
                    orderedItems.add(
                            new FoodItems.Builder(foodItems.get(userChoice).getItem())
                                        .unitPrice(foodItems.get(userChoice).getUnitPrice())
                                        .amount(total)
                                        .quantity(quantity)
                                        .stock(balanceStock)
                                        .id(foodItems.get(userChoice).getId())
                                        .build());
                    }
                    
           
             }catch (Exception e) {
                System.out.println(e);
            }

                System.out.println(total + " - Next choose Another item  OR  Press '100'");

            } else {
                System.out.println("Please use item code!");
            }

        } while (!userPromptedToExit);
        System.out.println(
                "SI.No\t Item \t \tPrice \t  \t Quantity\t Total Amount\n==================================================================");
        for (int i = 0; i < orderedItems.size(); i++) {

            System.out.println(i + 1 + " \t " + orderedItems.get(i).getItem() + "\t\t"
                    + orderedItems.get(i).getUnitPrice() + " \t *  "
                    + " \t " + orderedItems.get(i).getQuantity() + "    \t\t : " + orderedItems.get(i).getAmount()
                    + "\n");   
                    
        }
    }


    private void addNewToken(int tokenCount, List<FoodItems> orderedItems) throws SQLException {
        
        try {   
            UUID tokenId = UUID.randomUUID();        
            commonDbConnection();
                    
            con.setAutoCommit(false);      
            String updateQuery = "INSERT INTO dineshtoken(id, CurrentDate, token_no, is_cancelled)VALUES(?,?,?,?)";
            try(PreparedStatement pstmt = con.prepareStatement(updateQuery)){
            
            pstmt.setString(1, tokenId.toString());
            pstmt.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
            pstmt.setInt(3, tokenCount);
            pstmt.setBoolean(4, false);
            pstmt.execute();
           }    
            String updateQueryBill = "INSERT INTO billed_item(id, item_id, quantity, amount, is_cancel, token_id)VALUES(?,?,?,?,?,?)";
            try(PreparedStatement pstmt1 = con.prepareStatement(updateQueryBill)){
            for(FoodItems order : orderedItems){
            UUID uuid1 = UUID.randomUUID();
            pstmt1.setString(1, uuid1.toString());
            pstmt1.setString(2, order.getId());
            pstmt1.setInt(3, order.getQuantity());
            pstmt1.setInt(4,order.getAmount());
            pstmt1.setBoolean(5, false);
            pstmt1.setString(6, tokenId.toString());
            pstmt1.addBatch();
            }
            pstmt1.executeBatch();
           }
        }catch (Exception e) {
          System.out.println(e);
          con.rollback();
        }  finally{
            con.setAutoCommit(true);
        }   

    }

    private void displayToken() {
        for (Map.Entry<Integer, List<FoodItems>> _token : tokenMap.entrySet()) {
            Long totalAmount = _token.getValue().stream().mapToLong(e -> e.getAmount()).sum();
            System.out.println("Token No: " + _token.getKey() + " Total Amount: " + totalAmount);           
        }
    }

    public void getClosingCollection(boolean useByStream) {
        if (useByStream) {
            int totalTokens = tokenMap.size();
            Long totalAmounts = tokenMap.entrySet().stream()
                    .mapToLong(x -> x.getValue().stream().mapToLong(e -> e.getAmount()).sum()).sum();

            System.out.println("\n" + "Total Token: " + totalTokens + " Total Amount: " + totalAmounts);

        } else {
            Long count = tokenMap.entrySet().stream()
                    .mapToLong(x -> x.getValue().stream().mapToLong(e -> e.getAmount()).sum()).sum();

            System.out.println("\n" + "Total Token: " + tokenMap.size() + " Total Amount: " + count);
        }

    }

    public void getMaximumStockSold() {
        List<PrintingReport> queryResult = new ArrayList<>();
        Map<String, Integer> refeQty = new HashMap<>();
        Map<String, Integer> refeAmt = new HashMap<>();
        try{
            commonDbConnection();
            String selectQuery  = "select dineshtoken.id, dineshtoken.CurrentDate, dineshcafe.items, "+
            "sum(billed_item.quantity)as Quantity, sum(billed_item.amount)as Amount "+ 
            "from dineshtoken inner join billed_item on dineshtoken.id = billed_item.token_id "+
            "inner join dineshcafe on billed_item.item_id = dineshcafe.id "+
            "group by dineshcafe.items"; 
            try(Statement p = con.createStatement()){
            ResultSet rs = p.executeQuery(selectQuery);
            
            while(rs.next()){
                PrintingReport todayReport = new PrintingReport();
                todayReport.setTokenID(rs.getString("id"));
                todayReport.setTodayDate(rs.getDate("CurrentDate"));
                todayReport.setItem(rs.getString("items"));
                todayReport.setQuantity(rs.getInt("Quantity"));
                todayReport.setAmount(rs.getInt("Amount"));
                queryResult.add(todayReport);
                
            }
   
             for(PrintingReport ptm :queryResult){
                refeQty.put(ptm.getItem(), ptm.getQuantity());
                refeAmt.put(ptm.getItem(), ptm.getAmount());
             }

            }
            Optional<Map.Entry<String, Integer>> maxEntryQuantity = refeQty.entrySet().stream().max(Map.Entry.comparingByValue());
            System.out.println(maxEntryQuantity.get().getKey()+" "+maxEntryQuantity.get().getValue());
            Optional<Map.Entry<String, Integer>> maxEntryAmount = refeAmt.entrySet().stream().max(Map.Entry.comparingByValue());
            System.out.println(maxEntryAmount.get().getKey()+" "+maxEntryAmount.get().getValue());
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void findToknowWhatSoldToday() {
        Scanner scn = new Scanner(System.in);
        Set<String> todayItemSold = new HashSet<>();
        for (Map.Entry<Integer, List<FoodItems>> _token : tokenMap.entrySet()) {
            for (int i = 0; i < _token.getValue().size(); i++) {

                todayItemSold.add(_token.getValue().get(i).getItem());
            }

        }
        System.out.println("Enter a item to know if present today: ");
        String todayItem = scn.nextLine();
        if (todayItemSold.contains(todayItem)) {
            System.out.println("The item " + todayItem + " is present in today's sales");
        } else {
            System.out.println("The item " + todayItem + " is not present in today's sales");
        }

    }

    @Override
    public void madeStockAvailable(int index, String item) {
        throw new UnsupportedOperationException("Unimplemented method 'madeStockAvailable'");
    }

    @Override
    public void run() {
        getMaximumStockSold();
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");

    }

}

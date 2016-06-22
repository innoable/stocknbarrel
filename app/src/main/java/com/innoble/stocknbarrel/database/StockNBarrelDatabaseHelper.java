package com.innoble.stocknbarrel.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.innoble.stocknbarrel.model.DataEntity;
import com.innoble.stocknbarrel.model.Grocery;
import com.innoble.stocknbarrel.model.GroceryStockItem;
import com.innoble.stocknbarrel.model.Product;
import com.innoble.stocknbarrel.model.ShoppingList;
import com.innoble.stocknbarrel.model.ShoppingListItem;
import com.innoble.stocknbarrel.model.User;
import com.innoble.stocknbarrel.provider.Images;

import java.io.File;
import java.util.Random;

/**
 * Created by At3r on 5/17/2016.
 */
public class StockNBarrelDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_PATH = "/data/data/com.innoble.stocknbarrel/databases/";
    public static final String DATABASE_NAME = "stocknbarrel.sqlite";

    public StockNBarrelDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); //db will be created when constructor is called
    }

    private boolean databaseExist()
    {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }

    private void seedData(SQLiteDatabase db){

        Random random = new Random(System.currentTimeMillis());
        //create user
        User user = new User("Random Shopper", "testuser@gmail.com", 2000.0f);
        insertData(db, user);


        Grocery grocery = new Grocery("Massy Stores", "Port of Spain", "One Woodbrook Place, Damian St, Port of Spain");
        grocery.setPhone("868-361-7406");
        insertData(db, grocery);

        Grocery grocery2 = new Grocery("Extra Foods ", "Grand Bazaar", "Grand Bazaar, Bamboo Village, Tunapuna-Piarco");
        grocery2.setPhone("868-326-9324");
        insertData(db, grocery2);

        ShoppingList shoppingList = new ShoppingList("Monthly Grocery List", user.getId());
        insertData(db, shoppingList);

        //db = this.getWritableDatabase();
        Product product = new Product("Planters Unsalted Mixed Nuts");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        product.setShortDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient.");
        product.setLongDescription("CELEBRATE THE 2016 EUROPEAN TOURNAMENT IN ASPHALT 8! DEDICATED LIMITED-TIME EVENTS, EXCLUSIVE PROMOTIONS, AWESOME REWARDS... STAND UP & REPRESENT YOUR NATION!\n" +
                "200 MILLION PLAYERS CAN’T BE WRONG!\n" +
                "TAKE A SPIN WITH THE FRONTRUNNER AMONG MOBILE RACING GAMES!\n" +
                "\n" +
                "** The fully installed game requires at least 1.8 GB of free space in your internal storage. **\n" +
                "\n" +
                "ACCLAIMED BY PLAYERS & THE PRESS!\n" +
                "WINNER: **2015 MWC Best Mobile Game App**\n" +
                "WINNER: **Winner of 2014 Pocket Gamer Best Sports/Racing Game Award**\n" +
                "PERFECT SCORE: ** 5/5 – TouchArcade**\n" +
                "PERFECT SCORE: ** 5/5 – AppSpy**\n" +
                "PERFECT SCORE: ** 5/5 – Slide to Play**\n" +
                "PERFECT SCORE: ** 100/100 – GameReactor**\n" +
                "PERFECT SCORE: ** 5/5 – MacLife**\n" +
                "\n" +
                "LEAVE GRAVITY IN THE DUST!\n" +
                "• 140+ OFFICIAL SPEED MACHINES: Ferrari, Lamborghini, McLaren, Bugatti, Mercedes, Audi, Ford, Chevrolet… We got ‘em all! \n" +
                "• STUNNING GRAPHICS: Interactions between the vehicles, environments & tracks are a fully physics-based experience! \n" +
                "• ARCADE GAMEPLAY AT ITS FINEST: Feel the thrill of gravity-defying racing across 40+ high-speed tracks!\n" +
                "• THE ULTIMATE MULTIPLAYER RACING EXPERIENCE: Race in real-time multiplayer action for up to 12 opponents & dare your friends to ghost races!\n" +
                "• WIN BIG: Participate in our Limited-Time Events to stack up amazing & exclusive rewards! \n" +
                "• MASSIVE CONTENT DEPTH: 400+ career events, 1,500 car mastery challenges, 5 unique game modes, car collections. An endless stream of single-player content!\n" +
                "• CUSTOMIZE & UPGRADE YOUR RIDES: With over 2,300 decals, take down your opponents with style!\n" +
                "\n" +
                "JOIN OUR RACER COMMUNITY!\n" +
                "• FACEBOOK: facebook.com/AsphaltGame \n" +
                "• YOUTUBE: youtube.com/c/AsphaltGames\n" +
                "• TWITTER: twitter.com/Asphalt\n" +
                "• INSTAGRAM: instagram.com/asphalt8\n" +
                "\n" +
                "A game for fans of extreme arcade racing, with real dream cars and phenomenal graphics that will also please racing simulation enthusiasts.\n" +
                "\n" +
                "_____________________________________________\n" +
                "\n" +
                "Visit our official site at http://www.gameloft.com\n" +
                "Follow us on Twitter at http://glft.co/GameloftonTwitter or like us on Facebook at http://facebook.com/Gameloft to get more info about all our upcoming titles.\n" +
                "Check out our videos and game trailers on http://www.youtube.com/Gameloft \n" +
                "Discover our blog at http://glft.co/Gameloft_Official_Blog for the inside scoop on everything Gameloft.\n" +
                "\n" +
                "_____________________________________________\n" +
                "\n" +
                "This app allows you to purchase virtual items within the app.\n" +
                "\n" +
                "Privacy Policy: http://www.gameloft.com/privacy-notice/\n" +
                "Terms of Use: http://www.gameloft.com/conditions/\n" +
                "End-User License Agreement: http://www.gameloft.com/eula/?lang=en");
        insertData(db, product);
        GroceryStockItem groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 65.99, "tin", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 70.99, "tin", 1000);
        insertData(db, groceryStockItem);

        product.setName("Axe (Excite) Body Spray");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 25.99, "can", 1000);
        insertData(db, groceryStockItem);

        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 30.99, "can", 1000);
        insertData(db, groceryStockItem);

        ShoppingListItem  shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Panadol Hot Rem");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 19.99, "box", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 25.99, "can", 1000);
        insertData(db, groceryStockItem);


        product.setName("Gullon Sugar Free Vanilla Wafer");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),6, 15.99, "pack", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 20.99, "can", 1000);
        insertData(db, groceryStockItem);

        product.setName("Irish Spring Body Wash (Original)");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 26.00, "bottle", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 31.99, "can", 1000);
        insertData(db, groceryStockItem);
        shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Protox Insecticide Spray");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 16.00, "can", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 21.99, "can", 1000);
        insertData(db, groceryStockItem);

        product.setName("Airwick Airfreshener 4in1");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 23.00, "can", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 28.99, "can", 1000);
        insertData(db, groceryStockItem);
        shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Kiss Whole Grain Loaf ");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 13.00, "loaf", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 17.99, "can", 1000);
        insertData(db, groceryStockItem);
        shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Dole Pineapple Juice Unsweetened");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 9.00, "tin", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 14.99, "can", 1000);
        insertData(db, groceryStockItem);
        shoppingListItem = new ShoppingListItem(shoppingList.getId(), groceryStockItem.getId(), 1);
        insertData(db, shoppingListItem);

        product.setName("Kiss Cakes Orange Cupcakes");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 3.50, "pack", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 8.99, "can", 1000);
        insertData(db, groceryStockItem);

        product.setName("Bermudez Wheat Crisps");
        product.setThumbnailUri(Images.imageThumbUrls[random.nextInt(Images.imageThumbUrls.length)]);
        insertData(db, product);
        groceryStockItem = new GroceryStockItem(grocery.getId(), product.getId(),1, 3.00, "pack", 1000);
        insertData(db, groceryStockItem);
        groceryStockItem = new GroceryStockItem(grocery2.getId(), product.getId(),1, 8.99, "can", 1000);
        insertData(db, groceryStockItem);


        Product.createNameIndex(db);

        // Create a shopping list

        //Create a shopping list items
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

            Grocery.onCreate(db);
            User.onCreate(db);
            Product.onCreate(db);
            ShoppingList.onCreate(db);
            GroceryStockItem.onCreate(db);
            ShoppingListItem.onCreate(db);
            seedData(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Grocery.onUpgrade(db, oldVersion, newVersion);
        User.onUpgrade(db, oldVersion, newVersion);
        Product.onUpgrade(db, oldVersion, newVersion);
        ShoppingList.onUpgrade(db, oldVersion, newVersion);
        GroceryStockItem.onUpgrade(db, oldVersion, newVersion);
        ShoppingListItem.onUpgrade(db, oldVersion, newVersion);
        seedData(db);
    }

    public boolean insertData(SQLiteDatabase db, DataEntity entity){
        //SQLiteDatabase db = this.getWritableDatabase();
        entity.insert(db);
        return true;
    }


    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        //return result;
        return null;
    }

    public boolean  updateData(String Id, String name, String surname, String marks)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return true;
    }

    /**
     * Checks if there is registered user in database
     *
     * @return true if registered user exists and false otherwise
     */
    public boolean userExsits( )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user limit 1;", null);
        // make sure that potential listeners are getting notified
        if (cursor == null) {
            return false;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        }
        return cursor.getCount() > 0;
    }

    /**
     * Retrieves registered user from database
     * @return returns first registered user in database
     */
    public User getUser( )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user limit 1;", null);
        // make sure that potential listeners are getting notified
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }else if(cursor.getCount() > 0){
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setBudget(cursor.getFloat(3));
            return user;
        }
        return null;
    }


    /**
     * Retrieves user's shopping cart items
     * @return Shopping cart Cursor
     */
    public Cursor getShoppingList( )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select sli._id as _id, p.name as product_name, p.short_description as short_description,  p.long_description as long_description, p.thumbnail as product_thumbnail, gsi.price as price, sli.quantity as quantity, gsi.unit as unit, vendor.name as vendor_name,"
                + " vendor.phone as vendor_phone, vendor.location as vendor_location from " + ShoppingListItem.TABLE_SHOPPING_LIST_ITEM + " as sli "
                + " inner join " + GroceryStockItem.TABLE_GROCERY_STOCK_ITEM + " as gsi on sli." + ShoppingListItem.COLUMN_GROCERY_STOCK_ITEM_ID + "=gsi." + GroceryStockItem.COLUMN_ID
                + " inner join " + Product.TABLE_PRODUCT + " as p on gsi." + GroceryStockItem.COLUMN_PRODUCT_ID + "=p." + Product.COLUMN_ID
                + " inner join "+Grocery.TABLE_GROCERY+ " as vendor on gsi."+GroceryStockItem.COLUMN_GROCERY_ID + "=vendor."+Grocery.COLUMN_ID+";", null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }

    /**
     * Adds new user to database
     * @param name - user's name
     * @param email - user's email address
     * @param budget - user's budget
     *
     * @return returns true if insertion was successful and false otherwise
     */
    public boolean addUser(String name, String email, double budget )
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from user where email=?", new String[]{email});
        if (cursor != null && !cursor.moveToFirst() && cursor.getCount() > 0) {
            return false;
        }

        User user = new User(name, email, budget);
        insertData(db, user);
        /*ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("budget", budget);
        db.insert("user", null, contentValues);*/
        // make sure that potential listeners are getting notified
        return true;
    }


    /**
     * Removes user from database
     * @param id - Id of user to be removed
     * @return - Returns true if at least one record was removed
     */
    public boolean deleteUserById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int result = db.delete("user", "_id=?", new String[]{id + ""});
        return result > 0;

    }

    /**
     * Retrieves  user from database given a specific email address
     * @param email - email address of user to be retrieved
     * @return User model containing first matched record. Returns null if not record was found
     */
    public User getUserByEmail(String email) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email=?;", new String[]{email});
        // make sure that potential listeners are getting notified
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }else if(cursor.getCount() > 0){
            User user = new User();
            user.setId(cursor.getLong(0));
            user.setName(cursor.getString(1));
            user.setEmail(cursor.getString(2));
            user.setBudget(cursor.getFloat(3));
            return user;
        }
        return null;
    }
}

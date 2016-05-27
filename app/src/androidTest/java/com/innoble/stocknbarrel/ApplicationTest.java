package com.innoble.stocknbarrel;

import android.app.Application;
import android.database.Cursor;
import android.test.ApplicationTestCase;

import com.innoble.stocknbarrel.database.StockNBarrelDatabaseHelper;
import com.innoble.stocknbarrel.model.User;

import junit.framework.Assert;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    private Application application;
    private StockNBarrelDatabaseHelper provider;
    private User user;

    public ApplicationTest() {
        super(Application.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        application = getApplication();
        provider = new StockNBarrelDatabaseHelper(this.getContext());
        /*user = new User("Jessica Jones", "sheldonhhall@gmail.com", 500.0f);
        User userResult = provider.getUser();
        if(userResult != null)
            provider.deleteUserById(userResult.getId());*/
    }

    public void testCorrectVersion() throws Exception {
        StockNBarrelDatabaseHelper provider = new StockNBarrelDatabaseHelper(this.getContext());

        /*boolean userExists = provider.userExsits();
        Assert.assertFalse("User exists: ", userExists);
        boolean userAdded = provider.addUser(user.getName(), user.getEmail(), user.getBudget());
        Assert.assertTrue("User added: ", userAdded);
        userExists = provider.userExsits();
        Assert.assertTrue("User exists: ", userExists);
        User userResult = provider.getUser();
        Assert.assertEquals(user.getEmail(), userResult.getEmail());
        userResult = provider.getUserByEmail(user.getEmail());
        Assert.assertEquals(user.getEmail(), userResult.getEmail());*/


        Cursor cursor = provider.getShoppingList();
        Assert.assertNotNull(cursor);
        Assert.assertTrue(cursor.getCount() > 0);
        Assert.assertTrue(cursor.getCount() == 5);
    }

}
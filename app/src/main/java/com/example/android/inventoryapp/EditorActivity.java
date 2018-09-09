package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductDbHelper;
import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

import static com.example.android.inventoryapp.data.ProductDbHelper.LOG_TAG;

/**
 * Allows user to create a new product or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the product's name */
    private EditText mProductNameEditText;

    /** EditText field to enter the product's price*/
    private EditText mPriceEditText;

    /** EditText field to enter the product's quantity */
    private EditText mQuantityEditText;

    /** EditText field to enter the product's supplier name*/
    private EditText mSupplierNameEditText;

    /** EditText field to enter the product's, supplier's phone no. */
    private EditText mSupplierPhoneNoEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mProductNameEditText = findViewById(R.id.edit_product_name);
        mPriceEditText = findViewById(R.id.edit_price);
        mQuantityEditText = findViewById(R.id.edit_quantity);
        mSupplierNameEditText = findViewById(R.id.edit_supplier_name);
        mSupplierPhoneNoEditText = findViewById(R.id.edit_supplier_phone_number);
    }

    /**
     * Get user input from editor and save new product into database.
     */
    private void insertProduct() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String productNameString =  mProductNameEditText.getText().toString().trim();

        double price=0.0;
        //try-catch block to prevent app from crashing when no price input provided.
        try {
            price= Double.parseDouble(mPriceEditText.getText().toString().trim());
        } catch (NumberFormatException e){
            Log.e(LOG_TAG, "No input value in price", e);;
        }

        int quantity=0;
        //try-catch block to prevent app from crashing when no quantity input provided.
        try {
            quantity = Integer.parseInt(mQuantityEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "No input value in quantity", e);;
        }
        String supplierNameString =  mSupplierNameEditText.getText().toString().trim();
        String supplierPhoneNoString =  mSupplierPhoneNoEditText.getText().toString().trim();

        // Create database helper
        ProductDbHelper mDbHelper = new ProductDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and product attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, productNameString);
        values.put(ProductEntry.COLUMN_PRODUCT_PRICE, price);
        values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME, supplierNameString);
        values.put(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NO, supplierPhoneNoString);

        // Insert a new row for product in the database, returning the ID of that new row.
        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving product", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Product saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save product to database
                insertProduct();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

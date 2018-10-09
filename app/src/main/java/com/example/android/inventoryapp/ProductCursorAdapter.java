package com.example.android.inventoryapp;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract.ProductEntry;

import static android.media.CamcorderProfile.get;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView =  view.findViewById(R.id.name);
        TextView priceTextView =  view.findViewById(R.id.price);
        final TextView quantityTextView =  view.findViewById(R.id.quantity);
        Button saleButton=view.findViewById(R.id.sale_button);

        // Find the columns of pet attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);
        final int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
//        int nameSupplierColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_NAME);
//        int phoneColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_SUPPLIER_PHONE_NO);

        // Read the pet attributes from the Cursor for the current pet
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);
//        String productSupplierName = cursor.getString(nameSupplierColumnIndex);
//        String productSupplierPhone = cursor.getString(phoneColumnIndex);

//        // If the pet breed is empty string or null, then use some default text
//        // that says "Unknown breed", so the TextView isn't blank.
//        if (TextUtils.isEmpty(productBreed)) {
//            petBreed = context.getString(R.string.unknown_breed);
//        }

        // Update the TextViews with the attributes for the current pet
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);

        final int currentProductId = cursor.getInt(cursor.getColumnIndex(ProductEntry._ID));
        final Uri contentUri = Uri.withAppendedPath(ProductEntry.CONTENT_URI, String.valueOf(currentProductId));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.valueOf(quantityTextView.getText().toString());

                if (quantity > 0) {
                    quantity--;
                    // Content Values to update quantity
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, quantity);

                    // update the database
                    context.getContentResolver().update(contentUri, values, null, null);

                }else{
                    Toast.makeText(context, R.string.out_of_stock, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}

//https://stackoverflow.com/questions/11189545/android-android-content-res-resourcesnotfoundexception-string-resource-id-0x
//https://stackoverflow.com/questions/32988351/how-to-set-value-for-each-item-button-increase-and-decrease-click-and-set-that-v
//https://stackoverflow.com/questions/44034208/updating-listview-with-cursoradapter-after-an-onclick-changes-a-value-in-sqlite (P Fuster)
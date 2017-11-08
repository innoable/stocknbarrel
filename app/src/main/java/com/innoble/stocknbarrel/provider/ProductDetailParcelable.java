package com.innoble.stocknbarrel.provider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Data serializer for ProductDetailActivity
 *
 * @Author Sheldon Hall
 * Created by Sheldon on 22/06/2016.
 */
public class ProductDetailParcelable implements Parcelable {

    public static final Parcelable.Creator<ProductDetailParcelable> CREATOR =
            new Parcelable.Creator<ProductDetailParcelable>() {

                @Override
                public ProductDetailParcelable createFromParcel(Parcel source) {
                    return new ProductDetailParcelable(source);
                }

                @Override
                public ProductDetailParcelable[] newArray(int size) {
                    return new ProductDetailParcelable[size];
                }
            };
    public long itemCartID;
    public long productID;
    public String productName;
    public int qty;
    public String unit;
    public double price;
    public String shortDescription;
    public String longDescription;
    public long vendorID;
    public String vendorName;
    public long groceryStockItemId;
    public String vendorPhone;
    public String vendorLocation;
    public String vendorBranch;
    public String productThumbnail;


    public ProductDetailParcelable() {

    }


    public ProductDetailParcelable(Parcel in) {
        itemCartID = in.readLong();
        productID = in.readLong();
        productName = in.readString();
        qty = in.readInt();
        unit = in.readString();
        price = in.readDouble();
        shortDescription = in.readString();
        longDescription = in.readString();
        vendorID = in.readLong();
        vendorName = in.readString();
        groceryStockItemId = in.readLong();
        vendorPhone = in.readString();
        vendorLocation = in.readString();
        vendorBranch = in.readString();
        productThumbnail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(itemCartID);
        dest.writeLong(productID);
        dest.writeString(productName);
        dest.writeInt(qty);
        dest.writeString(unit);
        dest.writeDouble(price);
        dest.writeString(shortDescription);
        dest.writeString(longDescription);
        dest.writeLong(vendorID);
        dest.writeString(vendorName);
        dest.writeLong(groceryStockItemId);
        dest.writeString(vendorPhone);
        dest.writeString(vendorLocation);
        dest.writeString(vendorBranch);
        dest.writeString(productThumbnail);
    }
}

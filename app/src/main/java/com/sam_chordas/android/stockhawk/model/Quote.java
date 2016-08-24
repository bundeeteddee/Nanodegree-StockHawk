package com.sam_chordas.android.stockhawk.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by bundee on 8/22/16.
 */
public class Quote implements Parcelable{

    private int mIsUp;
    private String mBidPrice;
    private String mSymbol;
    private String mPercentChange;
    private String mChange;

    /**
     * Constructor given a cursor
     * @param cursor
     */
    public Quote(Cursor cursor){
        mSymbol = cursor.getString(cursor.getColumnIndex("symbol"));
        mBidPrice = cursor.getString(cursor.getColumnIndex("bid_price"));
        mIsUp = cursor.getInt(cursor.getColumnIndex("is_up"));
        mPercentChange = cursor.getString(cursor.getColumnIndex("percent_change"));
        mChange = cursor.getString(cursor.getColumnIndex("change"));
    }

    protected Quote(Parcel in) {
        mIsUp = in.readInt();
        mBidPrice = in.readString();
        mSymbol = in.readString();
        mPercentChange = in.readString();
        mChange = in.readString();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel in) {
            return new Quote(in);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };

    public String getBidPrice() {
        return mBidPrice;
    }

    public String getChange() {
        return mChange;
    }

    public boolean isUp() {
        return mIsUp == 1;
    }

    public String getPercentChange() {
        return mPercentChange;
    }

    public String getSymbol() {
        return mSymbol;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIsUp);
        dest.writeString(mBidPrice);
        dest.writeString(mSymbol);
        dest.writeString(mPercentChange);
        dest.writeString(mChange);
    }
}

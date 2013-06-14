package com.blackberry.howisundergroundtoday.objects;

import android.os.Parcel;
import android.os.Parcelable;
import org.w3c.dom.Node;

/**
 * Created by Hooman on 14/06/13.
 */
public abstract class ParserObject implements Parcelable {

    protected ParserObject(Parcel in) {};
    public abstract ParserObject parse(Node doc);

    @Override
    public  abstract void writeToParcel(Parcel parcel, int i);

    @Override
    public int describeContents() {
        return 0;
    }
}

package com.blackberry.howisundergroundtoday.objects;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.blackberry.howisundergroundtoday.R;
import com.blackberry.howisundergroundtoday.tools.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StatusObject extends ParserObject {

    private final static String CSSCLASS_ATTR = "CssClass";
    private final static String DESCRIPTION_ATTR = "Description";
    private final static String ID_ATTR = "ID";
    private final static String ISACTIVE_ATTR = "IsActive";
    private final static String GOODSERVICE_ID = "GS";
    private final static String PLANNEDCLOSURE_ID = "PC";
    private final static String PARTSUSPENDED_ID = "PS";
    private final static String MINORDELAYS_ID = "MD";
    private final static String SEVEREDELAYS_ID = "SD";
    private String statusCssClass;
    private String statusDescription;
    private String statusId;
    private boolean statusIsActive;
    private int statusResoureImage;

    public static final Parcelable.Creator<StatusObject> CREATOR
            = new Parcelable.Creator<StatusObject>() {
        public StatusObject createFromParcel(Parcel in) {
            return new StatusObject(in);
        }

        public StatusObject[] newArray(int size) {
            return new StatusObject[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle b = new Bundle();
        b.putString(CSSCLASS_ATTR, this.statusCssClass);
        b.putString(DESCRIPTION_ATTR, this.statusDescription);
        b.putString(ID_ATTR, this.statusId);
        b.putBoolean(ISACTIVE_ATTR, this.statusIsActive);
        parcel.writeBundle(b);
    }

    /**
     * Constructor with a parcel as its argument
     *
     * @param in Parcel arguement
     */
    private StatusObject(Parcel in) {
        super(in);
        Bundle b = in.readBundle();
        this.statusDescription = b.getString(DESCRIPTION_ATTR, "Good Service");
        this.setStatusId(b.getString(ID_ATTR, GOODSERVICE_ID));
        this.statusCssClass = b.getString(CSSCLASS_ATTR, "GoodService");
        this.statusIsActive = b.getBoolean(ISACTIVE_ATTR, true);
    }

    /**
     * Empty constructor
     */
    public StatusObject() {
        super(null);
        this.statusCssClass = "GoodService";
        this.statusDescription = "Good Service";
        this.statusId = "GS";
        this.statusIsActive = true;
        this.statusResoureImage = R.drawable.normalface;
    }


    /**
     * Return the resource ID for the image of the status
     *
     * @return integer representing the line image
     */
    public int getStatusResoureImage() {
        return this.statusResoureImage;
    }

    /**
     * @param lineResoureImage
     */
    public void setStatusResoureImage(int statusResoureImage) {
        this.statusResoureImage = statusResoureImage;
    }

    /**
     * @return the statusCssClass
     */
    public String getStatusCssClass() {
        return statusCssClass;
    }

    /**
     * @param statusCssClass the statusCssClass to set
     */
    public void setStatusCssClass(String statusCssClass) {
        this.statusCssClass = statusCssClass;
    }

    /**
     * @return the statusDescription
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    /**
     * @param statusDescription the statusDescription to set
     */
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    /**
     * @return the statusId
     */
    public String getStatusId() {
        return statusId;
    }

    /**
     * @param statusId the statusId to set
     */
    public void setStatusId(String statusId) {
        this.statusId = statusId;
        if (this.statusId.equalsIgnoreCase(GOODSERVICE_ID)) {
            this.setStatusResoureImage(R.drawable.smileyface);
        } else if (this.statusId.equalsIgnoreCase(MINORDELAYS_ID)) {
            this.setStatusResoureImage(R.drawable.sadface);
        } else {
            this.setStatusResoureImage(R.drawable.sadface2);
        }
    }

    /**
     * @return the statusIsActive
     */
    public boolean isStatusIsActive() {
        return statusIsActive;
    }

    /**
     * @param statusIsActive the statusIsActive to set
     */
    public void setStatusIsActive(boolean statusIsActive) {
        this.statusIsActive = statusIsActive;
    }

    /**
     * Parses the Status object from the XML node
     */
    @Override
    public ParserObject parse(Node doc) {
        if (doc == null) {
            return this;
        }
        Element statusElement = (Element) doc;
        Logger.i(StatusObject.class, statusElement.getNodeName());
        String isActiveString = statusElement.getAttribute(ISACTIVE_ATTR);
        this.setStatusId(statusElement.getAttribute(ID_ATTR));
        this.statusCssClass = statusElement.getAttribute(CSSCLASS_ATTR);
        this.statusDescription = statusElement.getAttribute(DESCRIPTION_ATTR);
        this.statusIsActive = true;
        if (isActiveString != null && isActiveString.equals("false")) {
            this.statusIsActive = false;
        }
        return this;
    }

    public String getTranslatedStatusDescription(Context context) {
        if (this.statusId.equalsIgnoreCase("GS")) {
            return context.getString(R.string.good_service_string);
        } else if (this.statusId.equalsIgnoreCase("MD")) {
            return context.getString(R.string.minor_delays_string);
        } else if (this.statusId.equalsIgnoreCase("SD")) {
            return context.getString(R.string.severe_delays_string);
        } else if (this.statusId.equalsIgnoreCase("PC")) {
            return context.getString(R.string.part_closure_string);
        } else {
            return this.statusDescription;
        }
    }
}

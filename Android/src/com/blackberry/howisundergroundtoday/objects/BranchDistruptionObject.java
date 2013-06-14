package com.blackberry.howisundergroundtoday.objects;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.blackberry.howisundergroundtoday.tools.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BranchDistruptionObject extends ParserObject {
	private final static String ID_ATTR = "ID";
	private final static String NAME_ATTR = "Name";
	private final static String STATIONFROM_NODE = "StationFrom";
	private final static String STATIONTO_NODE = "StationTo";
	public final static String XML_TAG_NAME = "BranchDisruption";
	public final static String FROM_ID_KEY = "From_ID";
	public final static String TO_ID_KEY = "To_ID";

	private int bdStationFromId;
	private String bdStationFromName;
	private int bdStationToId;
	private String bdStationToName;

    public static final Parcelable.Creator<BranchDistruptionObject> CREATOR
            = new Parcelable.Creator<BranchDistruptionObject>() {
        public BranchDistruptionObject createFromParcel(Parcel in) {
            return new BranchDistruptionObject(in);
        }

        public BranchDistruptionObject[] newArray(int size) {
            return new BranchDistruptionObject[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle b = new Bundle();
        b.putInt(FROM_ID_KEY, this.bdStationFromId);
        b.putInt(TO_ID_KEY, this.bdStationToId);
        b.putString(STATIONFROM_NODE,this.bdStationFromName);
        b.putString(STATIONTO_NODE,this.bdStationToName);
    }

    /**
     * Constructor with parcel as argument
     * @param in
     */
    private BranchDistruptionObject(Parcel in) {
        super(in);
        Bundle b = in.readBundle();
        this.bdStationFromId = b.getInt(FROM_ID_KEY, -1);
        this.bdStationToId = b.getInt(TO_ID_KEY, -1);
        this.bdStationFromName = b.getString(STATIONFROM_NODE, "");
        this.bdStationToName = b.getString(STATIONTO_NODE, "");
    }

    /**
     * Empty constructor
     */
    public BranchDistruptionObject() {
        super(null);
        this.bdStationFromId = -1;
        this.bdStationToId = -1;
        this.bdStationFromName = "";
        this.bdStationToName = "";
    }

    /**
	 * @return the bdStationFromId
	 */
	public int getBdStationFromId() {
		return bdStationFromId;
	}

	/**
	 * @return the bdStationFromName
	 */
	public String getBdStationFromName() {
		return bdStationFromName;
	}

	/**
	 * @return the bdStationToId
	 */
	public int getBdStationToId() {
		return bdStationToId;
	}

	/**
	 * @return the bdStationToName
	 */
	public String getBdStationToName() {
		return bdStationToName;
	}

	@Override
	public ParserObject parse(Node doc) {
		if (doc == null) {
			return this;
		}
		Logger.i(BranchDistruptionObject.class,doc.getNodeName());
		Element bdElement = (Element) doc;
		Element tempElement = (Element) bdElement.getElementsByTagName(STATIONFROM_NODE).item(0);
		this.bdStationFromId = Integer.parseInt(tempElement.getAttribute(ID_ATTR));
		this.bdStationFromName = tempElement.getAttribute(NAME_ATTR);
		tempElement = (Element) bdElement.getElementsByTagName(STATIONTO_NODE).item(0);
		this.bdStationToId = Integer.parseInt(tempElement.getAttribute(ID_ATTR));
		this.bdStationToName = tempElement.getAttribute(NAME_ATTR);
		return this;
	}

	/**
	 * @param bdStationFromId the bdStationFromId to set
	 */
	public void setBdStationFromId(int bdStationFromId) {
		this.bdStationFromId = bdStationFromId;
	}

	/**
	 * @param bdStationFromName the bdStationFromName to set
	 */
	public void setBdStationFromName(String bdStationFromName) {
		this.bdStationFromName = bdStationFromName;
	}

	/**
	 * @param bdStationToId the bdStationToId to set
	 */
	public void setBdStationToId(int bdStationToId) {
		this.bdStationToId = bdStationToId;
	}

	/**
	 * @param bdStationToName the bdStationToName to set
	 */
	public void setBdStationToName(String bdStationToName) {
		this.bdStationToName = bdStationToName;
	}

}

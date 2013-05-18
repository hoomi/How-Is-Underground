package com.blackberry.howisundergroundtoday.objects;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.blackberry.howisundergroundtoday.tools.ParserInterface;

public class BranchDistruptionObject implements ParserInterface {
	private final static String ID_ATTR = "ID";
	private final static String NAME_ATTR = "Name";
	private final static String STATIONFROM_NODE = "StationFrom";
	private final static String STATIONTO_NODE = "StationTo";

	private int bdStationFromId;
	private String bdStationFromName;
	private int bdStationToId;
	private String bdStationToName;
	
	public BranchDistruptionObject() {
		super();
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
	public ParserInterface parse(Node doc) {
		if (doc == null) {
			return this;
		}
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

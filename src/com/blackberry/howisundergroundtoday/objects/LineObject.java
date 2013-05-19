package com.blackberry.howisundergroundtoday.objects;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.blackberry.howisundergroundtoday.tools.Logger;
import com.blackberry.howisundergroundtoday.tools.ParserInterface;

public class LineObject implements ParserInterface {

	private final static String ID_ATTR = "ID";
	private final static String NAME_ATTR = "Name";
	private final static String STATUSDETAILS_ATTR = "StatusDetails";
	public final static String XML_TAG_NAME = "LineStatus";
	private ArrayList<BranchDistruptionObject> lineBranchDistruptions = null;
	private int lineColor;
	private int lineId;
	private String lineName;
	private int lineResoureImage;
	private boolean lineShowingStatus;
	private StatusObject lineStatus;
	private String lineStatusDetails;
	private int lineStatusId;

	public LineObject() {
		super();
		this.lineName = "Unknown";
		this.lineShowingStatus = false;
		this.lineBranchDistruptions = new ArrayList<BranchDistruptionObject>();
		this.lineStatus = new StatusObject();
		this.lineStatusDetails = "";
		this.lineId = -1;
		this.lineStatusId = -1;
	}

	/**
	 * Constructor
	 * 
	 * @param lineName
	 * @param lineStatus
	 * @param lineResoureImage
	 */
	public LineObject(String lineName, int lineResoureImage) {
		super();
		this.lineName = lineName;
		this.lineResoureImage = lineResoureImage;
		this.lineShowingStatus = false;
		this.lineBranchDistruptions = new ArrayList<BranchDistruptionObject>();
		this.lineStatus = new StatusObject();
	}

	/**
	 * 
	 * @return Color of the line
	 */
	public int getLineColor() {
		return lineColor;
	}

	/**
	 * To get the Id of line
	 * 
	 * @return Return the id of the line
	 */
	public int getLineId() {
		return lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public int getLineResoureImage() {
		return lineResoureImage;
	}

	/**
	 * @return the lineStatus
	 */
	public StatusObject getLineStatus() {
		return lineStatus;
	}

	/**
	 * 
	 * @return Status of the line
	 */
	public String getLineStatusDetails() {
		return lineStatusDetails;
	}

	/**
	 * @return the lineStatusId
	 */
	public int getLineStatusId() {
		return lineStatusId;
	}

	/**
	 * @return lineShowingStatus
	 */
	public boolean isLineShowingStatus() {
		return lineShowingStatus;
	}

	@Override
	public ParserInterface parse(Node doc) {
		Element lineElement = (Element) doc;
		if (doc == null) {
			return this;
		}
		Logger.i(LineObject.class, lineElement.getNodeName());
		this.lineStatusId = Integer.parseInt(lineElement.getAttribute(ID_ATTR));
		this.lineStatusDetails = lineElement.getAttribute(STATUSDETAILS_ATTR);
		Node node = lineElement.getElementsByTagName("BranchDisruptions").item(0);
		lineBranchDistruptions.clear();
		Logger.i(LineObject.class, node.getNodeName());
		if (node != null && node.hasChildNodes()) {
			node = node.getFirstChild();
			do {
				Logger.i(LineObject.class, node.getNodeName());
				if (node.getNodeName().equalsIgnoreCase(BranchDistruptionObject.XML_TAG_NAME)) {
					this.lineBranchDistruptions.add((BranchDistruptionObject) new BranchDistruptionObject().parse(node));
				}
				node = node.getNextSibling();
			} while (node != null);
		}
		Element elm = (Element) lineElement.getElementsByTagName("Line").item(0);
		this.lineName = elm.getAttribute(NAME_ATTR);
		this.lineId = Integer.parseInt(elm.getAttribute(ID_ATTR));
		node = lineElement.getElementsByTagName("Status").item(0);
		Logger.i(LineObject.class, node.getNodeName());
		this.lineStatus.parse(node);
		return this;
	}

	/**
	 * 
	 * @param lineColor
	 */
	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * Sets the id of the line
	 * 
	 * @param lineID
	 */
	public void setLineId(int lineID) {
		this.lineId = lineID;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	/**
	 * 
	 * @param lineResoureImage
	 */
	public void setLineResoureImage(int lineResoureImage) {
		this.lineResoureImage = lineResoureImage;
	}

	/**
	 * @param lineShowingStatus
	 */
	public void setLineShowingStatus(boolean lineShowingStatus) {
		this.lineShowingStatus = lineShowingStatus;
	}

	/**
	 * @param lineStatus
	 *            the lineStatus to set
	 */
	public void setLineStatus(StatusObject lineStatus) {
		this.lineStatus = lineStatus;
	}

	/**
	 * 
	 * @param lineStatusDetails
	 */
	public void setLineStatusDetails(String lineStatusDetails) {
		this.lineStatusDetails = lineStatusDetails;
	}

	/**
	 * @param lineStatusId
	 *            the lineStatusId to set
	 */
	public void setLineStatusId(int lineStatusId) {
		this.lineStatusId = lineStatusId;
	}
}

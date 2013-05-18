package com.blackberry.howisundergroundtoday.objects;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.blackberry.howisundergroundtoday.tools.ParserInterface;

public class LineObject implements ParserInterface {

	private final static String ID_ATTR = "ID";
	private final static String NAME_ATTR = "Name";
	private final static String STATUSDETAILS_ATTR = "StatusDetails";
	public final static String XML_TAG_NAME = "LineStatus";
	private int lineColor;
	private int lineId;
	private String lineName;
	private int lineResoureImage;
	private boolean lineShowingStatus;
	private StatusObject lineStatus;
	private String lineStatusDetails;
	private int lineStatusId;
	private ArrayList<BranchDistruptionObject> lineBranchDistruptions = null;

	public LineObject() {
		super();
		this.lineName = "Unknown";
		this.lineShowingStatus = false;
		this.lineBranchDistruptions = new ArrayList<BranchDistruptionObject>();
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
		this.lineStatusId = Integer.parseInt(lineElement.getAttribute(ID_ATTR));
		this.lineStatusDetails = lineElement.getAttribute(STATUSDETAILS_ATTR);
		Node node = lineElement.getElementsByTagName("BranchDisruptions").item(0);
		lineBranchDistruptions.clear();
		do {
			if (node == null || !node.hasChildNodes()) {
				break;
			}
			node = node.getFirstChild();
			this.lineBranchDistruptions.add((BranchDistruptionObject) new BranchDistruptionObject().parse(node));
			node = node.getNextSibling();
		} while (node != null);
		node = lineElement.getElementsByTagName("Line").item(0);
		this.lineName = lineElement.getAttribute(NAME_ATTR);
		this.lineId = Integer.parseInt(lineElement.getAttribute(ID_ATTR));
		node = lineElement.getElementsByTagName("Status").item(0);
		this.lineStatus = (StatusObject) new StatusObject().parse(node);
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

package com.blackberry.howisundergroundtoday;

public class LineObject {

	private String lineName;
	private String lineStatus;
	private int lineResoureImage;
	private String lineStatusDetails;
	private int lineColor;
	private int lineID;
	private boolean lineShowingStatus;

	/**
	 * @return lineShowingStatus
	 */
	public boolean isLineShowingStatus() {
		return lineShowingStatus;
	}

	/**
	 * @param lineShowingStatus
	 */
	public void setLineShowingStatus(boolean lineShowingStatus) {
		this.lineShowingStatus = lineShowingStatus;
	}

	/**
	 * To get the ID of line
	 * @return Return the id of the line
	 */
	public int getLineID() {
		return lineID;
	}

	/**
	 * Sets the id of the line
	 * @param lineID
	 */
	public void setLineID(int lineID) {
		this.lineID = lineID;
	}

	/**
	 * 
	 * @return Color of the line
	 */
	public int getLineColor() {
		return lineColor;
	}

	/**
	 * 
	 * @param lineColor
	 */
	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	/**
	 * 
	 * @return Status of the line
	 */
	public String getLineStatusDetails() {
		return lineStatusDetails;
	}

	/**
	 * 
	 * @param lineStatusDetails
	 */
	public void setLineStatusDetails(String lineStatusDetails) {
		this.lineStatusDetails = lineStatusDetails;
	}

	/**
	 * Constructor
	 * @param lineName
	 * @param lineStatus
	 * @param lineResoureImage
	 */
	public LineObject(String lineName, String lineStatus, int lineResoureImage) {
		super();
		this.lineName = lineName;
		this.lineStatus = lineStatus;
		this.lineResoureImage = lineResoureImage;
		this.lineShowingStatus = false;
	}
	
	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLineStatus() {
		return lineStatus;
	}

	public void setLineStatus(String lineStatus) {
		this.lineStatus = lineStatus;
	}

	public int getLineResoureImage() {
		return lineResoureImage;
	}

	/**
	 * 
	 * @param lineResoureImage
	 */
	public void setLineResoureImage(int lineResoureImage) {
		this.lineResoureImage = lineResoureImage;
	}
}

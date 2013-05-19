package com.blackberry.howisundergroundtoday.objects;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.blackberry.howisundergroundtoday.tools.Logger;
import com.blackberry.howisundergroundtoday.tools.ParserInterface;

public class StatusObject implements ParserInterface {
	
	private final static String CSSCLASS_ATTR = "CssClass";
	private final static String DESCRIPTION_ATTR = "Description";
	private final static String ID_ATTR = "ID";
	private final static String ISACTIVE_ATTR = "IsActive";
	
	private String statusCssClass;
	private String statusDescription;
	private String statusId;
	private boolean statusIsActive;
	
	public StatusObject() {
		super();
		this.statusCssClass = "GoodService";
		this.statusDescription = "Good Service";
		this.statusId = "GS";
		this.statusIsActive = true;
	}


	/**
	 * @return the statusCssClass
	 */
	public String getStatusCssClass() {
		return statusCssClass;
	}


	/**
	 * @return the statusDescription
	 */
	public String getStatusDescription() {
		return statusDescription;
	}


	/**
	 * @return the statusId
	 */
	public String getStatusId() {
		return statusId;
	}


	/**
	 * @return the statusIsActive
	 */
	public boolean isStatusIsActive() {
		return statusIsActive;
	}

	/**
	 * Parses the Status object from the XML node
	 */
	@Override
	public ParserInterface parse(Node doc) {
		if(doc == null) {
			return this;
		}
		Element statusElement = (Element) doc;
		Logger.i(StatusObject.class, statusElement.getNodeName());
		String isActiveString = statusElement.getAttribute(ISACTIVE_ATTR);
		this.statusId = statusElement.getAttribute(ID_ATTR);
		this.statusCssClass = statusElement.getAttribute(CSSCLASS_ATTR);
		this.statusDescription = statusElement.getAttribute(DESCRIPTION_ATTR);
		this.statusIsActive = true;
		if (isActiveString != null && isActiveString.equals("false")) {
			this.statusIsActive = false;
		}
		return this;
	}


	/**
	 * @param statusCssClass the statusCssClass to set
	 */
	public void setStatusCssClass(String statusCssClass) {
		this.statusCssClass = statusCssClass;
	}


	/**
	 * @param statusDescription the statusDescription to set
	 */
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}


	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}


	/**
	 * @param statusIsActive the statusIsActive to set
	 */
	public void setStatusIsActive(boolean statusIsActive) {
		this.statusIsActive = statusIsActive;
	}

}

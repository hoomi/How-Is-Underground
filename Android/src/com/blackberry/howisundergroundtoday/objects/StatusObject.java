package com.blackberry.howisundergroundtoday.objects;

import com.blackberry.howisundergroundtoday.R;
import com.blackberry.howisundergroundtoday.tools.Logger;
import com.blackberry.howisundergroundtoday.tools.ParserInterface;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StatusObject implements ParserInterface {

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

    public StatusObject() {
        super();
        this.statusCssClass = "GoodService";
        this.statusDescription = "Good Service";
        this.statusId = "GS";
        this.statusIsActive = true;
        this.statusResoureImage = R.drawable.normalface;
    }


    /**
     * Return the resource ID for the image of the status
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
    public ParserInterface parse(Node doc) {
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

}

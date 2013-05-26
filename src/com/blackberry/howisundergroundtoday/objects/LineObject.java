package com.blackberry.howisundergroundtoday.objects;

import com.blackberry.howisundergroundtoday.R;
import com.blackberry.howisundergroundtoday.tools.Logger;
import com.blackberry.howisundergroundtoday.tools.ParserInterface;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;

public class LineObject implements ParserInterface {

    public final static String XML_TAG_NAME = "LineStatus";
    private final static String ID_ATTR = "ID";
    private final static String NAME_ATTR = "Name";
    private final static String STATUSDETAILS_ATTR = "StatusDetails";
    private ArrayList<BranchDistruptionObject> lineBranchDistruptions = null;
    private int lineColor;
    private int lineId;
    private String lineName;
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
    public LineObject(String lineName) {
        super();
        this.lineName = lineName;
        this.lineShowingStatus = false;
        this.lineBranchDistruptions = new ArrayList<BranchDistruptionObject>();
        this.lineStatus = new StatusObject();
    }

    /**
     * @return Color of the line
     */
    public int getLineColor() {
        return lineColor;
    }

    /**
     * @param lineColor
     */
    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * To get the Id of line
     *
     * @return Return the id of the line
     */
    public int getLineId() {
        return lineId;
    }

    /**
     * Sets the id of the line and its color
     *
     * @param lineID
     */
    public void setLineId(int lineID) {
        this.lineId = lineID;
        switch (lineID) {
            case 1:
                this.lineColor = R.color.bak_color;
                break;
            case 2:
                this.lineColor = R.color.cen_color;
                break;
            case 7:
                this.lineColor = R.color.cir_color;
                break;
            case 8:
                this.lineColor = R.color.ham_color;
                break;
            case 4:
                this.lineColor = R.color.jub_color;
                break;
            case 11:
                this.lineColor = R.color.met_color;
                break;
            case 5:
                this.lineColor = R.color.nor_color;
                break;
            case 6:
                this.lineColor = R.color.pic_color;
                break;
            case 3:
                this.lineColor = R.color.vic_color;
                break;
            case 12:
                this.lineColor = R.color.wat_color;
                break;
            case 82:
                this.lineColor = R.color.ove_color;
                break;
            case 81:
                this.lineColor = R.color.dlr_color;
                break;
            case 9:
                this.lineColor = R.color.dis_color;
                break;
            default:
                this.lineColor = R.color.dis_color;
                break;
        }
    }

    /**
     * @return Name of the line
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * Set the line name
     *
     * @param lineName It is the name of the line
     */
    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    /**
     * @return the lineStatus
     */
    public StatusObject getLineStatus() {
        return lineStatus;
    }

    /**
     * @param lineStatus the lineStatus to set
     */
    public void setLineStatus(StatusObject lineStatus) {
        this.lineStatus = lineStatus;
    }

    /**
     * @return Status of the line
     */
    public String getLineStatusDetails() {
        return lineStatusDetails;
    }

    /**
     * @param lineStatusDetails
     */
    public void setLineStatusDetails(String lineStatusDetails) {
        this.lineStatusDetails = lineStatusDetails;
    }

    /**
     * @return the lineStatusId
     */
    public int getLineStatusId() {
        return lineStatusId;
    }

    /**
     * @param lineStatusId the lineStatusId to set
     */
    public void setLineStatusId(int lineStatusId) {
        this.lineStatusId = lineStatusId;
    }

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
        this.setLineName(elm.getAttribute(NAME_ATTR));
        this.setLineId(Integer.parseInt(elm.getAttribute(ID_ATTR)));
        node = lineElement.getElementsByTagName("Status").item(0);
        this.lineStatus.parse(node);

        return this;
    }
}

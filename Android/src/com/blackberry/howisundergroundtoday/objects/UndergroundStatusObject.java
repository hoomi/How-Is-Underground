package com.blackberry.howisundergroundtoday.objects;

import com.blackberry.howisundergroundtoday.tools.Logger;
import com.blackberry.howisundergroundtoday.tools.ParserInterface;
import org.w3c.dom.Node;

import java.util.ArrayList;

public class UndergroundStatusObject implements ParserInterface {
	private final ArrayList<LineObject> linesArray;
	private static UndergroundStatusObject singletonUndergroundObject = null;
	
	/**
	 * Private constructor for UndergroundStatusObject
	 */
	private UndergroundStatusObject() {
		super();
		this.linesArray = new ArrayList<LineObject>();
	}
	
	/**
	 * It returns the single instance of UndergroundStatusObject. It should be thread safe
	 * @return
	 */
	public static UndergroundStatusObject getInstance() {
		if (singletonUndergroundObject == null) {
			synchronized(UndergroundStatusObject.class) { 
				singletonUndergroundObject = new UndergroundStatusObject();
			}
		}
		return singletonUndergroundObject;
	}

	/**
	 * @return the linesArray
	 */
	public ArrayList<LineObject> getLinesArray() {
		return linesArray;
	}
	
	/**
	 * Checks if the line objects have been added to the list and is not empty
	 * @return True if the list is empty and false otherwise
	 */
	public boolean isLinesArrayEmpty() {
		if (this.linesArray.size() <= 0) {
			return true;
		}
		return false;
	}
	/**
	 * It parses UndergroundStatusObject from the xml node
	 */
	@Override
	public ParserInterface parse(Node doc) {
		if (doc == null) {
			return this;
		}
		Node parentNode = doc.getFirstChild();
		if (parentNode == null) {
			return this;
		}
		Logger.i(UndergroundStatusObject.class, parentNode.getNodeName());
		this.linesArray.clear();
		Node node = parentNode.getFirstChild();
		do {
			if (node == null) {
				break;
			}
			Logger.i(UndergroundStatusObject.class, node.getNodeName());
			if (node.getNodeName().equalsIgnoreCase(LineObject.XML_TAG_NAME)) {
				this.linesArray.add((LineObject) (new LineObject().parse(node)));
			}
			node = node.getNextSibling();
		}while(node != null);
		return this;
	}

}

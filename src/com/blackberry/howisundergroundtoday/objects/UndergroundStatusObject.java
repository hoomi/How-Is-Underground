package com.blackberry.howisundergroundtoday.objects;

import java.util.ArrayList;

import org.w3c.dom.Node;

import com.blackberry.howisundergroundtoday.tools.ParserInterface;

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
	 * It parses UndergroundStatusObject from the xml node
	 */
	@Override
	public ParserInterface parse(Node doc) {
		if (doc == null) {
			return this;
		}
		
		Node parentNode = doc.getParentNode();
		if (parentNode == null) {
			return this;
		}
		this.linesArray.clear();
		Node node = parentNode.getFirstChild();
		do {
			if (node == null) {
				break;
			}
			if (node.getNodeName().equalsIgnoreCase(LineObject.XML_TAG_NAME)) {
				this.linesArray.add((LineObject) (new LineObject().parse(node)));
			}
			node = node.getNextSibling();
		}while(node != null);
		return this;
	}

}

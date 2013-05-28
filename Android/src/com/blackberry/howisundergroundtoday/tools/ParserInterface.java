package com.blackberry.howisundergroundtoday.tools;

import org.w3c.dom.Node;

/**
 * Created by Hooman on 17/05/13.
 */
public interface ParserInterface {
	/**
	 * It returns an object from the given doc object
	 * @param doc
	 * @return
	 */
	public ParserInterface parse(Node doc);
}

package com.blackberry.howisundergroundtoday;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.app.Application;

public class UndergroundApplication extends Application {

	private Document doc;

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	private ArrayList<LineObject> lines;

	public ArrayList<LineObject> getLines() {
		return lines;
	}

	public void setLines(ArrayList<LineObject> lines) {
		this.lines = lines;
	}

}

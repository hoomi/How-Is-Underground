package com.blackberry.howisundergroundtoday.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;

/**
 * Created by Hooman on 17/05/13.
 */

/**
 * This class handles the download and parsing of the xml
 */
public class XMLHelper {
	private String url;
	private String cacheFilePath;
	private String cacheDirectory;
	private boolean cacheDownload;
	private ParserInterface parserObject;
	private final Context context;

	/**
	 * Constructor for the XMLHelper
	 * 
	 * @param url
	 *            It is the url for where to get the xml document from
	 * @param cacheDownload
	 *            It is a flag to indicate whether the downloaded xml should be
	 *            cached after it was downloaded
	 */
	public XMLHelper(Context context,String url, boolean cacheDownload) {
		this.url = url;
		this.context = context;
		this.cacheDownload = cacheDownload;
		this.cacheFilePath = context.getCacheDir() + "/cache.xml";
	}

	/**
	 * XMLHelper constructor
	 * 
	 * @param url
	 *            It is the url for where to get the xml from
	 */
	public XMLHelper(Context context, String url) {
		this.url = url;
		this.context = context;
		this.cacheDownload = false;
		this.cacheFilePath = context.getCacheDir() + "/cache.xml";
		this.cacheFilePath = this.cacheDirectory + "cache.xml";
	}

	/**
	 * XMLHelper constructor
	 * 
	 * @param url
	 *            It is the url for where to get the xml document from
	 * @param cacheFilePath
	 *            The path to where store the cached document
	 * @param cacheDownload
	 *            It is a flag to indicate whether the downloaded xml should be
	 *            cached after it was downloaded
	 */
	public XMLHelper(Context context, String url, String fileName, boolean cacheDownload) {
		this.url = url;
		this.context = context;
		this.cacheFilePath = this.context.getCacheDir() + "/" + fileName;
		this.cacheDownload = cacheDownload;
	}

	/**
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Sets whether the document should be cached after it was downloaded
	 * 
	 * @param cacheDownload
	 */
	public void setCacheDownload(boolean cacheDownload) {
		this.cacheDownload = cacheDownload;
	}

	/**
	 * Set the path to where the cache should be saved
	 * 
	 * @param cacheFilePath
	 *            Tha path to the cache file
	 */
	public void setCacheFilePath(String cacheFilePath) {
		this.cacheFilePath = cacheFilePath;
	}

	/**
	 * Show whether the helper cached the xml after it was downloaded
	 * 
	 * @return true if it is caching and false otherwise
	 */
	public boolean isCaching() {
		return cacheDownload;
	}

	/**
	 * Returns the parser object
	 * 
	 * @return parserObject
	 */
	public ParserInterface getParserObject() {
		return parserObject;
	}

	/**
	 * Sets the object that will parse the xml document
	 * 
	 * @param parserObject
	 *            It is the parser object
	 */
	public void setParserObject(ParserInterface parserObject) {
		this.parserObject = parserObject;
	}

	/**
	 * It shows whether there is a cache already available
	 * 
	 * @return It returns true if there is a cache otherwise it returns false
	 */
	public boolean isThereACache() {
		return FileHelper.doesTheFileExist(this.cacheFilePath);
	}
	
	/**
	 * It is used to get raw XML document from the cache
	 * @return  It returns the cached Document. It will return null if it does not exist
	 */
	public Document getCachedDocument() {
		Document returnDoc = null;
		URI uri;
		if (!isThereACache()) {
			return returnDoc;
		}
		try {
			uri = new URI("file://" + this.cacheFilePath);
			returnDoc = this.getXMLDocument(uri);
		} catch (URISyntaxException e) {
			Logger.printStackTrace(e);
		} catch (MalformedURLException e) {
			Logger.printStackTrace(e);
		} catch (ParserConfigurationException e) {
			Logger.printStackTrace(e);
		} catch (IOException e) {
			Logger.printStackTrace(e);
		}
		return returnDoc;
	}
	
	/**
	 * It is used to get the parsed object.
	 * @return  It returns the cached Document. It will return null if it does not exist
	 * @throws Exception  If there is no parser object set
	 */
	public ParserInterface getCachedXMLObject() throws Exception {
		if (this.parserObject == null) {
			throw new Exception("There is no parser object");
		}
		return this.parserObject.parse(getCachedDocument());
	}

	/**
	 * It starts downloading the XML online. It caches the XML if the flag is
	 * set to true
	 * @return Parsed object
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public ParserInterface startDownloading() throws URISyntaxException,
			IOException, ParserConfigurationException {
		InputStream is;
		Document doc = null;
		URI uri = new URI(this.url);
		if (this.cacheDownload) {
			URL url = new URL(this.url);
			is = new InputSource(url.openStream()).getByteStream();
			// First cache the xml in a file and then continue from the file
			FileHelper.saveStream(is, this.cacheFilePath, true);
			uri = new URI("file://" + this.cacheFilePath);
		}
		doc = this.getXMLDocument(uri);
		if (this.parserObject != null) {
			return this.parserObject.parse(doc);
		}
		return null;
	}
	
	/**
	 * Return the XML document from a given uri
	 * @param uri It is the uri to where the file exists
	 * @return
	 * @throws ParserConfigurationException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public Document getXMLDocument(URI uri) throws ParserConfigurationException, MalformedURLException, IOException {
		Document returnDocument = null;
		if (uri == null) {
			throw new MalformedURLException("uri cannot be empty");
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		try {
			returnDocument = db.parse(new InputSource(uri.toURL().openStream()));
		} catch (SAXException e) {
			Logger.printStackTrace(e);
		}
		return returnDocument;
	}
}

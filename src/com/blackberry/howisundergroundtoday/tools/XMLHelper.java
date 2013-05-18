package com.blackberry.howisundergroundtoday.tools;

import android.os.Environment;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URI;
import java.net.URL;

/**
 * Created by Hooman on 17/05/13.
 */

/**
 * This class handles the download and parsing of the xml
 */
public class XMLHelper {
    private String url;
    private String cacheFilePath;
    private boolean cacheDownload;
    private ParserInterface parserObject;


    /**
     * Constructor for the XMLHelper
     *
     * @param url           It is the url for where to get the xml document from
     * @param cacheDownload It is a flag to indicate whether the downloaded xml should be cached after it was downloaded
     */
    public XMLHelper(String url, boolean cacheDownload) {
        this.url = url;
        this.cacheDownload = cacheDownload;
        this.cacheFilePath = Environment.getDownloadCacheDirectory() + "/HowIsUnderground/";
    }

    /**
     * XMLHelper constructor
     *
     * @param url It is the url for where to get the xml from
     */
    public XMLHelper(String url) {
        this.url = url;
        this.cacheDownload = false;
        this.cacheFilePath = Environment.getDownloadCacheDirectory() + "/HowIsUnderground/";
    }

    /**
     * XMLHelper constructor
     *
     * @param url           It is the url for where to get the xml document from
     * @param cacheFilePath The path to where store the cached document
     * @param cacheDownload It is a flag to indicate whether the downloaded xml should be cached after it was downloaded
     */
    public XMLHelper(String url, String cacheFilePath, boolean cacheDownload) {
        this.url = url;
        this.cacheFilePath = cacheFilePath;
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
     * @param cacheFilePath Tha path to the cache file
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
     * @param parserObject It is the parser object
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
        //TODO After FileHelper was implemented use it to check whether there is a cache
        return false;
    }

    //TODO add the functionality
    public void startDownloading(int timeout) throws IOException {
        /*InputStream is;
        File cacheFile;
        OutputStream os;
        Document doc;
        final byte[] buffer = new byte[1024];
        URL url;
        url = new URL(this.url);
        if (this.cacheDownload) {
            cacheFile = new File(cachePath);
            if (cacheFile.exists()) {
                cacheFile.delete();
            }
            cacheFile = new File(cachePath);
            os = new FileOutputStream(cacheFile);
        } else {
            uri = new URI(cacheURI);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(uri.toURL().openStream()));
            doc.
            doc.getDocumentElement().normalize();
        }


        is = new InputSource(url.openStream()).getByteStream();
        int read;
        while ((read = is.read(buffer)) != -1) {
            os.write(buffer, 0, read);
        }
        os.flush();
        is.close();
        os.close();
        editor.putLong(LASTIMEDOWNLOADKEY, System.currentTimeMillis());
        editor.commit();
        return "file://" + cachePath;*/
    }
}

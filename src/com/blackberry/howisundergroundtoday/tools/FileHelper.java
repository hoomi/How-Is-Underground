package com.blackberry.howisundergroundtoday.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Hooman on 17/05/13.
 */
public class FileHelper {

    /**
     * Deletes the file at the given path
     *
     * @param filePath
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.canWrite()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Saves an input stream to a file and returns the file
     * @param is The input stream passed
     * @param filePath The file path on which the file will be saved
     * @param override If true the previous cache file will be deleted,If false and a cache file already exists it throws an IOException
     * @return
     * @throws IOException 
     */
    public static File saveStream(InputStream is, String filePath, boolean override) throws IOException{
    	int read;
    	OutputStream os = null;
    	final byte[] buffer = new byte[1024];
        File file2Save = new File(filePath);
        if (file2Save.exists() && override) {
            if (!file2Save.delete()){
                throw new IOException("File could not be overriden");
            }
        } else if (file2Save.exists() && !override) {
            throw new IOException("File already exists. I order to override set the override flag to true");
        }
        try {
        	os = new FileOutputStream(file2Save);
        	while ((read = is.read(buffer)) != -1) {
        		os.write(buffer, 0, read);
        	}
        } finally {
        	os.flush();
        	is.close();
        	os.close();
        }
        return file2Save;
    }
    
    /**
     * It checks whether a file exists or not.If the size of the file is zero, it returns false
     * @param path It is the path to the file
     * @return True if the file exists and its size is greater than zero. False if the file does not exist and its size is zero
     */
    public static boolean doesTheFileExist(String path) {
    	File file = new File(path);
    	if (file.exists() && file.getTotalSpace() > 0) {
			return true;
		}
    	return false;
    }
}

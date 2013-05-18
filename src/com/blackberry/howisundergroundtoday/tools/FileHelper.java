package com.blackberry.howisundergroundtoday.tools;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
     *
     * @param is
     * @param filePath
     * @param override
     * @return
     * @throws IOException
     */
    public static File saveStream(InputStream is, String filePath, boolean override) throws IOException{
        File file2Save = new File(filePath);
        if (file2Save.exists() && override) {
            if (!file2Save.delete()){
                throw new IOException("File could not be overriden");
            }
        } else if (file2Save.exists() && !override) {
            Logger.w(FileHelper.class, "File is being overriden");
        }
        //TODO add the rest of the funtion ti save the streams in a file
        return null;
    }
}

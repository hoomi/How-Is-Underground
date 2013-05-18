package com.blackberry.howisundergroundtoday.tools;
import android.util.Log;
/**
 * Created by Hooman on 17/05/13.
 */
public final class Logger {
    private static boolean DEBUG_MODE = true;
    public static void i(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.i(callingClass.getSimpleName(), logMessage);
    }

    public static void d(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.d(callingClass.getSimpleName(), logMessage);
    }

    public static void w(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.w(callingClass.getSimpleName(), logMessage);
    }

    public static void e(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.e(callingClass.getSimpleName(), logMessage);
    }

    public static void v(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.v(callingClass.getSimpleName(), logMessage);
    }
}

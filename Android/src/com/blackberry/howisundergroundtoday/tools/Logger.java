package com.blackberry.howisundergroundtoday.tools;

import android.util.Log;

/**
 * Created by Hooman on 17/05/13.
 */
public final class Logger {

    /**
     * This the flag to hide and show the debug message shown by Logger class
     */
    private static boolean DEBUG_MODE = true;

    /**
     * It works similar to Log.i
     *
     * @param callingClass The class type, it is used like TAG in Log.i
     * @param logMessage   The message to be shown on the logCat
     */
    @SuppressWarnings("rawtypes")
    public static void i(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.i(callingClass.getSimpleName(), logMessage);
    }

    /**
     * It works similar to Log.d
     *
     * @param callingClass The class type, it is used like TAG in Log.d
     * @param logMessage   The message to be shown on the logCat
     */
    @SuppressWarnings("rawtypes")
    public static void d(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.d(callingClass.getSimpleName(), logMessage);
    }

    /**
     * It works similar to Log.w
     *
     * @param callingClass The class type, it is used like TAG in Log.w
     * @param logMessage   The message to be shown on the logCat
     */
    @SuppressWarnings("rawtypes")
    public static void w(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.w(callingClass.getSimpleName(), logMessage);
    }

    /**
     * It works similar to Log.e
     *
     * @param callingClass The class type, it is used like TAG in Log.e
     * @param logMessage   The message to be shown on the logCat
     */
    @SuppressWarnings("rawtypes")
    public static void e(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.e(callingClass.getSimpleName(), logMessage);
    }

    /**
     * It works similar to Log.e
     *
     * @param callingClass The class type, it is used like TAG in Log.e
     * @param logMessage   The message to be shown on the logCat
     */
    @SuppressWarnings("rawtypes")
    public static void v(Class callingClass, String logMessage) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        Log.v(callingClass.getSimpleName(), logMessage);
    }

    /**
     * It works as printStackTrace in Exception class, except for when it is not in debug mode it does not show anything
     *
     * @param e It is the exception to print the stack trace for
     */
    public static void printStackTrace(Exception e) {
        if (!Logger.DEBUG_MODE) {
            return;
        }
        e.printStackTrace();
    }

    /**
     * It is used to change the debug mode from outside the logger class
     *
     * @param debugMode True to set debugMode and false for otherwise
     */
    public static void setDebugMode(boolean debugMode) {
        Logger.DEBUG_MODE = debugMode;
    }
}

package com.hyfata.autoclicker.utils;

public class OSValidator {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.contains("win"));
    }
}

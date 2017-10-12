package net.hitdetector.util;

import android.app.Application;

import net.hitdetector.application.HitDetectorLibApplication;

public class ApplicationUtils {
    private ApplicationUtils() {
        // utility class, forbidden constructor
    }

    public static boolean isLiteVersion(Application application) {
        return ((HitDetectorLibApplication) application).isLiteVersion();
    }
}

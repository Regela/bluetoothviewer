package net.hitdetecter.util;

import android.app.Application;

import net.hitdetecter.application.HitDetectorLibApplication;

public class ApplicationUtils {
    private ApplicationUtils() {
        // utility class, forbidden constructor
    }

    public static boolean isLiteVersion(Application application) {
        return ((HitDetectorLibApplication) application).isLiteVersion();
    }
}

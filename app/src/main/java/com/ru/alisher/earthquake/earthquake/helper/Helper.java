package com.ru.alisher.earthquake.earthquake.helper;

/**
 * Created by alisher on 2/9/18.
 */

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.ru.alisher.earthquake.earthquake.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public final class Helper {
    private static final String LOG_TAG = Helper.class.getName();

    public static String getConfigValue(Context context, String configKey) {
        Resources resources = context.getResources();

        try {
            InputStream rawResource = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(rawResource);

            return properties.getProperty(configKey);
        } catch (Resources.NotFoundException e) {
            Log.e(LOG_TAG, "Unable to find the config file: " + e.getMessage());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to open config file.");
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3") from a Date object.
     */
    public static String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "22:30") from a Date object.
     */
    public static String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        return timeFormat.format(dateObject);
    }
}

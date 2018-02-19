package com.ru.alisher.earthquake.earthquake.helper;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by alisher on 2/15/18.
 */

public class GeolocationDecimalToDegreesConverter{
    public static String convert(double latitude, double longitude){
        return convertLatitude(latitude) + ", " + convertLongitude(longitude);
    }

    private static String convertLatitude(double latitude){
        String result = "";
        if(latitude != 0){
            String direction = "N";
            if(latitude < 0){
                direction = "S";
            }
            result = convert(latitude) + direction;
        }
        return result;
    }

    private static String convertLongitude(double longitude){
        String result = "";
        if(longitude != 0){
            String direction = "E";
            if(longitude < 0){
                direction = "W";
            }
            result = convert(longitude) + direction;
        }
        return result;
    }

    private static String convert(double doubleToFormat){
        doubleToFormat = Math.abs(doubleToFormat);

        NumberFormat formatter = NumberFormat.getInstance(Locale.getDefault());
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumFractionDigits(0);
        formatter.setRoundingMode(RoundingMode.HALF_UP);

        return formatter.format(doubleToFormat) + '°';
    }
}

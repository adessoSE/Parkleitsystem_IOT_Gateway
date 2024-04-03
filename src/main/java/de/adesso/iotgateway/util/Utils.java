package de.adesso.iotgateway.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean validateIp(String ip) {
        String regex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}

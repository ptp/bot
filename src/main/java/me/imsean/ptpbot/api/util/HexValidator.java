package me.imsean.ptpbot.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sean on 11/3/15.
 */

public class HexValidator {

    private Pattern pattern;
    private Matcher matcher;

    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6})$";

    public HexValidator(){
        pattern = Pattern.compile(HEX_PATTERN);
    }

    /**
     * Validate hex with regular expression
     * @param hex hex for validation
     * @return true valid hex, false invalid hex
     */
    public boolean validate(final String hex){
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }
}
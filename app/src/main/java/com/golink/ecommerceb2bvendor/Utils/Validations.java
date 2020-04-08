package com.golink.ecommerceb2bvendor.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {

    public static boolean isValidMobile(String phone, int digits) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() == digits;
        }
        return false;
    }


    public static boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

}

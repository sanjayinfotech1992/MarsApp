package com.example.mars.util;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static boolean isEmptyData(EditText editText){
        String data=editText.getText().toString();
        if (data.isEmpty()){
            editText.setError("This field is required");
            return false;
        }
        return true;
    }


    public static boolean isValidEmail(EditText et){
        String data=et.getText().toString();
        Pattern pattern= Patterns.EMAIL_ADDRESS;
        Matcher matcher=pattern.matcher(data);
        if (matcher.matches()){
            return true;
        }
        et.setError("Invalid email");
        return matcher.matches();
    }

    public static boolean isPasswordMatch(EditText et1, EditText et2, Context context){
        String s1=et1.getText().toString();
        String s2=et2.getText().toString();

        if (s1.equalsIgnoreCase(s2)){
            return true;
        }
        Toast.makeText(context,"Password doesn't match",Toast.LENGTH_SHORT).show();
        return false;
    }

}

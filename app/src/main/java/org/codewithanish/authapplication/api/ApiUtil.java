package org.codewithanish.authapplication.api;

import com.google.gson.Gson;

import org.codewithanish.authapplication.data.UserClaim;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;
import java.util.Calendar;

import okhttp3.ResponseBody;

public class ApiUtil {
    public static String getErrorMessage(ResponseBody response) {
        if(response == null) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(response.string());
            return jsonObject.getString("errorMessage");
        } catch (JSONException | IOException e) {
            return null;
        }
    }

    public static UserClaim decodeAccessToken(String token)
    {
        if(token != null && token.contains("."))
        {
           String tokenBody = token.split("\\.")[1];
           String decodedToken = new String(Base64.getDecoder().decode(tokenBody));
           return new Gson().fromJson(decodedToken, UserClaim.class);
        }
        return null;
    }

    public static boolean isTokenExpired(String token)
    {
        UserClaim userClaim = decodeAccessToken(token);
        if(userClaim != null)
        {
         Long currentTimeInSec = Calendar.getInstance().getTimeInMillis()/1000;
         return userClaim.getExp() - currentTimeInSec <= 60;
        }
        return true;
    }

}

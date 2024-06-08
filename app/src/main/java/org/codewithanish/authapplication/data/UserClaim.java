package org.codewithanish.authapplication.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class UserClaim {
    @SerializedName("user-name")
    private String userName;
    private List<String> authorities;
    private Long exp;
}

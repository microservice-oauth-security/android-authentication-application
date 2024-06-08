package org.codewithanish.authapplication.data.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationRequest {
    private String userName;
    private String password;
}

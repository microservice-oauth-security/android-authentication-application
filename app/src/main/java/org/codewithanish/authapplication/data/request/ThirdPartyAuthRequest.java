package org.codewithanish.authapplication.data.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ThirdPartyAuthRequest {
    private String provider;
}

package com.congvo.be_myapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String accessToken;
//    private String refreshToken;
    private String type = "Bearer";
//    private String email;

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
        this.type = "Bearer";
    }
}

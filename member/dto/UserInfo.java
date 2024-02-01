package com.example.project.member.dto;

// 구글 로그인 + 프론트에서 사용할 정보

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class UserInfo {
    private Long id;
    private String email;
//    @SerializedName("verified_email")
//    private Boolean verifiedEmail;
    private String name;
//    @SerializedName("given_name")
//    private String givenName;
//    @SerializedName("family_name")
//    private String familyName;
//    @SerializedName("picture")
//    private String pictureUrl;
    private String locale;
}
package com.ehome.fintec.p2plending.common.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class UserInfoDTO implements Serializable {
    private Long userId;
    private String userName;
}

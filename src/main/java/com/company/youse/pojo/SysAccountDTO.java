package com.company.youse.pojo;

import lombok.Data;

import java.util.List;

@Data
public class SysAccountDTO {

    private String orgName;
    private String memberId;
    private String contactEmail;
    private String contactPhone;

    private List<OrgShortCodeDTO> orgShortCode;
}

package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * OrgUserDto
 *
 * @author Jinhua
 * @date 2019/10/9 15:32
 */
@Getter
@Setter
public class OrgUserDto implements Serializable {
    private static final long serialVersionUID = -5601592359765231733L;

    private String id;
    private String idNumber;
    private String salutation;
    private String userDomain;
    private String userId;
    private String userName;
    private String status;
    private String orgId;
    private String emailAddr;
    private String firstName;
    private String lastName;
    private String uenNo;
    private ArrayList<String> userRoles;

    public OrgUserDto() {
        userRoles = new ArrayList<>();
    }
}

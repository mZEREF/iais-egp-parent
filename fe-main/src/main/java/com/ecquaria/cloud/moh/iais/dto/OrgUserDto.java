package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

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

    @NotBlank(message = "UIN can not is blank!")
    @NotNull(message = "UIN can not is null!")
    private String idNumber;

    @NotBlank(message = "UEN can not is blank!")
    @NotNull(message = "UEN can not is null!")
    private String uenNo;

    @NotBlank(message = "email can not is blank!")
    @NotNull(message = "email can not is null!")
    private String emailAddr;

    @NotBlank(message = "First Name can not is blank!")
    @NotNull(message = "First Name can not is null!")
    private String firstName;

    @NotBlank(message = "Last Name can not is blank!")
    @NotNull(message = "Last Name can not is null!")
    private String lastName;

    private String salutation;
    private String userDomain;
    private String userId;
    private String userName;
    private String status;
    private String orgId;


    private ArrayList<String> userRoles;

    public OrgUserDto() {
        userRoles = new ArrayList<>();
    }
}

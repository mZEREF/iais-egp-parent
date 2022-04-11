package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**
 * LoginContext
 *
 * @author Jinhua
 * @date 2019/12/3 10:24
 */
@Getter
public class LoginContext implements Serializable {
    private static final long serialVersionUID = 1L;

    @Setter private String loginId;
    @Setter private String userId;
    @Setter private String userDomain;
    @Setter private String userName;
    @Setter private String nricNum;
    private ArrayList<String> roleIds;
    @Setter private String curRoleId;
    // BE User Info
    private Set<String> wrkGrpIds;

    // FE User Info
    @Setter private String licenseeId;
    @Setter private String orgId;
    @Setter private String uenNo;
    @Setter private String licenseeEntityType;

    public LoginContext() {
        wrkGrpIds = IaisCommonUtils.genNewHashSet();
        roleIds = IaisCommonUtils.genNewArrayList();
    }
}

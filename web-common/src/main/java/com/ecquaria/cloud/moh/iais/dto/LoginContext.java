package com.ecquaria.cloud.moh.iais.dto;

import java.io.Serializable;
import java.util.HashSet;
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
    private static final long serialVersionUID = 1619841935384186149L;

    @Setter private String loginId;
    @Setter private String userId;
    @Setter private String userDomain;
    private Set<String> wrkGrpIds;
    private Set<String> roleIds;

    public LoginContext() {
        wrkGrpIds = new HashSet<>();
        roleIds = new HashSet<>();
    }
}

package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.privilege.Privilege;
import com.ecquaria.cloud.role.Role;

import java.util.Date;

/**
 * RolePrivilegeAssignment
 *
 * @author junyu
 * @date 2023/3/6
 */
public class RolePrivilegeAssignment {
    private static final long serialVersionUID = -5024163177138533856L;
    public static final String TABLE_NAME = "ROLE_PRIVILEGE_ASSIGN";
    public static final String FIELD_REMARKS = "remarks";
    public static final String FIELD_IS_SYSTEM = "isSystem";
    public static final String PERMISSION_ALLOW = "A";
    public static final String PERMISSION_DENY = "D";
    private String roleId;
    private Long privilegeNo;
    private Long assignNo;
    private String permission;
    private String remarks;
    private Date fromDate;
    private Date toDate;
    private String isSystem;
    private Role role;
    private Privilege privilege;
    public static final String FIELD_ROLE_ID = "roleId";
    public static final String FIELD_PRIVILEGE_NO = "privilegeNo";

    public RolePrivilegeAssignment() {
    }

    public RolePrivilegeAssignment(RolePrivilegeAssignment assignment) {
        this.roleId = assignment.roleId;
        this.privilegeNo = assignment.privilegeNo;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getPrivilegeNo() {
        return this.privilegeNo;
    }

    public void setPrivilegeNo(Long privilegeNo) {
        this.privilegeNo = privilegeNo;
    }

    public Long getAssignNo() {
        return this.assignNo;
    }

    public void setAssignNo(Long assignNo) {
        this.assignNo = assignNo;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Privilege getPrivilege() {
        return this.privilege;
    }

    public void setPrivilege(Privilege privilege) {
        this.privilege = privilege;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getFromDate() {
        return this.fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return this.toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getIsSystem() {
        return this.isSystem;
    }

    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

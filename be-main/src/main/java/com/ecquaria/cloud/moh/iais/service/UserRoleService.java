package com.ecquaria.cloud.moh.iais.service;

public interface UserRoleService {
    String getAvailable(String userId);
    void setAvailable(String userId, String ava);
}

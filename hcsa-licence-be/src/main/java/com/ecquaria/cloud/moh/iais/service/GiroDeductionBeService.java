package com.ecquaria.cloud.moh.iais.service;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/10/19 15:52
 **/
public interface GiroDeductionBeService {
    void sendMessageEmail(List<String> appGroupList);
}

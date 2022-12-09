package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/2/5 9:40
 */
public interface AppealService {

    String submitData(HttpServletRequest request);

    String saveData(HttpServletRequest request);

    void getMessage(HttpServletRequest request);

    Map<String,String> validate(HttpServletRequest request);

    void inbox(HttpServletRequest request, String appNo);

    ProfessionalResponseDto prsFlag(String regNo);

    void print(HttpServletRequest request);
}

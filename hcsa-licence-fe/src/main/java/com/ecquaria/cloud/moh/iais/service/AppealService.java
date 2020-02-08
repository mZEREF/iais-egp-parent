package com.ecquaria.cloud.moh.iais.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/2/5 9:40
 */
public interface AppealService {

    List<String>  reasonAppeal(String applicationNoOrLicenceNo);

    String submitData(HttpServletRequest request);

    String saveData(HttpServletRequest request);

    void getMessage(HttpServletRequest request);

    Map<String,String> validate(HttpServletRequest request);
}

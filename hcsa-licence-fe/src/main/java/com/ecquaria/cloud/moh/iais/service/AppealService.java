package com.ecquaria.cloud.moh.iais.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Wenkang
 * @date 2020/2/5 9:40
 */
public interface AppealService {

    List<String>  reasonAppeal(String applicationNoOrLicenceNo);

    String submitData(HttpServletRequest request);

    String saveData(HttpServletRequest request);
}

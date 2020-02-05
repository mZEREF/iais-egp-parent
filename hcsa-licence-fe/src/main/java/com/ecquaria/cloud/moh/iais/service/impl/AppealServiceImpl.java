package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Wenkang
 * @date 2020/2/5 9:41
 */
@Service
@Slf4j
public class AppealServiceImpl implements AppealService {
    @Autowired
    private ApplicationClient applicationClient;


    @Override
    public List<String> reasonAppeal(String applicationNoOrLicenceNo) {
        ApplicationDto applicationDto = applicationClient.getApplicationDtoByVersion(applicationNoOrLicenceNo).getEntity();
        if(applicationDto!=null){
            String applicationType = applicationDto.getApplicationType();
            if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){



            }
        }


        return null;
    }

    @Override
    public String submitData(HttpServletRequest request) {
        String appealingFor = request.getParameter("appealingFor");
        String reasonSelect = request.getParameter("reasonSelect");
        String[] selectHciNames = request.getParameterValues("selectHciName");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");


        return null;
    }

    @Override
    public String saveData(HttpServletRequest request) {
        String appealingFor = request.getParameter("appealingFor");
        String reasonSelect = request.getParameter("reasonSelect");
        String[] selectHciNames = request.getParameterValues("selectHciName");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");


        return null;
    }



}

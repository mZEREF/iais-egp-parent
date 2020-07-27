package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import freemarker.template.TemplateException;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

/**
 * ApplicationService
 *
 * @author suocheng
 * @date 11/28/2019
 */
public interface ApplicationService {
    public List<ApplicationDto> getApplicaitonsByAppGroupId(String appGroupId);
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appNo,String status);
    //
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId);
    public Integer getAppBYGroupIdAndStatus(String appGroupId,String  status);

    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto);

    public List<ApplicationDto> updateFEApplicaitons(List<ApplicationDto> applicationDtos);

    public List<RequestInformationSubmitDto> getRequestInformationSubmitDtos(List<ApplicationDto> applicationDtos);

    public List<AppEditSelectDto> getAppEditSelectDtos(String appId, String changeType);

    void postInspectionBack();


    void alertSelfDeclNotification();

    /**
      * @author: shicheng
      * @Date 2020/4/23
      * @Param: applicationViewDto, applicationDto, licenseeId, licenseeDto, LoginContext loginContext
      * @return: void
      * @Descripation: Hcsa Application Request Information And Send Email
      */
    void applicationRfiAndEmail(ApplicationViewDto applicationViewDto, ApplicationDto applicationDto, String licenseeId, LicenseeDto licenseeDto, LoginContext loginContext, String externalRemarks) throws IOException, TemplateException;

    ApplicationDto getApplicationBytaskId(String ref);

    public ApplicationDto getApplicationDtoByGroupIdAndStatus(String appGroupId,String status);

    AppFeeDetailsDto getAppFeeDetailsDtoByApplicationNo(String applicationNo);

    AppReturnFeeDto saveAppReturnFee(AppReturnFeeDto appReturnFeeDto);
    List<ApplicationDto> getApplicationDtosByApplicationNo(String applicationNo);
    List<AppEditSelectDto>  getAppEditSelectDtosByAppIds( List<String> applicationIds);

    boolean closeTaskWhenWhAppApprove(String appId);
}

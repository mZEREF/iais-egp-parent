package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.HashMap;
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
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appNo,String status,boolean updateStatus);
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appNo,String status1, String status2);
    //
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId);
    public Integer getAppBYGroupIdAndStatus(String appGroupId,String  status);

    public ApplicationDto updateBEApplicaiton(ApplicationDto applicationDto);

    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto);

    public List<ApplicationDto> updateFEApplicaitons(List<ApplicationDto> applicationDtos);

    public List<PaymentRequestDto> eicFeStripeRefund(List<AppReturnFeeDto> appReturnFeeDtos);

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
    void applicationRfiAndEmail(ApplicationViewDto applicationViewDto, ApplicationDto applicationDto, LoginContext loginContext, String externalRemarks) throws IOException, TemplateException;
    void appealRfiAndEmail(ApplicationViewDto applicationViewDto,ApplicationDto applicationDto, HashMap<String, String> maskParams,String linkURL) throws Exception;
    ApplicationDto getApplicationBytaskId(String ref);

    public ApplicationDto getApplicationDtoByGroupIdAndStatus(String appGroupId,String status);

    AppFeeDetailsDto getAppFeeDetailsDtoByApplicationNo(String applicationNo);

    AppReturnFeeDto saveAppReturnFee(AppReturnFeeDto appReturnFeeDto);
    List<ApplicationDto> getApplicationDtosByApplicationNo(String applicationNo);
    List<AppEditSelectDto>  getAppEditSelectDtosByAppIds( List<String> applicationIds);

    boolean closeTaskWhenWhAppApprove(String appId);

    EventApplicationGroupDto updateFEApplicationStatus(String eventRefNum, String submissionId);
}

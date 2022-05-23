package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import freemarker.template.TemplateException;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public ApplicationDto callEicInterApplication(ApplicationDto applicationDto);

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
    void appealRfiAndEmail(ApplicationViewDto applicationViewDto,ApplicationDto applicationDto, HashMap<String, String> maskParams,String linkURL,String externalRemarks) throws Exception;
    ApplicationDto getApplicationBytaskId(String ref);

    public ApplicationDto getApplicationDtoByGroupIdAndStatus(String appGroupId,String status);

    AppFeeDetailsDto getAppFeeDetailsDtoByApplicationNo(String applicationNo);

    AppReturnFeeDto saveAppReturnFee(AppReturnFeeDto appReturnFeeDto);
    List<ApplicationDto> getApplicationDtosByApplicationNo(String applicationNo);
    List<AppEditSelectDto>  getAppEditSelectDtosByAppIds( List<String> applicationIds);

    boolean isWithdrawReturnFee(String appNo,String appGrpId);

    boolean closeTaskWhenWhAppApprove(String appId);

    EventApplicationGroupDto updateFEApplicationStatus(String eventRefNum, String submissionId);

    void sendRfcClarificationEmail(String licenseeId, ApplicationViewDto applicationViewDto, String internalRemarks, String recipientRole,String recipientUserId) throws Exception;

    void rollBackInspAo1InspLead(BaseProcessClass bpc, String roleId, String routeBackStatus, String wrkGpId, String userId) throws CloneNotSupportedException;

    void updateInspectionStatusByAppNo(String appId, String inspectionStatus);

    /**
      * @author: shicheng
      * @Date 2021/6/3
      * @Param: taskDto, vehicleOpenFlag
      * @return: String
      * @Descripation: getVehicleFlagToShowOrEdit
      */
    String getVehicleFlagToShowOrEdit(TaskDto taskDto, String vehicleOpenFlag, ApplicationViewDto applicationViewDto);

    /**
      * @author: shicheng
      * @Date 2021/6/3
      * @Param: vehicleFlag, applicationViewDto
      * @return: List<String>
      * @Descripation: getVehicleNoByFlag
      */
    List<String> getVehicleNoByFlag(String vehicleFlag, ApplicationViewDto applicationViewDto);

    /**
      * @author: shicheng
      * @Date 2021/6/4
      * @Param: applicationViewDto
      * @return: ApplicationViewDto
      * @Descripation: sortAppSvcVehicleListToShow
      */
    ApplicationViewDto sortAppSvcVehicleListToShow(List<String> vehicleNoList, ApplicationViewDto applicationViewDto);

    /**
      * @author: shicheng
      * @Date 2021/9/17
      * @Param: applicationGroupDto, broadcastApplicationDto
      * @return: broadcastApplicationDto
      * @Descripation: setRejectOtherAppGrps
      */
    BroadcastApplicationDto setRejectOtherAppGrps(ApplicationGroupDto applicationGroupDto, BroadcastApplicationDto broadcastApplicationDto);

    /**
      * @author: shicheng
      * @Date 2021/9/17
      * @Param: applicationGroupDto, broadcastApplicationDto
      * @return: broadcastApplicationDto
      * @Descripation: setAppGrpMiscInactive
      */
    BroadcastApplicationDto setAppGrpMiscInactive(ApplicationGroupDto applicationGroupDto, BroadcastApplicationDto broadcastApplicationDto);

    Map<String, String> checkApplicationByAppGrpNo(String appGrpNo);

    AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, String appType);

    void updateTasks(String appGrpNo);

    /**
     * Check Data For Edit App
     *
     * @param check {@link HcsaAppConst#CHECKED_BTN}: validate data with error; {@link HcsaAppConst#CHECKED_BTN}: only check
     * @param request
     * @return
     */
    boolean checkDataForEditApp(int check, HttpServletRequest request);

}

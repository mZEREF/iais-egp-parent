package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.service.EmailToResultService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/1/16 19:40
 */
@Service
public class EmailToResultServiceImpl implements EmailToResultService {
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private SystemBeLicClient systemBeLicClient;


    private final String SUCCESSMSGKEY = "SUCCESS";
    private final String FAILEDMSGKEY = "REJECT";
    private final String SUCCESSMSGTEMPLATEID = "08B58803-4F38-EA11-BE7E-000C29F371DC";
    private final String GETFAILEDMSGTEMPLATEID = "09B58803-4F38-EA11-BE7E-000C29F371DC";

    @Override
    public void sendRenewResultEmail() throws IOException, TemplateException {
        // get appGrpId which pay success
        List<ApplicationGroupDto> appGrpDtos = applicationClient.getAppGrpsPaySuc().getEntity();
        if(appGrpDtos!=null&&!appGrpDtos.isEmpty()){
            for(ApplicationGroupDto applicationGroupDto : appGrpDtos){
                String appGrpId = applicationGroupDto.getId();
                List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
                if(!applicationDtos.isEmpty()&&applicationDtos!=null){
                    for(ApplicationDto applicationDto :applicationDtos){
                        String applicationType = applicationDto.getApplicationType();
                        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
                            String appNo = applicationDto.getApplicationNo();
                            prepareAndSendSuccessEmail(appNo);
                        }

                    }
                }
            }
        }
        // get appGrpId which reject
        List<ApplicationDto> applicationDtos = applicationClient.getAppDtosReject().getEntity();
        if(applicationDtos!=null&&!appGrpDtos.isEmpty()){
            for(ApplicationDto applicationDto :applicationDtos){
                String applicationType = applicationDto.getApplicationType();
                if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
                    String applicationNo = applicationDto.getApplicationNo();
                    prepareAndSenEmail(applicationNo);
                }
            }
        }
    }

    @Override
    public void sendEfcResultEmail() throws IOException, TemplateException {
        // get appGrpId which pay success
        List<ApplicationGroupDto> appGrpDtos = applicationClient.getAppGrpsPaySuc().getEntity();
        if(appGrpDtos!=null&&!appGrpDtos.isEmpty()){
            for(ApplicationGroupDto applicationGroupDto : appGrpDtos){
                String appGrpId = applicationGroupDto.getId();
                List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
                if(!applicationDtos.isEmpty()&&applicationDtos!=null){
                    for(ApplicationDto applicationDto :applicationDtos){
                        String applicationType = applicationDto.getApplicationType();
                        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
                            String appNo = applicationDto.getApplicationNo();
                            prepareAndSendSuccessEmail(appNo);
                        }

                    }
                }
            }
        }
        // get appGrpId which reject
        List<ApplicationDto> applicationDtos = applicationClient.getAppDtosReject().getEntity();
        if(applicationDtos!=null&&!appGrpDtos.isEmpty()){
            for(ApplicationDto applicationDto :applicationDtos){
                String applicationType = applicationDto.getApplicationType();
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
                    String applicationNo = applicationDto.getApplicationNo();
                    prepareAndSenEmail(applicationNo);
                }
            }
        }
    }


    private void prepareAndSendSuccessEmail(String appNo) throws IOException, TemplateException {
        //check email
        JobRemindMsgTrackingDto findResult = systemBeLicClient.getJobRemindMsgTrackingDto(appNo, SUCCESSMSGKEY).getEntity();
        if(findResult!=null){
            return;
        }
        //save and send
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setRefNo(appNo);
        jobRemindMsgTrackingDto.setMsgKey(SUCCESSMSGKEY);
        jobRemindMsgTrackingDto.setStatus("ACTIVE");
        List<JobRemindMsgTrackingDto> list = new ArrayList<>();
        list.add(jobRemindMsgTrackingDto);
        systemBeLicClient.createJobRemindMsgTrackingDtos(list);
        Map<String,Object> map = new HashMap<>(34);
        map.put("applicationNo",appNo);
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate(SUCCESSMSGTEMPLATEID).getEntity();
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
        String s = templateMessageByContent;
    }

    private void prepareAndSenEmail(String appNo) throws IOException, TemplateException {
        //check email
        JobRemindMsgTrackingDto findResult = systemBeLicClient.getJobRemindMsgTrackingDto(appNo, FAILEDMSGKEY).getEntity();
        if(findResult!=null){
            return;
        }
        //save and send
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setRefNo(appNo);
        jobRemindMsgTrackingDto.setMsgKey(FAILEDMSGKEY);
        jobRemindMsgTrackingDto.setStatus("ACTIVE");
        List<JobRemindMsgTrackingDto> list = new ArrayList<>();
        list.add(jobRemindMsgTrackingDto);
        systemBeLicClient.createJobRemindMsgTrackingDtos(list);
        Map<String,Object> map = new HashMap<>(34);
        map.put("applicationNo",appNo);
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate(GETFAILEDMSGTEMPLATEID).getEntity();
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
        String s = templateMessageByContent;
    }

}

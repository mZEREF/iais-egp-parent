package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditSystemPotentialDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemPotitalListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/19 10:45
 */
@Slf4j
@Delegator("auditSystemListDelegator")
public class AuditSystemListDelegator {

    @Autowired
    AuditSystemPotitalListService auditSystemPotitalListService;
    @Autowired
    AuditSystemListService auditSystemListService;
    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void init(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
    }

    public void pre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;

    }

    public void vad(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the vad start ...."));
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, "isValid", "Y");

    }

    public void next(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the next start ...."));
        HttpServletRequest request = bpc.request;
        List<SelectOption> aduitTypeOp = auditSystemListService.getAuditOp();
        ParamUtil.setSessionAttr(request,"aduitTypeOp",(Serializable) aduitTypeOp);
        AuditSystemPotentialDto dto = new AuditSystemPotentialDto();
        List<String> serviceNmaeList = new ArrayList<>();
        serviceNmaeList.add("Clinical Laboratory");
        dto.setSvcNameList(serviceNmaeList);
        List<AuditTaskDataFillterDto> auditTaskDataDtos =  auditSystemPotitalListService.getSystemPotentailAdultList(dto);
        auditSystemListService.getInspectors(auditTaskDataDtos);
        ParamUtil.setSessionAttr(request,"auditTaskDataDtos",(Serializable) auditTaskDataDtos);
    }

    public void listpageNext(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void remove(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void confirm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        getListData(request);
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    private void getListData(HttpServletRequest request) {
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>)ParamUtil.getSessionAttr(request,"auditTaskDataDtos");
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(int i = 0;i<auditTaskDataDtos.size();i++){
                String auditType = ParamUtil.getString(request,i+"auditType");
                String inspectorId = ParamUtil.getString(request,i+"insOp");
                String inspectorName = getNameById(auditTaskDataDtos.get(i).getInspectors(),inspectorId);
                String forad = ParamUtil.getString(request,i+"selectForAd");
                String number = ParamUtil.getString(request,i+"number");
                auditTaskDataDtos.get(i).setAuditType(auditType);
                auditTaskDataDtos.get(i).setInspectorId(inspectorId);
                auditTaskDataDtos.get(i).setInspector(inspectorName);
                if(!StringUtil.isEmpty(forad)){
                    auditTaskDataDtos.get(i).setSelectedForAudit(true);
                }else{
                    auditTaskDataDtos.get(i).setSelectedForAudit(false);
                }
                if(!StringUtil.isEmpty(number)){
                    auditTaskDataDtos.get(i).setSelected(true);
                }else{
                    auditTaskDataDtos.get(i).setSelected(false);
                }
            }
        }
        ParamUtil.setSessionAttr(request,"auditTaskDataDtos",(Serializable) auditTaskDataDtos);
    }

    private String getNameById(List<SelectOption> inspectors,String inspectorId) {
        if(!IaisCommonUtils.isEmpty(inspectors)){
            for(SelectOption temp:inspectors){
                if(inspectorId.equals(temp.getValue())){
                    return temp.getText();
                }
            }
        }
        return null;
    }

    public void precreatehcl(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void precanceltask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void submit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the submit start ...."));
        HttpServletRequest request = bpc.request;
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>)ParamUtil.getSessionAttr(request,"auditTaskDataDtos");
        auditSystemListService.doSubmit(auditTaskDataDtos);
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void createhcl(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

    public void canceltask(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
    }

}

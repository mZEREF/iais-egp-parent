package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.QueryHandlerService;
import com.ecquaria.cloud.moh.iais.service.client.BelicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * HcsaApplicationDelegator
 *
 * @author zhilin
 * @date 10/22/2020
 */
@Delegator("deleteHelperDelegator")
@Slf4j
public class DeleteHelperDelegator {
    
    @Autowired
    private QueryHandlerService queryHandlerService;

    @Autowired
    private BelicationClient belicationClient;
    
    private final String MIMA = "P@ssword$";

    private final String HCSA_APPLICATION_SQL_FILE = "be_hacsa_application_delete.sql";

    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("deleteHelperDelegator do cleanSession start ...."));
        ParamUtil.setSessionAttr(bpc.request,"queryResult","E");
    }

    public void prepareLogin(BaseProcessClass bpc){

    }

    public void doDelete(BaseProcessClass bpc){
        String userAccountString = ParamUtil.getString(bpc.request,"userAccountString");
        LicenseeDto licenseeDto = queryHandlerService.getLicenseeByUserAccountInfo(userAccountString);
        if(licenseeDto != null){
            List<ApplicationGroupDto> groupDtos = belicationClient.getAppGrpsByLicenseeId(licenseeDto.getId()).getEntity();

        }else{
            log.debug(StringUtil.changeForLog("licenseeDto is null"));
        }
    }

    private int deleteLicence(List<ApplicationGroupDto> groupDtos){
        int flag = 1;
        if(!IaisCommonUtils.isEmpty(groupDtos)){
            for(ApplicationGroupDto applicationGroupDto : groupDtos){
                String groupNo = applicationGroupDto.getGroupNo();
                try{
                    String sql = getRunSql(HCSA_APPLICATION_SQL_FILE, groupNo, "AN200512000809C");
                }catch (Exception e){
                    flag = -1;
                    break;
                }

            }
        }else{
            flag = 0;
        }

        return flag;
    }

    private String getRunSql(String sqlPath, String replaceNo, String targetString) throws Exception{
        String sql = "";
        if(!StringUtil.isEmpty(sqlPath) && !StringUtil.isEmpty(replaceNo)){
            File file = new File("/" + sqlPath);
            log.debug(StringUtil.changeForLog("file path : " + file.getPath()));
            log.debug(StringUtil.changeForLog("file absolute path : " + file.getAbsolutePath()));
            sql = readFile(file,replaceNo,targetString);
        }
        return sql.toString();
    }

    private String readFile(File file,String replaceNo, String targetString) throws Exception{
        StringBuffer sql = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String temp = null;
        while((temp = br.readLine())!=null){
            if(temp.contains(targetString)){
                temp.replaceAll(targetString,replaceNo);
            }
            sql.append(temp);
            sql.append(System.lineSeparator());
        }
        br.close();

        return sql.toString();
    }

    public void doLogin(BaseProcessClass bpc){
        String queryHelperPassword = ParamUtil.getString(bpc.request, "queryHelperPassword");
        String flag = "N";
        if(getCurrentPassword().equals(queryHelperPassword)){
            flag = "Y";
        }
        ParamUtil.setRequestAttr(bpc.request, "crud_action_type", flag);
    }

    public void prepareDelete(BaseProcessClass bpc){
        List<SelectOption> moduleNameList = IaisCommonUtils.genNewArrayList();
        moduleNameList.add(new SelectOption("email-sms","email-sms"));
        moduleNameList.add(new SelectOption("event-bus","event-bus"));
        moduleNameList.add(new SelectOption("hsca-application-be","hsca-application-be"));
        moduleNameList.add(new SelectOption("audit-trail","audit-trail"));
        moduleNameList.add(new SelectOption("iais-appointment","iais-appointment"));
        moduleNameList.add(new SelectOption("hcsa-licence-be","hcsa-licence-be"));
        moduleNameList.add(new SelectOption("organization-be","organization-be"));
        moduleNameList.add(new SelectOption("hcsa-config","hcsa-config"));
        moduleNameList.add(new SelectOption("system-admin","system-admin"));
        ParamUtil.setSessionAttr(bpc.request, "moduleNameDropdown", (Serializable)moduleNameList);
    }

    private String getCurrentPassword(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DATE);
        String dayStr = day < 10 ? "0" + day : day + "";
        String currentPassword = MIMA + dayStr;
        return currentPassword;
    }
}

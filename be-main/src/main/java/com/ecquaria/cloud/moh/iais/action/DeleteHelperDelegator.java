package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.QueryHandlerService;
import com.ecquaria.cloud.moh.iais.service.client.BelicationClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;

    private final String MIMA = "P@ssword$";

    private final String HCSA_APPLICATION_SQL_FILE = "be_hacsa_application_delete.sql";

    private final String HCSA_LICENCE_SQL_FILE = "be_hacsa_licence_delete.sql";

    private final String HCSA_ORGANIZATION_SQL_FILE = "be_hacsa_organication_delete.sql";

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
            String licenseeId = licenseeDto.getId();
            String organizationId = licenseeDto.getOrganizationId();

            List<ApplicationGroupDto> groupDtos = belicationClient.getAppGrpsByLicenseeId(licenseeId).getEntity();
            deleteApplication(groupDtos);

            List<LicenceDto> licenceDtos = licenceClient.getLicenceDtosByLicenseeId(licenseeId).getEntity();
            deleteLicence(licenceDtos);

            deleteOrganization(organizationId);
        }else{
            log.debug(StringUtil.changeForLog("licenseeDto is null"));
        }
    }

    private int deleteLicence(List<LicenceDto> licenceDtos){
        int flag = 1;
        if(!IaisCommonUtils.isEmpty(licenceDtos)){
            for(LicenceDto licenceDto : licenceDtos){
                String licenceNo = licenceDto.getLicenceNo();
                try{
                    String sql = getRunSql(HCSA_LICENCE_SQL_FILE, licenceNo, "AN200512000809C");
                    log.info(StringUtil.changeForLog("delete hacsa licence be sql : " + sql));
                    licenceClient.doDeleteBySql(sql);
                }catch (Exception e){
                    flag = -1;
                    break;
                }
            }
        }else{
            flag = 0;
        }
        log.info(StringUtil.changeForLog("delete hacsa licence be flag : " + flag));
        return flag;
    }

    private int deleteOrganization(String organizationId){
        int flag = 1;
        if(!StringUtil.isEmpty(organizationId)){
            try{
                String sql = getRunSql(HCSA_ORGANIZATION_SQL_FILE, organizationId, "AN200512000809C");
                log.debug(StringUtil.changeForLog("delete hacsa organication be sql : " + sql));
                organizationMainClient.doDeleteBySql(sql);
            }catch (Exception e){
                flag = -1;
            }
        }else{
            flag = 0;
        }
        log.debug(StringUtil.changeForLog("delete hacsa organication be flag : " + flag));
        return flag;
    }

    private int deleteApplication(List<ApplicationGroupDto> groupDtos){
        int flag = 1;
        if(!IaisCommonUtils.isEmpty(groupDtos)){
            for(ApplicationGroupDto applicationGroupDto : groupDtos){
                String groupNo = applicationGroupDto.getGroupNo();
                try{
                    String sql = getRunSql(HCSA_APPLICATION_SQL_FILE, groupNo, "AN200512000809C");
                    log.debug(StringUtil.changeForLog("delete hacsa application be sql : " + sql));
                    belicationClient.doDeleteBySql(sql);
                }catch (Exception e){
                    flag = -1;
                    break;
                }
            }
        }else{
            flag = 0;
        }
        log.debug(StringUtil.changeForLog("delete hacsa application be flag : " + flag));
        return flag;
    }

    private String getRunSql(String sqlPath, String replaceNo, String targetString) throws Exception{
        String sql = "";
        if(!StringUtil.isEmpty(sqlPath) && !StringUtil.isEmpty(replaceNo)){
            File file = IaisCommonUtils.getFile("querySqls/" + sqlPath);
            log.debug(StringUtil.changeForLog("file path : " + file.getPath()));
            log.debug(StringUtil.changeForLog("file absolute path : " + file.getAbsolutePath()));
            if(file.exists()){
                sql = readFile(file,replaceNo,targetString);
            }else{
                log.error("file no exists");
            }
        }
        return sql;
    }

    private synchronized String readFile(File file,String replaceNo, String targetString) {
        StringBuilder sql = new StringBuilder();
        try (
                FileInputStream is = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br= new BufferedReader(isr);
                ) {
                    String temp = null;
                    while((temp = br.readLine())!=null){
                        if(temp.contains(targetString)){
                            temp = temp.replaceAll(targetString,replaceNo);
                        }
                        sql.append(temp);
                        sql.append(System.lineSeparator());
                    }
                }
        catch (IOException e){
            log.error(e.getMessage(),e);
        }

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

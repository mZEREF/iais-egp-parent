package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.IntranetUserService;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailClient;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * @Author weilu
 * @Date 2021/1/15 16:31
 */
@Delegator("activeBeUser")
@Slf4j
public class ActiveBeUserBatchJob {

    @Autowired
    private IntranetUserClient intranetUserClient;
    @Autowired
    private IntranetUserService intranetUserService;
    @Autowired
    private AuditTrailClient trailClient;
    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The activeBeUser is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        log.debug(StringUtil.changeForLog("The activeBeUser is do ..."));
        jobExecute();
    }

    public void jobExecute(){

        List<OrgUserDto> orgUserDtosActive = intranetUserClient.searchActiveBeUser().getEntity();
        List<OrgUserDto> orgUserDtosInActive = intranetUserClient.searchInActiveBeUser().getEntity();
        try {
            if(!IaisCommonUtils.isEmpty(orgUserDtosActive)){
                for(OrgUserDto orgUserDto : orgUserDtosActive){
                    OrgUserDto entity = intranetUserClient.retrieveOneOrgUserAccount(orgUserDto.getUserId()).getEntity();
                    if(entity==null){
                        orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_ACTIVE);
                    }
                }
                intranetUserService.createIntranetUsers(orgUserDtosActive);
            }
            if(!IaisCommonUtils.isEmpty(orgUserDtosInActive)){
                for(OrgUserDto orgUserDto : orgUserDtosInActive){
                    orgUserDto.setStatus(IntranetUserConstant.COMMON_STATUS_DEACTIVATED);
                }
                intranetUserService.createIntranetUsers(orgUserDtosInActive);
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

        //90 days inactive users
        List<String> inActiveUserIds= trailClient.getLastLoginInfoAllUserBe().getEntity();
        if(!IaisCommonUtils.isEmpty(inActiveUserIds)){
            for (String userId:inActiveUserIds
            ) {
                try {
                    OrgUserDto intranetUserByUserId = intranetUserService.findIntranetUserByUserId(userId);
                    if(intranetUserByUserId!=null){
                        intranetUserByUserId.setStatus(IntranetUserConstant.COMMON_STATUS_DEACTIVATED);
                        intranetUserService.updateOrgUser(intranetUserByUserId);
                    }
                }catch (Exception e){
                    log.error("not found user id {}",userId);
                }
            }
        }

    }
}

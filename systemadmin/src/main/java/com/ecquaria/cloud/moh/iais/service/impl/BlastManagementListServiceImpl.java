package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailAttachMentDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ResendListDto;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import com.ecquaria.cloud.moh.iais.service.client.BlastManagementListClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author guyin
 * @date 2020/2/6 10:41
 */
@Service
@Slf4j
public class BlastManagementListServiceImpl implements BlastManagementListService {

    @Autowired
    private BlastManagementListClient blastManagementListClient ;

    @Autowired
    private EmailHistoryClient emailHistoryClient ;
    @Autowired
    private EmailSmsClient emailSmsClient;
    @Autowired
    private SystemClient systemClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Override
    public SearchResult<BlastManagementListDto> blastList(SearchParam searchParam) {
        return blastManagementListClient.getBlastManagementList(searchParam).getEntity();
    }

    @Override
    public SearchResult<EmailAuditTrailDto> auditList(SearchParam searchParam){
        return emailHistoryClient.getAuditList(searchParam).getEntity();
    }

    @Override
    public SearchResult<ResendListDto> resendList(SearchParam searchParam){
        return emailHistoryClient.getResendList(searchParam).getEntity();
    }
    @Override
    public BlastManagementDto saveBlast(BlastManagementDto blastManagementDto){
        return blastManagementListClient.saveBlastList(blastManagementDto).getEntity();
    }

    @Override
    public void setSchedule(BlastManagementDto blastManagementDto){
        blastManagementListClient.setSchedule(blastManagementDto);
    }
    @Override
    public void deleteBlastList(List<String> list){
        blastManagementListClient.deleteBlastList(list);
    }
    @Override
    public BlastManagementDto getBlastById(String id){
        return blastManagementListClient.getBlastById(id).getEntity();
    }

    @Override
    public BlastManagementDto getBlastByMsgId(String id){
        return blastManagementListClient.getBlastByMsgId(id).getEntity();
    }
    @Override
    public List<BlastManagementDto> getBlastBySendTime(String date){
        return blastManagementListClient.getBlastBySendTime(date).getEntity();
    }

    @Override
    public List<BlastManagementDto> getSendedBlast(){
        return blastManagementListClient.getSendedBlast().getEntity();
    }

    @Override
    public List<BlastManagementDto> getSendedSMS(){
        return blastManagementListClient.getSendedSMS().getEntity();
    }
    @Override
    public void setActual(String id){
        blastManagementListClient.setActual(id).getEntity();
    }

    @Override
    public void sendSMS(List<String> recipts,SmsDto sms,String reqRefNum){
        emailHistoryClient.sendSMS(recipts,sms,reqRefNum);
    }
    @Override
    public void sendEmail(EmailDto emailDto, Map<String, byte[]> attachments) {
        try {
            emailSmsClient.sendEmail(emailDto,attachments);
        }catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }
    @Override
    public List<OrgUserDto> retrieveOrgUserAccount(List<String> ids){
        return organizationClient.retrieveOrgUserAccount(ids).getEntity();
    }


    @Override
    public String getMessageId(){
        return systemClient.messageID().getEntity();
    }

    @Override
    public Boolean checkUse(List<String> disList){
        return blastManagementListClient.checkUse(disList).getEntity();
    }

    @Override
    public EmailAttachMentDto getEmailById(String id){
        return emailHistoryClient.getEmailById(id).getEntity();
    }

    @Override
    public void setEmailResend(String id){
        emailHistoryClient.setEmailResend(id);
    }

    @Override
    public List<String> getEmailByRole(String role){
        return hcsaLicenceClient.getEmailByRole(role).getEntity();
    }

    @Override
    public List<String> getMobileByRole(String role){
        return hcsaLicenceClient.getMobileByRole(role).getEntity();
    }
}

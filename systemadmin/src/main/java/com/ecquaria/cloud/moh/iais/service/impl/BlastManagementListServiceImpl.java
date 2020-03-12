package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ResendListDto;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import com.ecquaria.cloud.moh.iais.service.client.BlastManagementListClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
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
    public List<BlastManagementDto> getBlastBySendTime(String date){
        return blastManagementListClient.getBlastBySendTime(date).getEntity();
    }

    @Override
    public void setActual(String id){
        blastManagementListClient.setActual(id).getEntity();
    }

    @Override
    public void sendEmail(EmailDto emailDto, Map<String, byte[]> attachments) {
        try {
            emailSmsClient.sendEmail(emailDto,attachments);
        }catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }


}

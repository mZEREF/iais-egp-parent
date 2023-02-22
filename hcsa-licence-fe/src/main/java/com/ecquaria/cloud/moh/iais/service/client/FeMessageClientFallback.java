package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

public class FeMessageClientFallback implements FeMessageClient{
    @Override
    public FeignResponseEntity<InterMessageDto> createInboxMessage(InterMessageDto interInboxDto) {
        return IaisEGPHelper.getFeignResponseEntity("createInboxMessage", interInboxDto);
    }

    @Override
    public FeignResponseEntity<Void> updateMsgStatus(String msgId, String status) {
        return IaisEGPHelper.getFeignResponseEntity("updateMsgStatus",msgId,status);
    }

    @Override
    public FeignResponseEntity<InterMessageDto> getInterMessageById(String msgId) {
        return IaisEGPHelper.getFeignResponseEntity("getInterMessageById", msgId);
    }

    @Override
    public FeignResponseEntity<InterMessageDto> getInterMessageBySubjectLike(String subject, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getInterMessageBySubjectLike",subject,status);
    }

    @Override
    public FeignResponseEntity<List<String>> getInterMsgIdsBySubjectLike(String subject, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getInterMsgIdsBySubjectLike", subject, status);
    }

    @Override
    public FeignResponseEntity<List<InterMessageDto>> getInboxMsgByRefNo(String refNo) {
        return IaisEGPHelper.getFeignResponseEntity("getInboxMsgByRefNo",refNo);
    }

    @Override
    public FeignResponseEntity<List<String>> updateNotificationContent(String bindSubmissionId, String content) {
        return IaisEGPHelper.getFeignResponseEntity("getInboxMsgByRefNo", bindSubmissionId, content);
    }
}

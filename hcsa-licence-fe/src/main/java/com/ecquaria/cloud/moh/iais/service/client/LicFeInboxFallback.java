package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-26 09:57
 **/
@Component
public class LicFeInboxFallback implements LicFeInboxClient {
    @Override
    public FeignResponseEntity<SearchResult<InboxQueryDto>> searchInbox(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchInbox",searchParam);
    }

    @Override
    public FeignResponseEntity<Integer> searchUnreadAndUnresponseNum(String userId) {
        return IaisEGPHelper.getFeignResponseEntity("searchUnreadAndUnresponseNum",userId);
    }

    @Override
    public FeignResponseEntity<Boolean> updateMsgStatusToArchive(String[] msgIds) {
        return IaisEGPHelper.getFeignResponseEntity("updateMsgStatusToArchive",msgIds);
    }

    @Override
    public FeignResponseEntity<Void> updateMsgStatusTo(String msgId, String msgStatus) {
        return IaisEGPHelper.getFeignResponseEntity("updateMsgStatusTo",msgId,msgStatus);
    }

    @Override
    public FeignResponseEntity<List<InboxMsgMaskDto>> getInboxMsgMask(String msgId) {
        return IaisEGPHelper.getFeignResponseEntity("getInboxMsgMask",msgId);
    }

    @Override
    public FeignResponseEntity<InterMessageDto> getInterMessageById(String msgId) {
        return IaisEGPHelper.getFeignResponseEntity("getInterMessageById",msgId);
    }
}
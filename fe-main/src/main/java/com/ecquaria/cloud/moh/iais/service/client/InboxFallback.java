package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-26 09:57
 **/
@Component
public class InboxFallback implements InboxClient {
    @Override
    public FeignResponseEntity<SearchResult<InboxQueryDto>> searchInbox(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchInbox");
    }

    @Override
    public FeignResponseEntity<Integer> searchUnreadAndUnresponseNum(InterMessageSearchDto interMessageSearchDto) {
        return IaisEGPHelper.getFeignResponseEntity("searchUnreadAndUnresponseNum");
    }

    @Override
    public FeignResponseEntity<Boolean> updateMsgStatusToArchive(String[] msgIds) {
        return IaisEGPHelper.getFeignResponseEntity("updateMsgStatusToArchive");
    }

    @Override
    public FeignResponseEntity<Void> updateMsgStatusTo(String msgId, String msgStatus) {
        return IaisEGPHelper.getFeignResponseEntity("updateMsgStatusTo");
    }

    @Override
    public FeignResponseEntity<List<InboxMsgMaskDto>> getInboxMsgMask(String msgId) {
        return IaisEGPHelper.getFeignResponseEntity("getInboxMsgMask");
    }
}
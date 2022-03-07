package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "inter-inbox",configuration = FeignConfiguration.class,fallback = InboxFallback.class)
public interface InboxClient {
    @PostMapping(path = "/iais-inter-inbox/inbox-param", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<InboxQueryDto>> searchInbox(SearchParam searchParam);

    @PostMapping(path = "/iais-inter-inbox/inbox/unread-unresponse-num", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> searchUnreadAndUnresponseNum(InterMessageSearchDto interMessageSearchDto);

    @PutMapping(path = "/iais-inter-inbox/archive", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> updateMsgStatusToArchive(String[] msgIds);

    @PutMapping(path = "/iais-inter-inbox/message-status")
    FeignResponseEntity<Void> updateMsgStatusTo(@RequestParam(value = "msgId") String msgId,@RequestParam(value = "msgStatus")String msgStatus);

    @GetMapping(value = "/iais-inter-inbox/inbox/mask")
    FeignResponseEntity<List<InboxMsgMaskDto>> getInboxMsgMask(@RequestParam(name = "msgId")String msgId);
}
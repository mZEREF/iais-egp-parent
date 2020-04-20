package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "inter-inbox",configuration = FeignConfiguration.class,fallback = InboxFallback.class)
public interface InboxClient {
    @PostMapping(path = "/iais-inter-inbox/inbox-param", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<InboxQueryDto>> searchInbox(SearchParam searchParam);

    @GetMapping(path = "/iais-inter-inbox/inbox/unread-unresponse-num", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> searchUnreadAndUnresponseNum(String userId);

    @PutMapping(path = "/iais-inter-inbox/archive", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> updateMsgStatusToArchive(String[] msgIds);

    @PutMapping(path = "/iais-inter-inbox/readed/{msgId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateMsgStatusToRead(@PathVariable("msgId") String msgId);

}
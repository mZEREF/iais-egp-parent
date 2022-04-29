package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@FeignClient(name = "inter-inbox",configuration = FeignConfiguration.class,fallback = FeMessageClientFallback.class)
public interface FeMessageClient {
    @PostMapping(path = "/iais-inter-inbox/message", consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InterMessageDto> createInboxMessage(@RequestBody InterMessageDto interInboxDto);

    @PutMapping(value = "/iais-inter-inbox/inbox-message")
    FeignResponseEntity<Void> updateMsgStatus(@RequestParam(value = "msgId")String msgId, @RequestParam(value = "status")String status);

    @GetMapping(value = "/iais-inter-inbox/inbox/{msgId}")
    FeignResponseEntity<InterMessageDto> getInterMessageById(@PathVariable(value = "msgId") String msgId);
    @GetMapping(value = "/iais-inter-inbox/inter-message-subject-like",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<InterMessageDto> getInterMessageBySubjectLike(@RequestParam("subject") String subject,@RequestParam("status") String status);

    @GetMapping(value = "/iais-inter-inbox/inter-msgIds-subject-like",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getInterMsgIdsBySubjectLike(@RequestParam("subject") String subject, @RequestParam("status") String status);
}

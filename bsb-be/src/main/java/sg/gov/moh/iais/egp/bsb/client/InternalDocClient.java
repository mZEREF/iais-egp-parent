package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.entity.InternalDocDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import java.util.List;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface InternalDocClient {
    @GetMapping(value = "/internal-doc/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<DocDisplayDto> getInternalDocForDisplay(@PathVariable("appId") String appId);

    @PostMapping(value = "/internal-doc/incomplete", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveInternalDoc4CurUser(@RequestBody InternalDocDto internalDocDto);

    @DeleteMapping(value = "/internal-doc/{appId}/{repoId}")
    void deleteInternalDoc(@PathVariable("appId") String appId,
                           @PathVariable("repoId") String repoId);
}

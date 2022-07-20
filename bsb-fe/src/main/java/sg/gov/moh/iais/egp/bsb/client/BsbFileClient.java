package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface BsbFileClient {
    /**
     * @deprecated Do not use API to synchronize files anymore
     */
    @Deprecated
    @PostMapping(value = "/bsb-file-sync", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveFiles(@RequestBody FileRepoSyncDto fileRepoSyncDto);
}

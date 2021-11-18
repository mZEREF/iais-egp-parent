package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import sg.gov.moh.iais.egp.bsb.dto.file.FileRepoSyncDto;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class)
public interface BsbFileClient {
    @PostMapping(value = "/file-sync", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FeignResponseEntity<Void> saveFiles(@RequestPart("syncFiles") FileRepoSyncDto fileRepoSyncDto);
}

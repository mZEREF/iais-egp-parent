package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "bsbFileSync")
public interface FileSyncClient {
    @PostMapping(value = "/bsb-file-sync/zip/load")
    void loadSyncZip();

    @PostMapping(value = "/bsb-file-sync/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void syncSaveFiles(@RequestPart("syncFiles") NewFileSyncDto[] newFileSyncDtoList);
}

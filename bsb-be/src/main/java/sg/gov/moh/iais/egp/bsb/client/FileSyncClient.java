package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "bsbFileSync")
public interface FileSyncClient {
    @PostMapping(value = "/bsb-file-sync/zip/load")
    void loadSyncZip();
}

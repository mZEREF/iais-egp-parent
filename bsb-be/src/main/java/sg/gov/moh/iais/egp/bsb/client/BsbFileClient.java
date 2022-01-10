package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;

@FeignClient(name = "bsb-be-api", configuration = FeignConfiguration.class)
public interface BsbFileClient {
    @PostMapping(value = "/file-sync/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> syncFiles(@RequestPart("syncFiles") NewFileSyncDto[] newFileSyncDtoList);
}

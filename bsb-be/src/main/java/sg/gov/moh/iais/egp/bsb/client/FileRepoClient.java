package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@FeignClient(name = "FILE-REPOSITORY", configuration = FeignClientsConfiguration.class)
public interface FileRepoClient {
    @PostMapping(value = "/saveFiles", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FeignResponseEntity<List<String>> saveFiles(@RequestPart("selectedFiles") MultipartFile[] files);

    @GetMapping(value = "/{guid}")
    FeignResponseEntity<byte[]> getFileFormDataBase(@PathVariable(name = "guid") String guid);

    @DeleteMapping(value = "/no-file", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> removeFileById(@RequestBody FileRepoDto fileRepoDto);

    @PostMapping(value = "/file/single")
    String saveSingleFile(@RequestParam("data") byte[] data);

    @PostMapping(value = "/file/multiple", consumes = MediaType.APPLICATION_JSON_VALUE)
    Map<String, String> saveMultipleFiles(@RequestBody Map<String, byte[]> dataMap);
}

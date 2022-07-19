package sg.gov.moh.iais.egp.bsb.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.dto.file.RefreshableDocDto;

import java.util.Collection;
import java.util.Map;


@Slf4j
@Service
public class DocDtoService {
    private final FileRepoClient fileRepoClient;


    @Autowired
    public DocDtoService(FileRepoClient fileRepoClient) {
        this.fileRepoClient = fileRepoClient;
    }


    /**
     * Saves new created documents in a DTO and refresh the status
     * @return file repo IDs of new created records
     */
    public Collection<String> saveDocDtoAndRefresh(RefreshableDocDto dto) {
        Map<String, byte[]> dataMap = dto.prepare4Saving();
        Map<String, String> idMap = fileRepoClient.saveMultipleFiles(dataMap);
        dto.refreshAfterSave(idMap);
        return idMap.values();
    }
}

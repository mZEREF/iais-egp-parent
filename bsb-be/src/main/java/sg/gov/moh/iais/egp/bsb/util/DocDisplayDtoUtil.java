package sg.gov.moh.iais.egp.bsb.util;

import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : LiRan
 * @date : 2022/1/27
 */
public class DocDisplayDtoUtil {
    private DocDisplayDtoUtil() {}

    /**
     * Convert docDisplayDto list to map, key is doc fileRepoId, value is docName,
     */
    public static Map<String, String> getRepoIdDocNameMap(List<DocDisplayDto> docDisplayDtoList){
        Map<String, String> map = new HashMap<>(docDisplayDtoList.size());
        if (!CollectionUtils.isEmpty(docDisplayDtoList)){
            for (DocDisplayDto docDisplayDto : docDisplayDtoList) {
                map.put(docDisplayDto.getFileRepoId(), docDisplayDto.getDocName());
            }
        }
        return map;
    }
}

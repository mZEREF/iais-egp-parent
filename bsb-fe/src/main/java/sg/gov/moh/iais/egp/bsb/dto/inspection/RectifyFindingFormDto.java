package sg.gov.moh.iais.egp.bsb.dto.inspection;

import io.jsonwebtoken.lang.Assert;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author YiMing
 * @version 2022/2/11 15:06
 **/

@Data
public class RectifyFindingFormDto implements Serializable {
    private List<RectifyFindingItemDto> itemDtoList;
    private List<DocRecordInfo> docDtoList;
    private String appId;
    private String configId;

    @Data
    public static class RectifyFindingItemDto implements Serializable{
        /* Item value is sectionId--v--itemId */
        private String itemValue;

        private String itemText;

        private String remarks;

        private String deadline;
    }

    /**
     * getDocRecordInfoBySubType
     * get a map key is docSubType and value is Document
     * */
    public Map<String,DocRecordInfo> getDocRecordInfoBySubType(){
        return CollectionUtils.uniqueIndexMap(docDtoList,DocRecordInfo::getDocSubType);
    }

    /**
     * isRectify
     * this method is used to charge this item if rectify
     * @param itemKey:itemKey is is sectionId--v--itemId and also value of docSubType
     * */
    public boolean isRectify(String itemKey){
        Assert.hasLength(itemKey,"itemKey is null");
        return getDocRecordInfoBySubType().containsKey(itemKey);
    }
}

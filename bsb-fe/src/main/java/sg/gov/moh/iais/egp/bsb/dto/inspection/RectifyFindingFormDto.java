package sg.gov.moh.iais.egp.bsb.dto.inspection;

import io.jsonwebtoken.lang.Assert;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
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

    public RectifyFindingFormDto() {
        docDtoList = new ArrayList<>();
        itemDtoList = new ArrayList<>();
    }

    /**
     * getDocRecordInfoBySubType
     * get a map key is docSubType and value is Document
     * */
    public Map<String,DocRecordInfo> getDocRecordInfoBySubType(){
        return CollectionUtils.uniqueIndexMap(docDtoList,DocRecordInfo::getDocSubType);
    }

    public RectifyFindingItemDto getRectifyFindingItemDtoByItemValue(String itemKey){
        Assert.hasLength(itemKey,"item value key is null");
        if(itemDtoList.isEmpty()){
            return null;
        }
        RectifyFindingItemDto itemDto = CollectionUtils.uniqueIndexMap(itemDtoList,RectifyFindingItemDto::getItemValue).get(itemKey);
        Assert.notNull(itemDto,"item find key is null");
        return itemDto;
    }

}

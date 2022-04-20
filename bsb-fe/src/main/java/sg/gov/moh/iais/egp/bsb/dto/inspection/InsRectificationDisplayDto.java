package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InsRectificationDisplayDto implements Serializable {
    private List<RectificationItemDto> itemDtoList;
    private List<DocRecordInfo> docDtoList;
    private String configId;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RectificationItemDto  implements Serializable  {
        /* Item value is sectionId--v--itemId */
        private String itemValue;
        private String itemText;
        private String finding;
        private String actionRequired;
        private String remark;
        private Boolean rectified;
        private String status;
    }

    /**
     * getDocRecordInfoBySubType
     * get a map key is docSubType and value is Document
     * */
    public Map<String,DocRecordInfo> getDocRecordInfoBySubType(){
        return CollectionUtils.uniqueIndexMap(docDtoList,DocRecordInfo::getDocSubType);
    }

    public InsRectificationDisplayDto.RectificationItemDto getRectifyFindingItemDtoByItemValue(String itemKey){
        Assert.hasLength(itemKey,"item value key is null");
        if(itemDtoList.isEmpty()){
            return null;
        }
        InsRectificationDisplayDto.RectificationItemDto itemDto = CollectionUtils.uniqueIndexMap(itemDtoList, InsRectificationDisplayDto.RectificationItemDto::getItemValue).get(itemKey);
        Assert.notNull(itemDto,"item find key is null");
        return itemDto;
    }

    public InsRectificationDisplayDto() {
        docDtoList = new ArrayList<>();
    }
}

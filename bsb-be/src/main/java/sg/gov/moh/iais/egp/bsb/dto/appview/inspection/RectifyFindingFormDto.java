package sg.gov.moh.iais.egp.bsb.dto.appview.inspection;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.List;


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
        //follow-up items special field
        private String requestExtensionOfDueDate;
        private String reasonForExtension;
    }
}

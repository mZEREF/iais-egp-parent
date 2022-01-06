package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;


@Data
public class InsFindingFormDto implements Serializable {
    ArrayList<InsFindingItemDto> itemDtoList;
    private String appId;
    private String configId;


    @Data
    public static class InsFindingItemDto implements Serializable {
        /* Item value is sectionId--v--itemId */
        private String itemValue;
        private String itemText;
        private String findingType;
        private String remarks;
        private String deadline;
    }
}

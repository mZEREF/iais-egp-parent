package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InsFindingFormDto implements Serializable {
    ArrayList<InsFindingItemDto> itemDtoList;
    private String appId;
    private String configId;


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class InsFindingItemDto implements Serializable {
        /* Item value is sectionId--v--itemId */
        private String itemValue;
        private String itemText;
        private String findingType;
        private String remarks;
        private String deadline;
    }
}

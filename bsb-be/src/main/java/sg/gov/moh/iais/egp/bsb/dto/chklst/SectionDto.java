package sg.gov.moh.iais.egp.bsb.dto.chklst;



import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * SectionDto
 *
 * @author junyu
 * @date 2022/4/29
 */
@Data
public class SectionDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sectionName;
    private String sectionNameSub;
    private List<ItemDto> itemDtoList;
    private List<InspectionCheckQuestionDto> checkList;
}

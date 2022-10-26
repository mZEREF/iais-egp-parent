package sg.gov.moh.iais.egp.bsb.dto.chklst;


import lombok.Data;

import java.io.Serializable;

/**
 * ItemDto
 *
 * @author junyu
 * @date 2022/4/29
 */
@Data
public class ItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String itemId;
    private InspectionCheckQuestionDto incqDto;

}

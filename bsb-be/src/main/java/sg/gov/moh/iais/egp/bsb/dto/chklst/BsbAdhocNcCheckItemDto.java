package sg.gov.moh.iais.egp.bsb.dto.chklst;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistItemDto;

/**
 * BsbAdhocNcCheckItemDto
 *
 * @author Jinhua
 * @date 2022/5/13 16:36
 */
@Getter
@Setter
public class BsbAdhocNcCheckItemDto extends AdhocChecklistItemDto {
    private static final long serialVersionUID = 1L;
    private String remark;
    private String adAnswer;
    private List<BsbAnswerForDifDto> adhocAnswerForDifDtos;
    private Map<String, BsbAnswerForDifDto> answerForDifDtoMaps;
    private boolean sameAnswer;
    private String deconflict;
    private String ncs;
}

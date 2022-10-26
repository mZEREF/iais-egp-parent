package sg.gov.moh.iais.egp.bsb.dto.chklst;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * InspectionCheckQuestionDto
 *
 * @author junyu
 * @date 2022/4/29
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InspectionCheckQuestionDto extends ChecklistQuestionDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String appPreCorreId;
    private String chkanswer;
    private String remark;
    private boolean isRectified;
    private String tcuRemark;
    private String ncItemId;
    private String isRec;
    private String sectionNameSub;
    private String sectionNameShow;
    private String sectionId;
    private List<BsbAnswerForDifDto> answerForDifDtos;
    private boolean ncSelfAnswer;
    private Map<String, BsbAnswerForDifDto> answerForDifDtoMaps;
    private boolean sameAnswer;
    private String deconflict;
    private String selfAnswer;
    private String ncs;
    private String followupItem;
    private String observeFollowup;
    private String followupAction;
    private String dueDate;
}

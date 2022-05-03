package sg.gov.moh.iais.egp.bsb.dto.chklst;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
    private List<AnswerForDifDto> answerForDifDtos;
    private boolean ncSelfAnswer;
    private Map<String, AnswerForDifDto> answerForDifDtoMaps;
    private boolean sameAnswer;
    private String deconflict;
    private String selfAnswer;
    private String ncs;
    private String followupItem;
    private String observeFollowup;
    private String followupAction;
    private String dueDate;
}

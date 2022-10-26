package sg.gov.moh.iais.egp.bsb.dto.chklst;

import java.io.Serializable;
import lombok.Data;

/**
 * ChecklistQuestionDto
 *
 * @author junyu
 * @date 2022/4/29
 */
@Data
public class ChecklistQuestionDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String itemId;
    private String configId;
    private Boolean common;
    private String svcCode;
    private String svcName;
    private String subTypeName;
    private String module;
    private String svcType;
    private String hciCode;
    private String sectionName;
    private String sectionDesc;
    private Integer secOrder;
    private String checklistItem;
    private String answerType;
    private String riskLvl;
    private String regClauseNo;
    private String regClause;
    private String svcId;
    private String inspectionEntity;
    private String answer;
}

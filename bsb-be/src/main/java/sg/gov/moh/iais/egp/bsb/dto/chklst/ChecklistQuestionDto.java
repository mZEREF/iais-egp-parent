package sg.gov.moh.iais.egp.bsb.dto.chklst;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * ChecklistQuestionDto
 *
 * @author junyu
 * @date 2022/4/29
 */
@Data
public class ChecklistQuestionDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    @Column(
            name = "item_id"
    )
    private String itemId;
    @Column(
            name = "config_id"
    )
    private String configId;
    @Column(
            name = "common"
    )
    private Boolean common;
    @Column(
            name = "svc_code"
    )
    private String svcCode;
    @Column(
            name = "svc_name"
    )
    private String svcName;
    @Column(
            name = "svc_subtype_name"
    )
    private String subTypeName;
    @Column(
            name = "module"
    )
    private String module;
    @Column(
            name = "svc_type"
    )
    private String svcType;
    @Column(
            name = "hci_code"
    )
    private String hciCode;
    @Column(
            name = "section_name"
    )
    private String sectionName;
    @Column(
            name = "section_desc"
    )
    private String sectionDesc;
    @Column(
            name = "section_order"
    )
    private Integer secOrder;
    @Column(
            name = "checklist_item"
    )
    private String checklistItem;
    @Column(
            name = "answer_type"
    )
    private String answerType;
    @Column(
            name = "risk_level"
    )
    private String riskLvl;
    @Column(
            name = "clause_no"
    )
    private String regClauseNo;
    @Column(
            name = "clause"
    )
    private String regClause;
    @Column(
            name = "service_id"
    )
    private String svcId;
    @Column(
            name = "INSPECTION_ENTITY"
    )
    private String inspectionEntity;
    @Transient
    private String answer;
}

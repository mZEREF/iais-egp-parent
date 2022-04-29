package sg.gov.moh.iais.egp.bsb.dto.chklst;

import lombok.Data;

import java.io.Serializable;

/**
 * AnswerForDifDto
 *
 * @author junyu
 * @date 2022/4/29
 */
@Data
public class AnswerForDifDto  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String submitName;
    private String submitId;
    private String answer;
    private String remark;
    private String isRec;
    private String ncs;
    private boolean sameAnswer;
}

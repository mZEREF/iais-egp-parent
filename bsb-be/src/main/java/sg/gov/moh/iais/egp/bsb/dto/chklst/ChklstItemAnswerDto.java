package sg.gov.moh.iais.egp.bsb.dto.chklst;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@CustomValidate(impClass = "sg.gov.moh.iais.egp.bsb.validation.ChklstItemAnswerValidator", properties = {"file", "page"})
public class ChklstItemAnswerDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Invlid value", profiles = {"file", "page"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"file", "page"})
    private String snNo;

    @NotBlank(message = "Invlid value", profiles = {"file", "page"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"file", "page"})
    private String configId;

    @NotBlank(message = "Invlid value", profiles = {"file", "page"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"file", "page"})
    private String sectionId;

    @NotBlank(message = "Invlid value", profiles = {"file", "page"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"file", "page"})
    private String itemId;

    @NotBlank(message = "Invlid value", profiles = {"file", "page"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"file", "page"})
    private String answer;

    private String remarks;

    @MaxLength(value = 500, message = "GENERAL_ERR0041", profiles = {"file", "page"})
    @CustomMsg(placeHolders = {"field", "maxlength"}, replaceVals = {"Findings/Non-Compliance", "500"})
    private String findings;

    @MaxLength(value = 500, message = "GENERAL_ERR0041", profiles = {"file", "page"})
    @CustomMsg(placeHolders = {"field", "maxlength"}, replaceVals = {"Actions Required", "500"})
    private String actionRequired;

    private Boolean rectified;

    @NotBlank(message = "Invlid value", profiles = {"file", "page"})
    @NotNull(message = "GENERAL_ERR0006")
    private String followupItem;

    @MaxLength(value = 500, message = "GENERAL_ERR0041", profiles = {"file", "page"})
    @CustomMsg(placeHolders = {"field", "maxlength"}, replaceVals = {"Observations for Follow-up", "500"})
    private String observeFollowup;

    @MaxLength(value = 500, message = "GENERAL_ERR0041", profiles = {"file", "page"})
    @CustomMsg(placeHolders = {"field", "maxlength"}, replaceVals = {"Action Required", "500"})
    private String followupAction;

    private String dueDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChklstItemAnswerDto answerDto = (ChklstItemAnswerDto) o;
        return Objects.equals(configId, answerDto.configId) && Objects.equals(sectionId, answerDto.sectionId) && Objects.equals(itemId, answerDto.itemId) && Objects.equals(answer, answerDto.answer) && Objects.equals(remarks, answerDto.remarks) && Objects.equals(findings, answerDto.findings) && Objects.equals(actionRequired, answerDto.actionRequired) && Objects.equals(rectified, answerDto.rectified) && Objects.equals(followupItem, answerDto.followupItem) && Objects.equals(observeFollowup, answerDto.observeFollowup) && Objects.equals(followupAction, answerDto.followupAction) && Objects.equals(dueDate, answerDto.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configId, sectionId, itemId, answer, remarks, findings, actionRequired, rectified, followupItem, observeFollowup, followupAction, dueDate);
    }
}

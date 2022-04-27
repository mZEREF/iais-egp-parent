package sg.gov.moh.iais.egp.bsb.dto.chklst;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;

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

    @NotNull(message = "GENERAL_ERR0006", profiles = {"file", "page"})
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

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"file", "page"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"file", "page"})
    private String dueDate;
}

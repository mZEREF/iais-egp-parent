package sg.gov.moh.iais.egp.bsb.dto.chklst;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.sf.oval.constraint.MaxLength;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChklstItemAnswerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String snNo;
    private String configId;
    private String sectionId;
    private String itemId;

    @NotBlank(message = "Invlid value")
    @NotNull(message = "GENERAL_ERR0006")
    private String answer;

    @MaxLength(value = 100, message = "GENERAL_ERR0041")
    @CustomMsg(placeHolders = {"field", "maxlength"}, replaceVals = {"Remarks", "100"})
    private String remarks;

    public ChklstItemAnswerDto(String configId, String sectionId, String itemId, String answer, String remarks) {
        this.configId = configId;
        this.sectionId = sectionId;
        this.itemId = itemId;
        this.answer = answer;
        this.remarks = remarks;
    }

}

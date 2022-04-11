package sg.gov.moh.iais.egp.bsb.dto.chklst;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChklstItemAnswerDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String configId;
    private String sectionId;
    private String itemId;
    private String answer;
    private String remarks;
}

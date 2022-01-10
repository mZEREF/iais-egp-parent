package sg.gov.moh.iais.egp.bsb.dto.chklst;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChklstItemAnswerDto {
    private String sectionId;
    private String itemId;
    private String answer;
}

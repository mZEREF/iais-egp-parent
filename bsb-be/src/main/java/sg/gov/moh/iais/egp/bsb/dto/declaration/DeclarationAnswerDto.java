package sg.gov.moh.iais.egp.bsb.dto.declaration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeclarationAnswerDto {
    private String declarationId;

    private Map<String, String> answerMap;
}

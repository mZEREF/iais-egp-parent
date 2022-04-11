package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;

import java.util.List;
import java.util.Map;

@Data
public class OtherApplicationInfoDto {
    private String declarationId;

    private List<DeclarationItemMainInfo> declarationConfig;

    /* The key is item ID */
    private Map<String, String> answerMap;
}

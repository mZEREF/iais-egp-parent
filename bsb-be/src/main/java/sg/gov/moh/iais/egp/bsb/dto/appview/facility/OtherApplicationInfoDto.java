package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
public class OtherApplicationInfoDto implements Serializable {
    private String declarationId;

    @JsonIgnore
    private List<DeclarationItemMainInfo> declarationConfig;

    /* The key is item ID */
    private final Map<String, String> answerMap;
}

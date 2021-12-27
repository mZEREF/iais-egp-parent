package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import lombok.Data;
import net.sf.oval.constraint.MatchPattern;
import net.sf.oval.constraint.NotNull;


@Data
public class OtherAppInfoDto {
    private static final String TMP_ERR_MSG = "You must declare Yes";

    @NotNull(message = TMP_ERR_MSG)
    @MatchPattern(message = TMP_ERR_MSG, pattern = "^Y$")
    @CustomMsg(placeHolders ="field", replaceVals= "declare")
    private String facOpDeInformedResponsibilities;

    @NotNull(message = TMP_ERR_MSG)
    @MatchPattern(message = TMP_ERR_MSG, pattern = "^Y$")
    @CustomMsg(placeHolders ="field", replaceVals= "declare")
    private String facCommitteeInformedResponsibilities;

    @NotNull(message = TMP_ERR_MSG)
    @MatchPattern(message = TMP_ERR_MSG, pattern = "^Y$")
    @CustomMsg(placeHolders ="field", replaceVals= "declare")
    private String bioRiskManagementDeclare;

    @NotNull(message = TMP_ERR_MSG)
    @MatchPattern(message = TMP_ERR_MSG, pattern = "^Y$")
    @CustomMsg(placeHolders ="field", replaceVals= "declare")
    private String infoAuthenticatedDeclare;
}

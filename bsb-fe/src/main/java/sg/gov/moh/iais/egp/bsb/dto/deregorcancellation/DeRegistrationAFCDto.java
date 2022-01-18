package sg.gov.moh.iais.egp.bsb.dto.deregorcancellation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author : LiRan
 * @date : 2022/1/11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeRegistrationAFCDto {
    private String organisationName;
    private String organisationAddress;
    private String reasons;
    private String remarks;
    private String declaration;
}

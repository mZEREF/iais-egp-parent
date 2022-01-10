package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;


/** Need to add field for inspection date etc. info */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class InsInfoDto extends InsFacInfoDto implements Serializable {
    private String alterAdminName;
}


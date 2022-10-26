package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * Bean holds data in BATInfo page.
 */
@Data
public class BATInfoPageDto implements Serializable {
    private List<BATInfo> batInfos;
}

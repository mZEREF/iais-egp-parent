package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo;

import java.io.Serializable;
import java.util.List;


@Data
public class ApprovalToLargeDto implements Serializable {
    private List<BATInfo> batInfos;
}

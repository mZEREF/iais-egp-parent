package sg.gov.moh.iais.egp.bsb.dto.appview.deferrenew;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.List;

@Data
public class DeferRenewViewDto implements Serializable {
    private String facilityNo;
    private String facilityName;
    private String deferRenewDate;
    private String deferRenewReason;
    private List<DocRecordInfo> savedDocInfos;
}

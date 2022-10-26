package sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class RemarkHistoryItemDto implements Serializable {
    private String user;
    private String remark;
    private Date lastUpdate;
}

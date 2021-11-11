package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.Date;
import java.util.List;

/**
 * @author tangtang
 **/
@Data
public class InboxDataSubResultDto {
    private PageInfo pageInfo;
    private List<DataSubInfo> dataSubInfos;

    @Data
    public static class DataSubInfo {
        private String id;
        private String submissionNo;
        private String type;
        private String status;
        private String facilityName;
        private String facilityAddress;
        private Date submittedOn;
    }
}

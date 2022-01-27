package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;

/**
 * @author YiMing
 * @version 2022/1/25 10:53
 **/
@Data
public class InboxRepResultDto {
    private PageInfo pageInfo;
    private List<DataIncidentDto> dataIncidentDtoList;

    @Data
    public static class DataIncidentDto{
        private String incidentId;

        private String referenceNo;

        private String incidentType;

        private String facilityName;

        private String facAddress;

        private String incidentEntityDate;

        private String batName;

        private String applicationId;

        private String haveInvest;

        private String haveFollowup1A;

        private String haveFollowup1B;

    }
}

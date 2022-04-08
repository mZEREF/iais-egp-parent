package sg.gov.moh.iais.egp.bsb.dto.inbox;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class InboxDashboardDto {
    @JsonProperty("new_message_amount")
    private int newMsgAmt;

    @JsonProperty("application_draft_amount")
    private int draftAppAmt;

    @JsonProperty("active_facility_amount")
    private int activeFacilityAmt;

    @JsonProperty("active_approvals_amount")
    private int activeApprovalsAmt;
}

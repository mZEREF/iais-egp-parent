package sg.gov.moh.iais.egp.bsb.dto.renew.defer;

import lombok.Data;

import java.util.List;

@Data
public class DeferEditSelectDto {

    // Supporting Documents
    private boolean docRenew;

    //
    private boolean deferReason;

    //
    private boolean deferDate;

    //
    public List<String> selectedList;

}

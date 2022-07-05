package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ApprovalToActivityDto implements Serializable {
    private List<String> facActivityTypes;
}

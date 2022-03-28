package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class BatSpecialHandleInfo implements Serializable {
    private String projectName;

    private String principalInvestigatorName;
}

package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class BatSpWorkActivity implements Serializable {
    private String intendedWorkActivity;

    private String startDate;

    private String endDate;

    private String remarks;
}

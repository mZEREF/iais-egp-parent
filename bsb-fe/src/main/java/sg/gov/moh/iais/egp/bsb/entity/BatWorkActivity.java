package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class BatWorkActivity implements Serializable {
    private String workType;

    private String workTypeOth;
}

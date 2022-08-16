package sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RFBatInfo implements Serializable {
    private String schedule;
    private String nameOfToxin;
}

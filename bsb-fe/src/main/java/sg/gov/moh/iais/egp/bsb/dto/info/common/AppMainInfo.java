package sg.gov.moh.iais.egp.bsb.dto.info.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppMainInfo extends AppBasicInfo {
    @JMap("applicationDt")
    private Date date;

    @JMap
    private String assigned;

    @JMap
    private String facilityNo;
}

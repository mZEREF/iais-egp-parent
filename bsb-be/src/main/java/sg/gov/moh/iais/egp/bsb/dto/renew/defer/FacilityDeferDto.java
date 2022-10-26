package sg.gov.moh.iais.egp.bsb.dto.renew.defer;

import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@JGlobalMap(excluded = {"facilityId","facilityNo","facilityName"})
public class FacilityDeferDto implements Serializable {

    private String uen;

    private String userId;

    @JMap("id")
    private String facilityId;

    @JMap("facilityNo")
    private String facilityNo;

    @JMap("name")
    private String facilityName;

    private String deferDate;

    private String deferReason;

    private String check;

    private String createDt;
}

package sg.gov.moh.iais.egp.bsb.dto.info.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrgAddressInfo extends AddressInfo implements Serializable {
    private String uen;

    @JsonProperty("company_name")
    private String compName;
}

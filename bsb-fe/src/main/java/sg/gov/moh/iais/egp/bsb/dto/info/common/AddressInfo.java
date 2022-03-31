package sg.gov.moh.iais.egp.bsb.dto.info.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressInfo implements Serializable {
    @JsonProperty("postal_code")
    private String postalCode;

    @JsonProperty("address_type")
    private String addressType;

    @JsonProperty("block_no")
    private String blockNo;

    private String floor;

    @JsonProperty("unit_no")
    private String unitNo;

    private String street;

    private String building;
}

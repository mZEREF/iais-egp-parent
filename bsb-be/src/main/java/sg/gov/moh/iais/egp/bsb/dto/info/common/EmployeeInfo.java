package sg.gov.moh.iais.egp.bsb.dto.info.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeInfo implements Serializable {
    @JsonProperty("id")
    private String entityId;

    private String salutation;

    private String name;

    private String nationality;

    @JsonProperty("id_type")
    private String idType;

    @JsonProperty("id_number")
    private String idNumber;

    private String designation;

    @JsonProperty("contact_no")
    private String contactNo;

    private String email;

    @JsonProperty("employment_start_date")
    private String employmentStartDt;
}

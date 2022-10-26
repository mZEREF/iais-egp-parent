package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEntityDto implements Serializable {
    private Date createdAt;

    private String createdBy;

    private Date modifiedAt;

    private String modifiedBy;
}

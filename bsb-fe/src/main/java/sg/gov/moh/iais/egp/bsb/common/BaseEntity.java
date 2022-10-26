package sg.gov.moh.iais.egp.bsb.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable  {
    private Date createdAt;

    private String createdBy;

    private Date modifiedAt;

    private String modifiedBy;
}

package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityDoc extends BaseEntity {
    private String id;

    private Facility facility;

    private String docName;

    private Long docSize;

    private String fileRepoId;

    private Date submitDt;

    private String submitBy;

    private Application application;
}

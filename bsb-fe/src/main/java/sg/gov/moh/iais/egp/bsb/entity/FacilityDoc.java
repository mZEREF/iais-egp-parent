package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class FacilityDoc extends BaseEntity {
    private String id;

    private Facility facility;

    private String name;

    private long size;

    private String fileRepoId;

    private Date submitAt;

    private String submitBy;

    private Integer seqNum = -1;

    private String docType;
}

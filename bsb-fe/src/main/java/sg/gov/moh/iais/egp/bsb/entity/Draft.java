package sg.gov.moh.iais.egp.bsb.entity;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Draft extends BaseEntity {
    private String id;

    private Application application;

    private String draftAppNo;

    private String userId;

    private String draftData;

    private String status;
}
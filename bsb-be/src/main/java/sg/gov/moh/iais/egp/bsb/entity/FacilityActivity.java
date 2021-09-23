package sg.gov.moh.iais.egp.bsb.entity;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

import java.util.List;


@Data
public class FacilityActivity extends BaseEntity {
    private String id;

    private Facility facility;

    private Application application;

    private List<FacilitySchedule> facilitySchedules;

    private String activityType;
}

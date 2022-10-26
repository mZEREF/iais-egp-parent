package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

@Data
public class BATInfo implements Serializable {
    private String batEntityId;

    private String schedule;

    // javers compare id, must be unique and non-empty
    @Id
    private String batName;

    private List<String> sampleType;

    private List<String> workType;

    private String sampleWorkDetail;

    private String estimatedMaximumVolume;

    private String methodOrSystem;

    private ProcModeDetails details;

    @JsonIgnore
    private String sampleTypeStr;
    @JsonIgnore
    private String workTypeStr;

    public void setSampleType(List<String> sampleType) {
        this.sampleType = sampleType;
        if (!CollectionUtils.isEmpty(sampleType)) {
            this.sampleTypeStr = sampleType.toString();
        }
    }

    public void setWorkType(List<String> workType) {
        this.workType = workType;
        if (!CollectionUtils.isEmpty(workType)) {
            this.workTypeStr = workType.toString();
        }
    }
}

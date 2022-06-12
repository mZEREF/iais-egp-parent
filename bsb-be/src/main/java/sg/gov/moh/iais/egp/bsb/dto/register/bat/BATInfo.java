package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import lombok.Data;
import org.javers.core.metamodel.annotation.Id;

import java.io.Serializable;
import java.util.List;

@Data
public class BATInfo implements Serializable {
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

    private ProcAlreadyInPossessionInfo inPossessionInfo;
}

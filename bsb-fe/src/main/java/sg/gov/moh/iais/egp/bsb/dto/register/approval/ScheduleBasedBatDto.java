package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.info.bat.BatCodeInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class ScheduleBasedBatDto {
    Map<String, List<BatCodeInfo>> biologicalInfoMap;

    Set<String> facExistBatSet;
}

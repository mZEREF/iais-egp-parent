package sg.gov.moh.iais.egp.bsb.dto.rfi;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class RfiProcessDto implements Serializable {
    private List<String> sectionsAllowedToChange;
    private Map<String, Boolean> rfiSelectMap;
}

package sg.gov.moh.iais.egp.bsb.dto.rfi;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class RfiDisplayDto implements Serializable {
    private String id;
    private String rfiNo;
    private String appId;
    private List<ApplicationRfiIndicatorDto> applicationRfiIndicatorDtoList;
    private Map<String, String> specialRfiIndicatorMap;
}

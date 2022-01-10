package sg.gov.moh.iais.egp.bsb.dto.incident.entity;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.List;


/**
 * @author YiMing
 * @version 2022/1/4 17:23
 **/
@Data
public class InvestViewDto implements Serializable {
    private IncidentInfoInvestDto incidentInfoInvestDto;
    private IncidentInvestDto incidentInvestDto;
    private MedicalInvestDto medicalInvestDto;
    private List<DocRecordInfo> docRecordInfos;

}

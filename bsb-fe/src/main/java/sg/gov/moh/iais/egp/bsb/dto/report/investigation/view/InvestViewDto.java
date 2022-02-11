package sg.gov.moh.iais.egp.bsb.dto.report.investigation.view;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.List;


/**
 * @author YiMing
 * @version 2022/1/4 17:23
 **/
@Data
public class InvestViewDto {
    private IncidentInfoInvestDto incidentInfoInvestDto;
    private IncidentInvestDto incidentInvestDto;
    private MedicalInvestDto medicalInvestDto;
    private List<DocRecordInfo> docRecordInfos;

}

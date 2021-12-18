package sg.gov.moh.iais.egp.bsb.dto.report.investigation;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.util.Collection;

/**
 * @author YiMing
 * @version 2021/12/17 17:43
 **/
@Data
@Slf4j
public class InvestReportDto {
    private IncidentInfoDto incidentInfoDto;
    private IncidentInvestDto incidentInvestDto;
    private MedicalInvestDto medicalInvestDto;
    private Collection<DocRecordInfo> docRecordInfos;
    private AuditTrailDto auditTrailDto;
}

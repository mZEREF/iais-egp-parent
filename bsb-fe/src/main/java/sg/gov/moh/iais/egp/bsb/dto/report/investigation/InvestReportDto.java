package sg.gov.moh.iais.egp.bsb.dto.report.investigation;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto;

import java.util.Collection;

import static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.ReportableEventConstants.NODE_NAME_DOCUMENTS;

/**
 * @author YiMing
 * @version 2021/12/17 17:43
 **/
@Data
@Slf4j
public class InvestReportDto {
    private String processType;
    private IncidentInfoDto incidentInfoDto;
    private IncidentInvestDto incidentInvestDto;
    private MedicalInvestDto medicalInvestDto;
    private Collection<DocRecordInfo> docRecordInfos;
    private AuditTrailDto auditTrailDto;


    public static InvestReportDto from(NodeGroup investRepoRoot){
        InvestReportDto dto = new InvestReportDto();
        dto.setProcessType("investigation");
        dto.setIncidentInfoDto((IncidentInfoDto) ((SimpleNode) investRepoRoot.at( NODE_NAME_INCIDENT_INFO)).getValue());
        dto.setIncidentInvestDto((IncidentInvestDto) ((SimpleNode) investRepoRoot.at(NODE_NAME_INCIDENT_INVESTIGATION)).getValue());
        dto.setMedicalInvestDto((MedicalInvestDto) ((SimpleNode) investRepoRoot.at(NODE_NAME_MEDICAL_INVESTIGATION)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) investRepoRoot.at(NODE_NAME_DOCUMENTS)).getValue();
        dto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
        return dto;
    }
}

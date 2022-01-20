package sg.gov.moh.iais.egp.bsb.dto.report.investigation;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import sg.gov.moh.iais.egp.bsb.common.node.simple.SimpleNode;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.report.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

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
    private ReferNoSelectionDto referNoSelectionDto;
    private IncidentInfoDto incidentInfoDto;
    private IncidentInvestDto incidentInvestDto;
    private MedicalInvestDto medicalInvestDto;
    private Collection<DocRecordInfo> docRecordInfos;
    private AuditTrailDto auditTrailDto;


    public static InvestReportDto from(NodeGroup investRepoRoot){
        InvestReportDto dto = new InvestReportDto();
        dto.setProcessType("investigation");
        dto.setReferNoSelectionDto((ReferNoSelectionDto) ((SimpleNode) investRepoRoot.at(NODE_NAME_REFERENCE_NO_SELECTION)).getValue());
        dto.setIncidentInfoDto((IncidentInfoDto) ((SimpleNode) investRepoRoot.at( NODE_NAME_INCIDENT_INFO)).getValue());
        dto.setIncidentInvestDto((IncidentInvestDto) ((SimpleNode) investRepoRoot.at(NODE_NAME_INCIDENT_INVESTIGATION)).getValue());
        dto.setMedicalInvestDto((MedicalInvestDto) ((SimpleNode) investRepoRoot.at(NODE_NAME_MEDICAL_INVESTIGATION)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) investRepoRoot.at(NODE_NAME_DOCUMENTS)).getValue();
        dto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
        return dto;
    }
    public NodeGroup toInvestReportDto(String name){
        //generate new NodeGroup
        SimpleNode referNoSelectionNode = new SimpleNode(referNoSelectionDto,NODE_NAME_REFERENCE_NO_SELECTION,new Node[0]);
        SimpleNode incidentNode = new SimpleNode(incidentInfoDto, NODE_NAME_INCIDENT_INFO, new Node[]{referNoSelectionNode});
        SimpleNode incidentInvestNode = new SimpleNode(incidentInvestDto, NODE_NAME_INCIDENT_INVESTIGATION,new Node[0]);
        SimpleNode medicalInvestNode = new SimpleNode(medicalInvestDto, NODE_NAME_MEDICAL_INVESTIGATION, new Node[0]);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
        SimpleNode documentNode = new SimpleNode(primaryDocDto, NODE_NAME_DOCUMENTS, new Node[0]);
        Node previewSubmitNode = new Node(NODE_NAME_PREVIEW_SUBMIT, new Node[]{incidentNode,incidentInvestNode,medicalInvestNode,documentNode});

        return new NodeGroup.Builder().name(name)
                .addNode(referNoSelectionNode)
                .addNode(incidentNode)
                .addNode(incidentInvestNode)
                .addNode(medicalInvestNode)
                .addNode(documentNode)
                .addNode(previewSubmitNode)
                .build();

    }

}

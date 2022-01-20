package sg.gov.moh.iais.egp.bsb.dto.report.notification;

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

/**
 * @author YiMing
 * @version 2021/12/6 17:26
 **/

@Data
@Slf4j
public class IncidentNotificationDto {
    private String processType;
    private IncidentInfoDto incidentInfoDto;
    private PersonInvolvedInfoDto involvedInfoDto;
    private PersonReportingDto reportingDto;
    private Collection<DocRecordInfo> docRecordInfos;
    private AuditTrailDto auditTrailDto;

        public static IncidentNotificationDto from(NodeGroup incidentNotRoot){
        IncidentNotificationDto dto = new IncidentNotificationDto();
        dto.setProcessType("notification");
        dto.setIncidentInfoDto((IncidentInfoDto) ((SimpleNode) incidentNotRoot.at( NODE_NAME_INCIDENT_INFO)).getValue());
        dto.setReportingDto((PersonReportingDto) ((SimpleNode) incidentNotRoot.at(NODE_NAME_PERSON_REPORTING_INFO)).getValue());
        dto.setInvolvedInfoDto((PersonInvolvedInfoDto) ((SimpleNode) incidentNotRoot.at(NODE_NAME_PERSON_INVOLVED_INFO)).getValue());
        PrimaryDocDto primaryDocDto = (PrimaryDocDto) ((SimpleNode) incidentNotRoot.at(NODE_NAME_DOCUMENTS)).getValue();
        dto.setDocRecordInfos(primaryDocDto.getSavedDocMap().values());
        return dto;
    }

    public NodeGroup toIncidentNotificationDto(String name){
        //generate new NodeGroup
        SimpleNode incidentNode = new SimpleNode(incidentInfoDto, NODE_NAME_INCIDENT_INFO, new Node[0]);
        SimpleNode reportingPersonNode = new SimpleNode(reportingDto, NODE_NAME_PERSON_REPORTING_INFO, new Node[0]);
        SimpleNode involvedPersonNode = new SimpleNode(involvedInfoDto, NODE_NAME_PERSON_INVOLVED_INFO, new Node[0]);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(docRecordInfos, DocRecordInfo::getRepoId));
        SimpleNode documentNode = new SimpleNode(primaryDocDto, NODE_NAME_DOCUMENTS, new Node[0]);
        Node previewSubmitNode = new Node(NODE_NAME_PREVIEW_SUBMIT, new Node[]{incidentNode,reportingPersonNode,involvedPersonNode,documentNode});

        return new NodeGroup.Builder().name(name)
                .addNode(incidentNode)
                .addNode(reportingPersonNode)
                .addNode(involvedPersonNode)
                .addNode(documentNode)
                .addNode(previewSubmitNode)
                .build();
    }
}

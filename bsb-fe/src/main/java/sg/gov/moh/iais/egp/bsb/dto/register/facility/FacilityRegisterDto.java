package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.common.node.NodeGroup;
import java.util.Map;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.*;


@Data
@NoArgsConstructor
public class FacilityRegisterDto {
    private FacilitySelectionDto facilitySelectionDto;
    private FacilityProfileDto facilityProfileDto;
    private FacilityOperatorDto facilityOperatorDto;
    private FacilityAuthoriserDto facilityAuthoriserDto;
    private FacilityAdministratorDto facilityAdministratorDto;
    private FacilityOfficerDto facilityOfficerDto;
    private FacilityCommitteeDto facilityCommitteeDto;
    private Map<String, BiologicalAgentToxinDto> biologicalAgentToxinMap;
    private PrimaryDocDto primaryDocDto;
    private PreviewSubmitDto previewSubmitDto;


    public static FacilityRegisterDto from(NodeGroup facRegRoot) {
        FacilityRegisterDto dto = new FacilityRegisterDto();
        dto.setFacilitySelectionDto((FacilitySelectionDto) facRegRoot.at(NODE_NAME_FAC_SELECTION));
        dto.setFacilityProfileDto((FacilityProfileDto) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_PROFILE));
        dto.setFacilityOperatorDto((FacilityOperatorDto) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OPERATOR));
        dto.setFacilityAuthoriserDto((FacilityAuthoriserDto) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_AUTH));
        dto.setFacilityAdministratorDto((FacilityAdministratorDto) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_ADMIN));
        dto.setFacilityOfficerDto((FacilityOfficerDto) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_OFFICER));
        dto.setFacilityCommitteeDto((FacilityCommitteeDto) facRegRoot.at(NODE_NAME_FAC_INFO + facRegRoot.getPathSeparator() + NODE_NAME_FAC_COMMITTEE));
        dto.setPrimaryDocDto((PrimaryDocDto) facRegRoot.at(NODE_NAME_PRIMARY_DOC));
        dto.setPreviewSubmitDto((PreviewSubmitDto) facRegRoot.at(NODE_NAME_PREVIEW_SUBMIT));

        NodeGroup batGroup = (NodeGroup) facRegRoot.at(NODE_NAME_FAC_BAT_INFO);
        Map<String, BiologicalAgentToxinDto> batInfoMap = getBatInfoMap(batGroup);
        dto.setBiologicalAgentToxinMap(batInfoMap);
        return dto;
    }

    public static Map<String, BiologicalAgentToxinDto> getBatInfoMap(NodeGroup batNodeGroup) {
        Assert.notNull(batNodeGroup, "Biological Agent/Toxin node group must not be null!");
        Map<String, BiologicalAgentToxinDto> batMap = Maps.newLinkedHashMapWithExpectedSize(batNodeGroup.count());
        for (Node node : batNodeGroup.getAllNodes()) {
            assert node instanceof BiologicalAgentToxinDto;
            batMap.put(node.getName(), (BiologicalAgentToxinDto) node);
        }
        return batMap;
    }
}

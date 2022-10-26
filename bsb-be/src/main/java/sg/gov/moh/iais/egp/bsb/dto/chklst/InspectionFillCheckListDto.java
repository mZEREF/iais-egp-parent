package sg.gov.moh.iais.egp.bsb.dto.chklst;


import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * InspectionFillCheckListDto
 *
 * @author junyu
 * @date 2022/4/29
 */
@Data
public class InspectionFillCheckListDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<InspectionCheckQuestionDto> checkList;
    private String bestPractice;
    private String tuc;
    private String tcuRemark;
    private String config;
    private List<SectionDto> sectionDtoList;
    private String svcName;
    private String svcCode;
    private String configId;
    private String subName;
    private String subType;
    private String svcNameShow;
    private boolean moreOneDraft;
    private String preCheckId;
    private Map<String, InspectionCheckQuestionDto> stringInspectionCheckQuestionDtoMap;
    private List<String> otherInspectionOfficer;
    private boolean commonConfig;
    private Map<String, String> draftRemarkMaps;
    private String vehicleName;
}

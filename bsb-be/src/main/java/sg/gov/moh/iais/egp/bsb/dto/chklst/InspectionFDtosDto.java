package sg.gov.moh.iais.egp.bsb.dto.chklst;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * InspectionFDtosDto
 *
 * @author junyu
 * @date 2022/4/29
 */
@Data
public class InspectionFDtosDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<InspectionFillCheckListDto> fdtoList;
    private String bestPractice;
    private String tuc;
    private String tcuRemark;
    private String serviceName;
    private String checkListTab;
    private String serviceTab;
    private String inspectionDate;
    private String inspectionStartDate;
    private String inspectionEndDate;
    private String inspectionLeader;
    private List<String> inspectionofficer;
    private String otherinspectionofficer;
    private boolean tcuFlag;
    private int generalTotal;
    private int generalDo;
    private int generalNc;
    private int serviceTotal;
    private int serviceDo;
    private int serviceNc;
    private int adhocTotal;
    private int adhocDo;
    private int adhocNc;
    private int totalNcNum;
    private String startTime;
    private String endTime;
    private String startHour;
    private String endHour;
    private String startMin;
    private String endMin;
    private String oldFileGuid;
    private String remarksForHistory;
    private boolean specServiceVehicle;
    private String observation;
}

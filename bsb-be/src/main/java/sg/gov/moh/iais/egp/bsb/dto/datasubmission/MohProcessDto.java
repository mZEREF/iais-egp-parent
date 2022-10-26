package sg.gov.moh.iais.egp.bsb.dto.datasubmission;

import lombok.Data;

/**
 * @author tangtang
 * @date 2022/2/23 9:57
 */
@Data
public class MohProcessDto {
    private String applicationId;
    private String taskId;
    private String doRemarks;
    private String aoRemarks;
    private String doDecision;
    private String aoDecision;

    public static MohProcessDto setDoProcessData(String applicationId, String taskId, String doRemarks,String doDecision){
        MohProcessDto processDto = new MohProcessDto();
        processDto.setApplicationId(applicationId);
        processDto.setTaskId(taskId);
        processDto.setDoRemarks(doRemarks);
        processDto.setDoDecision(doDecision);
        return processDto;
    }

    public static MohProcessDto setAoProcessData(String applicationId, String taskId, String aoRemarks,String aoDecision){
        MohProcessDto processDto = new MohProcessDto();
        processDto.setApplicationId(applicationId);
        processDto.setTaskId(taskId);
        processDto.setAoRemarks(aoRemarks);
        processDto.setAoDecision(aoDecision);
        return processDto;
    }
}

package sg.gov.moh.iais.egp.bsb.dto.withdrawn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author tangtang
 * @date 2021/12/10 10:41
 */
@Data
public class AppSubmitWithdrawnDto implements Serializable {
    private String appId;
    private String appNo;
    private String appType;
    private String processType;
    private String currentStatus;
    private String reason;
    private String remarks;

    @JsonIgnore
    private String from;

    @JsonIgnore
    private PrimaryDocDto primaryDocDto;

    private List<PrimaryDocDto.DocRecordInfo> savedInfos;

    private List<DocMeta> docMetas;

    @JsonIgnore
    private List<PrimaryDocDto.NewDocInfo> newDocInfos;
    @JsonIgnore
    private String docType;
    @JsonIgnore
    private String repoIdNewString;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    // validate
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("withdrawnFeignClient", "validateWithdrawnDto", new Object[]{this});
        return validationResultDto.isPass();
    }

    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    public void clearValidationResult() {
        this.validationResultDto = null;
    }
}

package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"dataSubmissionId"})
public class DataSubmissionDocDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${dataSubmission.id}")
    private String dataSubmissionId;

    private String docName;

    private Long docSize;

    private String fileRepoId;

    private Date submitDt;

    private String submitBy;

    private String docType;
}

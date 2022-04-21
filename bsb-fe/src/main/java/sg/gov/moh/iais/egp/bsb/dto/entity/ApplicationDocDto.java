package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * ApplicationDocDto
 *
 * @author junyu
 * @date 2022/4/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class ApplicationDocDto extends BaseEntityDto {
    private String id;

    private ApplicationDto application;

    private String docName;

    private Long docSize;

    private String fileRepoId;

    private Date submitDt;

    private String submitBy;

    private String docType;

    private String docSubType;

    private String status;


}

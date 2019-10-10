package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * AppGrpPrimaryDocDto
 *
 * @author suocheng
 * @date 10/10/2019
 */
@Getter
@Setter
public class AppGrpPrimaryDocDto implements Serializable {
    private static final long serialVersionUID = 3118782375153598883L;

    private String id;

    private String docTitle;

    private String docName;

    private String fileRepoGuid;

    private String appGrpId;

    private MultipartFile file;

    private AuditTrailDto auditTrailDto;
}

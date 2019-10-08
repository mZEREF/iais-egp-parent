package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * AppGrpPremisesDocDto
 *
 * @author suocheng
 * @date 9/30/2019
 */
@Getter
@Setter
public class AppGrpPremisesDocDto implements Serializable {
    private static final long serialVersionUID = 4268061744592222283L;
    private Integer id;

    private String rowguid;
    private String fileName;
    private MultipartFile file;


}

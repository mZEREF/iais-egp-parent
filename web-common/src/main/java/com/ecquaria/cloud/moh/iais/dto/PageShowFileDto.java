package com.ecquaria.cloud.moh.iais.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Wenkang
 * @date 2021/3/11 15:05
 */
@Data
public class PageShowFileDto implements Serializable {
    private static final long serialVersionUID = -112871109805321086L;
    private String fileUploadUrl;
    private String fileName;
    private String fileMapId;
    private String index;
    private Integer size;
    private String md5Code;
    private Integer version=1;
}

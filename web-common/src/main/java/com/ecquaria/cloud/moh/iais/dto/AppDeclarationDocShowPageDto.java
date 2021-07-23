package com.ecquaria.cloud.moh.iais.dto;

import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
public class AppDeclarationDocShowPageDto implements Serializable {


    private static final long serialVersionUID = 7257508164328788691L;
    private int fileMaxIndex;
    private Map<String,File> pageShowFileMap;
    private List<PageShowFileDto> pageShowFileDtos;
    private Map<String, PageShowFileDto> pageShowFileHashMap;
}

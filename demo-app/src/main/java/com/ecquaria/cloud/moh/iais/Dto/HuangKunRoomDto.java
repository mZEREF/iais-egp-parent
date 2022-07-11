package com.ecquaria.cloud.moh.iais.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: HuangKunRoomDto
 * @author: haungkun
 * @date: 2022/7/8 16:44
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class HuangKunRoomDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String roomType;

    private String roomNo;

}

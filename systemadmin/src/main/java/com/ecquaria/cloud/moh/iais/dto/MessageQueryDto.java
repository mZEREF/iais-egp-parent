package com.ecquaria.cloud.moh.iais.dto;

/*
 *author: yichen
 *date time:2019/8/26 10:42
 *description:
 */

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class MessageQueryDto implements Serializable {

    private static final long serialVersionUID = 4072618238107110105L;

    private Integer id;

    private String rowguid;

    private String domainType;

    private String msgType;

    private String module;

    private String description;

    private Character status;

    private String codeKey;

    private String message;
}

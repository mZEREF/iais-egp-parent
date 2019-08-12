package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class MasterCodeQuery {

    @Id
    @Getter
    @Setter
    @Column(name = "master_code_id")
    private Long masterCodeId;

    @Getter
    @Setter
    @Column(name = "rowguid")
    private String rowguid;

    @Getter
    @Setter
    @Column(name = "master_code_key")
    private String masterCodeKey;

    @Getter
    @Setter
    @Column(name = "code_category")
    private int  codeCategory;

    @Getter
    @Setter
    @Column(name = "code_value")
    private String codeValue;

    @Getter
    @Setter
    @Column(name = "code_description")
    private String codeDescription;

    @Getter
    @Setter
    @Column(name = "status")
    private int status;
}

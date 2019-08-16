package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;


@Entity
public class MasterCodeQuery implements Serializable {

    private static final long serialVersionUID = -8560512906902335303L;

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

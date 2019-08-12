package com.ecquaria.cloud.moh.iais.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Component
@Data
@Table(name = "cm_master_code")
public class MasterCode implements Serializable {
    private static final long serialVersionUID = 4934028881970802955L;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "master_code_id")
    private Long id;

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
    @Column(name = "filter_value")
    private String filterValue;

    @Getter
    @Setter
    @Column(name = "sequence")
    private float sequence;

    @Getter
    @Setter
    @Column(name = "remarks")
    private String remarks;

    @Getter
    @Setter
    @Column(name = "status")
    private int status;

    @Getter
    @Setter
    @Column(name = "effective_from")
    private Date effectiveFrom;

    @Getter
    @Setter
    @Column(name = "effective_to")
    private Date effectiveTo;

    @Getter
    @Setter
    @Column(name = "version")
    private int version;
}

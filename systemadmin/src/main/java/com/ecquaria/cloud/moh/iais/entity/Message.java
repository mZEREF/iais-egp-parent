package com.ecquaria.cloud.moh.iais.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CM_MESSAGE")
public class Message implements Serializable {
    @Getter
    @Setter
    @Id
    @Column(name = "msg_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rowguid")
    private String rowguid;

    @Getter
    @Setter
    @Column(name = "code_key")
    private String codeKey;

    @Getter
    @Setter
    @Column(name = "domain_type")
    private String domainType;

    @Getter
    @Setter
    @Column(name = "msg_type")
    private String msgType;

    @Getter
    @Setter
    @Column(name = "module")
    private String module;

    @Getter
    @Setter
    @Column(name = "description")
    private String description;

    @Getter
    @Setter
    @Column(name = "status")
    private Integer status;
}

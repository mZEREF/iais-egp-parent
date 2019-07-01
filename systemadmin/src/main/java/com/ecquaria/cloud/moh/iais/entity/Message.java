package com.ecquaria.cloud.moh.iais.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ERRORMESSAGE")
public class Message {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer codeId;


    @Setter
    @Getter
    @Column(name="CODE_KEY")
    private String codeKey;

    @Setter
    @Getter
    @Column(name="TYPE")
    private String type;

    @Setter
    @Getter
    @Column(name = "MESSAGE_TYPE")
    private String message_type;

    @Setter
    @Getter
    @Column(name="MODULE")
    private String module;

    @Setter
    @Getter
    @Column(name="DESCRIPTION")
    private String description;

    @Setter
    @Getter
    @Column(name="STATUS")
    private String status;
}

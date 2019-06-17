package com.ecquaria.cloud.moh.iais.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "SINGPOST_ADDRESS")
public class PostCode implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name="ID")
    @Getter @Setter private String id;
    @Column(name="POSTAL_CODE")
    @Getter @Setter private String postalCode;
    @Column(name="ADDRESS_TYPE")
    @Getter @Setter private String addressType;
    @Column(name="BLK_HSE_NO")
    @Getter @Setter private String blkHseNo;
    @Column(name="STREET_NAME")
    @Getter @Setter private String streetName;
    @Column(name="BUILDING_NAME")
    @Getter @Setter private String buildingName;

}

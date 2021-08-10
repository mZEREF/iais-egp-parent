package sg.gov.moh.iais.bsb.dto.biosafetyEnquiry;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/6 13:44
 * DESCRIPTION: TODO
 **/


@Getter
@Setter
@ToString
@Entity
@Table(name = "biological")
public class BiologicalDto extends BaseEntity{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", columnDefinition="uniqueidentifier")
    private String id;
    @Column(name = "BIOLOGICAL_TYPE")
    private String biologicalType;
    @Column(name = "NAME")
    private String name;
    @Column(name = "RISK_LEVEL")
    private String riskLevel;
    @Column(name = "SCHEDULE")
    private String schedule;
}

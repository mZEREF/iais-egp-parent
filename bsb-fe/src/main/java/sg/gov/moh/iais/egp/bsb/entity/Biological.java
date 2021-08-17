package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;


@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Biological extends BaseEntity {
    private String id;

    private String biologicalType;

    private String name;

    private String riskLevel;

    private String schedule;
}

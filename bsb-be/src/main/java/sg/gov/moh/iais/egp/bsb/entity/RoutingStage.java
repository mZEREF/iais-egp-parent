package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RoutingStage extends BaseEntity {
    private String id;

    private String code;

    private String name;

    private String description;

    private String status;

    private Integer order;

    private Integer level;
}

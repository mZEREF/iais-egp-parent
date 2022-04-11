package sg.gov.moh.iais.egp.bsb.dto.declaration;

import lombok.Data;

import java.io.Serializable;


@Data
public class DeclarationItemMainInfo implements Serializable {
    private String id;

    private String statement;

    private String constraint;
}

package sg.gov.moh.iais.egp.bsb.dto.declaration;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class DeclarationConfigInfo implements Serializable {
    private String id;

    private List<DeclarationItemMainInfo> config;
}

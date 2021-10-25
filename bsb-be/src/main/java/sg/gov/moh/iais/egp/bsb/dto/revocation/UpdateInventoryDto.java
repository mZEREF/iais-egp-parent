package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Zhu Tangtang
 * @date 2021/7/26 17:25
 */
@Data
public class UpdateInventoryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String biologicalAgentsOrToxins;
    private String initialState;
    private String currentState;
    private String initialQuantity;
    private BigDecimal qtyToChange;
}

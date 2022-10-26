package sg.gov.moh.iais.egp.common.modal.view;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class RichSelectOption extends SelectOption {
    private boolean disable;

    public RichSelectOption(String value, String text, boolean disable) {
        super(value, text);
        this.disable = disable;
    }
}

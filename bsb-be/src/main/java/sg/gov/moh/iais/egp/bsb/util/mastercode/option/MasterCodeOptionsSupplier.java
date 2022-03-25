package sg.gov.moh.iais.egp.bsb.util.mastercode.option;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;

import java.util.List;

public interface MasterCodeOptionsSupplier {
    List<SelectOption> allOptions();

    List<SelectOption> customOptions(String... codes);
}

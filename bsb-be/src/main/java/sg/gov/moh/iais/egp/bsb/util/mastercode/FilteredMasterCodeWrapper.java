package sg.gov.moh.iais.egp.bsb.util.mastercode;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import sg.gov.moh.iais.egp.bsb.util.mastercode.option.MasterCodeOptionsSupplier;

import java.util.ArrayList;
import java.util.List;

public class FilteredMasterCodeWrapper implements MasterCodeOptionsSupplier {
    private final String filteredValue;

    public FilteredMasterCodeWrapper(String filteredValue) {
        if (filteredValue == null || "".equals(filteredValue)) {
            throw new IllegalArgumentException("Filtered value should not be empty");
        }
        this.filteredValue = filteredValue;
    }

    public String getFilteredValue() {
        return filteredValue;
    }





    @Override
    public List<SelectOption> allOptions() {
        List<MasterCodeView> dataList = MasterCodeUtil.retrieveByFilter(this.filteredValue);
        List<SelectOption> options = new ArrayList<>(dataList.size());
        for (MasterCodeView data : dataList) {
            SelectOption option = new SelectOption(data.getCode(), data.getCodeValue());
            options.add(option);
        }
        return options;
    }

    @Override
    public List<SelectOption> customOptions(String... codes) {
        throw new UnsupportedOperationException();
    }

}

package sg.gov.moh.iais.egp.bsb.util.mastercode;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import sg.gov.moh.iais.egp.bsb.util.mastercode.option.MasterCodeOptionsSupplier;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeListRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeMapRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.validation.MasterCodeValidator;

import java.util.ArrayList;
import java.util.List;

public class FilteredMasterCodeWrapper implements MasterCodeOptionsSupplier, MasterCodeValidator {
    private final String filteredValue;
    private MasterCodeRetriever retriever;

    public FilteredMasterCodeWrapper(String filteredValue) {
        if (filteredValue == null || "".equals(filteredValue)) {
            throw new IllegalArgumentException("Filtered value should not be empty");
        }
        this.filteredValue = filteredValue;
    }

    public String getFilteredValue() {
        return filteredValue;
    }

    private boolean notLoaded() {
        return this.retriever == null;
    }

    /** Load data of the master code category */
    public void load() {
        List<MasterCodeView> dataList = MasterCodeUtil.retrieveByFilter(filteredValue);
        if (dataList.size() > 4) {
            this.retriever = new MasterCodeMapRetriever(dataList);
        } else {
            this.retriever = new MasterCodeListRetriever(dataList);
        }
    }

    /** Check if already loaded first, if not, load data */
    public void tryLoad() {
        if (notLoaded()) {
            load();
        }
    }



    @Override
    public List<SelectOption> allOptions() {
        tryLoad();
        List<MasterCodeView> dataList = retriever.retrieveAll();
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

    @Override
    public boolean validate(String code) {
        if (code == null) {
            return false;
        }
        tryLoad();
        return retriever.retrieveByCode(code) != null;
    }
}

package sg.gov.moh.iais.egp.bsb.util.mastercode;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import sg.gov.moh.iais.egp.bsb.util.mastercode.convert.DefaultMasterCodeConverter;
import sg.gov.moh.iais.egp.bsb.util.mastercode.convert.MasterCodeConverter;
import sg.gov.moh.iais.egp.bsb.util.mastercode.option.MasterCodeOptionsSupplier;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeListRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeMapRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.validation.DefaultMasterCodeValidator;
import sg.gov.moh.iais.egp.bsb.util.mastercode.validation.MasterCodeValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MasterCodeWrapper implements MasterCodeOptionsSupplier {
    private final String categoryId;

    public MasterCodeWrapper(String categoryId) {
        if (categoryId == null || "".equals(categoryId)) {
            throw new IllegalArgumentException("Category ID should not be empty");
        }
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }


    /** Return a retriever to retrieve detail info of master code */
    public MasterCodeRetriever retriever() {
        MasterCodeRetriever newRetriever;
        List<MasterCodeView> dataList = MasterCodeUtil.retrieveByCategory(this.categoryId);
        if (dataList.size() > 4) {
            newRetriever = new MasterCodeMapRetriever(dataList);
        } else {
            newRetriever = new MasterCodeListRetriever(dataList);
        }
        return newRetriever;
    }

    /** Return a converter to convert between code, value and description */
    public MasterCodeConverter converter() {
        return new DefaultMasterCodeConverter(this.categoryId);
    }


    @Override
    public List<SelectOption> allOptions() {
        List<MasterCodeView> dataList = MasterCodeUtil.retrieveByCategory(this.categoryId);
        List<SelectOption> options = new ArrayList<>(dataList.size());
        for (MasterCodeView data : dataList) {
            SelectOption option = new SelectOption(data.getCode(), data.getCodeValue());
            options.add(option);
        }
        return options;
    }

    @Override
    public List<SelectOption> customOptions(String... codes) {
        if (codes == null) {
            return Collections.emptyList();
        }
        List<MasterCodeView> dataList = MasterCodeUtil.retrieveByCategory(this.categoryId);
        MasterCodeRetriever newRetriever = new MasterCodeMapRetriever(dataList);
        List<SelectOption> options = new ArrayList<>(codes.length);
        for (String code : codes) {
            MasterCodeView data = newRetriever.retrieveByCode(code);
            SelectOption option = new SelectOption(data.getCode(), data.getCodeValue());
            options.add(option);
        }
        return options;
    }

    public MasterCodeValidator validator() {
        return new DefaultMasterCodeValidator(this.categoryId);
    }
}

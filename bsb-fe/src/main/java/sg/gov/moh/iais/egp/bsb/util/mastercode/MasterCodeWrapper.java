package sg.gov.moh.iais.egp.bsb.util.mastercode;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import sg.gov.moh.iais.egp.bsb.util.mastercode.convert.MasterCodeConverter;
import sg.gov.moh.iais.egp.bsb.util.mastercode.option.MasterCodeOptionsSupplier;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeListRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeMapRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeRetriever;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MasterCodeWrapper implements MasterCodeRetriever, MasterCodeConverter, MasterCodeOptionsSupplier {
    private final String categoryId;
    private MasterCodeRetriever retriever;
    public static final String DEFAULT_UNKNOWN = "Unknown";

    public MasterCodeWrapper(String categoryId) {
        if (categoryId == null || "".equals(categoryId)) {
            throw new IllegalArgumentException("Category ID should not be empty");
        }
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    private boolean notLoaded() {
        return this.retriever == null;
    }

    /** Load data of the master code category */
    public void load() {
        List<MasterCodeView> dataList = MasterCodeUtil.retrieveByCategory(categoryId);
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
    public List<MasterCodeView> retrieveAll() {
        tryLoad();
        return retriever.retrieveAll();
    }

    @Override
    public MasterCodeView retrieveByCode(String code) {
        tryLoad();
        return retriever.retrieveByCode(code);
    }

    @Override
    public MasterCodeView retrieveByValue(String value) {
        tryLoad();
        return retriever.retrieveByValue(value);
    }

    @Override
    public MasterCodeView retrieveByDesc(String desc) {
        tryLoad();
        return retriever.retrieveByDesc(desc);
    }

    @Override
    public String code2Value(String code) {
        if (code == null) {
            return null;
        }
        tryLoad();
        MasterCodeView data = retriever.retrieveByCode(code);
        return data == null ? DEFAULT_UNKNOWN : data.getCodeValue();
    }

    @Override
    public String value2Code(String value) {
        if (value == null) {
            return null;
        }
        tryLoad();
        MasterCodeView data = retriever.retrieveByValue(value);
        return data == null ? DEFAULT_UNKNOWN : data.getCode();
    }

    @Override
    public String code2Desc(String desc) {
        if (desc == null) {
            return null;
        }
        tryLoad();
        MasterCodeView data = retriever.retrieveByDesc(desc);
        return data == null ? DEFAULT_UNKNOWN : data.getDescription();
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
        if (codes == null) {
            return Collections.emptyList();
        }
        tryLoad();
        List<SelectOption> options = new ArrayList<>(codes.length);
        for (String code : codes) {
            MasterCodeView data = retriever.retrieveByCode(code);
            SelectOption option = new SelectOption(data.getCode(), data.getCodeValue());
            options.add(option);
        }
        return options;
    }
}

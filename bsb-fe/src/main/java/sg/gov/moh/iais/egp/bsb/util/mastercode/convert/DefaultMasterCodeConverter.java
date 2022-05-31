package sg.gov.moh.iais.egp.bsb.util.mastercode.convert;

import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeMapRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeRetriever;

import java.util.List;


public class DefaultMasterCodeConverter implements MasterCodeConverter {
    private final MasterCodeRetriever retriever;
    public static final String DEFAULT_UNKNOWN = "Unknown";

    public DefaultMasterCodeConverter(String categoryId) {
        if (categoryId == null || "".equals(categoryId)) {
            throw new IllegalArgumentException("Category ID should not be empty");
        }
        List<MasterCodeView> dataList = MasterCodeUtil.retrieveByCategory(categoryId);
        this.retriever = new MasterCodeMapRetriever(dataList);
    }

    public DefaultMasterCodeConverter(MasterCodeRetriever retriever) {
        if (retriever == null) {
            throw new IllegalArgumentException("Retriever should not be null");
        }
        this.retriever = retriever;
    }


    @Override
    public String code2Value(String code) {
        MasterCodeView data = retriever.retrieveByCode(code);
        return data == null ? DEFAULT_UNKNOWN : data.getCodeValue();
    }

    @Override
    public String value2Code(String value) {
        MasterCodeView data = retriever.retrieveByValue(value);
        return data == null ? DEFAULT_UNKNOWN : data.getCode();
    }

    @Override
    public String code2Desc(String code) {
        MasterCodeView data = retriever.retrieveByCode(code);
        return data == null ? DEFAULT_UNKNOWN : data.getDescription();
    }

    @Override
    public String desc2Code(String desc) {
        MasterCodeView data = retriever.retrieveByDesc(desc);
        return data == null ? DEFAULT_UNKNOWN : data.getCode();
    }
}

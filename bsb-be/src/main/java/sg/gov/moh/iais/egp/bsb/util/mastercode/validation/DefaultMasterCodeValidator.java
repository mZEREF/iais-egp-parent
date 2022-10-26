package sg.gov.moh.iais.egp.bsb.util.mastercode.validation;


import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeMapRetriever;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeRetriever;

import java.util.List;


public class DefaultMasterCodeValidator implements MasterCodeValidator {
    private final MasterCodeRetriever retriever;

    public DefaultMasterCodeValidator(String categoryId) {
        if (categoryId == null || "".equals(categoryId)) {
            throw new IllegalArgumentException("Category ID should not be empty");
        }
        List<MasterCodeView> dataList = MasterCodeUtil.retrieveByCategory(categoryId);
        this.retriever = new MasterCodeMapRetriever(dataList);
    }

    public DefaultMasterCodeValidator(MasterCodeRetriever retriever) {
        if (retriever == null) {
            throw new IllegalArgumentException("Retriever should not be null");
        }
        this.retriever = retriever;
    }

    @Override
    public boolean validate(String code) {
        return retriever.retrieveByCode(code) != null;
    }
}

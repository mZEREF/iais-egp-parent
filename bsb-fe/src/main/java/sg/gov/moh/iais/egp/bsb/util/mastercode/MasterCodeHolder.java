package sg.gov.moh.iais.egp.bsb.util.mastercode;

import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;

public class MasterCodeHolder {
    private MasterCodeHolder() {}

    public static final MasterCodeWrapper ID_TYPE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_ID_TYPE);
    public static final MasterCodeWrapper NATIONALITY = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_NATIONALITY);
}

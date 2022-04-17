package sg.gov.moh.iais.egp.bsb.util.mastercode;

import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;

public class MasterCodeHolder {
    private MasterCodeHolder() {}

    public static final MasterCodeWrapper ID_TYPE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_ID_TYPE);

    public static final MasterCodeWrapper APPLICATION_TYPE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_APP_TYPE);

    public static final MasterCodeWrapper APPLICATION_STATUS = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_APP_STATUS);

    public static final MasterCodeWrapper FACILITY_CLASSIFICATION = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_FAC_CLASSIFICATION);

    public static final MasterCodeWrapper APPROVE_FACILITY_CERTIFIER = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_APPROVED_FACILITY_CERTIFIER_SELECTION);

    public static final MasterCodeWrapper PROCESS_TYPE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_PROCESS_TYPE);

    //facility status is same as approval status
    public static final MasterCodeWrapper APPROVAL_STATUS = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_APPROVAL_STATUS);

    public static final MasterCodeWrapper APPROVAL_TYPE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_APPROVAL_TYPE);
}

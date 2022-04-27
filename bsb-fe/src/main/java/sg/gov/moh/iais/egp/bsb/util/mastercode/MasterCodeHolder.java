package sg.gov.moh.iais.egp.bsb.util.mastercode;

import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;

public class MasterCodeHolder {
    private MasterCodeHolder() {}

    public static final MasterCodeWrapper MSG_TYPE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_MSG_TYPE);

    public static final MasterCodeWrapper ID_TYPE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_ID_TYPE);
    public static final MasterCodeWrapper NATIONALITY = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_NATIONALITY);
    public static final MasterCodeWrapper SALUTATION = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_SALUTATION);

    public static final MasterCodeWrapper ADDRESS_TYPE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_ADDRESS_TYPE);

    public static final MasterCodeWrapper FACILITY_TYPE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_FACILITY_TYPE);
    public static final MasterCodeWrapper APPROVED_FACILITY_CERTIFIER = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_APPROVED_FACILITY_CERTIFIER_SELECTION);

    public static final MasterCodeWrapper SCHEDULE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_SCHEDULE_TYPE);

    public static final MasterCodeWrapper THE_COMPANY_REGISTER = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_IS_THE_COMPANY_REGISTERED_IN_LOCAL_OR_OVERSEAS);
    public static final MasterCodeWrapper ROLE = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_ROLE);

    public static final MasterCodeWrapper FACILITY_STATUS = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_FACILITY_STATUS);
    public static final MasterCodeWrapper ROLE_IN_FACILITY = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_ROLE_IN_FACILITY);
    public static final MasterCodeWrapper APPROVAL_STATUS = new MasterCodeWrapper(MasterCodeUtil.CATE_ID_BSB_APPROVAL_STATUS);
}

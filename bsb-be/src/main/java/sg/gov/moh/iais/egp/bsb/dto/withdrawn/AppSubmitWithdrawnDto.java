package sg.gov.moh.iais.egp.bsb.dto.withdrawn;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author tangtang
 * @date 2021/12/10 10:41
 */
@Data
public class AppSubmitWithdrawnDto implements Serializable {
    //search info
    private String taskId;
    private String appId;
    private String appNo;
    private String appType;
    private String processType;
    private String currentStatus;
    private Date createDate;
    private String facClassification;
    //
    private String doRemarks;
    private String doDecision;
    private String aoRemarks;
    private String aoDecision;
    //
    private String loginUser;
    private String module;
    //
    private Collection<DocRecordInfo> docRecordInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    private static final String KEY_DO_REMARKS = "doRemarks";
    private static final String KEY_DO_DECISION = "doDecision";
    private static final String KEY_AO_REMARKS = "aoRemarks";
    private static final String KEY_AO_DECISION = "aoDecision";

    public void reqObjMapping(HttpServletRequest request) {
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        this.setLoginUser(loginContext.getUserName());
        this.setDoRemarks(ParamUtil.getString(request, KEY_DO_REMARKS));
        this.setDoDecision(ParamUtil.getString(request, KEY_DO_DECISION));
        this.setAoRemarks(ParamUtil.getString(request, KEY_AO_REMARKS));
        this.setAoDecision(ParamUtil.getString(request, KEY_AO_DECISION));
    }
}

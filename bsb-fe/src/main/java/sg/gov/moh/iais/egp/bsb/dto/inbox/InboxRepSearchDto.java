package sg.gov.moh.iais.egp.bsb.dto.inbox;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author YiMing
 * @version 2022/1/25 10:49
 **/
@Data
public class InboxRepSearchDto implements Serializable {
    private String referenceNo;
    private String facilityName;
    private String incidentType;
    private String incidentDate;


    private static final String KEY_REFERENCE_NO = "referenceNo";
    private static final String KEY_FACILITY_NAME = "facilityName";
    private static final String KEY_INCIDENT_TYPE = "incidentType";
    private static final String KEY_INCIDENT_DATE = "incidentDate";
    public void reqObjMapping(HttpServletRequest request){
        this.referenceNo = ParamUtil.getString(request,KEY_REFERENCE_NO);
        this.facilityName = ParamUtil.getString(request,KEY_FACILITY_NAME);
        this.incidentType = ParamUtil.getString(request,KEY_INCIDENT_TYPE);
        this.incidentDate = ParamUtil.getString(request,KEY_INCIDENT_DATE);
    }

    public void clear(){
        this.referenceNo = "";
        this.facilityName = "";
        this.incidentType = "";
        this.incidentDate = "";
    }
}

package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class BATInfo implements Serializable {
    @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.schedule")
    private String schedule;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.name")
    private String batName;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.sample.addOrDelete")
    private List<String> sampleType;

    private List<String> workType;

    private String sampleWorkDetail;


    private ProcModeDetails details;

    public BATInfo() {
        this.sampleType = new ArrayList<>();
        this.workType = new ArrayList<>();
    }

    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_PREFIX_SCHEDULE                           = "schedule";
    private static final String KEY_PREFIX_BAT_NAME                           = "batName";
    private static final String KEY_PREFIX_SAMPLE_TYPE                        = "sampleType";
    private static final String KEY_PREFIX_WORK_TYPE                          = "workType";
    private static final String KEY_PREFIX_SAMPLE_WORK_DETAIL                 = "sampleWorkDetail";
    public void reqObjMapping(HttpServletRequest request,String idx){
        this.setSchedule(ParamUtil.getString(request, KEY_PREFIX_SCHEDULE + SEPARATOR +idx));
        this.setBatName(ParamUtil.getString(request, KEY_PREFIX_BAT_NAME + SEPARATOR +idx));
        String[] sampleTypes = ParamUtil.getStrings(request, KEY_PREFIX_SAMPLE_TYPE + SEPARATOR +idx);
        if (sampleTypes != null && sampleTypes.length > 0) {
            this.setSampleType(new ArrayList<>(Arrays.asList(sampleTypes)));
        } else {
            this.setSampleType(new ArrayList<>(0));
        }
        String[] workTypes = ParamUtil.getStrings(request, KEY_PREFIX_WORK_TYPE + SEPARATOR +idx);
        if (workTypes != null && workTypes.length > 0) {
            this.setWorkType(new ArrayList<>(Arrays.asList(workTypes)));
        } else {
            this.setWorkType(new ArrayList<>(0));
        }
        this.setSampleWorkDetail(ParamUtil.getString(request, KEY_PREFIX_SAMPLE_WORK_DETAIL + SEPARATOR +idx));

        ProcModeDetails procModeDetails = new ProcModeLSPDetails();
        procModeDetails.reqObjMapping(request,idx);
        this.setDetails(procModeDetails);
    }

}

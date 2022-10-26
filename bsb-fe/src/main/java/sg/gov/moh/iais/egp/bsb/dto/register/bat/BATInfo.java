package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import org.javers.core.metamodel.annotation.Id;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableJudger;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class BATInfo implements Serializable {
    private String batEntityId;

    private String schedule;

    @Id
    private String batName;

    private List<String> sampleType;

    private List<String> workType;

    private String sampleWorkDetail;

    private String estimatedMaximumVolume;

    private String methodOrSystem;

    private ProcModeDetails details;



    public BATInfo() {
        this.sampleType = new ArrayList<>();
        this.workType = new ArrayList<>();
        details = new ProcModeDetails();
    }

    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_PREFIX_SCHEDULE                           = "schedule";
    private static final String KEY_PREFIX_BAT_NAME                           = "batName";
    private static final String KEY_PREFIX_SAMPLE_TYPE                        = "sampleType";
    private static final String KEY_PREFIX_WORK_TYPE                          = "workType";
    private static final String KEY_PREFIX_SAMPLE_WORK_DETAIL                 = "sampleWorkDetail";
    private static final String KEY_PREFIX_ESTIMATED_MAX_VOL                  = "estimatedMaximumVolume";
    private static final String KEY_PREFIX_METHOD_OR_SYSTEM_FOR_LSP           = "methodOrSystem";

    public void reqObjMapping(HttpServletRequest request, String idx, FieldEditableJudger editableJudger) {
        if (editableJudger.editable(KEY_PREFIX_SCHEDULE)) {
            this.setSchedule(ParamUtil.getString(request, KEY_PREFIX_SCHEDULE + SEPARATOR + idx));
        }
        if (editableJudger.editable(KEY_PREFIX_BAT_NAME)) {
            this.setBatName(ParamUtil.getString(request, KEY_PREFIX_BAT_NAME + SEPARATOR + idx));
        }
        if (editableJudger.editable(KEY_PREFIX_SAMPLE_TYPE)) {
            String[] sampleTypes = ParamUtil.getStrings(request, KEY_PREFIX_SAMPLE_TYPE + SEPARATOR + idx);
            if (sampleTypes != null && sampleTypes.length > 0) {
                this.setSampleType(new ArrayList<>(Arrays.asList(sampleTypes)));
            } else {
                this.setSampleType(new ArrayList<>(0));
            }
        }
        if (editableJudger.editable(KEY_PREFIX_WORK_TYPE)) {
            String[] workTypes = ParamUtil.getStrings(request, KEY_PREFIX_WORK_TYPE + SEPARATOR + idx);
            if (workTypes != null && workTypes.length > 0) {
                this.setWorkType(new ArrayList<>(Arrays.asList(workTypes)));
            } else {
                this.setWorkType(new ArrayList<>(0));
            }
        }
        if (editableJudger.editable(KEY_PREFIX_SAMPLE_WORK_DETAIL)) {
            this.setSampleWorkDetail(ParamUtil.getString(request, KEY_PREFIX_SAMPLE_WORK_DETAIL + SEPARATOR + idx));
        }
        if (editableJudger.editable(KEY_PREFIX_ESTIMATED_MAX_VOL)) {
            this.setEstimatedMaximumVolume(ParamUtil.getString(request, KEY_PREFIX_ESTIMATED_MAX_VOL + SEPARATOR + idx));
        }
        if (editableJudger.editable(KEY_PREFIX_METHOD_OR_SYSTEM_FOR_LSP)) {
            this.setMethodOrSystem(ParamUtil.getString(request, KEY_PREFIX_METHOD_OR_SYSTEM_FOR_LSP + SEPARATOR + idx));
        }
        this.details.reqObjMapping(request, idx, editableJudger);
    }

}

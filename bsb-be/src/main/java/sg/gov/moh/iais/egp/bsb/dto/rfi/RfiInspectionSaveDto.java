package sg.gov.moh.iais.egp.bsb.dto.rfi;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_REMARKS_TO_APPLICANT;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RfiInspectionSaveDto extends InsProcessDto {
    private String remarksToApplicant;
    // which sections of this application can do RFI
    private PageAppEditSelectDto pageAppEditSelectDto;
    // which applications need to do RFI (if you only have one application, this column does not need setting)
    private RfiAppSelectDto rfiAppSelectDto;

    @Override
    public void reqObjMapping(HttpServletRequest request) {
        super.reqObjMapping(request);
        this.setRemarksToApplicant(ParamUtil.getString(request, KEY_REMARKS_TO_APPLICANT));
    }
}

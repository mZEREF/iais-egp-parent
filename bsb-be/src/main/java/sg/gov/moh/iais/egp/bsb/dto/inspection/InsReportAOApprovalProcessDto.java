package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InsReportAOApprovalProcessDto extends InsProcessDto {
    private String aoRecommendation;

    private static final String KEY_AO_RECOMMENDATION = "aoRecommendation";


    @Override
    public void reqObjMapping(HttpServletRequest request) {
        super.reqObjMapping(request);
        this.setAoRecommendation(ParamUtil.getString(request, KEY_AO_RECOMMENDATION));
    }
}

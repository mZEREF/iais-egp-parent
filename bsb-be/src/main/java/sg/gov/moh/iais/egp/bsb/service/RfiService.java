package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiProcessDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.CHECK_BOX_APP_SELECT;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.CHECK_BOX_BAT_SELECT;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.CHECK_BOX_DOC_SELECT;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.CHECK_BOX_FAC_SELECT;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.CLOSE_PAGE;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_PROCESS_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_TRUE;

@Service
@Slf4j
public class RfiService {
    public RfiProcessDto getRfiProcessDto(HttpServletRequest request) {
        RfiProcessDto dto = (RfiProcessDto) ParamUtil.getSessionAttr(request, KEY_RFI_PROCESS_DTO);
        if (dto == null) {
            dto = new RfiProcessDto();
        }
        return dto;
    }

    public void doNewFacilityRfi(HttpServletRequest request) {
        RfiProcessDto rfiProcessDto = getRfiProcessDto(request);
        Map<String, Boolean> rfiSelectMap = Maps.newHashMapWithExpectedSize(3);
        String facSelect = ParamUtil.getString(request, CHECK_BOX_FAC_SELECT);
        String batSelect = ParamUtil.getString(request, CHECK_BOX_BAT_SELECT);
        String docSelect = ParamUtil.getString(request, CHECK_BOX_DOC_SELECT);
        rfiSelectMap.put(CHECK_BOX_FAC_SELECT, KEY_TRUE.equals(facSelect));
        rfiSelectMap.put(CHECK_BOX_BAT_SELECT, KEY_TRUE.equals(batSelect));
        rfiSelectMap.put(CHECK_BOX_DOC_SELECT, KEY_TRUE.equals(docSelect));
        rfiProcessDto.setRfiSelectMap(rfiSelectMap);
        ParamUtil.setSessionAttr(request, KEY_RFI_PROCESS_DTO, rfiProcessDto);
        ParamUtil.setRequestAttr(request, CLOSE_PAGE, true);
    }

    public void doNewApprovalAppRfi(HttpServletRequest request) {
        RfiProcessDto rfiProcessDto = getRfiProcessDto(request);
        Map<String, Boolean> rfiSelectMap = Maps.newHashMapWithExpectedSize(2);
        String appSelect = ParamUtil.getString(request, CHECK_BOX_APP_SELECT);
        String docSelect = ParamUtil.getString(request, CHECK_BOX_DOC_SELECT);
        rfiSelectMap.put(CHECK_BOX_APP_SELECT, KEY_TRUE.equals(appSelect));
        rfiSelectMap.put(CHECK_BOX_DOC_SELECT, KEY_TRUE.equals(docSelect));
        rfiProcessDto.setRfiSelectMap(rfiSelectMap);
        ParamUtil.setSessionAttr(request, KEY_RFI_PROCESS_DTO, rfiProcessDto);
        ParamUtil.setRequestAttr(request, CLOSE_PAGE, true);
    }
}

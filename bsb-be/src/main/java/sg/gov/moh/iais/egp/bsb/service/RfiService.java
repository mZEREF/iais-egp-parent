package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiAppSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_PAGE_APP_EDIT_SELECT_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_SELECT_DTO;

@Service
@Slf4j
@RequiredArgsConstructor
public class RfiService {
    public void reqObjMappingRfiAppSelectDto(HttpServletRequest request) {
        RfiAppSelectDto rfiAppSelectDto = (RfiAppSelectDto) ParamUtil.getSessionAttr(request, KEY_RFI_APP_SELECT_DTO);
        if (rfiAppSelectDto == null) {
            rfiAppSelectDto = new RfiAppSelectDto();
        }
        String[] selects = ParamUtil.getStrings(request, "rfiSelectCheckbox");
        if (selects != null && selects.length > 0) {
            List<String> selectsList = Arrays.asList(selects);
            if (selectsList.contains("application")) {
                rfiAppSelectDto.setApplicationSelect(true);
            }
            if (selectsList.contains("preInspectionChecklist")) {
                rfiAppSelectDto.setPreInspectionChecklistSelect(true);
            }
        }
        ParamUtil.setSessionAttr(request, KEY_RFI_APP_SELECT_DTO, rfiAppSelectDto);
    }

    public void validateRfiSelection(String decision, ValidationResultDto validationResultDto, PageAppEditSelectDto pageAppEditSelectDto , RfiAppSelectDto rfiAppSelectDto) {
        boolean isSelected = false;
        if(MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION.equals(decision)) {
            boolean isAppSelected = (pageAppEditSelectDto != null && pageAppEditSelectDto.getSelectedList() != null && !pageAppEditSelectDto.getSelectedList().isEmpty());
            if(rfiAppSelectDto != null) {
                if(rfiAppSelectDto.isApplicationSelect()){
                    isSelected = isAppSelected;
                } else {
                    isSelected = rfiAppSelectDto.isPreInspectionChecklistSelect();
                }
            } else {
                isSelected = isAppSelected;
            }
        }

        if(!isSelected && MasterCodeConstants.MOH_PROCESS_DECISION_REQUEST_FOR_INFORMATION.equals(decision)) {
            validationResultDto.setPass(false);
            HashMap<String, String> errorMap = validationResultDto.getErrorMap();
            if(errorMap == null) {
                errorMap = new HashMap<>();
            }
            errorMap.put("allowForChange", "Sections Allowed for Change should not be empty");
            validationResultDto.setErrorMap(errorMap);
        }
    }
}

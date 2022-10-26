package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_PAGE_APP_EDIT_SELECT_DTO;


@Slf4j
@RestController
@RequiredArgsConstructor
public class RequestForInformationSubmitAjaxController {
    public static final String FACILITY_INFORMATION = "Facility Information";
    public static final String BIOLOGICAL_AGENTS_AND_TOXINS = "Biological Agents & Toxins";
    public static final String SUPPORTING_DOCUMENTS = "Supporting Documents";
    public static final String APPROVED_FACILITY_CERTIFIER = "Approved Facility Certifier";
    public static final String APPLICATION_INFORMATION = "Application Information";

    private static final String LI_BEFORE = "<li style=\"padding-left: 0px;\">";
    private static final String LI_END = "</li>";

    @RequestMapping(value = "/callRfiSubmit", method = RequestMethod.POST)
    public @ResponseBody String callRfiSubmit(HttpServletRequest request) {
        PageAppEditSelectDto pageAppEditSelectDto = (PageAppEditSelectDto) ParamUtil.getSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO);
        String data = "";
        String[] selects = ParamUtil.getStrings(request, "selectCheckbox");
        if (selects != null && selects.length > 0) {
            List<String> selectsList = Arrays.asList(selects);
            List<String> selectedList = new ArrayList<>(selectsList.size());
            String parentMsg = "";
            if (selectsList.contains("facility")) {
                pageAppEditSelectDto.setFacSelect(true);
                parentMsg = parentMsg + LI_BEFORE + FACILITY_INFORMATION + LI_END;
                selectedList.add(FACILITY_INFORMATION);
            }
            if (selectsList.contains("bat")) {
                pageAppEditSelectDto.setBatSelect(true);
                parentMsg = parentMsg + LI_BEFORE + BIOLOGICAL_AGENTS_AND_TOXINS + LI_END;
                selectedList.add(BIOLOGICAL_AGENTS_AND_TOXINS);
            }
            if (selectsList.contains("doc")) {
                pageAppEditSelectDto.setDocSelect(true);
                parentMsg = parentMsg + LI_BEFORE + SUPPORTING_DOCUMENTS + LI_END;
                selectedList.add(SUPPORTING_DOCUMENTS);
            }
            if (selectsList.contains("afc")) {
                pageAppEditSelectDto.setAfcSelect(true);
                parentMsg = parentMsg + LI_BEFORE + APPROVED_FACILITY_CERTIFIER + LI_END;
                selectedList.add(APPROVED_FACILITY_CERTIFIER);
            }
            if (selectsList.contains("application")) {
                pageAppEditSelectDto.setAppSelect(true);
                parentMsg = parentMsg + LI_BEFORE + APPLICATION_INFORMATION + LI_END;
                selectedList.add(APPLICATION_INFORMATION);
            }
            pageAppEditSelectDto.setSelectedList(selectedList);
            data = "<ul>";
            data = data + parentMsg;
            data = data + "</ul>";
        }
        ParamUtil.setSessionAttr(request, KEY_PAGE_APP_EDIT_SELECT_DTO, pageAppEditSelectDto);
        return data;
    }

}

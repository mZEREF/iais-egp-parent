package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryAjaxPatientResultsDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * OnlinArAjaxController
 *
 * @author junyu
 * @date 2021/11/30
 */
@Slf4j
@Controller
@RequestMapping("/hcsa/intranet/ar")
public class OnlinArAjaxController {
    @Autowired
    private AssistedReproductionService assistedReproductionService;


    @RequestMapping(value = "patientDetail.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> patientDetailAjax(HttpServletRequest request, HttpServletResponse response) {

        String patientId = request.getParameter("patientId");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(patientId)){
            SearchParam searchParam = new SearchParam(AssistedReproductionEnquiryAjaxPatientResultsDto.class.getName());
            searchParam.setPageSize(100);
            searchParam.setPageNo(1);
            searchParam.setSort("CREATED_DT", SearchParam.ASCENDING);
            //set filter
            searchParam.addFilter("patientId", patientId, true);
            //search
            QueryHelp.setMainSql("onlineEnquiry", "searchPatientAjaxByAssistedReproduction", searchParam);
            SearchResult<AssistedReproductionEnquiryAjaxPatientResultsDto> searchResult = assistedReproductionService.searchPatientAjaxByParam(searchParam);
            List<AssistedReproductionEnquiryAjaxPatientResultsDto> arAjaxList=searchResult.getRows();
            for (AssistedReproductionEnquiryAjaxPatientResultsDto ajax:arAjaxList
                 ) {
                String coFunding="";
                String arTreatment="";

                if(ajax.getTreatmentFreshNatural()!=null&&ajax.getTreatmentFreshNatural()){
                    arTreatment=arTreatment+ MasterCodeUtil.getCodeDesc(DataSubmissionConsts.CURRENT_AR_TREATMENT_FRESH_CYCLE_NATURAL);
                }
                if(ajax.getTreatmentFreshStimulated()!=null&&ajax.getTreatmentFreshStimulated()){
                    if(!"".equals(arTreatment)){
                        arTreatment=arTreatment+',';
                    }
                    arTreatment=arTreatment+ MasterCodeUtil.getCodeDesc(DataSubmissionConsts.CURRENT_AR_TREATMENT_FRESH_CYCLE_STIMULATED);
                }
                if(ajax.getTreatmentFrozenEmbryo()!=null&&ajax.getTreatmentFrozenEmbryo()){
                    if(!"".equals(arTreatment)){
                        arTreatment=arTreatment+',';
                    }
                    arTreatment=arTreatment+ MasterCodeUtil.getCodeDesc(DataSubmissionConsts.CURRENT_AR_TREATMENT_FROZEN_EMBRYO_CYCLE);

                }
                if(ajax.getTreatmentFrozenOocyte()!=null&&ajax.getTreatmentFrozenOocyte()){
                    if(!"".equals(arTreatment)){
                        arTreatment=arTreatment+',';
                    }
                    arTreatment=arTreatment+ MasterCodeUtil.getCodeDesc(DataSubmissionConsts.CURRENT_AR_TREATMENT_FROZEN_OOCYTE_CYCLE);
                }
                if(StringUtil.isNotEmpty(ajax.getArCoFunding())){
                    coFunding=coFunding+ajax.getArCoFunding();
                }
                if(StringUtil.isNotEmpty(ajax.getIuiCoFunding())){
                    if(!"".equals(coFunding)){
                        coFunding=coFunding+',';
                    }
                    coFunding=coFunding+ajax.getIuiCoFunding();

                }
                if(StringUtil.isNotEmpty(ajax.getPgtCoFunding())){
                    if(!"".equals(coFunding)){
                        coFunding=coFunding+',';
                    }
                    coFunding=coFunding+ajax.getPgtCoFunding();
                }
                ajax.setArTreatment(arTreatment);
                ajax.setCoFunding(coFunding);
                ajax.setStatus(MasterCodeUtil.getCodeDesc(ajax.getStatus()));
            }
            if(searchResult.getRowCount()>0){
                map.put("result", "Success");
            }else {
                map.put("result", "Fail");
            }
            map.put("ajaxResult", searchResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

}

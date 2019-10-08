package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppGrpPremisesDocDto;
import com.ecquaria.cloud.moh.iais.dto.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppGrpPremisesService;
import com.ecquaria.cloud.moh.iais.tags.SelectOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NewApplicationDelegator
 *
 * @author suocheng
 * @date 9/23/2019
 */
@Delegator("newApplicationDelegator")
@Slf4j
public class NewApplicationDelegator {
    private static final String APPGRPPREMISESDTO = "appGrpPremisesDto";
    private static final String APPGRPPREMISESDOCDTO = "appGrpPremisesDocDto";

    @Autowired
    private AppGrpPremisesService appGrpPremisesService;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug("the do Start start ....");
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDTO,null);
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDOCDTO,null);
        AuditTrailHelper.auditFunction("iais-cc", "premises create");
        log.debug("the do Start end ....");
    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc){
        log.debug("the do prepare start ....");
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if(StringUtil.isEmpty(action)){
            action = (String)ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
            if(StringUtil.isEmpty(action)){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
            }
        }
        log.debug("the do prepare end ....");
    }
    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc){
        log.debug("the do preparePremises start ....");
        List premisesSelect = new ArrayList<SelectOption>();
        SelectOption sp0 = new SelectOption("-1","Select One");
        premisesSelect.add(sp0);
        SelectOption sp1 = new SelectOption("newPremise","Add a new premises");
        premisesSelect.add(sp1);
        SelectOption sp2 = new SelectOption("001","111 North Bridge Rd # 07-04, 179098");
        premisesSelect.add(sp2);
        SelectOption sp3 = new SelectOption("002","514 Chai Chee Lane # 06-03, 65432");
        premisesSelect.add(sp3);
        SelectOption sp4 = new SelectOption("003","8 Foch Rd, 209786");
        premisesSelect.add(sp4);
        SelectOption sp5 = new SelectOption("004","400 Orchard Rd, 21-06 Orchard Tower, 23654");
        premisesSelect.add(sp5);
        ParamUtil.setRequestAttr(bpc.request,"premisesSelect",premisesSelect);
        log.debug("the do preparePremises end ....");
    }
    /**
     * StartStep: PrepareDocuments
     *
     * @param bpc
     * @throws
     */
    public void prepareDocuments(BaseProcessClass bpc){
        log.debug("the do prepareDocuments start ....");

        log.debug("the do prepareDocuments end ....");
    }
    /**
     * StartStep: PrepareForms
     *
     * @param bpc
     * @throws
     */
    public void prepareForms(BaseProcessClass bpc){
        log.debug("the do prepareForms start ....");

        log.debug("the do prepareForms end ....");
    }
    /**
     * StartStep: PreparePreview
     *
     * @param bpc
     * @throws
     */
    public void preparePreview(BaseProcessClass bpc){
        log.debug("the do preparePreview start ....");

        log.debug("the do preparePreview end ....");
    }
    /**
     * StartStep: PreparePayment
     *
     * @param bpc
     * @throws
     */
    public void preparePayment(BaseProcessClass bpc){
        log.debug("the do preparePayment start ....");

        log.debug("the do preparePayment end ....");
    }
    /**
     * StartStep: DoPremises
     *
     * @param bpc
     * @throws
     */
    public void doPremises(BaseProcessClass bpc){
        log.debug("the do doPremises start ....");
        AppGrpPremisesDto appGrpPremisesDto = (AppGrpPremisesDto)MiscUtil.generateDtoFromParam(bpc.request,new AppGrpPremisesDto());
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDTO,appGrpPremisesDto);
       // appGrpPremisesService.saveAppGrpPremises(appGrpPremisesDto);
        log.debug("the do doPremises end ....");
    }
    /**
     * StartStep: DoDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) throws IOException {
        log.debug("the do doDocument start ....");
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crud_action_type =  mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,crud_action_type);
        AppGrpPremisesDocDto appGrpPremisesDocDto = new AppGrpPremisesDocDto();
        MultipartFile file = null;
        for (Iterator<String> en = mulReq.getFileNames(); en.hasNext(); ) {
            String name = en.next();
            file = mulReq.getFile(name);
        }

        if(file != null && !StringUtil.isEmpty(file.getOriginalFilename())){
            appGrpPremisesDocDto.setFileName(file.getOriginalFilename());
            appGrpPremisesDocDto.setFile(file);
        }
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDOCDTO,appGrpPremisesDocDto);
        log.debug("the do doDocument end ....");
    }
    /**
     * StartStep: doFormsSubmit
     *
     * @param bpc
     * @throws
     */
    public void doFormsSubmit(BaseProcessClass bpc){
        log.debug("the do doFormsSubmit start ....");

        log.debug("the do doFormsSubmit end ....");
    }
    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPreview(BaseProcessClass bpc){
        log.debug("the do doPreview start ....");

        log.debug("the do doPreview end ....");
    }
    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPayment(BaseProcessClass bpc){
        log.debug("the do doPayment start ....");

        log.debug("the do doPayment end ....");
    }
    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    public void controlSwitch(BaseProcessClass bpc){
        log.debug("the do controlSwitch start ....");
        ParamUtil.setRequestAttr(bpc.request,"isSubmitSuccess","N");
        log.debug("the do controlSwitch end ....");

    }
    /**
     * StartStep: PrepareAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareAckPage(BaseProcessClass bpc){
        log.debug("the do prepareAckPage start ....");

        log.debug("the do prepareAckPage end ....");
    }

//    //=============================================================================
//    //private method
//    //=============================================================================
//    private void getValueFromPage(AppGrpPremisesDto appGrpPremisesDto, HttpServletRequest request) {
//        String premisesType = ParamUtil.getString(request,"premisesType");
//        String hciName = ParamUtil.getString(request,"hciName");
//        appGrpPremisesDto.setHciName(hciName);
//        MiscUtil.generateDtoFromParam(request,AppGrpPremisesDto.class);
//    }
}

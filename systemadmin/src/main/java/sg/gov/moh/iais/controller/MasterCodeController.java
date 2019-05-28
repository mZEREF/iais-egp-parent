package sg.gov.moh.iais.controller;

import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.entity.MasterCode;
import sg.gov.moh.iais.service.MasterCodeService;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;


public class MasterCodeController {

    public static MasterCode masterCode;

    public static MasterCodeService masterCodeService;

    public static void addMasterCode(BaseProcessClass process){
        MasterCode mc = masterCodeService.addMasterCode(masterCode);
    }

    public static void getMasterCodeList(BaseProcessClass process){
        System.out.println("MasterCodeService ===>>>>" + masterCodeService);
        List<MasterCode> lc =  masterCodeService.getMasterCodeList();
        process.request.setAttribute("masterCodeList", lc);
    }

}

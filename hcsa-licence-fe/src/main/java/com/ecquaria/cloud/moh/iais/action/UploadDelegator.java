package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.UploadFileService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;


/**
 * @author Wenkang
 * @date 2019/11/6 20:54
 */
@Slf4j
@Delegator("uploadDelegator")
public class UploadDelegator {
    @Autowired
    private UploadFileService uploadFileService;

    /*
    * start step
    * */
    public  void start(BaseProcessClass bpc){

        logAbout("start");

    }

    public void preparetionData (BaseProcessClass bpc){
        logAbout("preparetionData");
        //get all data of need Carry from DB
        uploadFileService. initFilePath();
        String data = uploadFileService.getData();
        log.info("------------------- getData  end --------------");
        //Parse the
        List<ApplicationListFileDto> parse = uploadFileService.parse(data);
        for(ApplicationListFileDto applicationListFileDto :parse){
            String s = JsonUtil.parseToJson(applicationListFileDto);
            uploadFileService. getRelatedDocuments(s);
            boolean saveFileSuccess = uploadFileService.saveFile(s);
            if(!saveFileSuccess){
                continue;
            }
            log.info("------------------- saveFile  end --------------");
            String compressFileName = uploadFileService.compressFile();
            boolean rename = uploadFileService.renameAndSave(compressFileName);
            log.info("------------------- compressFile  end --------------");
            try {
                if(rename){
                    uploadFileService.changeStatus(applicationListFileDto);
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
    }

    /**********************************/

    private  void logAbout(String methodName){
       log.debug(StringUtil.changeForLog("****The***** " +methodName +" ******Start ****"));
    }
}

package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.eLIS.ApplicationElisToHalpDto;
import com.ecquaria.cloud.moh.iais.common.dto.eLIS.HciElisToHalpDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * SftpElisFromHalpJob
 *
 * @author junyu
 * @date 2021/3/19
 */
@Delegator("sftpElisFromHalpDelegator")
@Slf4j
public class SftpElisFromHalpJob {

    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;

    private  FilterParameter appLicParameter = new FilterParameter.Builder()
            .clz(ApplicationElisToHalpDto.class)
            .searchAttr("appLicParam")
            .resultAttr("appLicResult")
            .sortField("ACK_NUM").sortType(SearchParam.ASCENDING).pageNo(0).build();

    private  FilterParameter hciLicParameter = new FilterParameter.Builder()
            .clz(HciElisToHalpDto.class)
            .searchAttr("hciLicParam")
            .resultAttr("hciLicResult")
            .sortField("LIC_START_DT").sortType(SearchParam.ASCENDING).pageNo(0).build();

    public void start(BaseProcessClass bpc) {

    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        SearchParam hciLicParam = SearchResultHelper.getSearchParam(request, hciLicParameter,true);
        QueryHelp.setMainSql("eLIS Query","searchForHciElis",hciLicParam);
        SearchResult<HciElisToHalpDto> hciElisParamResult = onlineEnquiriesService.searchHciElisParam(hciLicParam);

        List<String> hciStringList = new ArrayList<>();
        //header
        StringBuilder hciHeaderStr=new StringBuilder();
        hciHeaderStr.append("HDR");
        hciHeaderStr.append("^");
        hciHeaderStr.append("APPDATA");
        hciHeaderStr.append("^");
        hciHeaderStr.append(hciElisParamResult.getRowCount());
        hciHeaderStr.append("^");
        String calendarHciStr= Formatter.formatDateTime(new Date(), "YYYYMMDDhhmmss");
        hciHeaderStr.append(calendarHciStr);
        hciStringList.add(hciHeaderStr.toString());

        //data record
        for (HciElisToHalpDto hci:hciElisParamResult.getRows()
             ) {
            StringBuilder hciDataStr=new StringBuilder();
            hciDataStr.append(hci.getAppType()).append("^");
            hciDataStr.append(hci.getAppUpdateDt()).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");
            hciDataStr.append(hci).append("^");



            hciDataStr.append(hciElisParamResult.getRowCount());
            hciStringList.add(hciDataStr.toString());

        }

        //trailer
        StringBuilder hciTrailerStr=new StringBuilder();
        hciTrailerStr.append("TR");
        hciTrailerStr.append("^");
        hciTrailerStr.append(hciElisParamResult.getRowCount());
        hciStringList.add(hciTrailerStr.toString());

        writeDataHubData(hciStringList, "hci_file");

//========================================================================================================

        List<String> applicationStringList = new ArrayList<>();

        SearchParam appLicParam = SearchResultHelper.getSearchParam(request, appLicParameter,true);
        QueryHelp.setMainSql("eLIS Query","searchForHciElis",appLicParam);
        SearchResult<ApplicationElisToHalpDto> appLicElisParamResult = onlineEnquiriesService.searchAppElisParam(appLicParam);

        //header
        StringBuilder headerStr=new StringBuilder();
        headerStr.append("HDR");
        headerStr.append("^");
        headerStr.append("APPDATA");
        headerStr.append("^");
        headerStr.append(appLicElisParamResult.getRowCount());
        headerStr.append("^");
        String calendarStr= Formatter.formatDateTime(new Date(), "YYYYMMDDhhmmss");
        headerStr.append(calendarStr);
        applicationStringList.add(headerStr.toString());

        //data record
        for (ApplicationElisToHalpDto app:appLicElisParamResult.getRows()
        ) {
            StringBuilder appDataStr=new StringBuilder();
            appDataStr.append(app.getApplicationNo()).append("^");
            appDataStr.append(app.getLicStatus()).append("^");
            appDataStr.append(app.getAppType()).append("^");
            appDataStr.append(app.getHciCode()).append("^");
            appDataStr.append(app.getHciName()).append("^");
            appDataStr.append(app.getHciType()).append("^");
            appDataStr.append(app.getLicCreatedDt()).append("^");
            appDataStr.append(app.getUenNo()).append("^");
            appDataStr.append(app.getLicenceNo()).append("^");
            appDataStr.append(app.getLicType()).append("^");
            appDataStr.append(app.getLicStartDt()).append("^");
            appDataStr.append(app.getLicEndDt()).append("^");
            appDataStr.append(app.getAppCreatedDt()).append("^");
            appDataStr.append(app.getLicenseeType()).append("^");
            appDataStr.append(app.getLicenseeName()).append("^");
            appDataStr.append("").append("^");
            appDataStr.append("").append("^");
            appDataStr.append("").append("^");
            appDataStr.append("").append("^");
            appDataStr.append(app.getBlock()).append("^");
            appDataStr.append(app.getStreetName()).append("^");
            appDataStr.append(app.getLevel()).append("^");
            appDataStr.append(app.getUnit()).append("^");
            appDataStr.append(app.getBuildingName()).append("^");
            appDataStr.append(app.getPostalCode());

            applicationStringList.add(appDataStr.toString());

        }
        //trailer
        StringBuilder trailerStr=new StringBuilder();
        trailerStr.append("TR");
        trailerStr.append("^");
        trailerStr.append(appLicElisParamResult.getRowCount());
        applicationStringList.add(trailerStr.toString());

        writeDataHubData(applicationStringList, "app"+calendarStr);
    }

    public static boolean writeDataHubData(List<String> result, String fileName) {
        long start = System.currentTimeMillis();
        String filePath = "D:\\temp\\txt";
        StringBuilder content = new StringBuilder();
        boolean flag = false;
        BufferedWriter out = null;
        try {
            if (result != null && !result.isEmpty() && StringUtils.isNotEmpty(fileName)) {
                fileName += "_" + System.currentTimeMillis() + ".txt";
                File pathFile = new File(filePath);
                if (!pathFile.exists()) {
                    pathFile.mkdirs();
                }
                String relFilePath = filePath + File.separator + fileName;
                File file = new File(relFilePath);
                if (!file.exists()) {
                    boolean createFlag = file.createNewFile();
                    if (!createFlag) {
                        log.error("Create file Failed");
                    }
                }
                out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));
//                //header
//                out.write("curr_time,link_id,travel_time,speed,reliabilitycode,link_len,adcode,time_stamp,state,public_rec_time,ds");
//                out.newLine();
                for (String info : result) {

                    out.write(info);
                    out.newLine();
                }
                flag = true;
                log.info(fileName+"File Writing Time：*********************************" + (System.currentTimeMillis() - start) + "ms");
                System.out.println(fileName+"File Writing Time：*********************************" + (System.currentTimeMillis() - start) + "ms");
            }
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
            return flag;
        }
    }

}

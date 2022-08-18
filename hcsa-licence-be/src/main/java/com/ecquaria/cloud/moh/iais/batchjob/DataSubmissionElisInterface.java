package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.DataSubmissionElisInterfaceService;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@JobHandler(value="dataSubmissionElisInterface")
@Component
@Slf4j
public class DataSubmissionElisInterface extends IJobHandler {
    @Value("${iais.datasubmission.elis.path}")
    private String sharedPath;

    @Autowired
    private DataSubmissionElisInterfaceService dataSubmissionElisInterfaceService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            File folder = MiscUtil.generateFile(sharedPath);
            if (!folder.isDirectory()) {
                return ReturnT.SUCCESS;
            }
            File[] files = folder.listFiles();
            List<File> sortList = sortFilesByName(files);
            if (IaisCommonUtils.isEmpty(sortList)) {
                return ReturnT.SUCCESS;
            }
            dataSubmissionElisInterfaceService.processLicence(sortList);
            dataSubmissionElisInterfaceService.processUsers(sortList);
            dataSubmissionElisInterfaceService.processDoctor(sortList);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    private List<File> sortFilesByName(File[] files) {
        if (files == null || files.length == 0) {
            return null;
        }
        List<File> sortList = IaisCommonUtils.genNewArrayList(files.length);
        TreeMap<String, File> map = new TreeMap<>();
        for (File file : files) {
            map.put(file.getName(), file);
        }
        for (Map.Entry<String, File> ent : map.entrySet()) {
            sortList.add(ent.getValue());
        }

        return sortList;
    }
 }

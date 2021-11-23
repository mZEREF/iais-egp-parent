package com.ecquaria.cloud.moh.iais.action.datasubmission;

/**
 * Process: MohARPatientInfoUpload
 *
 * @Description PatientUploadDelegate
 * @Auther chenlei on 11/23/2021.
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigExcel;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.helper.excel.IrregularExcelWriterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @Description PatientDelegator
 * @Auther chenlei on 10/22/2021.
 */
@Delegator("patientUploadDelegate")
@Slf4j
public class PatientUploadDelegate {

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----PatientUploadDelegate Start -----"));
    }

    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PrepareSwitch -----"));
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionConstant.DS_TITLE_NEW);
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Patient Information</strong>");
    }

    /**
     * Step: PreparePage
     *
     * @param bpc
     */
    public void preparePage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PreparePage -----"));
    }

    /**
     * Step: PageAction
     *
     * @param bpc
     */
    public void doPageAction(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PageAction -----"));
    }

    /**
     * Step: Submission
     *
     * @param bpc
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- Submission -----"));
    }

    /**
     * Step: PrepareReturn
     *
     * @param bpc
     */
    public void prepareReturn(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("----- PrepareReturn -----"));
    }

    /**
     * Ajax: Download File Template
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/ds/ar/patient-info-file")
    public @ResponseBody
    void exportTemplate(HttpServletRequest request, HttpServletResponse response) {
        log.info(StringUtil.changeForLog("----- Export Patient Info File -----"));
        try {
            File inputFile = ResourceUtils.getFile("classpath:template/Data_Submission_ART_Patient.xlsx");
            if (!inputFile.exists() || !inputFile.isFile()) {
                log.error("No File Template Found!");
                return;
            }
            // write Id type
            List<MasterCodeView> masterCodes = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE);
            if (IaisCommonUtils.isNotEmpty(masterCodes)) {
                int size = masterCodes.size();
                List<String> values = IaisCommonUtils.genNewArrayList(size);
                Map<Integer, List<Integer>> excelConfigIndex = IaisCommonUtils.genNewLinkedHashMap(size);
                int i = 1;
                for (MasterCodeView view : masterCodes) {
                    values.add(view.getCodeValue());
                    excelConfigIndex.put(i++, Collections.singletonList(1));
                }
                inputFile = IrregularExcelWriterUtil.writerToExcelByIndex(inputFile, 1, values.toArray(new String[size]),
                        excelConfigIndex);
            }
            // wite nationality
            masterCodes = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_NATIONALITY);
            if (IaisCommonUtils.isNotEmpty(masterCodes)) {
                int size = masterCodes.size();
                List<String> values = IaisCommonUtils.genNewArrayList(size);
                Map<Integer, List<Integer>> excelConfigIndex = IaisCommonUtils.genNewLinkedHashMap(size);
                int i = 1;
                for (MasterCodeView view : masterCodes) {
                    values.add(view.getCodeValue());
                    excelConfigIndex.put(i++, Collections.singletonList(3));
                }
                inputFile = IrregularExcelWriterUtil.writerToExcelByIndex(inputFile, 1, values.toArray(new String[size]),
                        excelConfigIndex);
            }
            // wite ethnic group
            masterCodes = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_ETHNIC_GROUP);
            if (IaisCommonUtils.isNotEmpty(masterCodes)) {
                int size = masterCodes.size();
                List<String> values = IaisCommonUtils.genNewArrayList(size);
                Map<Integer, List<Integer>> excelConfigIndex = IaisCommonUtils.genNewLinkedHashMap(size);
                int i = 1;
                for (MasterCodeView view : masterCodes) {
                    values.add(view.getCodeValue());
                    excelConfigIndex.put(i++, Collections.singletonList(5));
                }
                inputFile = IrregularExcelWriterUtil.writerToExcelByIndex(inputFile, 1, values.toArray(new String[size]),
                        excelConfigIndex);
            }

            FileUtils.writeFileResponseContent(response, inputFile);
            FileUtils.deleteTempFile(inputFile);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("Export Template has error - " + e.getMessage()), e);
        }
    }
}

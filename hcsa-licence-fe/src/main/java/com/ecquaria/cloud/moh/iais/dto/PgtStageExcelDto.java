package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * PgtStageExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/14 9:14
 **/
@Data
@ExcelSheetProperty(sheetName = "Preimplantation Genetic Testing", sheetAt = 9, startRowIndex = 2)
public class PgtStageExcelDto implements Serializable{
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No.", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) PGT-(Common) Done?", readOnly = true)
    private String isPgtMCom;

    @ExcelProperty(cellIndex = 3, cellName = "(4) PGT-M (Rare) Done?", readOnly = true)
    private String isPgtMRare;

    @ExcelProperty(cellIndex = 4, cellName = "(5) PGT-SR Done?", readOnly = true)
    private String isPgtSr;

    @ExcelProperty(cellIndex = 5, cellName = "(6) PGT-A Done?", readOnly = true)
    private String isPgtA;

    @ExcelProperty(cellIndex = 6, cellName = "(7) PTT Done?", readOnly = true)
    private String isPtt;

    @ExcelProperty(cellIndex = 7, cellName = "(8) Other Preimplantation Genetic Testing Done? \n" +
            "\n" +
            "Please indicate the Test Type via freetext.", readOnly = true)
    private String isOtherPgt;

    @ExcelProperty(cellIndex = 8, cellName = "(9) PGT  (Common) Work-up Done?", readOnly = true)
    private String workUpCom;

    @ExcelProperty(cellIndex = 9, cellName = "(10) PGT  (Common) Embryo Biopsy + Testing Done?", readOnly = true)
    private String ebtCom;

    @ExcelProperty(cellIndex = 10, cellName = "(11) PGT (Rare) Work-up Done?", readOnly = true)
    private String workUpRare;

    @ExcelProperty(cellIndex = 11, cellName = "(12) PGT  (Rare) Embryo Biopsy + Testing Done?", readOnly = true)
    private String ebtRare;

    @ExcelProperty(cellIndex = 12, cellName = "(13) Date Started for PGT-M\n" +
            "\n" +
            "Note: Date of obtaining blood samples for the work-up", readOnly = true)
    private String pgtMDate;

    @ExcelProperty(cellIndex = 13, cellName = "(14) PGT-M performed to detect sex-linked disease", readOnly = true)
    private String isPgtMDsld;

    @ExcelProperty(cellIndex = 14, cellName = "(15) PGT-M performed together with HLA matching", readOnly = true)
    private String isPgtMWithHla;

    @ExcelProperty(cellIndex = 15, cellName = "(16) PGT-M performed for none of the above", readOnly = true)
    private String isPgtMNon;

    @ExcelProperty(cellIndex = 16, cellName = "(17) PGT-M Appeal Reference No. (If Applicable)\n" +
            "\n" +
            "Note: Only applicable if PGT-M is performed to detect sex-linked disease. Reference No. format must be MHXX:0X/0X-XX(XXXX)", readOnly = true)
    private String pgtMRefNo;

    @ExcelProperty(cellIndex = 17, cellName = "(18) What Condition and Gene was PGT-M Performed to Detect?", readOnly = true)
    private String pgtMCondition;

    @ExcelProperty(cellIndex = 18, cellName = "(19) Please indicate if co-funding was provided for PGT-M (Common)", readOnly = true)
    private String isPgtCoFunding;

    @ExcelProperty(cellIndex = 19, cellName = "(20) Please indicate if co-funding was provided for PGT-M (Rare)", readOnly = true)
    private String isPgtMRareCoFunding;

    @ExcelProperty(cellIndex = 20, cellName = "(21) Is the Co-Funding Provided on an Appeal Basis", readOnly = true)
    private String pgtMAppeal;

    @ExcelProperty(cellIndex = 21, cellName = "(22) PGT-A Performed Because of Advanced Maternal Age", readOnly = true)
    private String isPgtAAma;

    @ExcelProperty(cellIndex = 22, cellName = "(23) PGT-A Performed Because of Two or more recurrent implantation failure", readOnly = true)
    private String isPgtATomrif;

    @ExcelProperty(cellIndex = 23, cellName = "(24) PGT-A Performed Because of two or more repeated pregnancy losses", readOnly = true)
    private String isPgtATomrpl;

    @ExcelProperty(cellIndex = 24, cellName = "(25) What was the Result of the PGT-A  Test?", readOnly = true)
    private String pgtAResult;

    @ExcelProperty(cellIndex = 25, cellName = "(26) Please indicate if co-funding was provided for PGT-A", readOnly = true)
    private String isPgtACoFunding;

    @ExcelProperty(cellIndex = 26, cellName = "(27) Is the Co-Funding Provided on an Appeal Basis", readOnly = true)
    private String pgtAAppeal;

    @ExcelProperty(cellIndex = 27, cellName = "(28) Date Started for PGT - SR", readOnly = true)
    private String pgtSrDate;

    @ExcelProperty(cellIndex = 28, cellName = "(29) PGT-SR Appeal Reference No. (If Applicable)", readOnly = true)
    private String pgtSrRefNo;

    @ExcelProperty(cellIndex = 29, cellName = "(30) What was PGT-SR Performed for?", readOnly = true)
    private String pgtSrCondition;

    @ExcelProperty(cellIndex = 30, cellName = "(31) Please indicate if co-funding was provided for PGT-SR", readOnly = true)
    private String isPgtSrCoFunding;

    @ExcelProperty(cellIndex = 31, cellName = "(32) Is the Co-Funding Provided on an Appeal Basis", readOnly = true)
    private String pgtSrAppeal;

    @ExcelProperty(cellIndex = 32, cellName = "(33) What Condition was PTT Performed to Detect", readOnly = true)
    private String pttCondition;

    @ExcelProperty(cellIndex = 33, cellName = "(34) Please indicate if co-funding was provided for PTT", readOnly = true)
    private String isPttCoFunding;

    @ExcelProperty(cellIndex = 34, cellName = "(35) Is the Co-Funding Provided on an Appeal Basis", readOnly = true)
    private String pgtPttAppeal;

    @ExcelProperty(cellIndex = 35, cellName = "(37) Location where Embryos were Biopsied\n" +
            "\n" +
            "Note: If biopsy was done at a local licensed Healthcare Institution (HCI), to indicate the exact HCI Name.", readOnly = true)
    private String isEmbryosBiopsiedLocal;

    @ExcelProperty(cellIndex = 36, cellName = "(38) Other Centre where Embryos were Biopsied At\n" +
            "\n" +
            "Note: If biopsy was done at a centre that is not a local HCI, e.g. overseas centre.", readOnly = true)
    private String otherEmbryosBiopsiedAddr;

    @ExcelProperty(cellIndex = 37, cellName = "(39) Biopsy Done By\n" +
            "\n" +
            "Note: Only applicable to embryologists who are registered on your active Assisted Reproduction licence.", readOnly = true)
    private String isBiopsyLocal;

    @ExcelProperty(cellIndex = 38, cellName = "(40) Biopsy Done By (Others)", readOnly = true)
    private String otherBiopsyAddr;

}

package com.ecquaria.cloud.moh.iais.dto;


import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.Data;

import java.io.Serializable;

@Data
@ExcelSheetProperty(sheetName = "AR Cycle Stage", sheetAt = 0, startRowIndex = 2)
public class ArCycleStageExcelDto implements Serializable {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient Name", readOnly = true)
    private String patientName;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID Type", readOnly = true)
    private String idType;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Patient ID No.", readOnly = true)
    private String idNumber;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Date Started", readOnly = true)
    private String startDate;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Main Indication", readOnly = true)
    private String mainIndication;

    @ExcelProperty(cellIndex = 5, cellName = "(6) Main Indication (Others)", readOnly = true)
    private String mainIndicationOthers;

    @ExcelProperty(cellIndex = 6, cellName = "(7) Other Indication Advanced Maternal Age", readOnly = true)
    private String otherIndicationAdv;

    @ExcelProperty(cellIndex = 7, cellName = "(8) Other Indication Endometriosis", readOnly = true)
    private String otherIndicationEnd;

    @ExcelProperty(cellIndex = 8, cellName = "(9) Other Indication Failed Repeated IUIs", readOnly = true)
    private String otherIndicationFail;

    @ExcelProperty(cellIndex = 9, cellName = "(10) Other Indication Immune Factor", readOnly = true)
    private String otherIndicationInd;

    @ExcelProperty(cellIndex = 10, cellName = "(11) Other Indication Low Ovarian Reserve", readOnly = true)
    private String otherIndicationLow;

    @ExcelProperty(cellIndex = 11, cellName = "(12) Other Indication Male Factor", readOnly = true)
    private String otherIndicationMale;

    @ExcelProperty(cellIndex = 12, cellName = "(13) Other Indication Polycystic Ovarian Disease", readOnly = true)
    private String otherIndicationPol;

    @ExcelProperty(cellIndex = 13, cellName = "(14) Other Indication Preimplantation Genetic Testing", readOnly = true)
    private String otherIndicationPrei;

    @ExcelProperty(cellIndex = 14, cellName = "(15) Other Indication Premature Ovarian Failure", readOnly = true)
    private String otherIndicationPrem;

    @ExcelProperty(cellIndex = 15, cellName = "(16) Other Indication Previous Tubal Ligation", readOnly = true)
    private String otherIndicationPre;

    @ExcelProperty(cellIndex = 16, cellName = "(17) Other Indication Tubal Disease and/or Obstruction", readOnly = true)
    private String otherIndicationTubal;

    @ExcelProperty(cellIndex = 17, cellName = "(18) Other Indication Unexplained Subfertility", readOnly = true)
    private String otherIndicationUnexplained;

    @ExcelProperty(cellIndex = 18, cellName = "(19) Other Indication others", readOnly = true)
    private String otherIndicationOthers;

    @ExcelProperty(cellIndex = 19, cellName = "(20) Other Indication (Others)\n" +
            "\n" +
            "Note: Use free text to describe the indication", readOnly = true)
    private String otherIndicationText;

    @ExcelProperty(cellIndex = 20, cellName = "(21) In-Vitro Maturation", readOnly = true)
    private String inVitroMaturation;

    @ExcelProperty(cellIndex = 21, cellName = "(22) Fresh Cycle (Natural)", readOnly = true)
    private String treatmentFreshNatural;

    @ExcelProperty(cellIndex = 22, cellName = "(23) Fresh Cycle (Stimulated)", readOnly = true)
    private String treatmentFreshStimulated;

    @ExcelProperty(cellIndex = 23, cellName = "(24) Frozen Oocyte Cycle", readOnly = true)
    private String treatmentFrozenOocyte;

    @ExcelProperty(cellIndex = 24, cellName = "(25) Frozen Embryo Cycle", readOnly = true)
    private String treatmentFrozenEmbryo;

    @ExcelProperty(cellIndex = 25, cellName = "(26) No. of Children from Current Marriage", readOnly = true)
    private String currentMarriageChildren;

    @ExcelProperty(cellIndex = 26, cellName = "(27) No. of Children from Previous Marriage", readOnly = true)
    private String previousMarriageChildren;

    @ExcelProperty(cellIndex = 27, cellName = "(28) No. of Children conceived through AR", readOnly = true)
    private String deliveredThroughChildren;

    @ExcelProperty(cellIndex = 28, cellName = "(29) Total No. of AR Cycles previously undergone by patient\n" +
            "\n" +
            "Note: Based on AR centre’s knowledge of patient’s self-declaration", readOnly = true)
    private String totalPreviouslyPreviously;

    @ExcelProperty(cellIndex = 29, cellName = "(30) No. of AR Cycles undergone Overseas", readOnly = true)
    private String cyclesUndergoneOverseas;

    @ExcelProperty(cellIndex = 30, cellName = "(31) Enhanced Counselling Provided?", readOnly = true)
    private String enhancedCounselling;

    @ExcelProperty(cellIndex = 31, cellName = "(32) AR Practitioner", readOnly = true)
    private String practitioner;

    @ExcelProperty(cellIndex = 32, cellName = "(33) Embryologist\n" +
            "\n" +
            "Note: If no embryologist was involved in this cycle, to indicate \"Not Applicable\".", readOnly = true)
    private String embryologist;

    @ExcelProperty(cellIndex = 33, cellName = "(34) Was a donor's Oocyte(s)/Embryo(s)/Sperm(s) used in this cycle\n" +
            "\n" +
            "Note: The file upload function only allows user to submit up to two donors. If more than two donors are involved, please submit the information directly via the HALP AR forms.", readOnly = true)
    private String usedDonorOocyte;

    @ExcelProperty(cellIndex = 34, cellName = "(35) Is Donor 1 a Directed Donor?", readOnly = true)
    private String donorDirectedDonation1;

    @ExcelProperty(cellIndex = 35, cellName = "(36) Donor 1's ID Type", readOnly = true)
    private String donorIdType1;

    @ExcelProperty(cellIndex = 36, cellName = "(37) Donor 1's ID No./Donor Sample Code", readOnly = true)
    private String donorIdNo1;

    @ExcelProperty(cellIndex = 37, cellName = "(38) Donor 1's relation to patient\n" +
            "\n" +
            "Note: Applicable to directed donations only.", readOnly = true)
    private String donorRelation1;

    @ExcelProperty(cellIndex = 38, cellName = "(39) Donor 1's Fresh Oocyte(s) used?", readOnly = true)
    private String donorFreshOocyte1;

    @ExcelProperty(cellIndex = 39, cellName = "(40) Donor 1's Frozen Oocyte(s) used?", readOnly = true)
    private String donorFrozenOocyte1;

    @ExcelProperty(cellIndex = 40, cellName = "(41) Donor 1's Embryo(s) used?", readOnly = true)
    private String donorEmbryo1;

    @ExcelProperty(cellIndex = 41, cellName = "(42) Donor 1's Fresh Sperm(s) Used?", readOnly = true)
    private String donorFreshSperm1;

    @ExcelProperty(cellIndex = 42, cellName = "(43) Donor 1's Frozen Sperm(s) Used?", readOnly = true)
    private String donorFrozenSperm1;

    @ExcelProperty(cellIndex = 43, cellName = "(44) Is Donor 2 a Directed Donor?", readOnly = true)
    private String donorDirectedDonation2;

    @ExcelProperty(cellIndex = 44, cellName = "(45) Donor 2's ID Type", readOnly = true)
    private String donorIdType2;

    @ExcelProperty(cellIndex = 45, cellName = "(46) Donor 2's ID No./Donor Sample Code", readOnly = true)
    private String donorIdNo2;

    @ExcelProperty(cellIndex = 46, cellName = "(47) Donor 2's relation to patient\n" +
            "\n" +
            "Note: Applicable to directed donors only.", readOnly = true)
    private String donorRelation2;

    @ExcelProperty(cellIndex = 47, cellName = "(48) Donor 2's Fresh Oocyte(s) used?", readOnly = true)
    private String donorFreshOocyte2;

    @ExcelProperty(cellIndex = 48, cellName = "(49) Donor 2's Frozen Oocyte(s) used?", readOnly = true)
    private String donorFrozenOocyte2;

    @ExcelProperty(cellIndex = 49, cellName = "(50) Donor 2's Embryo(s) used?", readOnly = true)
    private String donorEmbryo2;

    @ExcelProperty(cellIndex = 50, cellName = "(51) Donor 2's Fresh Sperm(s) Used?", readOnly = true)
    private String donorFreshSperm2;

    @ExcelProperty(cellIndex = 51, cellName = "(52) Donor 2's Frozen Sperm(s) Used?", readOnly = true)
    private String donorFrozenSperm2;



    public boolean getBooleanValue(Object obj) {
        if ("Yes".equals(obj)) {
            return true;
        }
        return false;
    }
}

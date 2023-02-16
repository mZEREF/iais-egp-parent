package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * IUICycleStageExcelDto
 *
 * @author jiawei_gu
 * @date 2/8/2023
 */

@Data
@ExcelSheetProperty(sheetName = "IUI Cycle Stage", sheetAt = 0, startRowIndex = 2)
public class IUICycleStageExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) IUI Treatment performed in own premise ", readOnly = true)
    private String inOwn;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Name of Premise Where IUI Treatment Is Performed \n" +
            "\n" +
            "Note: Only applicable if \"No\" is indicated for (3).", readOnly = true)
    private String nameOfPremise;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Date Started \n" +
            "\n" +
            "Note: First day of menstrual cycle", readOnly = true)
    private String dateStarted;

    @ExcelProperty(cellIndex = 5, cellName = "(7) No. of children from current Marriage", readOnly = true)
    private String noOfMarriageChildren_current;

    @ExcelProperty(cellIndex = 6, cellName = "(8) No. of Children from previous marriage", readOnly = true)
    private String noOfMarriageChildren_previous;

    @ExcelProperty(cellIndex = 7, cellName = "(9) No. of children conceived through IUI\n" +
            "\n" +
            "Note: This does not include children that are conceived through Assisted Reproduction procedures (e.g. IVF, ICSI)", readOnly = true)
    private String noOfIUIChildren;

    @ExcelProperty(cellIndex = 8, cellName = "(10) Source of Semen", readOnly = true)
    private String sourceOfSemen;

    @ExcelProperty(cellIndex = 9, cellName = "(11) No. of vials of sperm extracted", readOnly = true)
    private String noOfVialsSpermExtracted;

    @ExcelProperty(cellIndex = 10, cellName = "(12) No. of vials of sperm used in this cycle", readOnly = true)
    private String noOfVialsSpermUsedInCycle;

    @ExcelProperty(cellIndex = 11, cellName = "(13) Is this a  Directed Donation?", readOnly = true)
    private String isDirectedDonation;

    @ExcelProperty(cellIndex = 12, cellName = "(14) Donor ID Type", readOnly = true)
    private String donorIdType;

    @ExcelProperty(cellIndex = 13, cellName = "(15) Donor ID No./ Donor Sample Code", readOnly = true)
    private String donorIdNoSampleCode;

    @ExcelProperty(cellIndex = 14, cellName = "(17) Age of donor when sperm was collected", readOnly = true)
    private String donorAge;

    @ExcelProperty(cellIndex = 15, cellName = "(18) Donor Relation to Patient\n" +
            "\n" +
            "Note: Only applicable for directed donation.", readOnly = true)
    private String donorRelationToPatient;


}

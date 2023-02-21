package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

@Data
@ExcelSheetProperty(sheetName = "Donation", sheetAt = 4, startRowIndex = 2)
public class OFODonationStageExcelDto {
    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No.", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Local or Overseas", readOnly = true)
    private String localOrOverseas;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Type of Sample", readOnly = true)
    private String typeOfSample;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Is the Oocyte donor the patient?", readOnly = true)
    private String isOocyteDonorPatient;

    @ExcelProperty(cellIndex = 5, cellName = "(6) Is the Female Donor's Identity Known", readOnly = true)
    private String femaleIdentityKnown;

    @ExcelProperty(cellIndex = 6, cellName = "(7) Female Donor ID Type", readOnly = true)
    private String femaleIdType;

    @ExcelProperty(cellIndex = 7, cellName = "(8) Female Donor Sample Code", readOnly = true)
    private String femaleSampleCode;

    @ExcelProperty(cellIndex = 8, cellName = "(9) Age of Female Donor at the Point of Donation", readOnly = true)
    private String femaleAge;

    @ExcelProperty(cellIndex = 9, cellName = "(10) Is the Sperm Donor the Patient's Husband", readOnly = true)
    private String isSpermDonorPatientsHus;

    @ExcelProperty(cellIndex = 10, cellName = "(11) Is the Male Donor's Identity Known", readOnly = true)
    private String maleIdentityKnown;

    @ExcelProperty(cellIndex = 11, cellName = "(12) Male Donor ID Type", readOnly = true)
    private String maleIdType;

    @ExcelProperty(cellIndex = 12, cellName = "(13) Male Donor ID No.", readOnly = true)
    private String maleIdNo;

    @ExcelProperty(cellIndex = 13, cellName = "(14) Age of Male Donor at the Point of Donation", readOnly = true)
    private String maleAge;

    @ExcelProperty(cellIndex = 14, cellName = "(15) Which Institution was the Sample Donated From?", readOnly = true)
    private String institutionFrom;

    @ExcelProperty(cellIndex = 15, cellName = "(16) Reasons for Donation", readOnly = true)
    private String reasonsForDonation;

    @ExcelProperty(cellIndex = 16, cellName = "(17) Other Reason for Donation", readOnly = true)
    private String otherReasonsForDonation;

    @ExcelProperty(cellIndex = 17, cellName = "(18) Purpose of Donation (Treatment)", readOnly = true)
    private String purposeOfDonation_treatment;

    @ExcelProperty(cellIndex = 18, cellName = "(19) Purpose of Donation (Research)", readOnly = true)
    private String purposeOfDonation_research;

    @ExcelProperty(cellIndex = 19, cellName = "(20) Purpose of Donation (Training)", readOnly = true)
    private String purposeOfDonation_training;

    @ExcelProperty(cellIndex = 20, cellName = "(21) Is the sample from a directed donation?", readOnly = true)
    private String isDirectedDonation;

    @ExcelProperty(cellIndex = 21, cellName = "(22) No. Donated For Treatment", readOnly = true)
    private String noDonatedForTreatment;

    @ExcelProperty(cellIndex = 22, cellName = "(23) No. Donated for Research (Usable for Treatment)", readOnly = true)
    private String noDonatedForResearch_useTreatment;

    @ExcelProperty(cellIndex = 23, cellName = "(24) No. Donated for Research (Unusable for Treatment)", readOnly = true)
    private String noDonatedForResearch_unUseTreatment;

    @ExcelProperty(cellIndex = 24, cellName = "(25) Donated for Human Embryotic Stem Cell Research", readOnly = true)
    private String donatedForHESCResearch;

    @ExcelProperty(cellIndex = 25, cellName = "(26) Donated for Research Related to Assisted Reproduction", readOnly = true)
    private String donatedForResearchRelatedToAR;

    @ExcelProperty(cellIndex = 26, cellName = "(27) Please indicate other type of research", readOnly = true)
    private String otherTypeOfResearch;

    @ExcelProperty(cellIndex = 27, cellName = "(28) No. Used for Training", readOnly = true)
    private String noUsedForTraining;
}

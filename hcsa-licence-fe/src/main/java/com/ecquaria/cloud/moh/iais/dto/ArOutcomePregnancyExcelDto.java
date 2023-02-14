package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ArOutcomePregnancyExcelDto
 *
 * @Author dongchi
 * @Date 2023/2/14 10:30
 **/
@Data
@ExcelSheetProperty(sheetName = "Outcome of Pregnancy", sheetAt = 7, startRowIndex = 2)
public class ArOutcomePregnancyExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "(1) Patient ID Type ", readOnly = true)
    private String patientIdType;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Patient ID No. ", readOnly = true)
    private String patientIdNo;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Order Shown in 1st Ultrasound (if Pregnancy confirmed)", readOnly = true)
    private String orderShown;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Was Selective foetal Reduction Carried Out?", readOnly = true)
    private String isFoetalReduction;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Outcome of Pregnancy", readOnly = true)
    private String outcomeOfPregnancy;

    @ExcelProperty(cellIndex = 5, cellName = "(6) No. Live Birth (Male)", readOnly = true)
    private String noLiveBirthMale;

    @ExcelProperty(cellIndex = 6, cellName = "(7) No. Live Birth (female)", readOnly = true)
    private String noLiveBirthFemale;

    @ExcelProperty(cellIndex = 7, cellName = "(8) No. Still Birth", readOnly = true)
    private String noStillBirth;

    @ExcelProperty(cellIndex = 8, cellName = "(9) No. of Spontaneous Abortion", readOnly = true)
    private String noOfSpontaneousAbortion;

    @ExcelProperty(cellIndex = 9, cellName = "(10) No. of Intra-uterine Death", readOnly = true)
    private String noOfIntraUterineDeath;

    @ExcelProperty(cellIndex = 10, cellName = "(11) No. Live Birth (Total)", readOnly = true)
    private String noLiveBirthTotal;

    @ExcelProperty(cellIndex = 11, cellName = "(12) Mode of Delivery", readOnly = true)
    private String modeOfDelivery;

    @ExcelProperty(cellIndex = 12, cellName = "(13) Date of Delivery", readOnly = true)
    private String dateOfDelivery;

    @ExcelProperty(cellIndex = 13, cellName = "(14) Date of Delivery Is Unknown", readOnly = true)
    private String dateOfDeliveryIsUnknown;

    @ExcelProperty(cellIndex = 14, cellName = "(15) Place of Birth", readOnly = true)
    private String placeOfBirth;

    @ExcelProperty(cellIndex = 15, cellName = "(16) Place of local Birth", readOnly = true)
    private String placeOfLocalBirth;

    @ExcelProperty(cellIndex = 16, cellName = "(17) Baby Details Unknown (Loss to Follow-up)", readOnly = true)
    private String babyDetailsUnknown;

    @ExcelProperty(cellIndex = 17, cellName = "(18) Baby 1 Birth Weight", readOnly = true)
    private String baby1BirthWeight;

    @ExcelProperty(cellIndex = 18, cellName = "(19) Baby 1 Birth Defect", readOnly = true)
    private String baby1BirthDefect;

    @ExcelProperty(cellIndex = 19, cellName = "(20) Baby 1 Chromosomal Anomalies", readOnly = true)
    private String baby1ChromosomalAnomalies;

    @ExcelProperty(cellIndex = 20, cellName = "(21) Baby 1 Heart Anomalies", readOnly = true)
    private String baby1HeartAnomalies;

    @ExcelProperty(cellIndex = 21, cellName = "(22) Baby 1 Musculoskeletal  Anomalies", readOnly = true)
    private String baby1MusculoskeletalAnomalies;

    @ExcelProperty(cellIndex = 22, cellName = "(23) Baby 1 Nervous System  Anomalies", readOnly = true)
    private String baby1NervousSystemAnomalies;

    @ExcelProperty(cellIndex = 23, cellName = "(24) Baby 1 Other Fetal Anomalies", readOnly = true)
    private String baby1OtherFetalAnomalies;

    @ExcelProperty(cellIndex = 24, cellName = "(25) Baby 1 Respiratory System  Anomalies", readOnly = true)
    private String baby1RespiratorySystemAnomalies;

    @ExcelProperty(cellIndex = 25, cellName = "(26) Baby 1 Urinary System  Anomalies", readOnly = true)
    private String baby1UrinarySystemAnomalies;

    @ExcelProperty(cellIndex = 26, cellName = "(27) Baby 1 other anomalies", readOnly = true)
    private String baby1OtherAnomalies;

    @ExcelProperty(cellIndex = 27, cellName = "(28) Baby 1 Defect Type (Others)", readOnly = true)
    private String baby1DefectTypeOthers;

    @ExcelProperty(cellIndex = 28, cellName = "(29) Baby 1 Defect Type (Others) \n" +
            "\n" +
            "Please indicate via freetext the birth defect for Baby 1.", readOnly = true)
    private String baby1FreetextDefectTypeOthers;

    @ExcelProperty(cellIndex = 29, cellName = "(30) Baby 2 Birth Weight", readOnly = true)
    private String baby2BirthWeight;

    @ExcelProperty(cellIndex = 30, cellName = "(31) Baby 2 Birth Defect", readOnly = true)
    private String baby2BirthDefect;

    @ExcelProperty(cellIndex = 31, cellName = "(32) Baby 2 Chromosomal Anomalies", readOnly = true)
    private String baby2ChromosomalAnomalies;

    @ExcelProperty(cellIndex = 32, cellName = "(33) Baby 2 Heart Anomalies", readOnly = true)
    private String baby2HeartAnomalies;

    @ExcelProperty(cellIndex = 33, cellName = "(34) Baby 2 Musculoskeletal  Anomalies", readOnly = true)
    private String baby2MusculoskeletalAnomalies;

    @ExcelProperty(cellIndex = 34, cellName = "(35) Baby 2 Nervous System  Anomalies", readOnly = true)
    private String baby2NervousSystemAnomalies;

    @ExcelProperty(cellIndex = 35, cellName = "(36) Baby 2 Other Fetal Anomalies", readOnly = true)
    private String baby2OtherFetalAnomalies;

    @ExcelProperty(cellIndex = 36, cellName = "(37) Baby 2 Respiratory System  Anomalies", readOnly = true)
    private String baby2RespiratorySystemAnomalies;

    @ExcelProperty(cellIndex = 37, cellName = "(38) Baby 2 Urinary System  Anomalies", readOnly = true)
    private String baby2UrinarySystemAnomalies;

    @ExcelProperty(cellIndex = 38, cellName = "(39) Baby 2 other anomalies", readOnly = true)
    private String baby2OtherAnomalies;

    @ExcelProperty(cellIndex = 39, cellName = "(40) Baby 2 Defect Type (Others)", readOnly = true)
    private String baby2DefectTypeOthers;

    @ExcelProperty(cellIndex = 40, cellName = "(41) Baby 2 Defect Type (Others) \n" +
            "\n" +
            "Please indicate via freetext the birth defect for Baby 2.", readOnly = true)
    private String baby2FreetextDefectTypeOthers;

    @ExcelProperty(cellIndex = 41, cellName = "(42) Baby 3 Birth Weight", readOnly = true)
    private String baby3BirthWeight;

    @ExcelProperty(cellIndex = 42, cellName = "(43) Baby 3 Birth Defect", readOnly = true)
    private String baby3BirthDefect;

    @ExcelProperty(cellIndex = 43, cellName = "(44) Baby 3 Chromosomal Anomalies", readOnly = true)
    private String baby3ChromosomalAnomalies;

    @ExcelProperty(cellIndex = 44, cellName = "(45) Baby 3 Heart Anomalies", readOnly = true)
    private String baby3HeartAnomalies;

    @ExcelProperty(cellIndex = 45, cellName = "(46) Baby 3 Musculoskeletal  Anomalies", readOnly = true)
    private String baby3MusculoskeletalAnomalies;

    @ExcelProperty(cellIndex = 46, cellName = "(47) Baby 3 Nervous System  Anomalies", readOnly = true)
    private String baby3NervousSystemAnomalies;

    @ExcelProperty(cellIndex = 47, cellName = "(48) Baby 3 Other Fetal Anomalies", readOnly = true)
    private String baby3OtherFetalAnomalies;

    @ExcelProperty(cellIndex = 48, cellName = "(49) Baby 3 Respiratory System  Anomalies", readOnly = true)
    private String baby3RespiratorySystemAnomalies;

    @ExcelProperty(cellIndex = 49, cellName = "(50) Baby 3 Urinary System  Anomalies", readOnly = true)
    private String baby3UrinarySystemAnomalies;

    @ExcelProperty(cellIndex = 50, cellName = "(51) Baby 3 other anomalies", readOnly = true)
    private String baby3OtherAnomalies;

    @ExcelProperty(cellIndex = 51, cellName = "(52) Baby 3 Defect Type (Others)", readOnly = true)
    private String baby3DefectTypeOthers;

    @ExcelProperty(cellIndex = 52, cellName = "(53) Baby 3 Defect Type (Others) \n" +
            "\n" +
            "Please indicate via freetext the birth defect for Baby 3.", readOnly = true)
    private String baby3FreetextDefectTypeOthers;

    @ExcelProperty(cellIndex = 53, cellName = "(54) Baby 4 Birth Weight", readOnly = true)
    private String baby4BirthWeight;

    @ExcelProperty(cellIndex = 54, cellName = "(55) Baby 4 Birth Defect", readOnly = true)
    private String baby4BirthDefect;

    @ExcelProperty(cellIndex = 55, cellName = "(56) Baby 4 Chromosomal Anomalies", readOnly = true)
    private String baby4ChromosomalAnomalies;

    @ExcelProperty(cellIndex = 56, cellName = "(57) Baby 3 Heart Anomalies", readOnly = true)
    private String baby4HeartAnomalies;

    @ExcelProperty(cellIndex = 57, cellName = "(58) Baby 3 Musculoskeletal  Anomalies", readOnly = true)
    private String baby4MusculoskeletalAnomalies;

    @ExcelProperty(cellIndex = 58, cellName = "(59) Baby 3 Nervous System  Anomalies", readOnly = true)
    private String baby4NervousSystemAnomalies;

    @ExcelProperty(cellIndex = 59, cellName = "(60) Baby 3 Other Fetal Anomalies", readOnly = true)
    private String baby4OtherFetalAnomalies;

    @ExcelProperty(cellIndex = 60, cellName = "(61) Baby 3 Respiratory System  Anomalies", readOnly = true)
    private String baby4RespiratorySystemAnomalies;

    @ExcelProperty(cellIndex = 61, cellName = "(62) Baby 3 Urinary System  Anomalies", readOnly = true)
    private String baby4UrinarySystemAnomalies;

    @ExcelProperty(cellIndex = 62, cellName = "(63) Baby 3 other anomalies", readOnly = true)
    private String baby4OtherAnomalies;

    @ExcelProperty(cellIndex = 63, cellName = "(64) Baby 3 Defect Type (Others)", readOnly = true)
    private String baby4DefectTypeOthers;

    @ExcelProperty(cellIndex = 64, cellName = "(65) Baby 3 Defect Type (Others) \n" +
            "\n" +
            "Please indicate via freetext the birth defect for Baby 3.", readOnly = true)
    private String baby4FreetextDefectTypeOthers;

    @ExcelProperty(cellIndex = 65, cellName = "(66) Total No. of Baby Admitted to NICU Care", readOnly = true)
    private String noOfBabyToNICUCare;

    @ExcelProperty(cellIndex = 66, cellName = "(67) No. of Baby Admitted to L2 Care", readOnly = true)
    private String noOfBabyToL2Care;

    @ExcelProperty(cellIndex = 67, cellName = "(68) No. Days Baby Stay in L2 (Provide average if > one baby stayed))", readOnly = true)
    private String noDaysBabyInL2;

    @ExcelProperty(cellIndex = 68, cellName = "(69) No. of Baby Admitted to L3 Care", readOnly = true)
    private String noOfBabyToL3Care;

    @ExcelProperty(cellIndex = 69, cellName = "(70) No. Days Baby Stay in L3 (Provide average if > one baby stayed))", readOnly = true)
    private String noDaysBabyInL3;

    @ExcelProperty(cellIndex = 70, cellName = "(71) No. of Still Birth", readOnly = true)
    private String noStillBirthNoLiveBirth;

    @ExcelProperty(cellIndex = 71, cellName = "(72) No. of Spontaneous Abortion", readOnly = true)
    private String noOfSpontaneousAbortionNoLiveBirth;

    @ExcelProperty(cellIndex = 72, cellName = "(73) No. of Intra-uterine Death", readOnly = true)
    private String noOfIntraUterineDeathNoLiveBirth;

    @ExcelProperty(cellIndex = 73, cellName = "(74) Outcome of Pregnancy (Others)\n" +
            "\n" +
            "Please indicate the outcome of pregnancy via freetext.", readOnly = true)
    private String freetextOutcomeOfPregnancy;
}


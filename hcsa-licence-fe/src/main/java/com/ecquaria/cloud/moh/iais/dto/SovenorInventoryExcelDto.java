package com.ecquaria.cloud.moh.iais.dto;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * SovenorInventoryExcelDto
 *
 * @author suocheng
 * @date 1/19/2022
 */
@Data
@ExcelSheetProperty(sheetName = "Sovenor Inventory List", sheetAt = 0, startRowIndex = 0)
public class SovenorInventoryExcelDto implements Serializable {

    @ExcelProperty(cellIndex = 0, cellName = "(1) HCI Name", readOnly = true)
    private String hciName;

    @ExcelProperty(cellIndex = 1, cellName = "(2) Drug Name", readOnly = true)
    private String drugName;

    @ExcelProperty(cellIndex = 2, cellName = "(3) Batch Number", readOnly = true)
    private String batchNumber;

    @ExcelProperty(cellIndex = 3, cellName = "(4) Drug Strength (ug/h)", readOnly = true)
    private String drugStrength;

    @ExcelProperty(cellIndex = 4, cellName = "(5) Quantity of Drug Purchased", readOnly = true)
    private String quantityDrugPurchased;

    @ExcelProperty(cellIndex = 5, cellName = "(6) Purchase Date", readOnly = true)
    private String purchaseDate;

    @ExcelProperty(cellIndex = 6, cellName = "(7) Delivery Date", readOnly = true)
    private String deliveryDate;

    @ExcelProperty(cellIndex = 7, cellName = "(8) Expiry Date", readOnly = true)
    private String expiryDate;

    @ExcelProperty(cellIndex = 8, cellName = "(9) Quantity of balance stock as at 31 Dec 2017 (total for each batch number)", readOnly = true)
    private String quantityBalanceStock;

    @ExcelProperty(cellIndex = 9, cellName = "(10) Quantity of expired stock as at 31 Dec 2017 (total for each batch number)", readOnly = true)
    private String quantityExpiredStock;

    @ExcelProperty(cellIndex = 10, cellName = "(11) Remarks (if any)", readOnly = true)
    private String remarks;

}

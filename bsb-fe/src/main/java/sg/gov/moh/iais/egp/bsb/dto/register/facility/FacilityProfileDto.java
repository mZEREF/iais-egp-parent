package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilityProfileDto extends ValidatableNodeValue {
    private String facilityEntityId;
    private String facName;
    private String block;
    private String streetName;
    private String floor;
    private String unitNo;
    private String postalCode;
    private String isFacilityProtected;


    @JsonIgnore
    private ValidationResultDto validationResultDto;

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityProfile", new Object[]{this});
        return validationResultDto.isPass();
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    @Override
    public void clearValidationResult() {
        this.validationResultDto = null;
    }


    public String getFacilityEntityId() {
        return facilityEntityId;
    }

    public void setFacilityEntityId(String facilityEntityId) {
        this.facilityEntityId = facilityEntityId;
    }

    public String getFacName() {
        return facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getIsFacilityProtected() {
        return isFacilityProtected;
    }

    public void setIsFacilityProtected(String isFacilityProtected) {
        this.isFacilityProtected = isFacilityProtected;
    }



//    ---------------------------- request -> object ----------------------------------------------
    private static final String KEY_FAC_NAME = "facName";
    private static final String KEY_BLOCK = "block";
    private static final String KEY_STREET_NAME = "streetName";
    private static final String KEY_FLOOR = "floor";
    private static final String KEY_UNIT_NO = "unitNo";
    private static final String KEY_POSTAL_CODE = "postalCode";
    private static final String KEY_IS_PROTECTED_PLACE = "protectedPlace";

    public void reqObjMapping(HttpServletRequest request) {
        this.setFacName(ParamUtil.getString(request, KEY_FAC_NAME));
        this.setBlock(ParamUtil.getString(request, KEY_BLOCK));
        this.setStreetName(ParamUtil.getString(request, KEY_STREET_NAME));
        this.setFloor(ParamUtil.getString(request, KEY_FLOOR));
        this.setUnitNo(ParamUtil.getString(request, KEY_UNIT_NO));
        this.setPostalCode(ParamUtil.getString(request, KEY_POSTAL_CODE));
        this.setIsFacilityProtected(ParamUtil.getString(request, KEY_IS_PROTECTED_PLACE));
    }
}

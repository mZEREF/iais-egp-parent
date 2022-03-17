package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class CompanyProfileDto extends ValidatableNodeValue {
    private String draftAppNo;

    private String facCertEntityId;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.name")
    private String orgName;

    private String sameAddress;

    private String registered;

    private String block;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.addressType")
    private String addressType;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.floor")
    private String floor;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.unitNo")
    private String unitNo;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.building")
    private String building;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.streetName")
    private String streetName;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.postalCode")
    private String postalCode;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.city")
    private String city;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.state")
    private String state;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.country")
    private String country;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.organisation.yearEstablished")
    private String yearEstablished;

    private ValidationResultDto validationResultDto;


    @Override
    public boolean doValidation() {
//        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateOrganisationProfile", new Object[]{this});
//        return validationResultDto.isPass();
        return true;
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    public String getDraftAppNo() {
        return draftAppNo;
    }

    public void setDraftAppNo(String draftAppNo) {
        this.draftAppNo = draftAppNo;
    }

    public String getSameAddress() {
        return sameAddress;
    }

    public void setSameAddress(String sameAddress) {
        this.sameAddress = sameAddress;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAddressType() {
        return addressType;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getFacCertEntityId() {
        return facCertEntityId;
    }

    public void setFacCertEntityId(String facCertEntityId) {
        this.facCertEntityId = facCertEntityId;
    }

    public String getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getYearEstablished() {
        return yearEstablished;
    }

    public void setYearEstablished(String yearEstablished) {
        this.yearEstablished = yearEstablished;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    //    ---------------------------- request -> object ----------------------------------------------
    private static final String KEY_ORG_NAME = "orgName";
    private static final String KEY_ADDRESS_TYPE = "addressType";
    private static final String KEY_STREET_NAME = "streetName";
    private static final String KEY_FLOOR = "floor";
    private static final String KEY_UNIT_NO = "unitNo";
    private static final String KEY_BUILDING = "building";
    private static final String KEY_POSTAL_CODE = "postalCode";
    private static final String KEY_ADDRESS1 = "address1";
    private static final String KEY_ADDRESS2 = "address2";
    private static final String KEY_ADDRESS3= "address3";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_COUNTRY= "country";
    private static final String KEY_YEAR_ESTABLISHED = "yearEstablished";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CONTACT_NO = "contactNo";
    private static final String KEY_CONTACT_PERSON = "contactPerson";


    public void reqObjMapping(HttpServletRequest request) {
        this.setOrgName(ParamUtil.getString(request,KEY_ORG_NAME));
        this.setAddressType(ParamUtil.getString(request,KEY_ADDRESS_TYPE));
        this.setStreetName(ParamUtil.getString(request,KEY_STREET_NAME));
        this.setFloor(ParamUtil.getString(request,KEY_FLOOR));
        this.setUnitNo(ParamUtil.getString(request,KEY_UNIT_NO));
        this.setBuilding(ParamUtil.getString(request,KEY_BUILDING));
        this.setPostalCode(ParamUtil.getString(request,KEY_POSTAL_CODE));
        this.setCity(ParamUtil.getString(request,KEY_CITY));
        this.setState(ParamUtil.getString(request,KEY_STATE));
        this.setCountry(ParamUtil.getString(request,KEY_COUNTRY));
        this.setYearEstablished(ParamUtil.getString(request,KEY_YEAR_ESTABLISHED));
    }
}

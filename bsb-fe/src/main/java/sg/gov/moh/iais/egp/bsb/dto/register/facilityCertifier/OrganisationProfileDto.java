package sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.*;


/**
 * AUTHOR: YiMing
 * DATE:2021/9/26 15:12
 * DESCRIPTION: TODO
 **/

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class OrganisationProfileDto extends Node {
    private String orgName;
    private String addressType;
    private String floor;
    private String unitNo;
    private String building;
    private String streetName;
    private String address1;
    private String address2;
    private String address3;
    private String postalCode;
    private String city;
    private String state;
    private String country;
    private String yearEstablished;
    private String email;
    private String contactNo;
    private String contactPerson;
    private ValidationResultDto validationResultDto;

    public OrganisationProfileDto(String name, Node[] dependNodes) {
        super(name, dependNodes);
    }

    public static OrganisationProfileDto getInstance(Node[] dependNodes) {
        return new OrganisationProfileDto(NODE_NAME_ORG_PROFILE, dependNodes);
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateOrganisationProfile", new Object[]{this});
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
    public void needValidation() {
        super.needValidation();
        this.validationResultDto = null;
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

    public void setAddressType(String addressType) {
        this.addressType = addressType;
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

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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
        this.setAddress1(ParamUtil.getString(request,KEY_ADDRESS1));
        this.setAddress2(ParamUtil.getString(request,KEY_ADDRESS2));
        this.setAddress3(ParamUtil.getString(request,KEY_ADDRESS3));
        this.setCity(ParamUtil.getString(request,KEY_CITY));
        this.setState(ParamUtil.getString(request,KEY_STATE));
        this.setCountry(ParamUtil.getString(request,KEY_COUNTRY));
        this.setYearEstablished(ParamUtil.getString(request,KEY_YEAR_ESTABLISHED));
        this.setEmail(ParamUtil.getString(request,KEY_EMAIL));
        this.setContactNo(ParamUtil.getString(request,KEY_CONTACT_NO));
        this.setContactPerson(ParamUtil.getString(request,KEY_CONTACT_PERSON));
    }
}

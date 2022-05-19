package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Data
public class ProcModeDetails implements Serializable {
    private String procurementMode;
    private String facNameT;
    private String postalCodeT;
    private String addressTypeT;
    private String blockNoT;
    private String floorNoT;
    private String unitNoT;
    private String streetNameT;
    private String buildingNameT;

    private String contactPersonNameT;
    private String emailAddressT;
    private String contactNoT;
    private String expectedDateT;
    private String courierServiceProviderNameT;
    private String remarksT;

    private String facNameE;
    private String postalCodeE;
    private String addressTypeE;
    private String blockNoE;
    private String floorNoE;
    private String unitNoE;
    private String streetNameE;
    private String buildingNameE;
    private String countryE;
    private String cityE;
    private String stateE;

    private String contactPersonNameE;
    private String emailAddressE;
    private String contactNoE;
    private String expectedDateE;
    private String courierServiceProviderNameE;
    private String remarksE;



    private static final String KEY_PREFIX_PROCUREMENT_MODE                   = "procurementMode";
    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_SECTION_IDXES                             = "sectionIdx";

    private static final String KEY_PREFIX_TRANSFERRING_FACILITY_NAME                      = "facNameT";
    private static final String KEY_PREFIX_TRANSFERRING_POSTAL_CODE                        = "postalCodeT";
    private static final String KEY_PREFIX_TRANSFERRING_ADDRESS_TYPE                       = "addressTypeT";
    private static final String KEY_PREFIX_TRANSFERRING_BLOCK_NO                           = "blockNoT";
    private static final String KEY_PREFIX_TRANSFERRING_FLOOR_NO                           = "floorNoT";
    private static final String KEY_PREFIX_TRANSFERRING_UNIT_NO                            = "unitNoT";
    private static final String KEY_PREFIX_TRANSFERRING_STREET_NAME                        = "streetNameT";
    private static final String KEY_PREFIX_TRANSFERRING_BUILDING_NAME                      = "buildingNameT";
    private static final String KEY_PREFIX_TRANSFERRING_CONTACT_PERSON_NAME                = "contactPersonNameT";
    private static final String KEY_PREFIX_TRANSFERRING_EMAIL_ADDRESS                      = "emailAddressT";
    private static final String KEY_PREFIX_TRANSFERRING_CONTACT_NO                         = "contactNoT";
    private static final String KEY_PREFIX_TRANSFERRING_EXPECTED_DATE                      = "expectedDateT";
    private static final String KEY_PREFIX_TRANSFERRING_COURIER_SERVICE_PROVIDER_NAME      = "courierServiceProviderNameT";
    private static final String KEY_PREFIX_TRANSFERRING_REMARKS                            = "remarksT";

    private static final String KEY_PREFIX_EXPORTING_FACILITY_NAME                      = "facNameE";
    private static final String KEY_PREFIX_EXPORTING_POSTAL_CODE                        = "postalCodeE";
    private static final String KEY_PREFIX_EXPORTING_ADDRESS_TYPE                       = "addressTypeE";
    private static final String KEY_PREFIX_EXPORTING_BLOCK_NO                           = "blockNoE";
    private static final String KEY_PREFIX_EXPORTING_FLOOR_NO                           = "floorNoE";
    private static final String KEY_PREFIX_EXPORTING_UNIT_NO                            = "unitNoE";
    private static final String KEY_PREFIX_EXPORTING_STREET_NAME                        = "streetNameE";
    private static final String KEY_PREFIX_EXPORTING_BUILDING_NAME                      = "buildingNameE";
    private static final String KEY_PREFIX_EXPORTING_COUNTRY                            = "countryE";
    private static final String KEY_PREFIX_EXPORTING_CITY                               = "cityE";
    private static final String KEY_PREFIX_EXPORTING_STATE                              = "stateE";
    private static final String KEY_PREFIX_EXPORTING_CONTACT_PERSON_NAME                = "contactPersonNameE";
    private static final String KEY_PREFIX_EXPORTING_EMAIL_ADDRESS                      = "emailAddressE";
    private static final String KEY_PREFIX_EXPORTING_CONTACT_NO                         = "contactNoE";
    private static final String KEY_PREFIX_EXPORTING_EXPECTED_DATE                      = "expectedDateE";
    private static final String KEY_PREFIX_EXPORTING_COURIER_SERVICE_PROVIDER_NAME      = "courierServiceProviderNameE";
    private static final String KEY_PREFIX_EXPORTING_REMARKS                            = "remarksE";
    public void reqObjMapping(HttpServletRequest request,String idx){
        this.setProcurementMode(ParamUtil.getString(request, KEY_PREFIX_PROCUREMENT_MODE + SEPARATOR +idx));
        if (MasterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER.equals(this.getProcurementMode())) {
            this.setFacNameT(ParamUtil.getString(request,KEY_PREFIX_TRANSFERRING_FACILITY_NAME + SEPARATOR +idx));
            this.setPostalCodeT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_POSTAL_CODE + SEPARATOR +idx));
            this.setAddressTypeT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_ADDRESS_TYPE + SEPARATOR +idx));
            this.setBlockNoT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_BLOCK_NO + SEPARATOR +idx));
            this.setFloorNoT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_FLOOR_NO + SEPARATOR +idx));
            this.setUnitNoT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_UNIT_NO + SEPARATOR +idx));
            this.setStreetNameT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_STREET_NAME + SEPARATOR +idx));
            this.setBuildingNameT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_BUILDING_NAME + SEPARATOR +idx));
            this.setContactPersonNameT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_CONTACT_PERSON_NAME + SEPARATOR +idx));
            this.setEmailAddressT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_EMAIL_ADDRESS + SEPARATOR +idx));
            this.setContactNoT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_CONTACT_NO + SEPARATOR +idx));
            this.setExpectedDateT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_EXPECTED_DATE + SEPARATOR +idx));
            this.setCourierServiceProviderNameT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_COURIER_SERVICE_PROVIDER_NAME + SEPARATOR +idx));
            this.setRemarksT(ParamUtil.getString(request, KEY_PREFIX_TRANSFERRING_REMARKS + SEPARATOR +idx));
        } else if (MasterCodeConstants.PROCUREMENT_MODE_IMPORT.equals(this.getProcurementMode())) {
            this.setFacNameE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_FACILITY_NAME + SEPARATOR + idx));
            this.setPostalCodeE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_POSTAL_CODE + SEPARATOR +idx));
            this.setAddressTypeE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_ADDRESS_TYPE + SEPARATOR +idx));
            this.setBlockNoE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_BLOCK_NO + SEPARATOR +idx));
            this.setFloorNoE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_FLOOR_NO + SEPARATOR +idx));
            this.setUnitNoE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_UNIT_NO + SEPARATOR +idx));
            this.setStreetNameE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_STREET_NAME + SEPARATOR +idx));
            this.setBuildingNameE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_BUILDING_NAME + SEPARATOR +idx));
            this.setCountryE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_COUNTRY + SEPARATOR + idx));
            this.setCityE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_CITY + SEPARATOR + idx));
            this.setStateE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_STATE + SEPARATOR + idx));
            this.setContactPersonNameE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_CONTACT_PERSON_NAME + SEPARATOR +idx));
            this.setEmailAddressE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_EMAIL_ADDRESS + SEPARATOR +idx));
            this.setContactNoE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_CONTACT_NO + SEPARATOR +idx));
            this.setExpectedDateE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_EXPECTED_DATE + SEPARATOR +idx));
            this.setCourierServiceProviderNameE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_COURIER_SERVICE_PROVIDER_NAME + SEPARATOR +idx));
            this.setRemarksE(ParamUtil.getString(request, KEY_PREFIX_EXPORTING_REMARKS + SEPARATOR +idx));
        }
    }
}

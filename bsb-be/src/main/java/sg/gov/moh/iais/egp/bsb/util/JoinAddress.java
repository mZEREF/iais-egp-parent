package sg.gov.moh.iais.egp.bsb.util;

import sg.gov.moh.iais.egp.bsb.entity.Application;

/**
 * @author Zhu Tangtang
 * @date 2021/8/20 10:41
 */
public class JoinAddress {

    private static StringBuilder joinAdd(String add){
        StringBuilder builder=new StringBuilder();
        if (add!=null){
            builder.append(add).append(" ");
        }else{
            builder.append("? ");
        }
        return builder;
    }

    /**
    * This method is used to concatenate addresses for display on the page
    */
    public static String joinAddress(Application application){
        String blockNo = application.getFacility().getBlkNo();
        String streetName = application.getFacility().getStreetName();
        String floorNo = application.getFacility().getFloorNo();
        String unitNo = application.getFacility().getUnitNo();
        String postalCode = application.getFacility().getPostalCode();

        StringBuilder builder=new StringBuilder();
        String facilityAddress="";
        builder.append(joinAdd(blockNo)).append(joinAdd(streetName));
        if (floorNo!=null){
            builder.append(floorNo).append("-");
        }else{
            builder.append("?-");
        }
        builder.append(joinAdd(unitNo)).append(joinAdd(postalCode));
        facilityAddress=builder.toString();

        return facilityAddress;
    }
}

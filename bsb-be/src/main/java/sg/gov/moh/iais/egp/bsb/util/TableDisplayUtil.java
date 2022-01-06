package sg.gov.moh.iais.egp.bsb.util;

import org.springframework.util.CollectionUtils;
import java.util.List;


public class TableDisplayUtil {
    private TableDisplayUtil() {}

    private static String displayItem(String item) {
        return item != null ? item : "?";
    }

    public static String getOneLineAddress(String blkNo, String streetName, String floorNo, String unitNo, String postalCode) {
        String facilityAddress="";
        StringBuilder builder = new StringBuilder();
        builder.append(displayItem(blkNo)).append(' ')
                .append(displayItem(streetName)).append(' ')
                .append(displayItem(floorNo)).append('-')
                .append(displayItem(unitNo)).append(' ')
                .append(displayItem(postalCode));
        facilityAddress = builder.toString();
        return facilityAddress;
    }

    public static String getOneLineBiologicalName(List<String> bioNames){
        if(CollectionUtils.isEmpty(bioNames)){
            return null;
        }
        return String.join(",", bioNames);
    }

    public static String getOneLineRiskLevel(List<String> riskLevels){
        if(CollectionUtils.isEmpty(riskLevels)){
            return null;
        }
        return String.join(",", riskLevels);
    }

    public static String getOneLineAdmin(List<String> admins){
        if(CollectionUtils.isEmpty(admins)){
            return null;
        }
        return String.join(",", admins);
    }


    /**
     * This method is intended for the initialization of index list in the 'add more' function.
     * If the size is 4, the resutl is '0 1 2 3 '.
     * @param size current section amount
     * @return a string in the format '0 1 2 ...'
     */
    public static String indexes(int size) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(i).append(' ');
        }
        return builder.toString();
    }
}

package sg.gov.moh.iais.egp.bsb.util;


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
}

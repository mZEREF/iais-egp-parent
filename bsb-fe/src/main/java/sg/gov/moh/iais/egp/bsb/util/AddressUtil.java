package sg.gov.moh.iais.egp.bsb.util;

import org.springframework.util.StringUtils;

public class AddressUtil {
    private AddressUtil() {}

    public static String getOneLineAddress(String block, String streetName, String floor, String unitNo, String postalCode) {
        StringBuilder sb = new StringBuilder();
        boolean existBlock = StringUtils.hasLength(block);
        boolean existStreetName = StringUtils.hasLength(streetName);
        boolean existFloor = StringUtils.hasLength(floor);
        boolean existUnitNo = StringUtils.hasLength(unitNo);
        boolean existPostalCode = StringUtils.hasLength(postalCode);

        if (existBlock) {
            sb.append(block);
        }
        if (existBlock && existStreetName) {
            sb.append(' ');
        }
        if (existStreetName) {
            sb.append(streetName);
        }
        if (existStreetName && (existFloor || existUnitNo || existPostalCode)) {
            sb.append(", ");
        }
        if (existFloor) {
            sb.append(floor);
        }
        if (existFloor && existUnitNo) {
            sb.append('-');
        }
        if (existUnitNo) {
            sb.append(unitNo);
        }
        if (existUnitNo && existPostalCode) {
            sb.append(' ');
        }
        if (existPostalCode) {
            sb.append(postalCode);
        }
        return sb.toString();
    }
}

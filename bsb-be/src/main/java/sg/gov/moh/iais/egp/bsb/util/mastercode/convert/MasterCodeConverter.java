package sg.gov.moh.iais.egp.bsb.util.mastercode.convert;

public interface MasterCodeConverter {
    String code2Value(String code);

    String value2Code(String value);

    String code2Desc(String code);

    String desc2Code(String desc);
}

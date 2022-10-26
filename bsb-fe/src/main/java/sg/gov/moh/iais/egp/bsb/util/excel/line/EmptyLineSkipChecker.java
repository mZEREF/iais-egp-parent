package sg.gov.moh.iais.egp.bsb.util.excel.line;

public class EmptyLineSkipChecker implements LineSkipChecker {
    public static final EmptyLineSkipChecker INSTANCE = new EmptyLineSkipChecker();

    @Override
    public boolean check(StringBuilder line) {
        return line.length() == 0;
    }
}

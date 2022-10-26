package sg.gov.moh.iais.egp.bsb.util.excel.line;

public class NoLineSkipChecker implements LineSkipChecker {
    public static final NoLineSkipChecker INSTANCE = new NoLineSkipChecker();

    @Override
    public boolean check(StringBuilder line) {
        return false;
    }
}

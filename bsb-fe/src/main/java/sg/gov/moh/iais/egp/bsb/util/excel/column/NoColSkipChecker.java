package sg.gov.moh.iais.egp.bsb.util.excel.column;

public class NoColSkipChecker implements ColSkipChecker {
    public static final NoColSkipChecker INSTANCE = new NoColSkipChecker();

    @Override
    public boolean check(int idx) {
        return false;
    }
}

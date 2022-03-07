package sg.gov.moh.iais.egp.bsb.util.excel.column;


public class FirstColSkipChecker implements ColSkipChecker {
    public static final FirstColSkipChecker INSTANCE = new FirstColSkipChecker();

    @Override
    public boolean check(int idx) {
        return idx == 0;
    }
}

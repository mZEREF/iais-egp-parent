package sg.gov.moh.iais.egp.bsb.util.excel.column;

public class ColumnsSkipChecker implements ColSkipChecker {
    private final int[] ignoreIndex;

    public ColumnsSkipChecker(int[] ignoreIndex) {
        this.ignoreIndex = ignoreIndex == null ? new int[]{} : ignoreIndex;
    }

    @Override
    public boolean check(int idx) {
        boolean skip = false;
        for (int i : ignoreIndex) {
            if (i == idx) {
                skip = true;
                break;
            }
        }
        return skip;
    }
}

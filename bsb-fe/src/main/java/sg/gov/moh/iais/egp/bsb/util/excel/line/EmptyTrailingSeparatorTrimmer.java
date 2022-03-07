package sg.gov.moh.iais.egp.bsb.util.excel.line;

public class EmptyTrailingSeparatorTrimmer implements LinePostProcessor {
    public static final EmptyTrailingSeparatorTrimmer INSTANCE = new EmptyTrailingSeparatorTrimmer();

    @Override
    public void process(StringBuilder line) {
        while (line.length() > 0 && line.charAt(line.length() - 1) == ',') {
            line.deleteCharAt(line.length() - 1);
        }
    }
}

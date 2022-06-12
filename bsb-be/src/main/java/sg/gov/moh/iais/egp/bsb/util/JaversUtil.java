package sg.gov.moh.iais.egp.bsb.util;

import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JaversUtil {
    public static final String SEPARATOR = "--s--";

    private JaversUtil() {}

    public static void convert(String sumDiffMapKey, Diff diff, Map<String, Map<String, String>> sumDiffMap) {
        // get change list
        List<ValueChange> valueChanges = diff.getChangesByType(ValueChange.class);
        if (!CollectionUtils.isEmpty(valueChanges)) {
            Map<String, String> diffMap = new HashMap<>(valueChanges.size());
            for (ValueChange valueChange : valueChanges) {
                // key: field name
                diffMap.put(valueChange.getPropertyName(), (String) valueChange.getRight());
            }
            sumDiffMap.put(sumDiffMapKey, diffMap);
        }
    }

    public static void convertList(String sumDiffMapKey, Diff diff, Map<String, Map<String, String>> sumDiffMap) {
        // get change list
        List<ValueChange> valueChanges = diff.getChangesByType(ValueChange.class);
        if (!CollectionUtils.isEmpty(valueChanges)) {
            Map<String, String> diffMap = new HashMap<>(valueChanges.size());
            for (ValueChange valueChange : valueChanges) {
                // key: field name + "--s--" + field value annotated with @Id (The @Id annotation field must be unique and non-empty)
                String diffMapKey = valueChange.getPropertyName() + SEPARATOR + valueChange.getAffectedLocalId();
                diffMap.put(diffMapKey, (String) valueChange.getRight());
            }
            sumDiffMap.put(sumDiffMapKey, diffMap);
        }
    }
}

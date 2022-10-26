package sg.gov.moh.iais.egp.bsb.util;

import com.google.common.collect.Sets;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Change;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.javers.core.diff.changetype.PropertyChange;

import java.util.Collection;
import java.util.List;
import java.util.Set;


/**
 * Util methods to use Javers library to compare two objects or collections.
 * <p>
 * There are three types in Javers:
 * <ol>
 *     <li>Entity</li>
 *     <li>Value object</li>
 *     <li>Value</li>
 * </ol>
 * Entity means an object contains a property annotated {@link org.javers.core.metamodel.annotation.Id}.
 * <br/>
 * Value object is an object without the {@code @Id}
 * <br/>
 * Value represents a value, such as BigDecimal, LocalDate, String, URL.
 * <p>
 * When you compare a collection of elements, if the elements are entities, Javers will use the ID to identify the
 * changes. If the elements are value objects, Javers will only compare them by index.
 */
public class JaversUtil {
    private JaversUtil() {}


    /**
     * Compares Javers Entities (with @Id), Value objects (without @Id) or top-level collections of primitives or
     * value. If you want to compare top-level collections of entities or value objects, please use
     * {@link #compareCollections(java.util.Collection, java.util.Collection, Class)}.
     * @param oldVersion old object
     * @param currentVersion current object
     * @return a unique set of properties
     */
    public static Set<String> compare(Object oldVersion, Object currentVersion) {
        Javers javers = JaversBuilder.javers()
                .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
                .build();
        Diff diff = javers.compare(oldVersion, currentVersion);
        return convertDiffToChangedProperties(diff);
    }

    /**
     * Compares top-level collections of Javers entity or value object.
     * @param oldVersion old objects collection
     * @param currentVersion current objects collection
     * @param itemClass element type class
     * @return a unique set of properties changed in any of the elements
     */
    public static <T> Set<String> compareCollections(Collection<T> oldVersion, Collection<T> currentVersion, Class<T> itemClass) {
        Javers javers = JaversBuilder.javers()
                .withListCompareAlgorithm(ListCompareAlgorithm.LEVENSHTEIN_DISTANCE)
                .build();
        Diff diff = javers.compareCollections(oldVersion, currentVersion, itemClass);
        return convertDiffToChangedProperties(diff);
    }

    /**
     * Converts Javers Diff to a unique list of changed properties.
     * This method only cares about value changes
     * @return a unique set of properties
     */
    private static Set<String> convertDiffToChangedProperties(Diff diff) {
        List<Change> listChanges = diff.getChanges(PropertyChange.class::isInstance);
        Set<String> changedProperties = Sets.newLinkedHashSetWithExpectedSize(listChanges.size());
        for (Change c : listChanges) {
            changedProperties.add(((PropertyChange<?>) c).getPropertyName());
        }
        return changedProperties;
    }
}

package sg.gov.moh.iais.egp.bsb.dto.info.bat;

import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sg.gov.moh.iais.egp.bsb.util.mastercode.retrieve.MasterCodeRetriever;
import sg.gov.moh.iais.egp.common.modal.view.RichSelectOption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RichSchedule implements Comparable<RichSchedule> {
    @NonNull
    private String schedule;
    private boolean disabled;


    public static RichSchedule of(String schedule) {
        return new RichSchedule(schedule, false);
    }

    public static RichSchedule of(String schedule, boolean disabled) {
        return new RichSchedule(schedule, disabled);
    }

    public static RichSchedule ofReverse(String schedule, boolean enabled) {
        return new RichSchedule(schedule, !enabled);
    }


    public static boolean containsEnabledSchedule(Collection<RichSchedule> richSchedules) {
        boolean contains = false;
        for (RichSchedule richSchedule : richSchedules) {
            if (!richSchedule.isDisabled()) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    public static Set<String> retrieveEnabledSchedules(Collection<RichSchedule> richSchedules) {
        return CollectionUtils.conditionalTransform2Set(s -> !s.isDisabled(), RichSchedule::getSchedule, richSchedules);
    }


    public static List<RichSelectOption> customRichOption(Collection<RichSchedule> richSchedules) {
        return CollectionUtils.safeApplyColl(RichSchedule::customRichOption0, richSchedules);
    }

    public static List<RichSelectOption> customRichOption0(Collection<RichSchedule> richSchedules) {
        List<RichSelectOption> optionList = new ArrayList<>(richSchedules.size());
        MasterCodeRetriever retriever = MasterCodeHolder.SCHEDULE.retriever();
        for(RichSchedule s : richSchedules) {
            MasterCodeView view = retriever.retrieveByCode(s.getSchedule());
            RichSelectOption option = new RichSelectOption(view.getCode(), view.getCodeValue(), s.isDisabled());
            optionList.add(option);
        }
        return optionList;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RichSchedule that = (RichSchedule) o;

        return schedule.equals(that.schedule);
    }

    @Override
    public int hashCode() {
        return schedule.hashCode();
    }

    @Override
    public int compareTo(RichSchedule o) {
        return this.schedule.compareTo(o.getSchedule());
    }
}

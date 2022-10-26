package sg.gov.moh.iais.egp.bsb.dto.info.bat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RichBatCodeInfo {
    @NonNull
    private String code;
    private String name;
    private boolean disable;


    public static RichBatCodeInfo of(BatCodeInfo batCodeInfo) {
        return new RichBatCodeInfo(batCodeInfo.getCode(), batCodeInfo.getName(), false);
    }

    public static RichBatCodeInfo of(BatCodeInfo batCodeInfo, boolean disable) {
        return new RichBatCodeInfo(batCodeInfo.getCode(), batCodeInfo.getName(), disable);
    }

    public static List<RichBatCodeInfo> of(List<BatCodeInfo> batCodeInfoList) {
        return CollectionUtils.listMapping(RichBatCodeInfo::of, batCodeInfoList);
    }

    public static List<RichBatCodeInfo> of(List<BatCodeInfo> batCodeInfoList, final Set<String> disabledBatCodes) {
        return CollectionUtils.listMapping(info -> RichBatCodeInfo.of(info, disabledBatCodes.contains(info.getCode())), batCodeInfoList);
    }


    public static Set<String> retrieveEnabledBatCodes(Collection<RichBatCodeInfo> richBatCodeInfos) {
        return CollectionUtils.conditionalTransform2Set(b -> !b.isDisable(), RichBatCodeInfo::getCode, richBatCodeInfos);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RichBatCodeInfo that = (RichBatCodeInfo) o;

        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}

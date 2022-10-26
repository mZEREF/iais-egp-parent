package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.edit.FieldEditableJudger;
import sg.gov.moh.iais.egp.bsb.util.RequestObjectMappingUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Bean holds data in BATInfo page.
 * That page is used by many modules, their DTOs can extend this class to inherit the method to read data.
 */
public class BATInfoPageDto implements Serializable {
    @Getter @Setter
    private List<BATInfo> batInfos;

    /* For validation */
    @Setter @Getter
    private boolean protectedPlace;


    public BATInfoPageDto() {
        batInfos = new ArrayList<>();
        batInfos.add(new BATInfo());
    }


    public void clearBatInfos() {
        this.batInfos.clear();
    }



    // ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_SECTION_IDXES                       = "sectionIdx";
    private static final String KEY_DELETED_SECTION_IDXES               = "deletedSectionIdx";

    @SneakyThrows({InstantiationException.class, IllegalAccessException.class})
    public void reqObjMapping(HttpServletRequest request, FieldEditableJudger editableJudger) {
        String idxes = request.getParameter(KEY_SECTION_IDXES);
        Set<Integer> idxSet = StringUtils.hasLength(idxes) ? Arrays.stream(idxes.trim().split(" +")).map(Integer::valueOf).collect(Collectors.toSet()) : Collections.emptySet();
        String deletedIdxes = request.getParameter(KEY_DELETED_SECTION_IDXES);
        Set<Integer> deletedIdxSet = StringUtils.hasLength(deletedIdxes) ? Arrays.stream(deletedIdxes.trim().split(" +")).map(Integer::valueOf).collect(Collectors.toSet()) : Collections.emptySet();

        if (idxSet.isEmpty()) {
            clearBatInfos();
            batInfos.add(new BATInfo());
        } else {
            Map<Integer, BATInfo> map = RequestObjectMappingUtil.readAndReuseSectionDto(BATInfo.class, batInfos, idxSet, deletedIdxSet);
            map.forEach((k, v) -> v.reqObjMapping(request, k.toString(), editableJudger));
            this.batInfos = new ArrayList<>(map.values());
        }
    }
}

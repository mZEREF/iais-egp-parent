package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsNCMiscDto {
    @Data
    public static class Unit {
        private String applicantRemarks;
        private String mohRemarks;
    }

    private Map<String, Unit> miscMap;


    /**
     * Gets NC Unit by key
     * @param key sectionId--v--itemId
     * @return a non-null Unit instance
     */
    public Unit unit(String key) {
        return miscMap.computeIfAbsent(key, k -> new Unit());
    }
}

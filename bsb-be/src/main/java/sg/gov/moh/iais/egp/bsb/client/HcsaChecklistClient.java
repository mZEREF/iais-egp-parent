package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class)
public interface HcsaChecklistClient {
    @PostMapping(path = "/iais-hcsa-checklist/item/results", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<CheckItemQueryDto>> listChklItem(SearchParam searchParam);

    @PostMapping(path = "/iais-hcsa-checklist/item/items-by-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ChecklistItemDto>> listChklItemByItemId(List<String> itemIds);
}

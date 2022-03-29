package sg.gov.moh.iais.egp.bsb.util;

import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.constant.ChecklistSvcCode;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;

import java.util.HashMap;
import java.util.Map;

public class ChecklistHelper {
    private ChecklistHelper() {}

    private static final String SECTION_ITEM_SEPARATOR = "--v--";

    private static final String ERR_MSG_CONFIG_DTO_NULL = "Config DTO should not be null";


    /**
     * Find the suitable service code for checklist according facility type
     *
     * @param classification facility classification
     * @return an enum instance of ChecklistSvcCode; or null if the data is not covered by the logic here.
     */
    public static ChecklistSvcCode getSvcCodeByClass(String classification) {
        Assert.hasLength(classification, "Classification should not be empty");
        ChecklistSvcCode svcCode = null;
        switch (classification) {
            case MasterCodeConstants.FAC_CLASSIFICATION_BSL3:
                svcCode = ChecklistSvcCode.CHC;
                break;
            case MasterCodeConstants.FAC_CLASSIFICATION_BSL4:
                svcCode = ChecklistSvcCode.CMC;
                break;
            case MasterCodeConstants.FAC_CLASSIFICATION_UF:
                svcCode = ChecklistSvcCode.UFT;
                break;
            case MasterCodeConstants.FAC_CLASSIFICATION_LSPF:
                svcCode = ChecklistSvcCode.LSP;
                break;
            case MasterCodeConstants.FAC_CLASSIFICATION_RF:
                svcCode = ChecklistSvcCode.RFT;
                break;
        }
        return svcCode;
    }


    /** Find the suitable type of checklist according to application type.
     * This method may be expanded in the future, it does not cover all types.
     * @param appType application type
     * @return module of checklist or null if no matching
     */
    public static String getChecklistModuleByAppType(String appType) {
        Assert.hasLength(appType, "App type should be not empty");
        String type;
        switch (appType) {
            case MasterCodeConstants.APP_TYPE_NEW:
                type = HcsaChecklistConstants.NEW;
                break;
            case MasterCodeConstants.APP_TYPE_RENEW:
                type = HcsaChecklistConstants.RENEWAL;
                break;
            case MasterCodeConstants.APP_TYPE_RFC:
                type = HcsaChecklistConstants.AMENDMENT;
                break;
            default:
                type = null;
                break;
        }
        return type;
    }


    /** Find the item DTO from a config DTO by section ID and item ID.
     * @see #findItemByIdPath(ChecklistConfigDto, String, String)
     * @param sectionIdItemIdKey sectionId--v--itemId
     * @return target DTO, or null if not found
     */
    public static ChecklistItemDto findItemByIdPath(ChecklistConfigDto configDto, String sectionIdItemIdKey) {
        String[] keyParts = sectionIdItemIdKey.split(SECTION_ITEM_SEPARATOR);
        return findItemByIdPath(configDto, keyParts[0], keyParts[1]);
    }

    /** Find the item DTO from a config DTo by section ID and item ID.
     * Attention, the efficiency of this method is low. If you need to call this method a lot of times, you'd better
     * Convert the configDto into a map. {@link  #convertConfigDtoIntoIdMap(ChecklistConfigDto)}
     * @return target DTO, or null if not found
     */
    public static ChecklistItemDto findItemByIdPath(ChecklistConfigDto configDto, String sectionId, String itemId) {
        ChecklistItemDto resultDto = null;
        for (ChecklistSectionDto sectionDto : configDto.getSectionDtos()) {
            if (sectionDto.getId().equals(sectionId)) {
                for (ChecklistItemDto itemDto : sectionDto.getChecklistItemDtos()) {
                    if (itemDto.getItemId().equals(itemId)) {
                        resultDto = itemDto;
                        break;
                    }
                }
                break;
            }
        }
        return resultDto;
    }


    /** Convert a config DTO into a map in order to provide the convenience to find the item.
     * @return a map whose key is sectionId--v--itemId, the value is the item DTO
     */
    public static Map<String, ChecklistItemDto> convertConfigDtoIntoIdMap(ChecklistConfigDto configDto) {
        Map<String, ChecklistItemDto> map = new HashMap<>();
        for (ChecklistSectionDto sectionDto : configDto.getSectionDtos()) {
            for (ChecklistItemDto itemDto : sectionDto.getChecklistItemDtos()) {
                map.put(sectionDto.getId() + SECTION_ITEM_SEPARATOR + itemDto.getItemId(), itemDto);
            }
        }
        return map;
    }




    public interface ConfigItemFinder {
        ChecklistItemDto findItem(String sectionId, String itemId);
    }

    /** Will determine use list or map according to queryAmt */
    public static class ConfigItemFinderHelper implements ConfigItemFinder {
        private final ConfigItemFinder finder;

        public ConfigItemFinderHelper(ChecklistConfigDto configDto, int queryAmt) {
            // 3 is a magic number, actually we need to test the speed/efficiency to determine the number
            this.finder = queryAmt > 3 ? new ConfigItemMapFinder(configDto) : new ConfigItemListFinder(configDto);
        }

        @Override
        public ChecklistItemDto findItem(String sectionId, String itemId) {
            return finder.findItem(sectionId, itemId);
        }
    }

    /** Find item by walk through list, intended for little query amount */
    private static class ConfigItemListFinder implements ConfigItemFinder {
        private final ChecklistConfigDto configDto;

        public ConfigItemListFinder(ChecklistConfigDto configDto) {
            Assert.notNull(configDto, ERR_MSG_CONFIG_DTO_NULL);
            this.configDto = configDto;
        }

        @Override
        public ChecklistItemDto findItem(String sectionId, String itemId) {
            return findItemByIdPath(this.configDto, sectionId, itemId);
        }
    }

    /** Convert config DTO into map first, intended for a lot of query amount */
    private static class ConfigItemMapFinder implements ConfigItemFinder {
        private final Map<String, ChecklistItemDto> configMap;

        public ConfigItemMapFinder(ChecklistConfigDto configDto) {
            Assert.notNull(configDto, ERR_MSG_CONFIG_DTO_NULL);
            this.configMap = convertConfigDtoIntoIdMap(configDto);
        }

        @Override
        public ChecklistItemDto findItem(String sectionId, String itemId) {
            return configMap.get(sectionId + SECTION_ITEM_SEPARATOR + itemId);
        }
    }
}

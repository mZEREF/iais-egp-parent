package sg.gov.moh.iais.egp.bsb.dto.file;

import java.util.Map;


/**
 * A refreshable document DTO which contains both new created and saved document info.
 * And we can save the new files anytime and then update the DTO status.
 */
public interface RefreshableDocDto {
    /**
     * Prepares the payload which will be used to call saving files API of file repo
     * @return a map, whose key is keyword and value is file content bytes
     */
    Map<String, byte[]> prepare4Saving();

    /**
     * Use the result of saving file API to update the DTO status
     * @param idMap whose key is keyword and value is file repo ID
     */
    void refreshAfterSave(Map<String, String> idMap);




    /**
     * Creates a {@link sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo} according to {@link sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo}
     * @see #refreshDocMap(java.util.Map, java.util.Map, java.util.Map)
     */
    static DocRecordInfo refreshDocInfo(NewDocInfo newDoc, String repoId) {
        if (newDoc == null || repoId == null) {
            return null;
        }
        DocRecordInfo docRecordInfo = new DocRecordInfo();
        docRecordInfo.setDocType(newDoc.getDocType());
        docRecordInfo.setFilename(newDoc.getFilename());
        docRecordInfo.setSize(newDoc.getSize());
        docRecordInfo.setRepoId(repoId);
        docRecordInfo.setSubmitBy(newDoc.getSubmitBy());
        docRecordInfo.setSubmitDate(newDoc.getSubmitDate());
        return docRecordInfo;
    }


    /**
     * If a refreshable document DTO use {@link sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo} map and
     * {@link sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo} map to keep the status, it can use this method
     * to update the status after saving.
     */
    static void refreshDocMap(Map<String, NewDocInfo> newDocMap,
                              Map<String, DocRecordInfo> savedDocMap,
                              Map<String, String> idMap) {
        if (newDocMap == null || newDocMap.isEmpty() || savedDocMap == null || idMap == null) {
            return;
        }
        for (Map.Entry<String, String> idEntry : idMap.entrySet()) {
            String newDocId = idEntry.getKey();
            String repoId = idEntry.getValue();

            NewDocInfo newDocInfo = newDocMap.get(newDocId);
            if (newDocInfo != null) {
                DocRecordInfo docRecordInfo = refreshDocInfo(newDocInfo, repoId);
                savedDocMap.put(repoId, docRecordInfo);
            }
        }
        newDocMap.clear();
    }
}

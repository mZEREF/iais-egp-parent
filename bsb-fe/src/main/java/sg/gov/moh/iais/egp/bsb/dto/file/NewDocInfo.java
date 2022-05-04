package sg.gov.moh.iais.egp.bsb.dto.file;

import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.multipart.ByteArrayMultipartFile;

import java.io.File;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
public class NewDocInfo implements Serializable {

    private String tmpId;
    private String docType;
    private String docSubType;
    private String filename;
    private long size;
    private Date submitDate;
    private String submitBy;
    private ByteArrayMultipartFile multipartFile;

    private File file;
    private String userId;
    private String maskedTempId;

    /**
     * Convert this new doc to meta info object with a specific module name
     *
     * @see DocMeta#DocMeta(String, String, String, long)
     */
    public DocMeta toDocMeta() {
        return new DocMeta(tmpId, docType, filename, size);
    }

    public static NewDocInfo toNewDocInfo(String docType, String userId, File file) {
        if (file == null) {
            return null;
        }
        long length = file.length();
        NewDocInfo newDocInfo = new NewDocInfo();
        String tmpId = docType + length + System.nanoTime();
        newDocInfo.setTmpId(tmpId);
        newDocInfo.setDocType(docType);
        newDocInfo.setFilename(file.getName());
        newDocInfo.setSize(length);
        newDocInfo.setSubmitDate(new Date());
        newDocInfo.setSubmitBy(userId);
        ByteArrayMultipartFile multipartFile = new ByteArrayMultipartFile(file.getName(), file.getName(), "multipart/form-data",
                FileUtils.readFileToByteArray(file));
        newDocInfo.setMultipartFile(multipartFile);
        newDocInfo.setFile(file);
        return newDocInfo;
    }

}

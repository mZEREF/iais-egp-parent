package sg.gov.moh.iais.egp.bsb.common.multipart;

import lombok.NonNull;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;


/**
 * A serializable version implementation of MultipartFile
 * This class is used to keep the Multipart file objects in session
 */
@Value
public class ByteArrayMultipartFile implements MultipartFile, Serializable {
    String name;
    String originalFilename;
    String contentType;
    @NonNull
    byte[] bytes;



    @Override
    public boolean isEmpty() {
        return bytes.length == 0;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public InputStream getInputStream(){
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (OutputStream outputStream = Files.newOutputStream(dest.toPath())) {
            outputStream.write(bytes);
        }
    }
}

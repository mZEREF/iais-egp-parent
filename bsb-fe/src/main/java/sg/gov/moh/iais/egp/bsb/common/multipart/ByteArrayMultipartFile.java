package sg.gov.moh.iais.egp.bsb.common.multipart;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.NonNull;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;


/**
 * A serializable version implementation of MultipartFile.
 * This class is used to keep the Multipart file objects in session.
 */
@Value
public class ByteArrayMultipartFile implements MultipartFile, Serializable {
    private static final String FIELD_NAME = "name";
    private static final String FIELD_FILENAME = "originalFilename";
    private static final String FIELD_CONTENT_TYPE = "contentType";
    private static final String FIELD_BYTES = "bytes";

    String name;
    String originalFilename;
    String contentType;
    byte[] bytes;

    @JsonCreator
    public ByteArrayMultipartFile(@JsonProperty(FIELD_NAME) String name,
                                  @JsonProperty(FIELD_FILENAME) String originalFilename,
                                  @JsonProperty(FIELD_CONTENT_TYPE) String contentType,
                                  @JsonProperty(FIELD_BYTES) @NonNull byte[] bytes) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.bytes = bytes;
    }


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


    @JsonValue
    public Map<String, Object> jsonMap() {
        Map<String, Object> map = new HashMap<>( (int)(4 / 0.75) + 1);
        map.put(FIELD_NAME, name);
        map.put(FIELD_FILENAME, originalFilename);
        map.put(FIELD_CONTENT_TYPE, contentType);
        map.put(FIELD_BYTES, bytes);
        return map;
    }
}

package sg.gov.moh.iais.egp.bsb.util.excel;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.util.List;

public class CsvConvertUtil {
    private CsvConvertUtil() {}

    /**
     * Convert a CSV string to resulting data
     * @param csv CSV string with header
     * @param clz Wanted object class
     * @return MappingIterator of the result data
     */
    public static <T> MappingIterator<T> csv2Iterator(String csv, Class<T> clz) throws IOException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();
        return mapper
                .readerFor(clz)
                .with(schema)
                .with(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE)
                .with(CsvParser.Feature.SKIP_EMPTY_LINES)
                .readValues(csv);
    }

    /** Convert a CSV string to resulting data list */
    public static <T> List<T> csv2List(String csv, Class<T> clz) throws IOException {
        MappingIterator<T> it = csv2Iterator(csv, clz);
        return it.readAll();
    }


    /**
     * Checks to see whether the field - which consists of the formatted contents of an Excel worksheet cell
     * encapsulated within a String - contains any embedded characters that must be escaped.
     * <p>
     * Firstly, in regard to any embedded speech marks ("), each occurrence should be escaped with another speech mark
     * and the whole field then surrounded with speech marks. Thus, if a field holds <em>"Hello" he said</em> then it
     * should be modified to appear as <em>"""Hello"" he said"</em>.
     * <p>
     * Furthermore, if the field contains either embedded separator or EOL characters, it should also be surrounded
     * with speech marks. As a result <em>1,400</em> would become <em>"1,400"</em>.
     * @param field An instance of the String class encapsulating the formatted contents
     *              of a cell on an Excel worksheet.
     * @return A String that encapsulates the formatted contents of that Excel worksheet cell but with any embedded
     *         separator, EOL or speech mark characters correctly escaped.
     */
    public static String escapeEmbeddedCharacters(String field) {
        StringBuilder buffer;
        /* Firstly, check if there are any speech marks (\") in the field, each occurrence must be escaped
         * with another set of speech marks. And then the entire field should be enclosed within another
         * set of speech marks. Thus, "Yes" he said would become """Yes"" he said" */
        if(field.contains("\"")) {
            buffer = new StringBuilder(field.replace("\"", "\\\"\\\""));
            buffer.insert(0, "\"");
            buffer.append("\"");
        } else {
            /* If the field contains either embedded separator or EOL characters, then escape the whole field
             * by surrounding it with speech marks. */
            buffer = new StringBuilder(field);
            if((buffer.indexOf(",")) > -1 || (buffer.indexOf("\n")) > -1) {
                buffer.insert(0, "\"");
                buffer.append("\"");
            }
        }
        return(buffer.toString().trim());
    }
}

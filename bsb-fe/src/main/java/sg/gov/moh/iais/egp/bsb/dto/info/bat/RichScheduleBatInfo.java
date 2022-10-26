package sg.gov.moh.iais.egp.bsb.dto.info.bat;

import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RichScheduleBatInfo {
    @JsonSerialize(keyUsing = RichScheduleKeySerializer.class)
    @JsonDeserialize(keyUsing = RichScheduleKeyDeserializer.class)
    Map<RichSchedule, List<RichBatCodeInfo>> scheduleBatMap;



    public static class RichScheduleKeySerializer extends StdSerializer<RichSchedule> {
        public RichScheduleKeySerializer() {
            super(RichSchedule.class);
        }

        @Override
        public void serialize(RichSchedule value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeFieldName(JsonUtil.parseToJson(value));
        }
    }


    public static class RichScheduleKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return JsonUtil.parseToObject(key, RichSchedule.class);
        }
    }
}

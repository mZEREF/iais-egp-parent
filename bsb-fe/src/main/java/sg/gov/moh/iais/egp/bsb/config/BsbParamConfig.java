package sg.gov.moh.iais.egp.bsb.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RefreshScope
@Getter
@Setter
public class BsbParamConfig implements Serializable {
    @Value("${iais.system.maximum.file.size}")
    private int maxFileSize;
}

package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author : YiMing
 * @date: 2021/9/17 9:40
 **/
@Data
public class EmailTemplateDto {
    private List<String> receiptEmail;

    private Map<String,String> adminTypes;

    private Map<String,String> emailAddress;
}

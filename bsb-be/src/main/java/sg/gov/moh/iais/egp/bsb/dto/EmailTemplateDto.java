package sg.gov.moh.iais.egp.bsb.dto;

import lombok.Data;

import java.util.List;

/**
 * @author : YiMing
 * @date: 2021/9/17 9:40
 **/
@Data
public class EmailTemplateDto {
    private List<String> receipts;

    private List<String> ccList;

    private List<String> bccList;
}

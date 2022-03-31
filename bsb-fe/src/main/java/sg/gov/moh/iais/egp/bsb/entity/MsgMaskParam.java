package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class MsgMaskParam implements Serializable {

	private String paramName;

	private String paramValue;
}

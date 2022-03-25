package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class BatLspInfo implements Serializable {
    private String estimatedMaximumVolume;

    private String lspMethodOrSystem;
}

package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
public class BatSample implements Serializable {
    private String sampleNature;

    private String sampleNatureOth;


    public BatSample(BatSample sample) {
        this.sampleNature = sample.getSampleNature();
        this.sampleNatureOth = sample.getSampleNatureOth();
    }
}

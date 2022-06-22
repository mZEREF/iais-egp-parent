package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BATInfo;


@Data
@NoArgsConstructor
public class CompareBATInfoDto {
    private BATInfo oldBATInfo;
    private BATInfo newBATInfo;

    public CompareBATInfoDto(BATInfo oldBATInfo, BATInfo newBATInfo) {
        this.oldBATInfo = oldBATInfo;
        this.newBATInfo = newBATInfo;
    }
}

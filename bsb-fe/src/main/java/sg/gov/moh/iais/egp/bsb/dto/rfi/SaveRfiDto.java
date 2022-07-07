package sg.gov.moh.iais.egp.bsb.dto.rfi;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SaveRfiDto<T> {
    private RfiDisplayDto rfiDisplayDto;
    private T mainSavedDto;

    public SaveRfiDto(RfiDisplayDto rfiDisplayDto, T mainSavedDto) {
        this.rfiDisplayDto = rfiDisplayDto;
        this.mainSavedDto = mainSavedDto;
    }
}

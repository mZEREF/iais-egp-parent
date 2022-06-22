package sg.gov.moh.iais.egp.bsb.dto.compare;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompareWrap<T> {
    private T oldDto;
    private T newDto;

    public CompareWrap(T oldDto, T newDto) {
        this.oldDto = oldDto;
        this.newDto = newDto;
    }
}

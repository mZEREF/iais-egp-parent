package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@NoArgsConstructor
public class BatContactPerson implements Serializable {
    private String name;

    private String email;

    private String contactNo;

    private LocalDate expectedDateOfTransfer;

    private String courierServiceProviderName;

    private String remarks;
}

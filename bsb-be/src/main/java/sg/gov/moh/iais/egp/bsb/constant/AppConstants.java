package sg.gov.moh.iais.egp.bsb.constant;


import java.time.format.DateTimeFormatter;

public class AppConstants {
    private AppConstants() {}

    public static final String DEFAULT_DATE_FORMAT                      = "dd/MM/yyyy";
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER        = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    public static final String CODE_DATE_FORMAT                         = "yyMMdd";
    public static final DateTimeFormatter CODE_DATE_FORMATTER           = DateTimeFormatter.ofPattern(CODE_DATE_FORMAT);

    public static final String YEAR_FORMAT                              = "yyyy";
    public static final DateTimeFormatter YEAR_FORMATTER                = DateTimeFormatter.ofPattern(YEAR_FORMAT);
}

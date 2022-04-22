package sg.gov.moh.iais.egp.bsb.constant;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

public class RoleConstants {
    private RoleConstants() {}

    // routed by role
    public static final String ROLE_APPLICANT                = "Applicant";
    public static final String ROLE_DO                       = "DO";
    public static final String ROLE_AO                       = "AO";
    public static final String ROLE_HM                       = "HM";

    public static final Set<String> MOH_OFFICER;

    static {
        Set<String> officerRoles = Sets.newLinkedHashSetWithExpectedSize(3);
        officerRoles.add(ROLE_DO);
        officerRoles.add(ROLE_AO);
        officerRoles.add(ROLE_HM);
        MOH_OFFICER = Collections.unmodifiableSet(officerRoles);
    }
}

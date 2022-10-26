package sg.gov.moh.iais.egp.bsb.dto.appview.afc;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
public class CertifyingTeamDto implements Serializable {
    @Data
    @NoArgsConstructor
    public static class CertifierTeamMember implements Serializable {
        private String memberEntityId;
        private String memberName;
        private String idType;
        private String idNo;
        private String birthDate;
        private String sex;
        private String nationality;
        private String telNo;
        private String jobDesignation;
        private String leadCertifier;
        private String expertiseArea;
        private String certBSL3Exp;
        private String commBSL34Exp;
        private String otherBSL34Exp;
        private String eduBackground;
        private String proActivities;
        private String proRegAndCert;
        private String facRelatedPub;
    }

    private List<CertifierTeamMember> certifierTeamMemberList;
}

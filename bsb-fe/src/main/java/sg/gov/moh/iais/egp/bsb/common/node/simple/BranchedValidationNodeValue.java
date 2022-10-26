package sg.gov.moh.iais.egp.bsb.common.node.simple;


public interface BranchedValidationNodeValue extends ValidatableNodeValue {
    String getValidationProfile();

    void setValidationProfile(String profile);

    default BranchedValidationNodeValue p(String profile) {
        setValidationProfile(profile);
        return this;
    }
}

package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.role.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class EgpUserClientFallback implements EgpUserClient {
    @Override
    public List<Role> search(Map<String, String> map) {
        return new ArrayList<>(0);
    }
}

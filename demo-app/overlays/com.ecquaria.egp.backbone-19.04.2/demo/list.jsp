<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<egov-smc:table data="${userData}" var="userItem">
    <egov-smc:header width="1%"><input type="checkbox" name="id"/></egov-smc:header>
    <egov-smc:header name="userdomian"></egov-smc:header>
    <egov-smc:column>
        <input type="checkbox" name="ids"/>
        <input type="hidden" name="userDomains">
    </egov-smc:column>
</egov-smc:table>
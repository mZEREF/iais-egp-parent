<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="content/msgContentDashboard.jsp" %>
<%@ include file="content/msgContent.jsp" %>
<script type="text/javascript">
    function submit(action) {
        $("[name='msg_view_type']").val(action);
        $("#msgContentForm").submit();
    }

    function cotToApp() {
        submit("toApp");
    }

    function cotToLic() {
        submit("toLic");
    }

    function cotToMsg() {
        submit('toMsg');
    }

</script>
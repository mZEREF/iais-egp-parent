<%--
  Created by IntelliJ IDEA.
  User:wangyu
  Date: 2020/4/23
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <h2>${modulename}</h2>

                </div>
            </div>
            <iais:action style="text-align:right;">
                <button type="button" class="btn btn-secondary" onclick="javascript:cancel('');">Cancel
                </button>
                <button type="button" class="btn btn-primary next" onclick="javascript:addAudit('');">
                    Create Audit Task
                </button>
            </iais:action>
        </div>
    </div>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>

<script type="text/javascript">
    function cancel() {

    }
    function addAudit() {

    }
</script>
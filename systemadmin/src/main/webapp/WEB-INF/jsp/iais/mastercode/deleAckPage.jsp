<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form method="post" id="ackForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <div class="bg-title">
                            <h2>You have Removed/Deactivated Master code on <fmt:formatDate value="${DELETE_DATE}" pattern="dd/MM/yyyy"/> at <fmt:formatDate value="${DELETE_DATE}" pattern="HH:mm"/></h2>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-12">
                        <div class="text-right text-center-mobile"><a class="btn btn-primary" href="#"
                                                                      onclick="submit('back')">DONE</a></div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    function submit(action) {
        $("[name='crud_action_type']").val(action);
        $("#ackForm").submit();
    }
</script>
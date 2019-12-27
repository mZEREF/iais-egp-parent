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
<%@ include file="mainContent.jsp" %>

<script src="<%=webroot%>js/tinymce/tinymce.min.js"></script>
<script src="<%=webroot%>js/initTinyMce.js"></script>
<script>
    function submit(action){
        $("[name='crud_action_type']").val(action);
        $("#TemplatesForm").submit();
    }

    function doPreview(msgId) {
        $("[name='crud_action_value']").val(msgId);
        submit("preview");
    }
    $("#ANT_Search").click(function () {
        submit("search");
    });

    function doEdit(msgId){
        $("[name='crud_action_value']").val(msgId);
        submit('edit');
    }

</script>



<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<webui:setLayout name="iais-blank"/>

<div id="svcDiv" class="panel-main-content" style="margin: 2%">
  <%@include file="../view/previewSvcInfo.jsp"%>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        var height = $(document).height();
        var iframe = $(parent.document.getElementById('${iframeId}'));
        iframe.css('height',height+10);
        iframe.prop('height',height+10);
        window.parent.dismissWaiting();
    });

</script>
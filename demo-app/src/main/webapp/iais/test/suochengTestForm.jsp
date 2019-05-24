<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setAttribute name="header-ext">
    <%
        /* You can add additional content (SCRIPT, STYLE elements)
         * which need to be placed inside HEAD element here.
         */
    %>
    header-ext
</webui:setAttribute>

<webui:setAttribute name="title">
    <%
        /* You can set your page title here. */
    %>
    title
    <%=process.runtime.getCurrentComponentName()%>

</webui:setAttribute>
<div class="form-div" id="the-form-div"></div>

<form method="post" class="__egovform" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
</form>
<script type="text/javascript">
    $(function() {
        $(".__egovform")[0].submit();
    });

</script>
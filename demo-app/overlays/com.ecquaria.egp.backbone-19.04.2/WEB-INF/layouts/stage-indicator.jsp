<!--    depends on jquery.js and jstl tag.   -->
<% /*   HELP
        def           wrapper
        id            stageId
        name          stageName
        desc          desc
        tooltip       tooltip
        icon          icon
   */%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@page import="sop.webflow.rt.engine5.flow.FlowConstants"%>
<%@page import="sop.webflow.def5.auxiliary.StageIndicatorDef"%>
<%@ page import="com.ecquaria.cloud.stageindicator.StageIndicatorDefWrapper" %>
<%@ page import="java.util.Map" %>
<%
    Object displaySI = request.getAttribute(FlowConstants.ATTR_DISPLAY_STAGE_INDICATOR);
    pageContext.setAttribute("egov_displaySI", displaySI);

    Object stageIndicators = request.getAttribute(FlowConstants.ATTR_STAGE_INDICATORS);
    pageContext.setAttribute("egov_stageIndicators", stageIndicators);

    Object currentIndicator = request.getAttribute(FlowConstants.ATTR_CURRENT_STAGE_INDICATOR);

    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    if(process != null && process.currentCase != null){
        pageContext.setAttribute("egov_currentIndicator2", process.currentCase.getProject() +"."+ process.currentCase.getProcess() +"." +currentIndicator);

    }else{
        pageContext.setAttribute("egov_currentIndicator2", currentIndicator);
    }
// else {
        pageContext.setAttribute("egov_currentIndicator", currentIndicator);
//    }
%>
<c:if test="${egov_displaySI == 'true'}">
    <p class="subheader"><egov-smc:commonLabel>Step</egov-smc:commonLabel> <%=((String)currentIndicator).replace("stage.","")%> <egov-smc:commonLabel>of</egov-smc:commonLabel> <%=((Map<String, StageIndicatorDef>) stageIndicators).size()%>  &nbsp; <egov-smc:commonLabel>${subHeader}</egov-smc:commonLabel></p>
    <br>
    <ul class="list-unstyled multi-steps" id="stageIndicator"></ul>
    <div style="clear:both;"></div>
    <script type="text/javascript">
        var stageIndicatorIds = [];
        jQuery(document).ready(function() {
            // jQuery("#stageIndicator").append("<ul></ul>");
            <c:forEach items="${egov_stageIndicators}" var="stage">
            <c:set var="def" value="${stage.value}" />
            <%
               StageIndicatorDef def = (StageIndicatorDef) pageContext.getAttribute("def");
               StageIndicatorDefWrapper wrapper = new StageIndicatorDefWrapper(def);
               pageContext.setAttribute("wrapper", wrapper);
               pageContext.setAttribute("tooltip", def.getTooltip());

               String str = wrapper.getStageId().substring(wrapper.getStageId().indexOf(".")+1);
               String stageId = str.substring(str.indexOf(".")+1);
            %>

            stageIndicatorIds.push('<%=stageId%>');

            var highlightClass = "";
            if("<c:out value='${wrapper.stageId}'/>" == "<c:out value='${egov_currentIndicator}'/>" || "<c:out value='${wrapper.stageId}'/>" == "<c:out value='${egov_currentIndicator2}'/>") {
                highlightClass = "is-active";
                if("stage."+"<c:out value='${fn:length(egov_stageIndicators)}'/>"=="<c:out value='${egov_currentIndicator}'/>"){
                    highlightClass="";
                }
            }
            jQuery("#stageIndicator").append("<li class=\""+highlightClass+"\"><span class=\"hidden-xs\"><egov-smc:commonLabel><c:out value='${wrapper.stageName}'/></egov-smc:commonLabel></span></li>");
            </c:forEach>
        });
    </script>
    <br/>
</c:if>
<%
    pageContext.setAttribute("egov_displaySI", null);
    pageContext.setAttribute("egov_stageIndicators", null);
    pageContext.setAttribute("egov_currentIndicator", null);
%>
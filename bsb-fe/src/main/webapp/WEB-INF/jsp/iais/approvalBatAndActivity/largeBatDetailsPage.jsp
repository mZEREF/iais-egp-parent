<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="bat" tagdir="/WEB-INF/tags/bat" %>


<%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto"--%>
<%--@elvariable id="addressTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="nationalityOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="activeNodeKey" type="java.lang.String"--%>
<%--@elvariable id="processType" type="java.lang.String"--%>
<%--@elvariable id="firstScheduleOp" type="java.lang.String"--%>
<%--@elvariable id="scheduleBatMap" type="java.util.Map<java.lang.String, java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>>"--%>
<%--@elvariable id="scheduleOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<bat:largeBatDetails batInfo="${batInfo}" addressTypeOps="${addressTypeOps}" nationalityOps="${nationalityOps}"
                      firstScheduleOp="${firstScheduleOp}" scheduleBatMap="${scheduleBatMap}" scheduleOps="${scheduleOps}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-approval-bat-and-activity.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%@include file="InnerFooter.jsp" %>
    </jsp:attribute>
</bat:largeBatDetails>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="bat" tagdir="/WEB-INF/tags/bat" %>
<%@taglib prefix="appv" tagdir="/WEB-INF/tags/approvalApp" %>

<%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto"--%>
<%--@elvariable id="addressTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="nationalityOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="activeNodeKey" type="java.lang.String"--%>
<%--@elvariable id="processType" type="java.lang.String"--%>
<%--@elvariable id="firstScheduleOp" type="java.lang.String"--%>
<%--@elvariable id="scheduleBatMap" type="java.util.Map<java.lang.String, java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>>"--%>
<%--@elvariable id="scheduleOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<bat:largeBatDetails batInfos="${batInfo.batInfos}" addressTypeOps="${addressTypeOps}" nationalityOps="${nationalityOps}" activeNodeKey="${activeNodeKey}"
                      firstScheduleOp="${firstScheduleOp}" scheduleBatMap="${scheduleBatMap}" scheduleOps="${scheduleOps}" lspJudge="true">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-rfi-approval-bat-and-activity.js"></script>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-display-or-not.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <appv:innerFooter editApp="true"/>
    </jsp:attribute>
    <jsp:attribute name="titleFrag">
        <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Details of Biological Agent</p>
    </jsp:attribute>
</bat:largeBatDetails>
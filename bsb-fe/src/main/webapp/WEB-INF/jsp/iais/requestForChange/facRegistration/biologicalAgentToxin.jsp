<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%@page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto"--%>
<%--@elvariable id="sourceFacDetails" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.SourceFacDetails"--%>
<%--@elvariable id="addressTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="nationalityOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="activeNodeKey" type="java.lang.String"--%>
<%--@elvariable id="activityTypes" type="java.util.List<java.lang.String>"--%>
<%--@elvariable id="firstScheduleOp" type="java.lang.String"--%>
<%--@elvariable id="scheduleBatMap" type="java.util.Map<java.lang.String, java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>>"--%>
<%--@elvariable id="scheduleOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<%--@elvariable id="editableFieldSet" type="java.util.Set<java.lang.String>"--%>
<%--@elvariable id="errorMsg" type="java.lang.String"--%>
<fac:biologicalAgentToxin batInfos="${batInfo.batInfos}" addressTypeOps="${addressTypeOps}" countryOps="${nationalityOps}" activeNodeKey="${activeNodeKey}"
                          activityTypes="${activityTypes}" firstScheduleOp="${firstScheduleOp}" scheduleBatMap="${scheduleBatMap}" scheduleOps="${scheduleOps}"
                          sourceFac="${sourceFacDetails}" lspJudge="${MasterCodeConstants.ACTIVITY_LSP_FIRST_THIRD_SCHEDULE eq activeNodeKey}"
                          spFifthJudge="${MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED eq activeNodeKey}"  editJudge="${true}" editableFieldSet="${editableFieldSet}"
                          hasError="${not empty errorMsg}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-facility-register.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="../common/dashboard.jsp"%>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%--@elvariable id="isAllowToSaveDraft" type="java.lang.Boolean"--%>
        <fac:innerFooter canSaveDraftJudge="${isAllowToSaveDraft}"/>
    </jsp:attribute>
</fac:biologicalAgentToxin>
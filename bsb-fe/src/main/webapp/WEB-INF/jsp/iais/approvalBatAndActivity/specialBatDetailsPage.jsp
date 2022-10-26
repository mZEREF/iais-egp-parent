<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="bat" tagdir="/WEB-INF/tags/bat" %>
<%@taglib prefix="appv" tagdir="/WEB-INF/tags/approvalApp" %>

<%--@elvariable id="firstScheduleOp" type="java.lang.String"--%>
<%--@elvariable id="scheduleBatMap" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.common.modal.view.RichSelectOption>>"--%>
<%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto"--%>
<%--@elvariable id="editJudge" type="java.lang.Boolean"--%>
<%--@elvariable id="editableFieldSet" type="java.lang.String"--%>
<%--@elvariable id="errorMsg" type="java.lang.String"--%>
<bat:specialBatDetails firstScheduleOp="${firstScheduleOp}" scheduleBatMap="${scheduleBatMap}" batInfo="${batInfo}"
                       editJudge="${editJudge}" editableFieldSet="${editableFieldSet}" hasError="${not empty errorMsg}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-approval-bat-and-activity.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%--@elvariable id="editApp" type="java.lang.Boolean"--%>
        <appv:innerFooter editApp="${editApp}"/>
    </jsp:attribute>
</bat:specialBatDetails>
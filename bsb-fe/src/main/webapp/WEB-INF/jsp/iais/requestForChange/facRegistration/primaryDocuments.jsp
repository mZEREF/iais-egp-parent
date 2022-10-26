<%@ page import="sg.gov.moh.iais.egp.bsb.config.BsbParamConfig" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="comm" tagdir="/WEB-INF/tags/common" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="savedFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>"--%>
<%--@elvariable id="newFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>"--%>
<%--@elvariable id="docSettings" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>"--%>
<%--@elvariable id="editableFieldSet" type="java.util.Set<java.lang.String>"--%>
<%--@elvariable id="errorMsg" type="java.lang.String"--%>
<comm:supportingDocuments savedFiles="${savedFiles}" newFiles="${newFiles}" docSettings="${docSettings}" otherDocTypes="${otherDocTypes}" docTypeOps="${docTypeOps}"
                          newFileDLUrl="/bsb-web/ajax/doc/download/facReg/new/" savedFileDLUrl="/bsb-web/ajax/doc/download/facReg/repo/" editJudge="${true}" editableFieldSet="${editableFieldSet}"
                          hasError="${not empty errorMsg}">
    <jsp:attribute name="innerNavTabFrag">
        <%@include file="/WEB-INF/jsp/iais/facRegistration/InnerNavTab.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="specialJsFrag">
    <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-facility-register.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="../common/dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <%--@elvariable id="isAllowToSaveDraft" type="java.lang.Boolean"--%>
        <fac:innerFooter canSaveDraftJudge="${isAllowToSaveDraft}"/>
    </jsp:attribute>
</comm:supportingDocuments>
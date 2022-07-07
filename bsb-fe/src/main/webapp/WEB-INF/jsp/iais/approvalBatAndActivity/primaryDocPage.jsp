<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/common" %>
<%@taglib prefix="appv" tagdir="/WEB-INF/tags/approvalApp" %>
<%--@elvariable id="savedFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>"--%>
<%--@elvariable id="newFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>"--%>
<%--@elvariable id="docSettings" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>"--%>
<fac:supportingDocuments savedFiles="${savedFiles}" newFiles="${newFiles}" docSettings="${docSettings}" otherDocTypes="${otherDocTypes}" docTypeOps="${docTypeOps}"
                         newFileDLUrl="/bsb-web/ajax/doc/download/facReg/new/" savedFileDLUrl="/bsb-web/ajax/doc/download/facReg/repo/">
    <jsp:attribute name="innerNavTabFrag">
        <%@include file="InnerNavTab.jsp" %>
    </jsp:attribute>
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
</fac:supportingDocuments>
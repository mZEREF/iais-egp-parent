<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="withdrawal" tagdir="/WEB-INF/tags/withdrawn" %>
<%--@elvariable id="newFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>"--%>
<%--@elvariable id="docSettings" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>"--%>
<withdrawal:supportingDocuments newFiles="${newFiles}" docSettings="${docSettings}" otherDocTypes="${otherDocTypes}" docTypeOps="${docTypeOps}"
                         newFileDLUrl="/bsb-web/ajax/doc/download/withdrawn/new/">
</withdrawal:supportingDocuments>
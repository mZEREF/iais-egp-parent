<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="adhocRfi" tagdir="/WEB-INF/tags/adhocRfi" %>
<%--@elvariable id="adhocReqForInfoDto" type="sg.gov.moh.iais.egp.bsb.dto.adhocrfi.AdhocRfiViewDto"--%>
<%--@elvariable id="newFiles" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>"--%>
<%--@elvariable id="docTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
<adhocRfi:supportingDocuments savedFiles="${adhocReqForInfoDto.applicationDocDtos}" newFiles="${newFiles}" docTypeOps="${docTypeOps}" newFileDLUrl="/bsb-web/ajax/doc/download/adhocRfi/new/" savedFileDLUrl="/bsb-web/ajax/doc/download/adhocRfi/repo/">
</adhocRfi:supportingDocuments>
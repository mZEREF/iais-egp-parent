<%@tag description="Preview documents, this tag should be used by preview.tag" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@attribute name="docSettings" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.DocSetting>" %>
<%@attribute name="savedFiles" required="true" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo>>" %>
<%@attribute name="newFiles" required="true" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo>>" %>
<%@attribute name="certTeamNewFiles" required="true" type="java.util.Map<java.lang.String,sg.gov.moh.iais.egp.bsb.dto.register.afc.CertTeamNewDoc>" %>
<%@attribute name="certTeamSavedFiles" required="true" type="java.util.Map<java.lang.String,sg.gov.moh.iais.egp.bsb.dto.register.afc.CertTeamSavedDoc>" %>
<%@attribute name="certTeamDto" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.afc.CertifyingTeamDto" %>

<c:forEach var="doc" items="${docSettings}">
    <c:set var="savedFileList" value="${savedFiles.get(doc.type)}" />
    <c:set var="newFileList" value="${newFiles.get(doc.type)}" />
    <c:if test="${not empty savedFileList or not empty newFileList}">
        <div class="form-group">
            <div class="col-10"><strong>${doc.typeDisplay}</strong></div>
            <div class="clear"></div>
        </div>
        <div>
            <c:forEach var="file" items="${savedFileList}">
                <c:set var="repoId"><iais:mask name="file" value="${file.repoId}"/></c:set>
                <div class="form-group">
                    <div class="col-10"><p><a href="/bsb-web/ajax/doc/download/facCertifierReg/repo/${repoId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                    <div class="clear"></div>
                </div>
            </c:forEach>
            <c:forEach var="file" items="${newFileList}">
                <c:set var="tmpId"><iais:mask name="file" value="${file.tmpId}"/></c:set>
                <div class="form-group">
                    <div class="col-10"><p><a href="/bsb-web/ajax/doc/download/facCertifierReg/new/${tmpId}">${file.filename}</a>(<fmt:formatNumber value="${file.size/1024.0}" type="number" pattern="0.0"/>KB)</p></div>
                    <div class="clear"></div>
                </div>
            </c:forEach>
        </div>
    </c:if>
</c:forEach>
<div class="form-group">
    <div class="col-10"><strong>Team members' testiomonials and curriculum vitae</strong></div>
    <div class="clear"></div>
</div>
<div class="form-group">
    <table aria-describedby="">
        <tr style="text-align: center">
            <th id="no" style="width: 20%;">SN</th>
            <th id="name" style="width: 20%;">Name</th>
            <th id="idNo" style="width: 20%;">ID No.</th>
            <th id="testimonialsUpload" style="width: 20%">Testimonials Upload</th>
            <th id="curriculumVitaeUpload" style="width: 20%">Curriculum Vitae Upload</th>
        </tr>
        <c:forEach var="item" items="${certTeamDto.certifierTeamMemberList}" varStatus="status">
            <c:set var="itemKeyT" value="${item.idNumber}--v--Testimonial"/>
            <c:set var="itemKeyCV" value="${item.idNumber}--v--CurriculumVitae"/>
            <c:set var="newFileT"  value="${certTeamNewFiles.get(itemKeyT)}"/>
            <c:set var="savedFileT"  value="${certTeamSavedFiles.get(itemKeyT)}"/>
            <c:set var="newFileCV"  value="${certTeamNewFiles.get(itemKeyCV)}"/>
            <c:set var="savedFileCV"  value="${certTeamSavedFiles.get(itemKeyCV)}"/>
            <tr style="text-align: center">
                <td>${status.index+1}</td>
                <td>${item.name}</td>
                <td>${item.idNumber}</td>
                <td>
                    <c:if test="${newFileT ne null}">
                        <c:set var="repoId"><iais:mask name="file" value="${newFileT.tmpId}"/></c:set>
                        <a href="/bsb-web/ajax/doc/download/facCertifierReg/certTeam/new/${repoId}">${newFileT.filename}</a>
                    </c:if>
                    <c:if test="${savedFileT ne null}">
                        <c:set var="repoId"><iais:mask name="file" value="${savedFileT.repoId}"/></c:set>
                        <a href="/bsb-web/ajax/doc/download/facCertifierReg/certTeam/repo/${repoId}">${savedFileT.filename}</a>
                    </c:if>
                </td>
                <td>
                    <c:if test="${newFileCV ne null}">
                        <c:set var="repoId"><iais:mask name="file" value="${newFileCV.tmpId}"/></c:set>
                        <a href="/bsb-web/ajax/doc/download/facCertifierReg/certTeam/new/${repoId}">${newFileCV.filename}</a>
                    </c:if>
                    <c:if test="${savedFileCV ne null}">
                        <c:set var="repoId"><iais:mask name="file" value="${savedFileCV.repoId}"/></c:set>
                        <a href="/bsb-web/ajax/doc/download/facCertifierReg/certTeam/repo/${repoId}">${savedFileCV.filename}</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>

    </table>
</div>
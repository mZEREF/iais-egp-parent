<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="document-upload-gp">
    <h2>Supporting Documents</h2>
    <c:forEach var="doc" items="${docSettings}">
        <c:set var="maskDocType"><iais:mask name="file" value="${doc.type}"/></c:set>
        <div class="document-upload-list">
            <h3>${doc.typeDisplay}<c:if test="${doc.mandatory}"> <span class="mandatory otherQualificationSpan">*</span></c:if></h3>
            <div class="file-upload-gp">
                <c:if test="${savedFiles.get(doc.type) ne null}">
                    <c:forEach var="info" items="${savedFiles.get(doc.type)}">
                        <c:set var="tmpId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                        <div id="${tmpId}FileDiv">
                            <a href="/bsb-fe/ajax/doc/download/facCertifierReg/repo/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedFile('${tmpId}')">Delete</button><button
                                type="button" class="btn btn-secondary btn-sm" onclick="reloadSavedFile('${tmpId}', '${maskDocType}')">Reload</button>
                            <span data-err-ind="${info.repoId}" class="error-msg"></span>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${newFiles.get(doc.type) ne null}">
                    <c:forEach var="info" items="${newFiles.get(doc.type)}">
                        <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                        <div id="${tmpId}FileDiv">
                            <a href="/bsb-fe/ajax/doc/download/facCertifierReg/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a>(<fmt:formatNumber value="${info.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                                type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button><button
                                type="button" class="btn btn-secondary btn-sm" onclick="reloadNewFile('${tmpId}', '${maskDocType}')">Reload</button>
                            <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                        </div>
                    </c:forEach>
                </c:if>
                <a class="btn file-upload btn-secondary" data-upload-file="${maskDocType}" href="javascript:void(0);">Upload</a><span data-err-ind="${doc.type}" class="error-msg"></span>
            </div>
        </div>
    </c:forEach>
    <div class="document-upload-list">
        <h3>Team members' testimonials and curriculum vitae <span class="mandatory otherQualificationSpan">*</span></h3>
        <div>
            <table aria-describedby="">
                <tr style="text-align: center">
                    <th id="no" style="width: 20%;">SN</th>
                    <th id="name" style="width: 20%;">Name</th>
                    <th id="idNo" style="width: 20%;">ID No.</th>
                    <th id="testimonialsUpload" style="width: 20%">Testimonials Upload</th>
                    <th id="curriculumVitaeUpload" style="width: 20%">Curriculum Vitae Upload</th>
                </tr>
                <tr style="text-align: center">
                    <td>1</td>
                    <td>Kim</td>
                    <td>S9992887K</td>
                    <td><button type="button" class="btn btn-default btn-sm" >Upload</button></td>
                    <td><button type="button" class="btn btn-default btn-sm" >Upload</button></td>
                </tr>
            </table>
        </div>
    </div>
</div>
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
    <a id="certFile"></a>
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
                <c:forEach var="member" items="${certifierTeamList}" varStatus="status">
                    <c:set var="testimonial"><iais:mask name="file" value="${member.idNumber}--v--Testimonial"/></c:set>
                    <c:set var="curriculumVitae"><iais:mask name="file" value="${member.idNumber}--v--CurriculumVitae"/></c:set>
                    <c:set var="itemKeyT" value="${member.idNumber}--v--Testimonial"/>
                    <c:set var="itemKeyCV" value="${member.idNumber}--v--CurriculumVitae"/>
                    <tr style="text-align: center">
                        <td>${status.index+1}</td>
                        <td>${member.name}</td>
                        <td>${member.idNumber}</td>
                        <td>
                            <c:choose>
                                <c:when test="${certTeamSavedFiles.get(itemKeyT) ne null || certTeamNewFiles.get(itemKeyT) ne null}">
                                    <c:if test="${certTeamSavedFiles.get(itemKeyT) ne null}">
                                        <c:set var="info" value="${certTeamSavedFiles.get(itemKeyT)}"/>
                                        <c:set var="tmpId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                                        <div id="${tmpId}FileDiv">
                                            <a href="/bsb-fe/ajax/doc/download/facCertifierReg/certTeam/repo/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a><button
                                                type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedCertTeamFile('${tmpId}','${testimonial}')">Delete</button>
                                            <span data-err-ind="${info.repoId}" class="error-msg"></span>
                                        </div>
                                    </c:if>
                                    <c:if test="${certTeamNewFiles.get(itemKeyT) ne null}">
                                        <c:set var="info" value="${certTeamNewFiles.get(itemKeyT)}"/>
                                        <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                                        <div id="${tmpId}FileDiv">
                                            <a href="/bsb-fe/ajax/doc/download/facCertifierReg/certTeam/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a><button
                                                type="button" class="btn btn-secondary btn-sm" onclick="deleteNewCertTeamFile('${tmpId}','${testimonial}')">Delete</button>
                                            <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                        </div>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn file-upload btn-sm" certify-data-upload-file="${testimonial}" href="javascript:void(0);">Upload</a>
                                </c:otherwise>
                            </c:choose>
                            <span data-err-ind="${itemKeyT}" class="error-msg"></span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${certTeamSavedFiles.get(itemKeyCV) ne null || certTeamNewFiles.get(itemKeyCV) ne null}">
                                    <c:if test="${certTeamSavedFiles.get(itemKeyCV) ne null}">
                                        <c:set var="info" value="${certTeamSavedFiles.get(itemKeyCV)}"/>
                                        <c:set var="tmpId"><iais:mask name="file" value="${info.repoId}"/></c:set>
                                        <div id="${tmpId}FileDiv">
                                            <a href="/bsb-fe/ajax/doc/download/facCertifierReg/certTeam/repo/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a><button
                                                type="button" class="btn btn-secondary btn-sm" onclick="deleteSavedCertTeamFile('${tmpId}','${curriculumVitae}')">Delete</button>
                                            <span data-err-ind="${info.repoId}" class="error-msg"></span>
                                        </div>
                                    </c:if>
                                    <c:if test="${certTeamNewFiles.get(itemKeyCV) ne null}">
                                        <c:set var="info" value="${certTeamNewFiles.get(itemKeyCV)}"/>
                                        <c:set var="tmpId"><iais:mask name="file" value="${info.tmpId}"/></c:set>
                                        <div id="${tmpId}FileDiv">
                                            <a href="/bsb-fe/ajax/doc/download/facCertifierReg/certTeam/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${info.filename}</span></a><button
                                                type="button" class="btn btn-secondary btn-sm" onclick="deleteNewCertTeamFile('${tmpId}','${curriculumVitae}')">Delete</button>
                                            <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                        </div>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn file-upload btn-sm" certify-data-upload-file="${curriculumVitae}" href="javascript:void(0);" >Upload</a>
                                </c:otherwise>
                            </c:choose>
                            <span data-err-ind="${itemKeyCV}" class="error-msg"></span>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <span data-err-ind="certifyTeamFile" class="error-msg"></span>
        </div>
    </div>
</div>
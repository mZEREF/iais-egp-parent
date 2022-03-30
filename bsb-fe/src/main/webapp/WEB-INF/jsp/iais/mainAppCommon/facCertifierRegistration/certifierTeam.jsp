<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Please upload Details of Certifying Team File</h3>
<div class="row">
    <ul>
        <li>The maximum file size for each upload is 10MB.</li>
        <li>Acceptable file format is XLSX, CSV.</li>
        <li>You may download the template by clicking <a>here</a></li>
        <li>The maximum number of records allowed is 10,000.</li>
        <li>Please note to indicate the following for the data fields:
            <ul style="list-style: circle">
                <li><strong>Experience in certification of BSL-3/4 facility:</strong> The list should include the name of the facility and the country where it is located, the type of facility (BSL-3 or BSL-4) and the year of certification.</li>
                <li><strong>Experience in commissioning of BSL-3/4 facility:</strong> The list should include the name of the facility and the country where it is located, the type of facility (BSL-3 or BSL-4) and the year of certification.</li>
                <li><strong>Experience in other BSL-3/4 related activities:</strong> This can include consultation work, design and construction of a BSL-3/4 facility; development of Biorisk Management Programme or training programme for BSL-3/4 facility, past work experience in a BSL-3/4 facility, etc.</li>
                <li><strong>Highest level of education completed:</strong> Include the field of study, e.g. BSc (Medical Sciences), BSc (Engineering)</li>
                <li><strong>Relevant professional registration and certificates:</strong> E.g. Professional Engineer, Registered Biosafety Professional with the American Biological Safety Association</li>
                <li><strong>Other related achievements/activities:</strong> Refers to achievements/activities related to biosafety, biosecurity, and facility engineering. E.g. authorship for publications, professional trainings conducted.</li>
            </ul>
        </li>
    </ul>
</div>
<div class="document-content">
    <div class="document-upload-gp">
        <div class="document-upload-list">
            <h3>Certifying Team Information (${certTeam.amount} records uploaded)</h3>
            <div class="file-upload-gp">
                <span data-err-ind="committeeData" class="error-msg"></span>
                <c:if test="${DATA_HAS_ERROR}">
                    <span class="error-msg">There are invalid record(s) in the file. Please rectify them and re-upload the file.</span>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="table-gp">
                                <table class="table" aria-describedb="">
                                    <thead>
                                    <tr style="font-weight: bold">
                                        <th style="width: 15%" id="no">S/N</th>
                                        <th style="width: 35%" id="fieldName">Filed Name (Column)</th>
                                        <th style="width: 50%" id="errorMsg">Error Message</th>
                                    </tr>
                                    </thead>
                                        <%--@elvariable id="DATA_ERRORS" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.validation.ValidationListResultUnit>"--%>
                                    <c:forEach var="error" items="${DATA_ERRORS}">
                                        <tr style="border-top: 1px solid black">
                                            <td>${error.sequence}</td>
                                            <td>${error.field}</td>
                                            <td>${error.message}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>
                <c:if test="${certTeam.savedFile ne null}">
                    <c:set var="repoId"><iais:mask name="file" value="${certTeam.savedFile.repoId}"/></c:set>
                    <div id="${repoId}FileDiv">
                        <a href="/bsb-fe/ajax/doc/download/facCertReg/certTeamDto/repo/${repoId}" style="text-decoration: underline"><span id="${repoId}Span">${certTeam.savedFile.filename}</span></a>(<fmt:formatNumber value="${certTeam.savedFile.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                            type="button" class="btn btn-secondary btn-sm" onclick="delete1DataFile('${repoId}')">Delete</button>
                        <span data-err-ind="${certTeam.savedFile.repoId}" class="error-msg"></span>
                    </div>
                </c:if>
                <c:if test="${certTeam.newFile ne null}">
                    <c:set var="tmpId"><iais:mask name="file" value="${certTeam.newFile.tmpId}"/></c:set>
                    <div id="${tmpId}FileDiv">
                        <a href="/bsb-fe/ajax/doc/download/facCertReg/certTeamDto/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${certTeam.newFile.filename}</span></a>(<fmt:formatNumber value="${certTeam.newFile.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                            type="button" class="btn btn-secondary btn-sm" onclick="delete1DataFile('${tmpId}')">Delete</button>
                        <span data-err-ind="${certTeam.newFile.tmpId}" class="error-msg"></span>
                    </div>
                </c:if>
                <div><a class="btn file-upload btn-secondary" data-upload-data-file="certifyingTeamData" href="javascript:void(0);">Upload</a></div>
                <input type="file" id="certifyingTeamData" name="certifyingTeamData" data-data-file-input="certifyingTeamData" style="display: none">
            </div>
        </div>
    </div>
</div>
<c:if test="${VALID_FILE}">
    <div><p>Click <a href="javascript:void(0)" onclick="expandFile('appInfo_certTeam', 'bsbCertTeam')">here</a> to expand all information</p></div>
</c:if>
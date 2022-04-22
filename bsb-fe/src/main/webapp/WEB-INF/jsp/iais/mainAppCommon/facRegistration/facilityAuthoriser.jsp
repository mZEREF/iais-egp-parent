<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Please upload the list of Personnel Authorised to Access the Facility</h3>
<div class="document-info-list">
    <div style="margin-bottom: 10px">
    Note: The Facility Administrator/Alternate Facility Administrator is responsible to ensure that the list of authorised personnel is always kept up to date i.e. prompt submission of updates to include newly authorised personnel or to remove personnel who are no longer authorised to access the facility.
    </div>
    <ul>
        <li>Click <a href="#" style="text-decoration: underline">here</a> to download the template for the list of Personnel Authorised to Access the Facility.</li>
        <li>Acceptable file format is XLSX, CSV.</li>
        <li>The maximum file size is 10 MB.</li>
    </ul>
</div>

<%--@elvariable id="facAuth" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto"--%>
<div class="document-content">
    <div class="document-upload-gp">
        <div class="document-upload-list">
            <h3>List of Personnel Authorised to Access the Facility (${facAuth.amount} records uploaded)</h3>
            <div class="file-upload-gp">
                <span data-err-ind="authoriserData" class="error-msg"></span>
                <c:if test="${DATA_HAS_ERROR}">
                    <span class="error-msg">There are invalid record(s) in the file. Please rectify them and re-upload the file.</span>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="table-gp">
                                <table class="table" aria-describedby="">
                                    <thead>
                                    <tr style="font-weight: bold">
                                        <th scope="col" style="width: 15%">S/N</th>
                                        <th scope="col" style="width: 35%">Filed Name (Column)</th>
                                        <th scope="col" style="width: 50%">Error Message</th>
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
                <c:if test="${facAuth.savedFile ne null}">
                    <c:set var="repoId"><iais:mask name="file" value="${facAuth.savedFile.repoId}"/></c:set>
                    <div id="${repoId}FileDiv">
                        <a href="/bsb-fe/ajax/doc/download/facReg/authoriser/repo/${repoId}" style="text-decoration: underline"><span id="${repoId}Span">${facAuth.savedFile.filename}</span></a>(<fmt:formatNumber value="${facAuth.savedFile.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                            type="button" class="btn btn-secondary btn-sm" onclick="deleteDataFile('${repoId}')">Delete</button>
                        <span data-err-ind="${facAuth.savedFile.repoId}" class="error-msg"></span>
                    </div>
                </c:if>
                <c:if test="${facAuth.newFile ne null}">
                    <c:set var="tmpId"><iais:mask name="file" value="${facAuth.newFile.tmpId}"/></c:set>
                    <div id="${tmpId}FileDiv">
                        <a href="/bsb-fe/ajax/doc/download/facReg/authoriser/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${facAuth.newFile.filename}</span></a>(<fmt:formatNumber value="${facAuth.newFile.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                            type="button" class="btn btn-secondary btn-sm" onclick="delete1DataFile('${tmpId}')">Delete</button>
                        <span data-err-ind="${facAuth.newFile.tmpId}" class="error-msg"></span>
                    </div>
                </c:if>
                <div><a class="btn file-upload btn-secondary" data-upload-data-file="authoriserData" href="javascript:void(0);">Upload</a></div>
                <input type="file" id="authoriserData" name="authoriserData" data-data-file-input="authoriserData" style="display: none">
            </div>
        </div>
    </div>
</div>
<c:if test="${VALID_FILE}">
    <div><p>Click <a href="javascript:void(0)" onclick="expandFile('facInfo_facAuth', 'facAuth')">here</a> to expand all information</p></div>
</c:if>
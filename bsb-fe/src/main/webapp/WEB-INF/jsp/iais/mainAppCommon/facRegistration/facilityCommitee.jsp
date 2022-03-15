<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Please upload the list of Biosafety Committee Members</h3>
<div class="document-info-list">
    <ul>
        <li>Click <a href="#" style="text-decoration: underline">here</a> to download the template for the list of biosafety committee members.</li>
        <li>Acceptable file format is XLSX, CSV.</li>
        <li>The maximum file size is 10 MB.</li>
    </ul>
</div>

<%--@elvariable id="facCommittee" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto"--%>
<div class="document-content">
    <div class="document-upload-gp">
        <div class="document-upload-list">
            <h3>Biosafety Committee Information (${facCommittee.amount} records uploaded)</h3>
            <div class="file-upload-gp">
                <span data-err-ind="committeeData" class="error-msg"></span>
                <c:if test="${DATA_HAS_ERROR}">
                    <span class="error-msg">There are invalid record(s) in the file. Please rectify them and re-upload the file.</span>
                    <div class="row">
                        <div class="col-xs-12">
                            <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr style="font-weight: bold">
                                        <th style="width: 15%">S/N</th>
                                        <th style="width: 35%">Filed Name (Column)</th>
                                        <th style="width: 50%">Error Message</th>
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
                <c:if test="${facCommittee.savedFile ne null}">
                    <c:set var="repoId"><iais:mask name="file" value="${facCommittee.savedFile.repoId}"/></c:set>
                    <div id="${repoId}FileDiv">
                        <a href="/bsb-fe/ajax/doc/download/facReg/committee/repo/${repoId}" style="text-decoration: underline"><span id="${repoId}Span">${facCommittee.savedFile.filename}</span></a>(<fmt:formatNumber value="${facCommittee.savedFile.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                            type="button" class="btn btn-secondary btn-sm" onclick="deleteDataFile('${repoId}')">Delete</button>
                        <span data-err-ind="${facCommittee.savedFile.repoId}" class="error-msg"></span>
                    </div>
                </c:if>
                <c:if test="${facCommittee.newFile ne null}">
                    <c:set var="tmpId"><iais:mask name="file" value="${facCommittee.newFile.tmpId}"/></c:set>
                    <div id="${tmpId}FileDiv">
                        <a href="/bsb-fe/ajax/doc/download/facReg/committee/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${facCommittee.newFile.filename}</span></a>(<fmt:formatNumber value="${facCommittee.newFile.size/1024.0}" type="number" pattern="0.0"/>KB)<button
                            type="button" class="btn btn-secondary btn-sm" onclick="delete1DataFile('${tmpId}')">Delete</button>
                        <span data-err-ind="${facCommittee.newFile.tmpId}" class="error-msg"></span>
                    </div>
                </c:if>
                <div><a class="btn file-upload btn-secondary" data-upload-data-file="committeeData" href="javascript:void(0);">Upload</a></div>
                <input type="file" id="committeeData" name="committeeData" data-data-file-input="committeeData" style="display: none">
                <input type="hidden" id="deleteDataFile" name="deleteDataFile" value="">
            </div>
        </div>
    </div>
</div>
<c:if test="${VALID_FILE}">
<div><p>Click <a href="javascript:void(0)" onclick="expandFile('facInfo_facCommittee', 'bsbCommittee')">here</a> to expand all information</p></div>
</c:if>
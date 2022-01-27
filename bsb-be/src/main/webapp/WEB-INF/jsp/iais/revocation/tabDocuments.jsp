<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div id="fileUploadInputDiv" style="display: none"></div>
<div id="fileUploadInputDiv" style="display: none"></div>
<div class="alert alert-info" role="alert"><strong>
    <h4>Supporting Document</h4>
</strong></div>
<div id="u8522_text" class="text ">
    <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed
												documents are those defined for this digital service only.</span></p>
    <p>The maximum file size for each upload is 5MB</p>
    <p>Acceptable file formats are JPG, JPEG, DOC, DOCX, PNG, PDF, XLS</p>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col">Document</th>
                    <th scope="col">Document Type</th>
                    <th scope="col">File</th>
                    <th scope="col">Size</th>
                    <th scope="col">Submitted By</th>
                    <th scope="col">Date Submitted</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>

            <div class="alert alert-info" role="alert"><strong>
                <h4>Internal Document</h4>
            </strong></div>
            <div class="text ">
                <p><span>These are documents uploaded by an agency officer to support back office processing.</span></p>
            </div>
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col">Document</th>
                    <th scope="col">Document Type</th>
                    <th scope="col">File</th>
                    <th scope="col">Size</th>
                    <th scope="col">Submitted By</th>
                    <th scope="col">Date Submitted</th>
                    <th scope="col">Action</th>
                </tr>
                </thead>
                <%--@elvariable id="revokeDto" type="sg.gov.moh.iais.egp.bsb.dto.revocation.SubmitRevokeDto"--%>
                <%--@elvariable id="info" type="sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo"--%>
                <tbody id="tbodyFileListId">
                <c:if test="${revokeDto.primaryDocDto.newDocMap ne null}">
                    <c:set var="newDocMap" value="${revokeDto.primaryDocDto.newDocMap}"/>
                    <c:forEach var="info" items="${newDocMap.values()}">
                        <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                        <div id="${tmpId}FileDiv">
                            <tr id="${tmpId}FileTr">
                                <td>${info.filename}</td>
                                <td>${info.docType}</td>
                                <td><p><a href="javascript:void(0)" onclick="downloadNewRevokeFile('${tmpId}')">${info.filename}</a></p></td>
                                <td>${String.format("%.1f", info.size/1024.0)}KB</td>
                                <td>${info.submitBy}</td>
                                <td><fmt:formatDate value='${info.submitDate}' pattern='dd/MM/yyyy'/></td>
                                <td>
                                    <button type="button" class="btn btn-secondary-del btn-sm"
                                            onclick="javascript:deleteNewFile(this,'${tmpId}');">
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        </div>
                    </c:forEach>
                </c:if>
                <%--@elvariable id="docInfo" type="sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo"--%>
                <c:if test="${revokeDto.primaryDocDto.savedDocMap ne null}">
                    <c:forEach var="docInfo" items="${revokeDto.primaryDocDto.savedDocMap.values()}">
                        <c:set var="tmpId" value="${MaskUtil.maskValue('file', docInfo.repoId)}"/>
                        <div id="${tmpId}FileDiv">
                            <tr id="${tmpId}FileTr">
                                <td>${docInfo.filename}</td>
                                <td>${docInfo.docType}</td>
                                <td><p><a href="javascript:void(0)" onclick="downloadSavedRevokeFile('${tmpId}')">${docInfo.filename}</a></p></td>
                                <td>${String.format("%.1f", docInfo.size/1024.0)}KB</td>
                                <td>${docInfo.submitByName}</td>
                                <td><fmt:formatDate value='${docInfo.submitDate}' pattern='dd/MM/yyyy'/></td>
                                <td>
                                    <button type="button" class="btn btn-secondary-del btn-sm"
                                            onclick="javascript:deleteSavedFile(this,'${tmpId}');">
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        </div>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
            <%--upload file--%>
            <iais:action>
                <a class="back" href="${backUrl}"><em class="fa fa-angle-left"></em>Back</a>
                <c:if test="${canUpload eq 'Y'}">
                    <div style="text-align: right">
                        <a class="btn file-upload btn-secondary" data-upload-file="upload" href="javascript:void(0);">Upload</a>
                        <span data-err-ind="upload" class="error-msg"></span>
                    </div>
                </c:if>
            </iais:action>
        </div>
    </div>
</div>
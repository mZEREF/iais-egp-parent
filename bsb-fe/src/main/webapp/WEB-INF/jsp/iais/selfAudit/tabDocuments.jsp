<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<div id="fileUploadInputDiv" style="display: none"></div>
<div class="alert alert-info" role="alert"><strong>
    <h4>Supporting Document</h4>
</strong></div>
<div id="u8522_text" class="text ">
    <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed
												documents are those defined for this digital service only.</span></p>
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
                <tbody id="tbodyFileListId">
                <c:if test="${savedFiles ne null}">
                    <c:forEach var="docTypes" items="${docTypes}">
                        <c:forEach var="info" items="${savedFiles.get(docTypes)}">
                            <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.repoId)}"/>
                            <div id="${tmpId}FileDiv">
                                <tr id="${tmpId}FileTr">
                                    <td>${info.filename}</td>
                                    <td>${info.docType}</td>
                                    <td><p><a href="javascript:void(0)" onclick="downloadFile('saved','${tmpId}')">${info.filename}</a></p></td>
                                    <td>${String.format("%.1f", info.size/1024.0)}KB</td>
                                    <td>${info.submitBy}</td>
                                    <td><fmt:formatDate value='${info.submitDate}' pattern='dd/MM/yyyy'/></td>
                                </tr>
                            </div>
                        </c:forEach>
                    </c:forEach>
                </c:if>
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
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
            <%--upload file--%>
            <iais:action>
                <a style="float:left;padding-top: 1.1%;" class="back" href="/bsb-web/eservice/INTERNET/OngoingAuditList"><em class="fa fa-angle-left"></em> Back</a>
                    <a style="float: right" class="btn file-upload btn-secondary" data-upload-file="upload" href="javascript:void(0);">Upload</a>
                    <span data-err-ind="upload" class="error-msg"></span>
            </iais:action>
        </div>
    </div>
</div>
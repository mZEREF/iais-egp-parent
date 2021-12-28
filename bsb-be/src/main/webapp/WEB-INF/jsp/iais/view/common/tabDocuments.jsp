<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>

<div class="alert alert-info" role="alert"><strong>
    <h4>Supporting Document</h4>
</strong></div>
<div id="u8522_text" class="text ">
    <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed document are those defined for this digital service only</span></p>
</div>
<div class="row">
    <div class="col-xs-12">
        <div class="table-gp">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" style="width: 15%">Document</th>
                    <th scope="col" style="width: 15%">Document Type</th>
                    <th scope="col" style="width: 15%">File</th>
                    <th scope="col" style="width: 15%">Size</th>
                    <th scope="col" style="width: 15%">Submitted By</th>
                    <th scope="col" style="width: 15%">Date Submitted</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${savedFiles ne null}">
                    <c:forEach var="docTypes" items="${docTypes}">
                        <c:forEach var="info" items="${savedFiles.get(docTypes)}">
                            <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.repoId)}"/>
                            <tr>
                                <td>${info.filename}</td>
                                <td>${info.docType}</td>
                                <td><p><a href="javascript:void(0)" onclick="downloadFile('saved', '${tmpId}')">${info.filename}</a></p></td>
                                <td>${String.format("%.1f", info.size/1024.0)}KB</td>
                                <td>${info.submitBy}</td>
                                <td><fmt:formatDate value='${info.submitDate}' pattern='dd/MM/yyyy'/></td>
                            </tr>
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
                    <th scope="col" style="width: 15%">Document</th>
                    <th scope="col" style="width: 15%">Document Type</th>
                    <th scope="col" style="width: 15%">File</th>
                    <th scope="col" style="width: 15%">Size</th>
                    <th scope="col" style="width: 15%">Submitted By</th>
                    <th scope="col" style="width: 15%">Date Submitted</th>
                    <th scope="col" style="width: 15%">Action</th>
                </tr>
                </thead>
                <tbody id="tbodyFileListId">
                </tbody>
            </table>
            <a class="btn file-upload btn-primary" data-upload-file="" href="javascript:void(0);" style="float: right">Upload</a>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading"><strong>Upload Documents</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class = "col-xs-12 col-sm-12">
                <div class = "row" style="border-bottom: 1px solid">
                    <p style="font-size: large;font-weight: bolder">Attachments:</p>
                </div>
                <div class = "row">
                    <div class="col-xs-12">
                        <div id="PrimaryDocsPanel" role="tabpanel">
                            <div class="document-content" style="margin: 0 auto;background-color: #F2F2F2;height: 150px">
                                <div class="document-upload-gp">
                                    <c:forEach var="doc" items="${docSettings}">
                                        <c:set var="maskDocType" value="${MaskUtil.maskValue('file', doc.type)}"/>
                                        <div class="document-upload-list">
                                            <h3>${doc.typeDisplay}<c:if test="${doc.mandatory}"> <span class="mandatory otherQualificationSpan">*</span></c:if></h3>
                                            <div class="file-upload-gp">
                                                <c:if test="${newFiles.get(doc.type) ne null}">
                                                    <c:forEach var="info" items="${newFiles.get(doc.type)}">
                                                        <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                                                        <div id="${tmpId}FileDiv">
                                                            <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                                                                type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button><button
                                                                type="button" class="btn btn-secondary btn-sm" onclick="reloadNewFile('${tmpId}', '${maskDocType}')">Reload</button><button
                                                                type="button" class="btn btn-secondary btn-sm" onclick="downloadFile('new', '${tmpId}', '${maskDocType}', '${info.filename}')">Download</button>
                                                            <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                                        </div>
                                                    </c:forEach>
                                                </c:if>
                                                <a class="btn file-upload btn-secondary" data-upload-file="${maskDocType}" href="javascript:void(0);">Upload</a><span data-err-ind="${doc.type}" class="error-msg"></span>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%@ include file="../InnerFooter.jsp" %>
            </div>
        </div>
    </div>
</div>


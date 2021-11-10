<div id="fileUploadInputDiv" style="display: none"></div>
<div class="panel panel-default">
    <div class="panel-heading"><strong>Upload Documents</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class = "col-xs-12 col-sm-12">
                <div class = "row" style="border-bottom: 1px solid;margin-bottom: 20px">
                    <p style="font-size: large;font-weight:normal">Attachments:</p>
                </div>
                <div class = "row">
                    <div class="col-xs-12">
                        <div id="PrimaryDocsPanel" role="tabpanel">
                            <div class="document-content" style="margin: 0 auto;background-color: #F8F8F8;border-radius: 8px">
                                <div class="document-upload-gp">
                                    <p style="padding-top: 10px;padding-left: 10px;border-bottom: 1px #333333 solid"><strong>Report Type</strong><span class="mandatory otherQualificationSpan">*</span></p>
                                    <div class="document-upload-list">
                                            <div class="form-group">
                                                <div class="col-sm-5 control-label">
                                                    <label for="reportType">Select Upload Type:</label>
                                                </div>
                                                <div class="col-sm-6 col-md-7">
<%--                                                    <iais:select name="reportType" id="reportType"--%>
<%--                                                                 value=""--%>
<%--                                                                 codeCategory="CATE_ID_BSB_DATA_SUBMISSION_REPORT_TYPE"--%>
<%--                                                                 firstOption="Please Select"/>--%>
                                                          <select name="reportType" id="reportType">
                                                              <option value="REPTYPE01">Red Teaming Report</option>
                                                              <option value="REPTYPE02">PNEF Inventory Report</option>
                                                              <option value="REPTYPE03">Bi-annual Toxin Report</option>
                                                              <option value="REPTYPE04">PEF Inventory Report</option>
                                                              <option value="REPTYPE05">Facility (FSF/TF/LSPF) Self-inspection Report</option>
                                                              <option value="REPTYPE06">Emergency Response Self-Audit Report</option>
                                                              <option value="REPTYPE07">Annual Lentivirus Report</option>
                                                              <option value="REPTYPE08">Fifth Schedule Inventory Update</option>
                                                          </select>
                                                    <span data-err-ind="reportType" class="error-msg"></span>
                                                </div>
                                            </div>
                                            <div class="file-upload-gp-report">
                                                <c:if test="${newFiles ne null}">
                                                    <c:forEach var="info" items="${newFiles}">
                                                        <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                                                        <div id="${tmpId}FileDiv">
                                                            <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                                                                type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button><button
                                                                type="button" class="btn btn-secondary btn-sm" onclick="reloadNewFile('${tmpId}')">Reload</button><button
                                                                type="button" class="btn btn-secondary btn-sm" onclick="downloadFile('new', '${tmpId}',  '${info.filename}')">Download</button>
                                                            <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                                        </div>
                                                    </c:forEach>
                                                </c:if>
                                                <a class="btn file-upload btn-secondary" id="file-upload-report" data-upload-file="report" href="javascript:void(0);">Upload</a><span data-err-ind="${doc.type}" class="error-msg"></span>
                                            </div>
                                        </div>
                                </div>
                            </div>
                            <div class="document-content" style="margin: 0 auto;background-color: #F8F8F8;border-radius: 8px">
                                <div class="document-upload-gp">
                                    <p style="padding-top: 10px;padding-left: 10px;border-bottom: 1px #333333 solid"><strong>Other</strong></p>
                                    <div class="document-upload-list">
                                        <div class="file-upload-gp-others">
                                            <c:if test="${newFiles ne null}">
                                                <c:forEach var="info" items="${newFiles}">
                                                    <c:set var="tmpId" value="${MaskUtil.maskValue('file', info.tmpId)}"/>
                                                    <div id="${tmpId}FileDiv">
                                                        <span id="${tmpId}Span">${info.filename}(${String.format("%.1f", info.size/1024.0)}KB)</span><button
                                                            type="button" class="btn btn-secondary btn-sm" onclick="deleteNewFile('${tmpId}')">Delete</button><button
                                                            type="button" class="btn btn-secondary btn-sm" onclick="reloadNewFile('${tmpId}')">Reload</button><button
                                                            type="button" class="btn btn-secondary btn-sm" onclick="downloadFile('new', '${tmpId}',  '${info.filename}')">Download</button>
                                                        <span data-err-ind="${info.tmpId}" class="error-msg"></span>
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                            <a class="btn file-upload btn-secondary" id="file-upload-others" data-upload-file="others" href="javascript:void(0);">Upload</a><span data-err-ind="${doc.type}" class="error-msg"></span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


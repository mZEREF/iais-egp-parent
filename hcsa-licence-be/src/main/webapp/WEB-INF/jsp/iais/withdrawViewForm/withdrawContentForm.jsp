
<div class="col-lg-12 col-xs-12">
    <div class="internet-content">
        <div class="center-content">
            <h2>You are withdrawing for</h2>
            <div class="row">
                <div class="col-lg-8 col-xs-12">
                    <div class="withdraw-content-box">
                        <div class="withdraw-info-gp">
                            <div class="withdraw-info-row">
                                <div class="withdraw-info">
                                    <c:forEach items="${withdrawDtoList}" var="withdrawDtoList">
                                        <p>${withdrawDtoList.applicationNo}</p>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="center-content">
                <h3>Reason for Withdrawal </h3>
                <div class="col-md-9">
                    <input type="text" value="<iais:code code="${withdrawDto.withdrawnReason}"/>" readonly="readonly">
                </div>
            </div>
        </div>

        <div id="reason">
            <div class="row">
                <div class="center-content">
                    <h3>Remarks</h3>
                    <div class="col-md-6">
                        <div class="file-upload-gp">
                                            <textarea name="withdrawnRemarks" cols="90" rows="15" id="withdrawnRemarks"
                                                      title="content"
                                                      maxlength="500" readonly="readonly">${withdrawDto.withdrawnRemarks}</textarea>
                        </div>
                        <span id="error_withdrawnRemarks" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="center-content">
                <div class="document-upload-gp">
                    <div class="document-upload-list">
                        <h3>File upload for Withdrawal Reasons</h3>
                        <div class="file-upload-gp">
                            <div id="delFile" style="margin-top: 13px;color: #1F92FF;"
                                 hidden="hidden">
                                <c:if test="${not empty appealSpecialDocDto}">
                                    <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo&fileRo=<iais:mask name="fileRo"  value="${appealSpecialDocDto.fileRepoId}"/>&fileRepoName=${appealSpecialDocDto.docName}"
                                       title="Download" class="downloadFile"><c:out
                                            value="${appealSpecialDocDto.docName}"></c:out></a>
                                </c:if>
                            </div>
                        </div>
                        <span id="error_withdrawalFile" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
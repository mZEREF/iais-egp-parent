<div class="col-lg-12 col-xs-12">
    <div class="internet-content">
        <div class="center-content">
            <h1 style="border-bottom: none;margin-top:60px;">Withdrawal Form</h1>
            <h2>You are withdrawing for</h2>
            <div class="row">
                <div class="col-lg-8 col-xs-12">
                    <div class="withdraw-content-box">
                        <div class="withdraw-info-gp">
                            <div class="withdraw-info-row">
                                <div class="withdraw-info">
                                    <p>${withdrawDtoList[0].applicationNo}</p>
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

        <div id="reason" <c:if test="${withdrawDto.withdrawnReason != 'WDR005'}">hidden</c:if>>
            <div class="row">
                <div class="center-content">
                    <h3>Remarks</h3>
                    <div class="col-md-6">
                        <div class="file-upload-gp">
                                            <textarea name="withdrawnRemarks" cols="90" rows="15" id="withdrawnRemarks"
                                                      title="content"
                                                      maxlength="500" readonly="readonly">${withdrawDto.withdrawnRemarks}</textarea>
                        </div>
                        <br/><span id="error_withdrawnRemarks" name="iaisErrorMsg" class="error-msg"></span>
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
                           <span name="selectedFileShowId" id="selectedFileShowId">
                                <c:forEach items="${pageShowFiles}" var="pageShowFileDto"
                                           varStatus="ind">
                                  <div id="${pageShowFileDto.fileMapId}">
                                      <span name="fileName"
                                            style="font-size: 14px;color: #2199E8;text-align: center">
                                          <iais:downloadLink fileRepoIdName="fileRo${ind.index}" fileRepoId="${pageShowFileDto.fileUploadUrl}" docName="${pageShowFileDto.fileName}"/>
                                      </span>
                                      <span class="error-msg" name="iaisErrorMsg"
                                            id="file${ind.index}"></span>
                                      <span class="error-msg" name="iaisErrorMsg"
                                            id="error_${configIndex}error"></span>
                                  </div>
                                </c:forEach>
                            </span>
                        </div>
                        <span id="error_withdrawalFile" name="iaisErrorMsg" class="error-msg"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

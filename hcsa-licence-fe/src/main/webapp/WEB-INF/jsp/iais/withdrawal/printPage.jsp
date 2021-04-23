<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-blank"/>
<div class="container">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <div class="navigation-gp">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="internet-content">
                        <div class="center-content">
                            <h2>You are withdrawing for </h2>
                            <div class="row">
                                <div class="col-lg-8 col-xs-12">
                                    <div class="withdraw-content-box">
                                        <div class="withdraw-info-gp">
                                            <div class="withdraw-info-row">
                                                <div class="withdraw-info">
                                                    <p>${withdrawDtoView.applicationNo}</p>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="center-content">
                            <h3>Reason for Withdrawal<span style="color: red"> *</span></h3>
                            <div class="row">
                                <div class="col-md-7">
                                    <iais:select name="withdrawalReason" firstOption="${withdrawDtoView.withdrawnReason}" value="${withdrawDtoView.withdrawnReason}"/>
                                </div>
                            </div>
                        </div>

                        <div id="reason"
                             <c:if test="${withdrawDtoView.withdrawnReason != 'WDR005' || withdrawDtoView.withdrawnReason== null}">hidden</c:if>>
                            <div class="row">
                                <div class="center-content">
                                    <label class="col-md-4" style="font-size:2rem">Remarks<span style="color: red"> *</span></label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="center-content">
                                    <div class="col-md-6">
                                        <div class="file-upload-gp">
                                    <textarea name="withdrawnRemarks" cols="90" rows="15" id="withdrawnRemarks"
                                              title="content"
                                              maxlength="500">${withdrawDtoView.withdrawnRemarks}</textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="center-content">
                            <div class="">
                                <div class="document-upload-gp">
                                    <div class="document-upload-list">
                                        <h3>File upload for Withdrawal Reasons</h3>
                                        <div class="file-upload-gp">
                                        <span name="selectedFileShowId" id="selectedFileShowId">
                                        <c:forEach items="${withdrawPageShowFiles}" var="withdrawPageShowFile"
                                                   varStatus="ind">
                                          <div id="${withdrawPageShowFile.fileMapId}">
                                              <span name="fileName"
                                                    style="font-size: 14px;color: #2199E8;text-align: center">
                                              <a title="Download"
                                                 class="downloadFile">${withdrawPageShowFile.fileName}</a></span>
                                          </div>
                                        </c:forEach>
                                        </span>
                                            <input id="selectedFile" name="selectedFile"
                                                   class="selectedFile commDoc"
                                                   type="file" style="display: none;"
                                                   aria-label="selectedFile1"/><a
                                                class="btn btn-file-upload btn-secondary">Upload</a>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="center-content">
                        <div class="components">
                            <a class="btn btn-primary" style="float:right">Submit</a>
                            <span style="float:right">&nbsp;</span>
                            <a class="btn btn-secondary" style="float:right">Cancel</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        doPrint();

    });
    var doPrint = function () {
        $('a').prop('disabled',true);
        window.print();
        window.close();
    }
</script>
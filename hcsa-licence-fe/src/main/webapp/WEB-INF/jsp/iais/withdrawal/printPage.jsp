<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-blank"/>
<style>

    .withdraw-content-box {
        background-color: #fafafa;
        border-radius: 14px;
        padding: 20px;
        border:1px solid #d1d1d1;
        margin-bottom: 0;
    }

    .withdraw-info-gp .withdraw-info-row .withdraw-info p:before {
        color: #a2d9e7;
    }


</style>
<div class="container">
    <form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
        <div class="navigation-gp">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="internet-content">
                        <div class="center-content">
                            <h2>You are withdrawing for </h2>
                            <div class="withdraw-content-box">
                                <div class="withdraw-info-gp">
                                    <div class="withdraw-info-row">
                                        <div class="withdraw-info">
                                            <p><a class="appNo">${withdrawDtoView.applicationNo}</a></p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <c:if test="${empty rfiWithdrawDto}">
                                <c:if test="${!empty addWithdrawnDtoList}">
                                    <c:forEach items="${addWithdrawnDtoList}" var="wdList">
                                        <div class="withdraw-content-box">
                                            <div class="withdraw-info-gp">
                                                <div class="withdraw-info-row">
                                                    <div class="withdraw-info">
                                                        <p><a class="appNo">${wdList.applicationNo}</a></p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </c:if>
                            </c:if>
                        </div>
                        <div class="center-content">
                            <h3>Reason for Withdrawal</h3>
                            <div class="row">
                                <div class="col-md-7">
                                    <c:if test="${!empty withdrawDtoView.withdrawnReason}">
                                        <iais:select name="withdrawalReason" firstOption="${withdrawDtoView.withdrawnReason}"/>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <div id="reason"
                             <c:if test="${withdrawDtoView.withdrawnReason == null
                             || withdrawDtoView.withdrawnReason != 'Others'}">hidden</c:if>>
                            <div class="row">
                                <div class="center-content">
                                    <label class="col-md-4" style="font-size:2rem">Remarks</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="center-content">
                                    <div class="col-md-6">
                                        <div class="file-upload-gp">
                                    <textarea name="withdrawnRemarks" cols="90" rows="10" id="withdrawnRemarks"
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
                                                      ${withdrawPageShowFile.fileName}</span>
                                          </div>
                                        </c:forEach>
                                        </span>
                                        </div>

                                    </div>
                                </div>
                            </div>
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

    var userAgent = navigator.userAgent;
    var isChrome = userAgent.indexOf("Chrome") > -1 && userAgent.indexOf("Safari") > -1;


    var doPrint = function () {
        $('a').prop('disabled',true);
        if(isChrome){
            addPrintListener();
            window.print();
        }else{
            window.print();
            window.close();
        }
    }
    var addPrintListener = function () {
        if (window.matchMedia) {
            var mediaQueryList = window.matchMedia('print');
            mediaQueryList.addListener(function(mql) {
                if (mql.matches) {

                } else {
                    window.close();
                }
            });
        }
    }
</script>
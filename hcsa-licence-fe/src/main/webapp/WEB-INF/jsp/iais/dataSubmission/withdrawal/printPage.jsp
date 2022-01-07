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
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <div class="navigation-gp">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="internet-content">
                        <div class="center-content">
                            <p style="color: #ff0000">* Withdrawal of the target submission requires the withdrawal of all submissions in the cycle that were submitted after the target submission.</p>
                            <p style="color: #ff0000">Please note that the submissions submitted after the target submission have been automatically submitted.</p>
                        </div>
                        <div class="center-content">
                            <h2>You are withdrawing for</h2>
                            <div class="row">
                                <div class="col-lg-8 col-xs-12">
                                    <c:forEach items="${addWithdrawnDtoList}" var="wdList">
                                        <div class="withdraw-content-box">
                                            <div class="withdraw-info-gp">
                                                <div class="withdraw-info-row">
                                                    <div class="withdraw-info">
                                                        <p><a href="javascript:void(0);" class="appNo" onclick="toDsView('${wdList.dataSubmissionDto.submissionNo}','${wdList.cycleDto.dsType}')">${wdList.dataSubmissionDto.submissionNo}</a></p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                            </div>
                        </div>

                        <div class="center-content">
                            <h2>Remarks <span style="color: #ff0000"> *</span> </h2>
                            <div class="row">
                                <div class="col-md-6">
                                        <textarea name="withdrawnRemarks" cols="80" rows="5" id="withdrawnRemarks"
                                                  title="content" maxlength="100" >${withdrawnRemarks}</textarea>
                                    <span id="error_withdrawnRemarks" name="iaisErrorMsg" class="error-msg"></span>
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
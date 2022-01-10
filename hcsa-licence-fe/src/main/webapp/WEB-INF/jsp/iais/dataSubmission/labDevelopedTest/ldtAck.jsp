<%@ include file="./common/ldtHeader.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="container">
            <p class="print">
            <div style="font-size: 16px;text-align: right;"><a href="javascript:void(0)" onclick="printData()"> <em
                    class="fa fa-print"></em>Print</a></div>
            </p>
            <label class="col-xs-12" style="font-size: 20px">Submission successful</label>
            <p class="col-xs-12 margin-btm">- <strong>Laboratory Developed Test</strong></p>
            <div class="ack-font-16">
                <p class="col-xs-12">A notification email will be sent to ${emailAddress}.</p>
                <p class="col-xs-12 margin-btm"><iais:message key="DS_MSG004" escape="false"></iais:message></p>
            </div>
            <div class="ack-font-16">
                <p class="col-xs-12">Submission details:</p>
                <div class="col-xs-12 col-sm-12 margin-btm table-responsive">
                    <table aria-describedby="" class="table">
                        <thead>
                        <tr>
                            <th scope="col" >Submission ID</th>
                            <th scope="col" >Submitted By</th>
                            <th scope="col" >Submission Date and Time</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>${LdtSuperDataSubmissionDto.dataSubmissionDto.submissionNo}</td>
                            <td>${submittedBy}</td>
                            <td><fmt:formatDate value="${LdtSuperDataSubmissionDto.dataSubmissionDto.submitDt}" pattern="dd/MM/yyyy HH:mm"/></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="col-xs-12 col-md-2 text-left">
                <a style="padding-left: 5px;" class="back" href="/main-web/eservice/INTERNET/MohDataSubmissionsInbox">
                    <em class="fa fa-angle-left">&nbsp;</em> Back
                </a>
            </div>
            <div class="col-xs-12 col-md-10 margin-bottom-10">
                <div class="text-right">
<%--                    <a class="btn btn-secondary" href="/hcsa-licence-web/eservice/INTERNET/MohARDataSubmission">Start--%>
<%--                        Another Submission</a>--%>
                    <a class="btn btn-primary" href="/main-web/eservice/INTERNET/MohInternetInbox">Go to DashBoard</a>
                </div>
            </div>
        </div>
    </div>
</form>

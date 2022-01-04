<%@ include file="./common/ldtHeader.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <div class="main-content">
        <div class="container">
            <p class="print"><div style="font-size: 16px;text-align: right;"><a href="javascript:void(0)" onclick="printWDPDF()"> <em class="fa fa-print"></em>Print</a></div></p>
            <div class="row">
                <div class="col-xs-12">
                    <div class="center-content">
                        <span style="font-size:2rem;">Thank you for your submission.</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<div class="col-xs-12 col-md-2 text-left">
    <a style="padding-left: 5px;" class="back" href="/main-web/eservice/INTERNET/MohDataSubmissionsInbox">
        <em class="fa fa-angle-left">&nbsp;</em> Back
    </a>
</div>
<div class="col-xs-12 col-md-10 margin-bottom-10">
    <div class="text-right">
        <a class="btn btn-secondary" href="/hcsa-licence-web/eservice/INTERNET/MohARDataSubmission">Start Another Submission</a>
        <a class="btn btn-primary" href="/main-web/eservice/INTERNET/MohInternetInbox">Go to DashBoard</a>
    </div>
</div>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<webui:setLayout name="iais-internet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
    <div class="container">
        <div class="row col-xs-12 col-sm-12">
            <div class="dashboard-page-title" style="border-bottom: 1px solid black;">
                <h1>New Application</h1>
            </div>
        </div>
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <p><strong>Submission successful</strong></p>
            <br/>
            <p>We will notify you if any changes are required.</p>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-10">
                    <div class="text-right">
                        <a class="btn btn-secondary" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg">HOME</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
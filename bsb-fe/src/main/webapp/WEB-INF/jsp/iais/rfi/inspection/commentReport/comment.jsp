<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="repo" tagdir="/WEB-INF/tags/inspection" %>

<%--@elvariable id="reportDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.ReportDto"--%>
<repo:ins-report reportDto="${reportDto}" >
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-inspection-comment-report.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp"%>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <div class="application-tab-footer">
            <div class="row">
                <div class="col-xs-12 col-sm-6 ">
                    <a class="back" id="previous" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${appId}'/>"><em class="fa fa-angle-left"></em> Previous</a>
                </div>
                <div class="col-xs-12">
                    <div class="button-group">
                        <a class="btn btn-primary" id="submitBtn" >Submit</a>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</repo:ins-report>
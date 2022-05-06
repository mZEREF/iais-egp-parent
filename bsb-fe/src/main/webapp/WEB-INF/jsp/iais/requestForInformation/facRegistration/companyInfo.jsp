<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>
<%--@elvariable id="organizationAddress" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo"--%>
<fac:companyInfo organizationAddress="${organizationAddress}">
    <jsp:attribute name="specialJsFrag">
        <iais-bsb:single-constant constantName="WEB_ROOT" classFullName="sg.gov.moh.iais.egp.bsb.constant.GlobalConstants" attributeKey="webroot"/>
        <%--@elvariable id="webroot" type="java.lang.String"--%>
        <script type="text/javascript" src="${webroot}/js/bsb/bsb-rfi-facility-register.js"></script>
    </jsp:attribute>
    <jsp:attribute name="dashboardFrag">
        <%@include file="dashboard.jsp" %>
    </jsp:attribute>
    <jsp:attribute name="innerFooterFrag">
        <div class="application-tab-footer">
            <div class="row">
                <div class="col-xs-12 col-sm-6 ">
                    <a class="back" id="previous" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                </div>
                <div class="col-xs-12 col-sm-6">
                    <div class="button-group">
                        <a class="btn btn-primary next" id="next" >Next</a>
                    </div>
                </div>
            </div>
        </div>
    </jsp:attribute>
</fac:companyInfo>
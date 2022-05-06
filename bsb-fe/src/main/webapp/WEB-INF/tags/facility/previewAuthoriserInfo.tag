<%@tag description="Preview authoriser info tag of facility registration" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="dataList" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto>" %>
<%@attribute name="srcNodePath" required="true" type="java.lang.String" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<jsp:invoke fragment="dashboardFrag"/>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

    <div class="main-content">
        <div class="container" style="padding-left: 0">
            <div class="row">
                <div class="col-xs-12" style="padding-left: 0">
                    <div class="tab-gp steps-tab" style="margin-top: 0;">
                        <div class="tab-content" style="padding-left: 30px">
                            <div>
                                <p style="font-weight: bold; padding-left: 8px;">Preview Personnel Authorised to Access the Facility Information</p>
                            </div>

                            <div>
                                <table class="table" aria-describedby="">
                                    <thead>
                                    <tr>
                                        <th scope="col">S/N</th>
                                        <th scope="col">Salutation</th>
                                        <th scope="col">Name</th>
                                        <th scope="col">ID Type</th>
                                        <th scope="col">ID Number</th>
                                        <th scope="col">Nationality</th>
                                        <th scope="col">Contact No.</th>
                                        <th scope="col">Email</th>
                                        <th scope="col">Employment Start Date</th>
                                        <th scope="col">Designation</th>
                                        <th scope="col">Employment Period</th>
                                        <th scope="col">Work Area</th>
                                        <th scope="col">Security Clearance Date</th>
                                    </tr>
                                    </thead>
                                    <c:forEach var="personnel" items="${dataList}" varStatus="status">
                                        <tr>
                                            <td>${status.count}</td>
                                            <td>${personnel.salutation}</td>
                                            <td>${personnel.name}</td>
                                            <td>${personnel.idType}</td>
                                            <td>${personnel.idNumber}</td>
                                            <td>${personnel.nationality}</td>
                                            <td>${personnel.contactNo}</td>
                                            <td>${personnel.email}</td>
                                            <td>${personnel.employmentStartDt}</td>
                                            <td>${personnel.designation}</td>
                                            <td>${personnel.employmentPeriod}</td>
                                            <td>${personnel.workArea}</td>
                                            <td>${personnel.securityClearanceDt}</td>
                                        </tr>
                                    </c:forEach>
                                </table>
                            </div>

                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6 ">
                                        <a class="back" href="javascript:void(0)" data-step-key="${srcNodePath}"><em class="fa fa-angle-left"></em> Previous</a>
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
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<div class="main-content" style="min-height: 73vh;">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <div class="col-xs-12">
            <div class="center-content">
                <div class="intranet-content" id="clearSelect">
                    <iais:body>
                        <iais:section title="" id="demoList">
                            <div class="row">
                                <div class="col-xs-10 col-md-12">
                                    <div class="components">
                                        <a class="btn btn-secondary" data-toggle="collapse" data-target="#beInboxFilter">Filter</a>
                                    </div>
                                </div>
                            </div>
                            <p></p>
                            <div id="beInboxFilter" class="collapse intranet-content">
                                <iais:row>
                                    <iais:field value="Facility Name"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityName" id="facilityName"
                                                     cssClass="facilityNameDropDown"
                                                     value="${auditSearch.facilityName}"
                                                     options="facilityName"
                                                     firstOption="All"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Facility classification"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityClassification" id="facilityClassification"
                                                     cssClass="facilityClassificationDropDown"
                                                     value="${auditSearch.facilityClassification}"
                                                     codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION"
                                                     firstOption="All"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Active Type"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityType" id="facilityType"
                                                     cssClass="facilityTypeDropDown"
                                                     value="${auditSearch.activeType}"
                                                     codeCategory="CATE_ID_BSB_ACTIVE_TYPE" firstOption="All"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Audit Type"/>
                                    <iais:value width="18">
                                        <iais:select name="auditType" id="auditType"
                                                     cssClass="auditTypeDropDown"
                                                     value="${auditSearch.auditType}"
                                                     codeCategory="CATE_ID_BSB_AUDIT_TYPE" firstOption="All"/>
                                        <span id="error_auditType"
                                              name="iaisErrorMsg"
                                              class="error-msg"></span>
                                    </iais:value>
                                </iais:row>

                                <iais:action style="text-align:right;">
                                    <button class="btn btn-secondary" type="button" id="clearBtn" name="clearBtn">
                                        Clear
                                    </button>
                                    <button class="btn btn-primary" type="button" id="searchBtn" name="searchBtn">
                                        Search
                                    </button>
                                </iais:action>
                            </div>
                        </iais:section>
                        <br>
                        <br>
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <%--@elvariable id="pageInfo" type="sg.gov.moh.iais.egp.bsb.dto.PageInfo"--%>
                        <iais-bsb:Pagination size="${pageInfo.size}" pageNo="${pageInfo.pageNo + 1}" pageAmt="${pageInfo.totalPages}" totalElements="${pageInfo.totalElements}"/>
                        <div class="table-gp">
                            <table aria-describedby="" class="table application-group" style="border-collapse:collapse;">
                                <thead>
                                <tr>
                                    <th scope="col" style="display: none"></th>
                                </tr>
                                <tr>
                                    <iais:sortableHeader needSort="false" field="" value=" " isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="facility.facilityName" value="Facility Name" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="facility.facilityClassification" value="Facility Classification" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="facility.facilityType" value="Activity type" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="auditDt" value="Audit Date" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="auditType" value="Audit Type" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="cancelReason" value="Cancellation Reasons" isFE="false"/>
                                </tr>
                                </thead>
                                    <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.audit.FacilityQueryResultDto.FacInfo>"--%>
                                <c:forEach var="item" items="${dataList}" varStatus="status">
                                    <c:set var="auditIndex" value="${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}"></c:set>
                                    <tr style="display: table-row;">
                                        <td><input name="auditId" type="checkbox" id="auditId${auditIndex}" value="<iais:mask name='auditId' value='${item.auditId}'/>"></td>
                                        <td>${item.facName}</td>
                                        <td><iais:code code="${item.facClassification}"/></td>
                                        <td><iais:code code="${item.activityType}"/></td>
                                        <td><fmt:formatDate value='${item.auditDate}' pattern='dd/MM/yyyy'/></td>
                                        <td><iais:code code="${item.auditType}"/></td>
                                        <td><c:out value="${item.cancelReason}"/></td>
                                    </tr>
                                </c:forEach>
                            </table>
<%--                            <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>--%>
                            <div style="text-align: right">
                                <button name="submitBtn" id="doCancel" type="button" class="btn btn-primary">
                                    Submit
                                </button>
                            </div>
                        </div>
                    </iais:body>
                </div>
            </div>
        </div>
    </form>
</div>
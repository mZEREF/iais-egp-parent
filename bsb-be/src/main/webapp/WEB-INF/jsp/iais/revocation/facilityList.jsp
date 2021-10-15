<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-revocation.js"></script>
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
                                        <a class="btn btn-secondary" data-toggle="collapse" name="filterBtn"
                                           data-target="#beInboxFilter">Filter</a>
                                    </div>
                                </div>
                            </div>
                            <p></p>
                            <div id="beInboxFilter" class="collapse intranet-content">
                                <iais:row>
                                    <iais:field value="Facility Name"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityName" id="facilityName"
                                                     value="${facilitySearch.facilityName}"
                                                     options="facilityName"
                                                     firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Facility classification"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityClassification" id="facilityClassification"
                                                     value="${facilitySearch.facilityClassification}"
                                                     codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION"
                                                     firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Active Type"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityType" id="facilityType"
                                                     value="${facilitySearch.activeType}"
                                                     codeCategory="CATE_ID_BSB_ACTIVE_TYPE" firstOption="Please Select"/>
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
                            <table class="table application-group" style="border-collapse:collapse;">
                                <thead>
                                <tr>
                                    <iais:sortableHeader needSort="false" field="" value="S/N" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Active Approval No." isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Facility Name/Address" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Facility Classification" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Process type" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Agents/Toxins" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Approved Date(dd/mm/yyyy)" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Status" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Action" isFE="false"/>
                                </tr>
                                </thead>
                                <c:forEach var="item" items="${dataList}" varStatus="status">
                                    <c:set var="auditIndex" value="${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}"></c:set>
                                    <tr style="display: table-row;">
                                        <td>${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}</td>
                                        <td>${item.approveNo}</td>
                                        <td>${item.facility.facilityName}/${item.facility.facilityAddress}</td>
                                        <td><iais:code code="${item.facility.facilityClassification}"></iais:code></td>
                                        <td><iais:code code="${item.processType}"></iais:code></td>
                                        <td></td>
                                        <td><fmt:formatDate value="${item.approvalDate}" pattern="dd/MM/yyyy"/></td>
                                        <td><iais:code code="${item.status}"></iais:code></td>
                                        <td>
                                            <p><a id="doRevoke" onclick="doRevoke('<iais:mask name="approvalId" value="${item.id}"/>','fac')">Revoke</a></p>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                                <%--                            <a style="float:left;padding-top: 1.1%;" class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>--%>
                        </div>
                    </iais:body>
                </div>
            </div>
        </div>
        <input name="approvalId" id="approvalId" value="" hidden>
        <input name="from" id="from" value="" hidden>
    </form>
</div>
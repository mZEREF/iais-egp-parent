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
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-revocation.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
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
                            <br>
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
                                        <input type="text" name="facilityName" id="facilityName"
                                               value="${applicationSearch.facilityName}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Facility Address"/>
                                    <iais:value width="18">
                                        <input type="text" name="facilityAddress" id="facilityAddress"
                                               value="${applicationSearch.facilityAddress}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Facility classification"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityClassification" id="facilityClassification"
                                                     value="${applicationSearch.facilityClassification}"
                                                     codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION"
                                                     firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Facility Type"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityType" id="facilityType"
                                                     value="${applicationSearch.facilityType}"
                                                     codeCategory="CATE_ID_BSB_FAC_TYPE" firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Process Type"/>
                                    <iais:value width="18">
                                        <iais:select name="processType" id="processType"
                                                     value="${applicationSearch.processType}"
                                                     codeCategory="CATE_ID_BSB_PRO_TYPE" firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Application No."/>
                                    <iais:value width="18">
                                        <input type="text" name="applicationNo" id="applicationNo"
                                               value="${applicationSearch.applicationNo}"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Application Type"/>
                                    <iais:value width="18">
                                        <iais:select name="applicationType" id="applicationType"
                                                     value="${applicationSearch.applicationType}"
                                                     codeCategory="CATE_ID_BSB_APP_TYPE" firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Application Status"/>
                                    <iais:value width="18">
                                        <iais:select name="applicationStatus" id="applicationStatus"
                                                     value="${applicationSearch.applicationStatus}"
                                                     codeCategory="CATE_ID_BSB_APP_STATUS" firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>
                                <iais:row>
                                    <iais:field value="Application Date"/>
                                    <iais:value width="18">
                                        <span data-err-ind="searchAppDateFrom" class="error-msg"></span>
                                        <iais:datePicker id="searchAppDateFrom" name="searchAppDateFrom" value="${applicationSearch.searchAppDateFrom}"/>
                                        To
                                        <span data-err-ind="searchAppDateTo" class="error-msg"></span>
                                        <iais:datePicker id="searchAppDateTo" name="searchAppDateTo" value="${applicationSearch.searchAppDateTo}"/>
                                    </iais:value>
                                </iais:row>

                                <iais:action style="text-align:right;">
                                    <button class="btn btn-secondary" type="button" id="clearBtn2" name="clearBtn2">
                                        Clear
                                    </button>
                                    <button class="btn btn-primary" type="button" id="searchBtn2" name="searchBtn2">
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
                                    <iais:sortableHeader needSort="true" field="applicationNo" value="Application No." isFE="false"/>
                                    <iais:sortableHeader needSort="true" field="appType" value="Application Type" isFE="false"/>
                                    <iais:sortableHeader needSort="true" field="facility.facilityName" value="Facility Name/Address" isFE="false"/>
                                    <iais:sortableHeader needSort="true" field="facility.facilityType" value="Facility type" isFE="false"/>
                                    <iais:sortableHeader needSort="true" field="processType" value="Process Type" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Biological Agents/Toxins" isFE="false"/>
                                    <iais:sortableHeader needSort="true" field="applicationDt" value="Application Date (dd/mm/yyyy)" isFE="false"/>
                                    <iais:sortableHeader needSort="true" field="facility.expiryDt" value="Facility / Approval Expiry Date (dd/mm/yyyy)" isFE="false"/>
                                    <iais:sortableHeader needSort="true" field="status" value="Application Status" isFE="false"/>
                                </tr>
                                </thead>
                                    <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.Application>"--%>
                                <c:forEach var="item" items="${dataList}" varStatus="status">
                                    <tr style="display: table-row;">
                                        <td><c:out value="${(status.index + 1) + (pageInfo.pageNo) * pageInfo.size}"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${item.appType eq 'BSBAPTY006'}">
                                                    <c:out value="${item.applicationNo}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <a onclick="doProcess('<iais:mask name="appId" value="${item.id}"/>')"><c:out value="${item.applicationNo}"/></a>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><iais:code code="${item.appType}"></iais:code></td>
                                        <td><c:out value="${item.facility.facilityName}/${item.facility.facilityAddress}"/></td>
                                        <td><iais:code code="${item.facility.facilityType}"></iais:code></td>
                                        <td><iais:code code="${item.processType}"></iais:code></td>
                                        <td><c:out value="${item.biologicalName}"/></td>
                                        <td><fmt:formatDate value='${item.applicationDt}' pattern='dd/MM/yyyy'/></td>
                                        <td><fmt:formatDate value='${item.facility.expiryDt}' pattern='dd/MM/yyyy'/></td>
                                        <td><iais:code code="${item.status}"></iais:code></td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </iais:body>
                </div>
            </div>
        </div>
        <input name="appId" id="appId" value="" hidden>
        <iais:confirm msg="GENERAL_ERR0023" needCancel="false" callBack="cancel()" popupOrder="support"></iais:confirm>
        <iais:confirm msg="" needCancel="false" callBack="aocancel()" popupOrder="approveAo"></iais:confirm>
    </form>
</div>
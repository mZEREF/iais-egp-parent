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
                                    <iais:field value="Approval Status"/>
                                    <iais:value width="18">
                                        <iais:select name="approvalStatus" id="approvalStatus"
                                                     value="${facilitySearch.approvalStatus}"
                                                     codeCategory="CATE_ID_BSB_APPROVAL_STATUS"
                                                     firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Approval No"/>
                                    <iais:value width="18">
                                        <input type="text" name="approvalNo" id="approvalNo" value="${facilitySearch.approvalNo}">
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
                                    <iais:sortableHeader needSort="false" field="" value="S/N"/>
                                    <iais:sortableHeader needSort="false" field="Approval No" value="Approval No"/>
                                    <iais:sortableHeader needSort="false" field="Approval Type" value="Approval Type"/>
                                    <iais:sortableHeader needSort="false" field="Approval Status" value="Approval Status"/>
                                    <iais:sortableHeader needSort="false" field="Facility Classification" value="Facility Classification"/>
                                    <iais:sortableHeader needSort="false" field="Facility Type" value="Facility Type"/>
                                    <iais:sortableHeader needSort="false" field="Facility Name" value="Facility Name"/>
                                    <iais:sortableHeader needSort="false" field="Facility Address" value="Facility Address"/>
                                    <iais:sortableHeader needSort="false" field="Facility Status" value="Facility Status"/>
                                    <iais:sortableHeader needSort="false" field="" value="action"/>
                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty dataList}">
                                        <tr>
                                            <td colspan="6">
                                                <iais:message key="GENERAL_ACK018" escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="items" items="${dataList}" varStatus="status">
                                            <tr>
                                                <td><c:out value="${status.index + 1}"/></td>
                                                <td><c:out value="${items.apprNo}"/></td>
                                                <td><iais:code code="${items.type}"/></td>
                                                <td><iais:code code="${items.status}"/></td>
                                                <td><iais:code code="${items.facClassification}"/></td>
                                                <td><iais:code code="${items.facType}"/></td>
                                                <td><iais:code code="${items.facName}"/></td>
                                                <td><c:out value="${items.facAddress}"/></td>
                                                <td><iais:code code="${items.facStatus}"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${items.status eq 'APPRSTA001' or items.status eq 'APPRSTA004'}">
                                                            <a href="/bsb-be/eservicecontinue/INTRANET/MohDOSubmitRevocation?approvalId=<iais:mask name='id' value='${items.id}'/>&OWASP_CSRFTOKEN=null&from=fac">revoke</a>
                                                        </c:when>
                                                        <c:otherwise>

                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </iais:body>
                </div>
            </div>
        </div>
        <input name="from" id="from" value="" hidden>
    </form>
</div>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%--@elvariable id="filterParam" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesQueryDto"--%>
<%--@elvariable id="tcuDateFrom" type="java.lang.String"--%>
<%--@elvariable id="tcuDateTo" type="java.lang.String"--%>
<%--@elvariable id="licencePremiseResult" type="com.ecquaria.cloud.moh.iais.common.dto.SearchResult<com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesQueryDto>"--%>
<webui:setLayout name="iais-intranet"/>
<div class="main-content dashboard">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>Change TCU Date</h2>
                        </div>
                        <div class="form-group">
                            <div class="col-xs-12 col-md-12">
                                <div class="col-xs-12 col-md-12">
                                    <div class="components">
                                        <a class="btn btn-secondary" data-toggle="collapse"
                                           data-target="#searchCondition" aria-expanded="true">Filter</a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="searchCondition" class="collapse" aria-expanded="true">
                            <iais:row>
                                <iais:field value="Licence No."/>
                                <iais:value width="18">
                                    <iais:input maxLength="24" type="text" name="licenceNo"
                                                value="${filterParam.licenceNo}"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field value="HCI Code"/>
                                <iais:value width="18">
                                    <iais:input maxLength="20" type="text" name="hciCode"
                                                value="${filterParam.hciCode}"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field value="Business Name"/>
                                <iais:value width="18">
                                    <iais:input maxLength="24" type="text" name="businessName"
                                                value="${filterParam.businessName}"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field value="HCI Postal Code"/>
                                <iais:value width="18">
                                    <iais:input maxLength="20" type="text" name="postalCode"
                                                value="${filterParam.postalCode}"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field value="Service Name"/>
                                <iais:value width="18">
                                    <iais:select name="serviceName" options="licSvcTypeOption"
                                                 firstOption="Please Select"
                                                 value="${filterParam.serviceName}" needSort="true"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field value="TCU Date From"/>
                                <iais:value width="18">
                                    <iais:datePicker id="fromDate" name="tcuDateFrom" value="${tcuDateFrom}"/>
                                </iais:value>
                            </iais:row>

                            <iais:row>
                                <iais:field value="TCU Date To"/>
                                <iais:value width="18">
                                    <iais:datePicker id="fromDate" name="tcuDateTo" value="${tcuDateTo}"/>
                                </iais:value>
                            </iais:row>

                            <div class="col-xs-12 col-md-12">
                                <iais:action style="text-align:right;">
                                    <button type="button" id="crud_clear_button" class="btn btn-secondary">Clear
                                    </button>

                                    <button type="button" id="crud_search_button" value="search"
                                            class="btn btn-primary">Search
                                    </button>
                                </iais:action>
                            </div>
                        </div>
                    </div>

                    <div class="components">
                        <h3>
                            <span>Search Results</span>
                        </h3>
                        <iais:pagination param="licencePremiseParam" result="licencePremiseResult"/>
                        <br><br>
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" style="display: none"></th>
                                    <iais:sortableHeader style="width:1%" needSort="false" field="checked" value=" "/>
                                    <iais:sortableHeader style="width:5%" needSort="true" field="licence_no"
                                                         value="Licence No."/>
                                    <iais:sortableHeader style="width:5%" needSort="true" field="business_name"
                                                         value="Business Name"/>
                                    <iais:sortableHeader style="width:5%" needSort="true" field="hci_code"
                                                         value="HCI Code"/>
                                    <iais:sortableHeader style="width:10%" needSort="true" field="address"
                                                         value="Address"/>
                                    <iais:sortableHeader style="width:5%" needSort="true" field="svc_name"
                                                         value="Service Name"/>
                                    <iais:sortableHeader style="width:5%" needSort="true" field="tcu_date"
                                                         value="TCU Date"/>
                                    <iais:sortableHeader style="width:5%" needSort="true" field="remarks"
                                                         value="Remarks"/>
                                </tr>
                                </thead>
                                <tbody style="text-align: left">
                                <c:choose>
                                    <c:when test="${empty licencePremiseResult.rows}">
                                        <tr>
                                            <td colspan="7">
                                                <iais:message key="GENERAL_ACK018" escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="licencePremise" items="${licencePremiseResult.rows}"
                                                   varStatus="status">
                                            <tr>
                                                <td align="left"><input name="itemCheckbox" type="checkbox"
                                                                        value="${licencePremise.licenceId}"/></td>
                                                <td align="left"><c:out value="${licencePremise.licenceNo}"/></td>
                                                <td align="left"><c:out value="${licencePremise.businessName}"/></td>
                                                <td align="left"><c:out value="${licencePremise.hciCode}"/></td>
                                                <td align="left"><c:out value="${licencePremise.address}"/></td>
                                                <td align="left"><c:out value="${licencePremise.serviceName}"/></td>
                                                <td align="left"><fmt:formatDate
                                                        value="${licencePremise.tcuDate}"/></td>
                                                <td align="left"><c:out value="${licencePremise.tcuDateRemarks}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>

                        <span id="error_premiseItems" name="iaisErrorMsg" class="error-msg"></span>
                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <a class="btn btn-secondary"
                                   href="${pageContext.request.contextPath}/change-tcu-date/search-result-download">DOWNLOAD</a>

                                <button type="button" class="btn btn-primary"
                                        onclick="doChangeTCUDate();">CHANGE TCU DATE
                                </button>
                            </iais:action>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<%@include file="/WEB-INF/jsp/include/validation.jsp"%>
<script type="text/javascript">
    function doDownload() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "download");
    }

    function doChangeTCUDate() {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "changeTCUDate")
    }
</script>
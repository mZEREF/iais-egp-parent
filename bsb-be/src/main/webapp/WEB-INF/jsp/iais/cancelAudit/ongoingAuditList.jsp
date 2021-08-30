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
                                                     value=""
                                                     codeCategory=""
                                                     firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Facility classification"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityClassification" id="facilityClassification"
                                                     value="${facilityClassification}"
                                                     codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION"
                                                     firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Facility Type"/>
                                    <iais:value width="18">
                                        <iais:select name="facilityType" id="facilityType"
                                                     value="${facilityType}"
                                                     codeCategory="CATE_ID_BSB_FAC_TYPE" firstOption="Please Select"/>
                                    </iais:value>
                                </iais:row>

                                <iais:row>
                                    <iais:field value="Audit Type" required="true"/>
                                    <iais:value width="18">
                                        <iais:select name="auditType" id="auditType"
                                                     value="${auditType}"
                                                     codeCategory="CATE_ID_BSB_AUDIT_TYPE" firstOption="Please Select"/>
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
                            <table class="table application-group" style="border-collapse:collapse;">
                                <thead>
                                <tr>
                                    <iais:sortableHeader needSort="false" field="" value="" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="facility.facilityName" value="Facility Name" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Facility Classification" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="facility.facilityType" value="Facility type" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Date of Last Audit" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Audit Type" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Scenario Category" isFE="false"/>
                                    <iais:sortableHeader needSort="false" field="" value="Audit Outcome" isFE="false"/>
                                </tr>
                                </thead>
                                    <%--@elvariable id="dataList" type="java.util.List<sg.gov.moh.iais.egp.bsb.entity.Application>"--%>
<%--                                <c:forEach var="item" items="" varStatus="status">--%>
                                    <tr style="display: table-row;">
                                        <td><input type="checkbox"></td>
                                        <td>facilityName</td>
                                        <td><iais:code code="Facility Classification"></iais:code></td>
                                        <td><iais:code code="facility.facilityType"></iais:code></td>
                                        <td>07/09/2021
<%--                                            <fmt:formatDate value='' pattern='dd/MM/yyyy'/>--%>
                                        </td>
                                        <td><iais:code code="Audit Type"></iais:code></td>
                                        <td><c:out value="Scenario Category"/></td>
                                        <td><c:out value="Audit Outcome"/></td>
                                    </tr>
<%--                                </c:forEach>--%>
                            </table>

                            <div class="row">
                                <div class="col-xs-12 col-sm-6">
                                    <a class="back" id="backToAuditCreation" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <div align="right">
                                    <button name="submitBtn3" id="submitBtn" type="button" class="btn btn-primary">
                                        Submit
                                    </button>
                                </div>
                            </div>
                        </div>
                    </iais:body>
                </div>
            </div>
        </div>
        <input name="appId" id="appId" value="" hidden>
<%--        <iais:confirm msg="GENERAL_ERR0023" needCancel="false" callBack="cancel()" popupOrder="support"></iais:confirm>--%>
<%--        <iais:confirm msg="" needCancel="false" callBack="aocancel()" popupOrder="approveAo"></iais:confirm>--%>
    </form>
</div>
<script>
    $("#submitBtn").click(function (){
        showWaiting();
        $("[name='action_type']").val("doCancel");
        $("#mainForm").submit();
    });
</script>
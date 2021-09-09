<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");


%>
<webui:setLayout name="iais-internet"/>
<%@include file="../common/dashboard.jsp"%>
<form  method="post" id="mainForm"   action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>


    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="components">
                    <div class="row">&nbsp;</div>
                    <iais:pagination param="searchGiroAccountParam" result="searchGiroDtoResult"/>
                    <div class="table-responsive">
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr >
                                    <style>
                                        .glyphicon {
                                            position: relative;
                                            top: -5px;
                                            display: inline-block;
                                            font-family: 'Glyphicons Halflings', serif;
                                            font-style: normal;
                                            font-weight: normal;
                                            line-height: 1;
                                            -webkit-font-smoothing: antialiased;
                                            -moz-osx-font-smoothing: grayscale;
                                        }
                                    </style>
                                    <th scope="col" style="display: none">

                                    </th>

                                    <iais:sortableHeader needSort="true" field="LICENCE_NO"
                                                         value="Licence No."/>
                                    <iais:sortableHeader needSort="true" field="SVC_NAME"
                                                         value="Service Type"/>
                                    <iais:sortableHeader needSort="true" field="LICENSEE_NAME" style="width:10%"
                                                         value="Licensee"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="ACCT_NAME"
                                                         value="Account Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="BANK_NAME" style="width:8%"
                                                         value="Bank Name"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="BANK_CODE"
                                                         value="Bank Code"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="BRANCH_CODE" style="width:9%"
                                                         value="Branch Code"/>
                                    <iais:sortableHeader needSort="true"
                                                         field="ACCT_NO"
                                                         value="Bank Account No."/>
                                </tr>
                                </thead>
                                <tbody class="form-horizontal">
                                <c:choose>
                                    <c:when test="${empty searchGiroDtoResult.rows}">
                                        <tr>
                                            <td colspan="15">
                                                <iais:message key="GENERAL_ACK018"
                                                              escape="true"/>
                                            </td>
                                        </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach var="pool"
                                                   items="${searchGiroDtoResult.rows}"
                                                   varStatus="status">
                                            <tr>

                                                <td>
                                                    <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                    <p><c:out value="${pool.licenceNo}"/></p>
                                                </td>
                                                <td >
                                                    <p class="visible-xs visible-sm table-row-title">Service Type</p>
                                                    <p><c:out value="${pool.svcName}"/></p>
                                                </td>
                                                <td>
                                                    <p class="visible-xs visible-sm table-row-title">Licensee</p>
                                                    <p><c:out value="${pool.licenseeName}"/></p>
                                                </td>
                                                <td>
                                                    <p class="visible-xs visible-sm table-row-title">Account Name</p>
                                                    <p><c:out value="${pool.acctName}"/></p>
                                                </td>
                                                <td>
                                                    <p class="visible-xs visible-sm table-row-title">Bank Name</p>
                                                    <p><c:out value="${pool.bankName}"/></p>
                                                </td>
                                                <td>
                                                    <p class="visible-xs visible-sm table-row-title">Bank Code</p>
                                                    <p><c:out value="${pool.bankCode}"/></p>
                                                </td>
                                                <td>
                                                    <p class="visible-xs visible-sm table-row-title">Branch Code</p>
                                                    <p><c:out value="${pool.branchCode}"/></p>
                                                </td>
                                                <td>
                                                    <p class="visible-xs visible-sm table-row-title">Bank Account No.</p>
                                                    <p><c:out value="${pool.acctNo}"/></p>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>

                    </div>
                    <div class="row">
                        <iais:action style="text-align:left;">
                            <div align="left"><span><a  href="/main-web/eservice/INTERNET/MohInternetInbox?init_to_page=initLic"><em class="fa fa-angle-left"> </em> Back</a></span></div>
                        </iais:action>
                    </div>

                    <div class="row">&nbsp;</div>
                </div>
            </div>
        </div>
    </div>
</form>
<script>
    function jumpToPagechangePage() {
        showWaiting();
        $("#mainForm").submit();
    }


    function sortRecords(sortFieldName, sortType) {
        showWaiting();
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("#mainForm").submit();
    }
</script>


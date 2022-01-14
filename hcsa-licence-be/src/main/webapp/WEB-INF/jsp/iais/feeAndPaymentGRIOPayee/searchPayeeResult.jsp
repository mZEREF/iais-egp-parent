<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<style>
    .form-check {
        display: revert;
    }
</style>
<div class="main-content dashboard">
    <form id="mainForm"  method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row form-horizontal">
                        <div class="bg-title col-xs-12 col-md-12">
                            <h2>
                                <span>List of GIRO Payees</span>
                            </h2>
                        </div>
                        <iais:row>
                            <iais:field value="UEN"/>
                            <iais:value width="18">
                                <label>
                                    <input type="text"
                                           style="width:180%; font-weight:normal;"
                                           name="uen" maxlength="35"
                                           value="${uen}"/>
                                </label>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Licensee"/>
                            <iais:value width="18">
                                <label>
                                    <input type="text"
                                           style="width:180%; font-weight:normal;"
                                           name="licensee" maxlength="35"
                                           value="${licensee}"/>
                                </label>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Licence No."/>
                            <iais:value width="18">
                                <label>
                                    <input type="text"
                                           style="width:180%; font-weight:normal;"
                                           name="licenceNo" maxlength="35"
                                           value="${licenceNo}"/>
                                </label>
                            </iais:value>
                        </iais:row>

                        <div class="col-xs-12 col-md-12">
                            <iais:action style="text-align:right;">
                                <button class="btn btn-secondary" type="button"
                                        onclick="javascript:doClear()">Clear</button>
                                <button type="button" class="btn btn-primary"
                                        onclick="javascript:doSearch();">Search
                                </button>
                            </iais:action>
                        </div>
                    </div>
                    <br>
                    <div class="row">
                        <div class="tab-pane active" id="tabInbox" role="tabpanel">
                            <div class="tab-content">
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="components">
                                            <h3>
                                                <span>Search Results</span>
                                            </h3>

                                            <iais:pagination param="searchGiroAccountParam" result="searchGiroDtoResult"/>
                                            <div class="table-responsive">
                                                <div class="table-gp">
                                                    <table aria-describedby="" class="table">
                                                        <thead>
                                                        <tr >
                                                            <th scope="col" style="width:2%">

                                                            </th>
                                                            <iais:sortableHeader needSort="true" field="UEN"
                                                                                 value="UEN"/>
                                                            <iais:sortableHeader needSort="true" field="LICENCE_NO"
                                                                                 value="Licence No."/>
                                                            <iais:sortableHeader needSort="true" field="SVC_NAME"
                                                                                 value="Service Type"/>
                                                            <iais:sortableHeader needSort="true" field="LICENSEE_NAME"
                                                                                 value="Licensee"/>
                                                            <iais:sortableHeader needSort="true"
                                                                                 field="ACCT_NAME"
                                                                                 value="Account Name"/>
                                                            <iais:sortableHeader needSort="true"
                                                                                 field="BANK_NAME"
                                                                                 value="Bank Name"/>
                                                            <iais:sortableHeader needSort="true"
                                                                                 field="BANK_CODE"
                                                                                 value="Bank Code"/>
                                                            <iais:sortableHeader needSort="true"
                                                                                 field="BRANCH_CODE"
                                                                                 value="Branch Code"/>
                                                            <iais:sortableHeader needSort="true"
                                                                                 field="ACCT_NO"
                                                                                 value="Bank Account No."/>
                                                            <iais:sortableHeader needSort="true"
                                                                                 field="DDA_REF_NO"
                                                                                 value="DDA Ref No."/>
                                                            <iais:sortableHeader needSort="false"
                                                                                 field="FILE_NAME"
                                                                                 value="GIRO Form"/>
                                                            <iais:sortableHeader needSort="false" field="REMARKS"
                                                                                 value="Internal Remarks"/>
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
                                                                        <td class="form-check"
                                                                            onclick="javascript:controlCease()">
                                                                            <input class="form-check-input licenceCheck"
                                                                                   id="giroAcct${status.index + 1}"
                                                                                   type="checkbox"
                                                                                   name="acctIds"
                                                                                   value="${pool.id}">
                                                                            <label class="form-check-label"
                                                                                   for="giroAcct${status.index + 1}"><span
                                                                                    class="check-square"></span>
                                                                            </label>
                                                                        </td>
                                                                        <td >
                                                                            <c:out value="${pool.uen}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:out value="${pool.licenceNo}"/>
                                                                        </td>
                                                                        <td >
                                                                            <c:out value="${pool.svcName}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:out value="${pool.licenseeName}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:out value="${pool.acctName}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:out value="${pool.bankName}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:out value="${pool.bankCode}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:out value="${pool.branchCode}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:out value="${pool.acctNo}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:out value="${pool.customerReferenceNo}"/>
                                                                        </td>
                                                                        <td>
                                                                            <c:forEach items="${pool.giroAccountFormDocDtoList}"
                                                                                       var="giroDoc" varStatus="docStatus">
                                                                                <iais:downloadLink fileRepoIdName="fileRo${docStatus.index}" fileRepoId="${giroDoc.fileRepoId}" docName="${giroDoc.docName}"/>
                                                                                <br>
                                                                            </c:forEach>
                                                                        </td>
                                                                        <td>
                                                                            <c:out value="${pool.remarks}"/>
                                                                        </td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        </tbody>
                                                    </table>
                                                </div>

                                            </div>
                                            <div class="row">&nbsp;</div>
                                            <iais:action style="text-align:right;">
                                                <button type="button" class="btn btn-primary AddAcctBtn"
                                                        onclick="javascript:doAddAcct();">Add
                                                </button>
                                                <button type="button" class="btn btn-primary DeleteBtn"
                                                        disabled
                                                        onclick="javascript:doDelete();">Delete
                                                </button>
                                            </iais:action>
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
</div>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">
    function doClear() {
        $('input[type="text"]').val("");
    }
    function controlCease() {
        var checkOne = false;
        var checkBox = $("input[name='acctIds']");
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                checkOne = true;
            }
            ;
        }
        ;
        if (checkOne) {
            $('.DeleteBtn').prop('disabled', false);
        } else {
            $('.DeleteBtn').prop('disabled', true);
        }
    }
    function jumpToPagechangePage() {
        showWaiting();
        $("[name='crud_action_type']").val("search");
        $("#mainForm").submit();    }

    function doSearch() {
        showWaiting();
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        $("[name='crud_action_type']").val("search");
        $("#mainForm").submit();
    }

    function doAddAcct(){
        showWaiting();
        $("[name='crud_action_type']").val("add");
        $("#mainForm").submit();
    }
    function doDelete(){
        showWaiting();
        $("[name='crud_action_type']").val("delete");
        $("#mainForm").submit();
    }
    function sortRecords(sortFieldName, sortType) {
        showWaiting();
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        $("[name='crud_action_type']").val("search");
        $("#mainForm").submit();
    }
</script>

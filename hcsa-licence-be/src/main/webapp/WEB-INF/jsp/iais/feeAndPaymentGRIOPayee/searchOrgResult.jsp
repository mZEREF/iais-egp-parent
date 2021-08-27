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
                                <span>Add a GIRO Payee</span>
                            </h2>
                        </div>
                        <div class="col-xs-12 col-md-12">
                            <iais:row>
                                <div class=" col-xs-12 col-md-12">
                                    Note: This function is to add a GIRO Payee who submitted a manual application.
                                </div>
                            </iais:row>
                            <iais:row>
                                <div class=" col-xs-12 col-md-12">
                                    The GIRO arrangement must be approved by the bank, otherwise GIRO deductions for that payee will fail.
                                </div>
                            </iais:row>
                            <iais:row>&nbsp;</iais:row>
                            <iais:row>
                                <h3>
                                    Search Organisation
                                </h3>
                            </iais:row>
                            <iais:row>
                                <iais:field value="UEN :"/>
                                <iais:value width="18">
                                    <label>
                                        <input type="text"
                                               style="width:180%; font-weight:normal;"
                                               name="uenNo" maxlength="20"
                                               value="${uenNo}"/>
                                    </label>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Licensee"/>
                                <iais:value width="18">
                                    <label>
                                        <input type="text"
                                               style="width:180%; font-weight:normal;"
                                               name="licenseeName" maxlength="35"
                                               value="${licenseeName}"/>
                                    </label>
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Licence No."/>
                                <iais:value width="18">
                                    <label>
                                        <input type="text"
                                               style="width:180%; font-weight:normal;"
                                               name="licenceNoSer" maxlength="35"
                                               value="${licenceNoSer}"/>
                                    </label>
                                </iais:value>
                            </iais:row>
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

                                            <iais:pagination param="orgPremParam" result="orgPremResult"/>
                                            <div class="table-responsive">
                                                <div class="table-gp">
                                                    <table aria-describedby="" class="table">
                                                        <thead>
                                                        <tr >
                                                            <th scope="col" style="width:2%">&nbsp;</th>
                                                            <iais:sortableHeader needSort="true"
                                                                                 field="UEN_NO"
                                                                                 value="UEN"/>
                                                            <iais:sortableHeader needSort="true" field="LICENCE_NO"
                                                                                 value="Licence No."/>
                                                            <iais:sortableHeader needSort="true" field="SVC_NAME"
                                                                                 value="Service Type"/>
                                                            <iais:sortableHeader needSort="true" field="LICENSEE_NAME"
                                                                                 value="Licensee"/>
                                                        </tr>
                                                        </thead>
                                                        <tbody class="form-horizontal">
                                                        <c:choose>
                                                            <c:when test="${empty orgPremResult.rows}">
                                                                <tr>
                                                                    <td colspan="15">
                                                                        <iais:message key="GENERAL_ACK018"
                                                                                      escape="true"/>
                                                                    </td>
                                                                </tr>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:forEach var="pool"
                                                                           items="${orgPremResult.rows}"
                                                                           varStatus="status">
                                                                    <tr>
                                                                        <td class="form-check"
                                                                            onclick="javascript:controlCease()">
                                                                            <input class="form-check-input licenceCheck"
                                                                                   id="orgPer${status.index + 1}"
                                                                                   type="checkbox"
                                                                                   name="opIds"
                                                                                   value="${pool.id}">
                                                                            <label class="form-check-label"
                                                                                   for="orgPer${status.index + 1}"><span
                                                                                    class="check-square"></span>
                                                                            </label>
                                                                        </td>
                                                                        <td >
                                                                            <c:out value="${pool.uenNo}"/>
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
                                                <a style=" float:left;padding-top: 1.1%;text-decoration:none;" href="#" onclick="javascript:doBack()"><em class="fa fa-angle-left"> </em> Back</a>

                                                <button type="button" class="btn btn-primary SelectBtn"
                                                        disabled
                                                        onclick="javascript:doSelect();">Select
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
    function doBack(){
        showWaiting();
        $("[name='crud_action_type']").val("back");
        $("#mainForm").submit();
    }
    function controlCease() {
        var checkOne = false;
        var checkBox = $("input[name='opIds']");
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                checkOne = true;
            }
            ;
        }
        ;
        if (checkOne) {
            $('.SelectBtn').prop('disabled', false);
        } else {
            $('.SelectBtn').prop('disabled', true);
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
    function doSelect(){
        showWaiting();
        $("[name='crud_action_type']").val("select");
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

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<div class="main-content">
<form id = "mainForm" method = "post" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="actionTodo" id="actionTodo" />
            <div class="row">
                <div class="col-xs-12">
                    <div class="instruction-content center-content">
                        <div class="bg-title"><h2>Manual Audit List Creation List</h2></div>
                         <div class="table-gp">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th width="15%">Facility Name</th>
                                        <th width="25%">Facility Classification</th>
                                        <th  width="15%">Facility Type</th>
                                        <th  width="22%">Audit Type</th>
                                        <th width="19%">Remarks</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach var="item" items="${auditSearchResult}">
                                        <tr style="display: table-row;">
                                            <td>
                                                <p>
                                                    <c:out  value="${item.facilityName}"/>
                                                </p>
                                            </td>
                                            <td>
                                                <p>
                                                    <c:out  value="${item.facilityClassification}"/>
                                                </p>
                                            </td>
                                            <td>
                                                <p><c:out value="${item.facilityType}"/></p>
                                            </td>
                                            <td></td>
                                            <td>
                                                <select id="AuditTypeSelect">
                                                    <option>Please Select</option>
                                                    <option>Audit by MOH</option>
                                                    <option>Facility self-audit</option>
                                                    <option>Not required</option>
                                                </select>
                                                <span class="error-msg" id="error_selectedOne" name="iaisErrorMsg"></span>
                                            </td>
                                            <td>
                                                <textarea cols="32"  rows="5" name="${id}reason"  maxlength="2000"><c:out value="${item.cancelReason}"></c:out></textarea>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <input hidden id="rows" value="1">
                                    </tbody>

                                </table>
                                <div class="table-footnote">
                                </div>
                            </div>
                        <iais:action style="text-align:right;">
                            <button type="button" class="btn btn-secondary" onclick="javascript:cancel();">Back
                            </button>

                            <button id="submitButtom" type="button" class="btn btn-primary next" onclick="javascript:confirm();">Submit
                            </button>
                        </iais:action>
                    </div>
                </div>
            </div>


</form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">

    // $("#submitButtom").click(function () {
    //     // var index =$("#AuditTypeSelect option:selected").val();
    //     if (index == "Please Select"){
    //         $("#error_selectedOne").text("This filed is mandatory");
    //     }else {
    function confirm() {
        SOP.Crud.cfxSubmit("mainForm", "confirm");
    }
    //     }
    // });

    function cancel() {
        SOP.Crud.cfxSubmit("mainForm","doback");
    }

</script>

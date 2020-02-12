<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value=""/>
    <input type="hidden" name="crud_action_value" value=""/>
    <input type="hidden" name="crud_action_additional" value=""/>
    <iais:body >
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br>
                    <h2>Basic Search Criteria</h2>
                    <div class="panel panel-default">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="search no"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="search_no"  />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <input type="radio" name="hci" />HCI Name
                                            </iais:value>
                                            <iais:value width="18">
                                                <input type="radio" name="application"  />Application No
                                            </iais:value>
                                            <iais:value width="18">
                                                <input type="radio" name="licence" />Licence No
                                            </iais:value>
                                            <iais:value width="18">
                                                <input type="radio" name="licensee"  />Licensee Name
                                            </iais:value>
                                            <iais:value width="18">
                                                <input type="radio" name="servicePersonnel"  />Service Personnel Name
                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:center;">
                                            <button type="button" class="btn btn-lg btn-login-submit" type="button"
                                                    style="background:#2199E8; color: white" onclick="javascript:doSearch();">Search</button>
                                            <button type="button" class="btn btn-lg btn-login-submit" type="button"
                                                    style="background:#2199E8; color: white" onclick="javascript:doAdvancedSearch();">Advanced</button>
                                            <button type="button" class="btn btn-lg btn-login-clear" type="button"
                                                    style="background:#2199E8; color: white" onclick="javascript:doClear();">Clear</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container">
            <div class="col-xs-14">
                <div class="components">
                    <h3>
                        <span>Search Result</span>
                    </h3>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="APPLICATION_NO" value="Application No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="APP_TYPE" value="Application Type"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="LICENCE_NO" value="Licence No."></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="HCI_CODE" value="HCI Code"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="HCI_NAME" value="HCI Name "></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="ADDRESS" value=" Address"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="LICENSEE_NAME" value="Licensee NAME"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="SERVICE_NAME" value="Service NAME"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Period" value="Licence Period"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Status" value="Licence Status"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="past_compliance_history" value="Past Compliance History"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="current_risk_tagging" value="Current Risk Tagging"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty SearchResult.rows}">
                                    <tr>
                                        <td colspan="7">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${SearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                            <td><a onclick="doAppInfo('${pool.licenseeId}')">${pool.applicationNo}</a></td>
                                            <td><c:out value="${pool.applicationType}"/></td>
                                            <td><c:out value="${pool.licenceNo}"/></td>
                                            <td><c:out value="${pool.hciCode}"/></td>
                                            <td><c:out value="${pool.hciName}"/></td>
                                            <td><c:out value="${pool.blkNo}-${pool.floorNo}-${pool.unitNo}-${pool.streetName}-${pool.buildingName}"/></td>
                                            <td><c:out value="${pool.licenseeName}"/></td>
                                            <td><c:out value="${pool.serviceName}"/></td>
                                            <td><fmt:formatDate value="${pool.startDate}" pattern="dd/MM/yyyy" />-<fmt:formatDate value="${pool.expiryDate}" pattern="dd/MM/yyyy" /></td>
                                            <td><c:out value="${pool.licenceStatus}"/></td>
                                            <td><c:out value="${pool.pastComplianceHistory}"/></td>
                                            <td><c:out value="${pool.currentRiskTagging}"/></td>

                                        </tr>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </iais:body>
</form>
<script type="text/javascript">


    function doClear(){
        $('input[name="search_no"]').val("");
    }

    function doAdvancedSearch(){
        SOP.Crud.cfxSubmit("mainForm", "advSearch");
    }

    function doSearch(){
        var chkNum = 0;
        var checkBox = $('input[type = checkbox]');
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                chkNum++;
            };
        };
        if(chkNum==0||chkNum==1){
            SOP.Crud.cfxSubmit("mainForm", "basicSearch");
        }
        else {
            SOP.Crud.cfxSubmit("mainForm", "advSearch");
        }
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('licSort');
    }
    function doAppInfo(licenseeId) {
        SOP.Crud.cfxSubmit("mainForm", "details",licenseeId);
    }
</script>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
            <div class="col-xs-15">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br><br><br>
                    <h2>Basic Search Criteria</h2>

                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="Search No"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text"  style="width:180%; font-weight:normal;" name="searchNo" maxlength="100" value="${searchNo}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <c:choose>
                                                    <c:when test="${count==1}"><input type="checkbox" name="hci" checked /> HCI Name</c:when>
                                                    <c:otherwise><input type="checkbox" name="hci"  /> HCI Name</c:otherwise>
                                                </c:choose>
                                            </iais:value>
                                            <iais:value width="18">
                                                <c:choose>
                                                    <c:when test="${count==2}"><input type="checkbox" name="application" checked /> Application No</c:when>
                                                    <c:otherwise><input type="checkbox" name="application"  /> Application No</c:otherwise>
                                                </c:choose>
                                            </iais:value>
                                            <iais:value width="18">
                                                <c:choose>
                                                    <c:when test="${count==3}"><input type="checkbox" name="licence" checked /> Licence No</c:when>
                                                    <c:otherwise><input type="checkbox" name="licence"  /> Licence No</c:otherwise>
                                                </c:choose>
                                            </iais:value>
                                            <iais:value width="18">
                                                <c:choose>
                                                    <c:when test="${count==4}"><input type="checkbox" name="licensee" checked /> Licensee Name</c:when>
                                                    <c:otherwise><input type="checkbox" name="licensee"  /> Licensee Name</c:otherwise>
                                                </c:choose>
                                            </iais:value>
                                            <iais:value width="18">
                                                <c:choose>
                                                    <c:when test="${count==5}"><input type="checkbox" name="servicePersonnel" checked /> Service Personnel Name</c:when>
                                                    <c:otherwise><input type="checkbox" name="servicePersonnel"  /> Service Personnel Name</c:otherwise>
                                                </c:choose>
                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:right;">
                                            <button type="button" class="btn btn-primary" type="button"
                                                     onclick="javascript:doSearch();">Search</button>
                                            <button type="button" class="btn btn-primary" type="button"
                                                     onclick="javascript:doAdvancedSearch();">Advanced Search</button>
                                            <button type="button" class="btn btn-secondary" type="button"
                                                     onclick="javascript:doClear();">Clear</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>

                </div>
            </div>
        </div>
        <div class="container">
            <div class="col-xs-15">
                <div class="components">
                    <h3>
                        <span>Search Results</span>
                    </h3>
                    <iais:pagination  param="SearchParam" result="SearchResult"/>
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
                                <iais:sortableHeader needSort="false"  field="HCI_ADDRESS" value="HCI Address"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="LICENSEE_NAME" value="Licensee Name"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="SERVICE_NAME" value="Service Name"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Period" value="Licence Period"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="Licence_Status" value="Licence Status"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="2nd_last_compliance_history" value="2nd Last Compliance History"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="last_compliance_history" value="Last Compliance History"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false"  field="current_risk_tagging" value="Current Risk Tagging"></iais:sortableHeader>
                                <iais:sortableHeader needSort="false" field="" value="Action"></iais:sortableHeader>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty SearchResult.rows}">
                                    <tr>
                                        <td colspan="15">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${SearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${status.index + 1+ (SearchParam.pageNo - 1) * SearchParam.pageSize}"/></td>
                                            <td>
                                                <c:if test="${pool.appCorrId==null}">${pool.applicationNo}</c:if>
                                                <c:if test="${pool.appCorrId!=null}"><a onclick="javascript:doAppInfo('${pool.appCorrId}')">${pool.applicationNo}</a></c:if>
                                            </td>
                                            <td><c:out value="${pool.applicationType}"/></td>
                                            <td><a onclick="javascript:doLicInfo('${pool.licenceId}')">${pool.licenceNo}</a></td>
                                            <td><c:out value="${pool.hciCode}"/></td>
                                            <td><c:out value="${pool.hciName}"/></td>
                                            <td><c:out value="${pool.blkNo}-${pool.floorNo}-${pool.unitNo}-${pool.streetName}-${pool.buildingName}"/></td>
                                            <td><c:out value="${pool.licenseeName}"/></td>
                                            <td><iais:service value="${pool.serviceName}"></iais:service></td>
                                            <td><fmt:formatDate value="${pool.startDate}" pattern="dd/MM/yyyy" />-<fmt:formatDate value="${pool.expiryDate}" pattern="dd/MM/yyyy" /></td>
                                            <td><c:out value="${pool.licenceStatus}"/></td>
                                            <td><c:out value="${pool.twoLastComplianceHistory}"/></td>
                                            <td><c:out value="${pool.lastComplianceHistory}"/></td>
                                            <td><c:out value="${pool.currentRiskTagging}"/></td>
                                            <td>
                                                <c:if test="${pool.isCessation==1}">
                                                    <iais:action style="text-align:center;">
                                                        <a onclick="javascript:doCessation('${pool.licenceId}');" >Cessation</a>
                                                    </iais:action>
                                                </c:if>
                                                <c:if test="${pool.isCessation==0}">
                                                    <iais:action style="text-align:center;">
                                                        <a  data-toggle="modal" data-target="#editUser"  >Cessation</a>
                                                    </iais:action>
                                                </c:if>
                                            </td>
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
        <div class="modal fade" id="editUser" tabindex="-1" role="dialog" aria-labelledby="editUser"
             style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                            <h5 class="modal-title" id="gridSystemModalLabel">Confirmation Box</h5>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem">There is a pending application for this licence, please withdraw the application before proceeding with cessation</span>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </iais:body>
</form>
<script type="text/javascript">


    function doClear(){
        $('input[name="searchNo"]').val("");
        $('input[type="checkbox"]').prop("checked", false);
    }

    function doAdvancedSearch(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "advSearch");
    }
    function jumpToPagechangePage(){
        doSearch()
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
            showWaiting();SOP.Crud.cfxSubmit("mainForm", "basicSearch");
        }
        else {
            showWaiting();SOP.Crud.cfxSubmit("mainForm", "advSearch");
        }
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('licSort');
    }
    function doLicInfo(licenceId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "details",licenceId);

    }
    function doAppInfo(appCorrId) {
        showWaiting();

        SOP.Crud.cfxSubmit("mainForm", "appDetails",appCorrId);
    }
    function doCessation(licId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "cessation",licId);
    }
</script>
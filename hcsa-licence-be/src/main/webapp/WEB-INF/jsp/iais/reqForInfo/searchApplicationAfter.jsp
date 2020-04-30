<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MiscUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">

    <iais:body >
        <div class="container">
            <div class="col-xs-14">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br><br><br>
                    <h3>
                        <span>Advanced Search Criteria For Application</span>
                    </h3>

                    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                        <div class="panel-body">
                            <div class="panel-main-content">
                                <iais:section title="" id = "supPoolList">
                                    <iais:row>
                                        <iais:field value="Application No."/>
                                        <iais:value width="18">
                                            <label>
                                                <input type="text" style="width:180%; font-weight:normal;" name="application_no" maxlength="20" value="${SearchParam.filters['appNo']}" />
                                            </label>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Application Type"/>
                                        <iais:value width="18">
                                            <div id="application_type">
                                                <iais:select  name="application_type" options="appTypeOption" firstOption="Please Select" value="${SearchParam.filters['appType']}" ></iais:select>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Application Status"/>
                                        <iais:value width="18">
                                            <div id="application_status">
                                                <iais:select  name="application_status" options="appStatusOption" firstOption="Please Select" value="${SearchParam.filters['appStatus']}" ></iais:select>
                                            </div>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Application Submitted Date From"/>
                                        <iais:value width="18">
                                            <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['subDate']}" ></iais:datePicker>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Application Submitted Date To"/>
                                        <iais:value width="18">
                                            <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['toDate']}"></iais:datePicker>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row style="color:#ff0000; display: none" id="submittedDateError">
                                        <iais:field value=""/>
                                        <iais:value width="18">
                                            Application Submitted Date From cannot be later than Application Submitted Date To.                                            </p>
                                        </iais:value>
                                    </iais:row>
                                    <iais:action style="text-align:left;">
                                        <a   onclick="javascript:doAppBack()">< Back</a>
                                    </iais:action>
                                    <iais:action style="text-align:right;">
                                        <button class="btn btn-secondary" type="button"  onclick="javascript:doAppClear()">Clear</button>
                                        <button class="btn btn-primary" type="button"  onclick="javascript:doAppSearch()">Search</button>
                                    </iais:action>
                                </iais:section>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <div class="container">
            <div class="col-xs-14">
                <div class="components">

                    <iais:pagination  param="SearchParam" result="SearchResult"/>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field="" value="S/N"/>
                                <iais:sortableHeader needSort="false"  field="APPLICATION_NO" value="Application No."/>
                                <iais:sortableHeader needSort="false"  field="APP_TYPE" value="Application Type"/>
                                <iais:sortableHeader needSort="false"  field="LICENCE_NO" value="Licence No."/>
                                <iais:sortableHeader needSort="false"  field="HCI_CODE" value="HCI Code"/>
                                <iais:sortableHeader needSort="false"  field="HCI_NAME" value="HCI Name "/>
                                <iais:sortableHeader needSort="false"  field="ADDRESS" value=" Address"/>
                                <iais:sortableHeader needSort="false"  field="LICENSEE_NAME" value="Licensee Name"/>
                                <iais:sortableHeader needSort="false"  field="SERVICE_NAME" value="Service Name"/>
                                <iais:sortableHeader needSort="false"  field="Licence_Period" value="Licence Period"/>
                                <iais:sortableHeader needSort="false"  field="Licence_Status" value="Licence Status"/>
                                <iais:sortableHeader needSort="false"  field="past_compliance_history" value="Past Compliance History"/>
                                <iais:sortableHeader needSort="false"  field="current_risk_tagging" value="Current Risk Tagging"/>

                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty SearchResult.rows}">
                                    <tr>
                                        <td colspan="13">
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
                                                <c:if test="${pool.appCorrId!=null}"><a onclick="javascript:doAppInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.appCorrId)}')">${pool.applicationNo}</a></c:if>
                                            </td>
                                            <td><c:out value="${pool.applicationType}"/></td>
                                            <td><a onclick="javascript:doLicInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.licenceId)}')">${pool.licenceNo}</a></td>
                                            <td><c:out value="${pool.hciCode}"/></td>
                                            <td><c:out value="${pool.hciName}"/></td>
                                            <td>
                                                <c:if test="${pool.licenceNo==null}">
                                                    <c:out value="${MiscUtil.getAddress(pool.blkNo,pool.streetName,pool.buildingName,pool.floorNo,pool.unitNo,'')}"/>
                                                </c:if>
                                                <c:if test="${pool.licenceNo!=null}">
                                                    <c:choose>
                                                        <c:when test="${pool.address.size() == 1}">
                                                            <c:out value="${pool.address[0]}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <select>
                                                                <option value ="">Multiple</option>
                                                                <c:forEach items="${pool.address}" var="address" varStatus="index">
                                                                    <option value ="${address}">${address}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </td>
                                            <td><c:out value="${pool.licenseeName}"/></td>
                                            <td><iais:service value="${pool.serviceName}"></iais:service></td>
                                            <td><fmt:formatDate value="${pool.startDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" />-<fmt:formatDate value="${pool.expiryDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /></td>
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
    function appSearch(){
        showWaiting();
        var to=$('#to_date').val();
        var sub=$('#sub_date').val();
        if(sub>to&&to!=""){
            $("#submittedDateError").show();
            dismissWaiting();
        }
        else {
            SOP.Crud.cfxSubmit("mainForm", "searchApp");
        }

    }

    function doAppSearch(){
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        appSearch()
    }
    function doAppBack(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doAppClear(){
        $('input[name="application_no"]').val("");
        $("#application_type option:first").prop("selected", 'selected');
        $("#application_status option:first").prop("selected", 'selected');
        $("#application_type .current").text("Please Select");
        $("#application_status .current").text("Please Select");
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
    }

    function doAppInfo(appCorrId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "appInfo",appCorrId);
    }
    function doLicInfo(licenceId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "licInfo",licenceId);
    }
    function jumpToPagechangePage(){
        appSearch()
    }

</script>
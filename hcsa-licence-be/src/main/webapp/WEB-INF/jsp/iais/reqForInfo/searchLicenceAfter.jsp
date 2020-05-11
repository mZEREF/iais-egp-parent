<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
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
    <input type="hidden" name="crud_action_type" value=""/>
    <input type="hidden" name="crud_action_value" value=""/>
    <input type="hidden" name="crud_action_additional" value=""/>
    <iais:body >
        <div class="container">
            <div class="col-xs-12">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br><br><br>
                    <h3>
                        <span>Advanced Search Criteria For Licence</span>
                    </h3>
                    <div class="row">
                        <div class="col-xs-10 col-md-12">
                            <div class="components">
                                <a class="btn btn-secondary" data-toggle="collapse"
                                   data-target="#searchCondition">Filter</a>
                            </div>
                        </div>
                    </div>

                    <div id="searchCondition" class="collapse">
                        <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
                            <div class="panel-body">
                                <div class="panel-main-content">
                                    <iais:section title="" id = "supPoolList">
                                        <iais:row>
                                            <iais:field value="Licence No."/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" style="width:180%; font-weight:normal;" name="licence_no" maxlength="20" value="${SearchParam.filters['licence_no']}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Service Type"/>
                                            <iais:value width="18">
                                                <div id="service_licence_type">
                                                    <iais:select  name="service_licence_type" options="licSvcTypeOption" firstOption="Please Select" value="${SearchParam.filters['licSvcName']}" ></iais:select>
                                                </div>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Status"/>
                                            <iais:value width="18">
                                                <div id="licence_status">
                                                    <iais:select  name="licence_status" options="licStatusOption" firstOption="Please Select" value="${SearchParam.filters['licence_status']}"  ></iais:select>
                                                </div>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Period From"/>
                                            <iais:value width="18">
                                                <iais:datePicker id = "sub_date" name = "sub_date" value="${SearchParam.filters['start_date']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:field value="Licence Period To"/>
                                            <iais:value width="18">
                                                <iais:datePicker id = "to_date" name = "to_date" value="${SearchParam.filters['expiry_date']}"></iais:datePicker>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row style="color:#ff0000; display: none" id="periodDateError">
                                            <iais:field value=""/>
                                            <iais:value width="18">
                                                Licence Period Date From cannot be later than Licence Period Date To.                                            </p>
                                            </iais:value>
                                        </iais:row>
                                        <iais:action style="text-align:left;">
                                            <a    onclick="javascript:doLicBack()">< Back</a>
                                        </iais:action>
                                        <iais:action style="text-align:right;">
                                            <button class="btn btn-secondary" type="button"  onclick="javascript:doLicClear()">Clear</button>
                                            <button class="btn btn-primary" type="button"  onclick="javascript:doLicSearch()">Search</button>
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
            <div class="col-xs-12">
                <div class="components">

                    <iais:pagination  param="SearchParam" result="SearchResult"/>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <iais:sortableHeader needSort="false" field="" value="S/N"/>
                                <iais:sortableHeader needSort="false"  field="LICENCE_NO" value="Licence No."/>
                                <iais:sortableHeader needSort="false"  field="HCI_CODE" value="HCI Code"/>
                                <iais:sortableHeader needSort="false"  field="HCI_NAME" value="HCI Name "/>
                                <iais:sortableHeader needSort="false"  field="ADDRESS" value=" Address"/>
                                <iais:sortableHeader needSort="false"  field="LICENSEE_NAME" value="Licensee Name"/>
                                <iais:sortableHeader needSort="false"  field="SERVICE_NAME" value="Service Name"/>
                                <iais:sortableHeader needSort="false"  field="Licence_Period" value="Licence Period"/>
                                <iais:sortableHeader needSort="false"  field="Licence_Status" value="Licence Status"/>
                                <iais:sortableHeader needSort="false" field="" value="Action"/>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty SearchResult.rows}">
                                    <tr>
                                        <td colspan="12">
                                            <iais:message key="ACK018" escape="true"></iais:message>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${SearchResult.rows}" varStatus="status">
                                        <tr>
                                            <td class="row_no"><c:out value="${status.index + 1 + (SearchParam.pageNo - 1) * SearchParam.pageSize}"/></td>
                                            <td><a onclick="javascript:doLicInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.licenceId)}')">${pool.licenceNo}</a></td>
                                            <td><c:out value="${pool.hciCode}"/></td>
                                            <td><c:out value="${pool.hciName}"/></td>
                                            <td>
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
                                            </td>
                                            <td><c:out value="${pool.licenseeName}"/></td>
                                            <td><c:out value="${pool.serviceName}"/></td>
                                            <td><fmt:formatDate value="${pool.startDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" />-<fmt:formatDate value="${pool.expiryDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /></td>
                                            <td><c:out value="${pool.licenceStatus}"/></td>
                                            <td>
                                                <c:if test="${pool.licenceStatus=='Active'}">
                                                    <iais:action style="text-align:right;">
                                                        <a onclick="javascript:doReqForInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.licPremId)}');" >ReqForInfo</a>
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
    </iais:body>


</form>
<script type="text/javascript">

    function doLicSearch(){
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        licSearch()

    }
    function licSearch(){
        showWaiting();
        var to=$('#to_date').val();
        var sub=$('#sub_date').val();
        if(sub>to&&to!=""){
            $("#periodDateError").show();
            dismissWaiting();
        }
        else {
            SOP.Crud.cfxSubmit("mainForm", "searchLic");
        }

    }
    function doLicBack(){
        showWaiting();SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doLicClear(){
        $('input[name="licence_no"]').val("");
        $("#service_licence_type option:first").prop("selected", 'selected');
        $("#licence_status option:first").prop("selected", 'selected');
        $("#service_licence_type .current").text("Please Select");
        $("#licence_status .current").text("Please Select");
        $('input[name="to_date"]').val("");
        $('input[name="sub_date"]').val("");
    }
    function doReqForInfo(licPremId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "reqForInfo",licPremId);
    }
    function doLicInfo(licenceId) {
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "licInfo",licenceId);
    }
    function jumpToPagechangePage(){
        licSearch()
    }
</script>
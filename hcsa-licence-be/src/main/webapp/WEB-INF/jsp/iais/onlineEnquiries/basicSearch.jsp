<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MiscUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
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
            <div class="col-xs-15">
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <br><br><br><br><br><br>
                    <h2>Basic Search Criteria</h2>
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
                                            <iais:field value="Keyword search or part of"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text"  style="width:180%; font-weight:normal;" name="searchNo" maxlength="100" value="${searchNo}" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18" >
                                                <input  id="hciChk" type="radio"
                                                       name="searchChk" value="1" <c:if test="${count=='1'}">checked</c:if> />&nbsp;HCI Name
                                            </iais:value>
                                            <iais:value width="18" cssClass="form-check">
                                                <input  id="applicationChk" type="radio"
                                                       <c:if test="${count=='2'}">checked</c:if>      name="searchChk" value="2" />&nbsp;Application No
                                            </iais:value>
                                            <iais:value width="18" cssClass="form-check">
                                                <input  id="licenceChk" type="radio" <c:if test="${count=='3'}">checked</c:if>  value="3"   name="searchChk"  />&nbsp;Licence No
                                            </iais:value>
                                            <iais:value width="18" cssClass="form-check">
                                                <input  id="licenseeChk" type="radio"
                                                       <c:if test="${count=='4'}">checked</c:if>     value="4"    name="searchChk"  />&nbsp;Licensee Name
                                            </iais:value>
                                            <iais:value width="18" cssClass="form-check">
                                                <input  id="servicePersonnelChk" type="radio"
                                                       value="5"   <c:if test="${count=='5'}">checked</c:if>   name="searchChk"  />&nbsp;Service Personnel Name
                                            </iais:value>
                                        </iais:row>
                                        <iais:row id="selectSearchChkMsg" style="display: none">
                                            <div class="col-sm-9">
                                                <p style="color:#ff0000;">
                                                    Please select one option
                                                </p>
                                            </div>
                                        </iais:row>
                                        <iais:action style="text-align:right;">
                                            <button type="button" class="btn btn-secondary" type="button"
                                                    onclick="javascript:doClear();">Clear</button>
                                            <button type="button" class="btn btn-primary" type="button"
                                                    onclick="javascript:doSearch();">Search</button>
                                            <button type="button" class="btn btn-primary" type="button"
                                                    onclick="javascript:doAdvancedSearch();">Advanced Search</button>
                                        </iais:action>
                                    </iais:section>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <br>
        <div class="container">
            <div class="col-xs-15">
                <div class="components">

                    <iais:pagination  param="SearchParam" result="SearchResult"/>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <c:if test="${cease==1}">
                                    <th class="form-check">
                                        <c:if test="${!empty SearchResult.rows}">
                                            <input class="form-check-input licenceCheck" type="checkbox" name="userUids" id="checkboxAll" onchange="javascirpt:checkAll('${isASO}');"/>
                                            <label class="form-check-label" for="checkboxAll">
                                                <span class="check-square"></span>
                                            </label>
                                        </c:if>
                                    </th>
                                </c:if>
                                <iais:sortableHeader needSort="false" field="" value="S/N"/>
                                <iais:sortableHeader needSort="false"  field="APPLICATION_NO" value="Application No."/>
                                <iais:sortableHeader needSort="false"  field="APP_TYPE" value="Application Type"/>
                                <iais:sortableHeader needSort="false"  field="LICENCE_NO" value="Licence No."/>
                                <iais:sortableHeader needSort="false"  field="HCI_CODE" value="HCI Code"/>
                                <iais:sortableHeader needSort="false"  field="HCI_NAME" value="HCI Name "/>
                                <iais:sortableHeader needSort="false"  field="HCI_ADDRESS" value="HCI Address"/>
                                <iais:sortableHeader needSort="false"  field="LICENSEE_NAME" value="Licensee Name"/>
                                <iais:sortableHeader needSort="false"  field="SERVICE_NAME" value="Service Name"/>
                                <iais:sortableHeader needSort="false"  field="Licence_Period" value="Licence Period"/>
                                <iais:sortableHeader needSort="false"  field="Licence_Status" value="Licence Status"/>
                                <iais:sortableHeader needSort="false"  field="2nd_last_compliance_history" value="2nd Last Compliance History"/>
                                <iais:sortableHeader needSort="false"  field="last_compliance_history" value="Last Compliance History"/>
                                <iais:sortableHeader needSort="false"  field="current_risk_tagging" value="Current Risk Tagging"/>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${empty SearchResult.rows}">
                                    <tr>
                                        <td colspan="15">
                                            <iais:message key="ACK018" escape="true"/>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="pool" items="${SearchResult.rows}" varStatus="status">
                                        <tr>
                                            <c:if test="${cease==1}">
                                                <td class="form-check" onclick="javascript:controlCease('${isASO}')" >
                                                    <c:if test="${pool.licenceStatus=='Active'&&pool.licenceId!=null}">
                                                        <input class="form-check-input licenceCheck" id="licence${status.index + 1}" type="checkbox"
                                                               name="appIds" value="${pool.appId}|${pool.isCessation}|${pool.licenceId}|${pool.licenceStatus}"   >
                                                        <label class="form-check-label" for="licence${status.index + 1}"><span
                                                                class="check-square"></span>
                                                        </label>
                                                    </c:if>
                                                </td>
                                            </c:if>
                                            <td class="row_no">
                                                <c:out value="${status.index + 1+ (SearchParam.pageNo - 1) * SearchParam.pageSize}"/>
                                            </td>
                                            <td>
                                                <c:if test="${pool.appCorrId==null}">${pool.applicationNo}</c:if>
                                                <c:if test="${pool.appCorrId!=null}"><a onclick="javascript:doAppInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.appCorrId)}')">${pool.applicationNo}</a></c:if>
                                            </td>
                                            <td><c:out value="${pool.applicationType}"/></td>
                                            <td>
                                                <c:if test="${pool.licenceId!=null&&pool.licenceStatus!='Inactive'}"><a onclick="javascript:doLicInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.licenceId)}')">${pool.licenceNo}</a></c:if>
                                                <c:if test="${pool.licenceId==null|| pool.licenceStatus=='Inactive'}">${pool.licenceNo}</c:if>
                                            </td>
                                            <td><c:out value="${pool.hciCode}"/><c:if test="${empty pool.hciCode}">-</c:if></td>
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
                                            <td><iais:service value="${pool.serviceName}"/></td>
                                            <td><fmt:formatDate value="${pool.startDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" />-<fmt:formatDate value="${pool.expiryDate}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /></td>
                                            <td><c:out value="${pool.licenceStatus}"/></td>
                                            <td><c:out value="${pool.twoLastComplianceHistory}"/></td>
                                            <td><c:out value="${pool.lastComplianceHistory}"/></td>
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
        <c:if test="${ SearchResult.rowCount<5}">
            <div class="row">&nbsp;</div>
            <div class="row">&nbsp;</div>
            <div class="row">&nbsp;</div>
            <div class="row">&nbsp;</div>
            <div class="row">&nbsp;</div>
            <div class="row">&nbsp;</div>
            <div class="row">&nbsp;</div>
        </c:if>
    </iais:body>
    <div class="row" height="1" style="display: none ;color:#ff0000;" id="selectDecisionMsg">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<iais:message key="CESS_ERR002" escape="flase"></iais:message>
    </div>
    <iais:action style="text-align:right;">
        <a class="btn btn-secondary" onclick="$(this).attr('class', 'btn btn-secondary disabled')" href="${pageContext.request.contextPath}/officer-online-enquiries-information-file">Download</a>
        <c:if test="${cease==1}">
            <button type="button" class="btn btn-primary ReqForInfoBtn" disabled
                    onclick="javascript:doReqForInfo();">ReqForInfo</button>
            <button type="button" class="btn btn-primary CeaseBtn" disabled
                    onclick="javascript:doCessation();">Cease</button>
        </c:if>
    </iais:action>


</form>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
<script type="text/javascript">


    function controlCease(isAso) {
        var checkOne = false;
        var checkBox = $("input[name='appIds']");
        for (var i = 0; i < checkBox.length; i++) {
            if (checkBox[i].checked) {
                checkOne = true;
            }
            ;
        }
        ;
        if (checkOne) {
            $('.ReqForInfoBtn').prop('disabled',false);
        } else {
            $('.ReqForInfoBtn').prop('disabled',true);
        }
        if (checkOne&&isAso==="1") {
            $('.CeaseBtn').prop('disabled',false);
        } else {
            $('.CeaseBtn').prop('disabled',true);
        }
        ;
    }
    function doClear(){
        $('input[name="searchNo"]').val("");
        $('input[type="radio"]').prop("checked", false);
    }

    function doAdvancedSearch(){
        showWaiting();
        var chk=$("[name='searchChk']:checked");
        var dropIds = new Array();
        chk.each(function(){
            dropIds.push($(this).val());
        });
        if(dropIds.length===0){
            $("#selectSearchChkMsg").show();
            dismissWaiting();
        }else {
            SOP.Crud.cfxSubmit("mainForm", "advSearch");
        }
    }
    function jumpToPagechangePage(){
        search();
    }
    function doSearch(){
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        search();
    }
    function search(){
        showWaiting();
        var chk=$("[name='searchChk']:checked");
        var dropIds = new Array();
        chk.each(function(){
            dropIds.push($(this).val());
        });
        if(dropIds.length===0){
            $("#selectSearchChkMsg").show();
            dismissWaiting();
        }else {
            SOP.Crud.cfxSubmit("mainForm", "basicSearch");
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

    function checkAll(isAso) {
        if ($('#checkboxAll').is(':checked')) {
            $("input[name='appIds']").attr("checked", "true");
            var chk = $("[name='appIds']:checked");
            var dropIds = new Array();
            chk.each(function () {
                dropIds.push($(this).val());
            });
            if(dropIds.length!==0){
                $('.ReqForInfoBtn').prop('disabled',false);
                if(isAso==="1"){
                    $('.CeaseBtn').prop('disabled',false);
                }
            }

        } else {
            $("input[name='appIds']").removeAttr("checked");
            $('.CeaseBtn').prop('disabled',true);
            $('.ReqForInfoBtn').prop('disabled',true);
        }
    }

    function doCessation() {
        showWaiting();
        var chk=$("[name='appIds']:checked");
        var dropIds = new Array();
        chk.each(function(){
            dropIds.push($(this).val());
        });
        var flog=false;
        for(var i=0;i<dropIds.length;i++){
            var str=dropIds[i].split('|')[1];
            if(str=='1'){
                flog=true;
            }
        }
        if(flog){
            SOP.Crud.cfxSubmit("mainForm", "cessation");
        }
        else {
            $("#selectDecisionMsg").show();
            dismissWaiting();
        }
    }

    function doReqForInfo() {
        showWaiting();
        var chk=$("[name='appIds']:checked");
        var dropIds = new Array();
        chk.each(function(){
            dropIds.push($(this).val());
        });
        var flog=false;
        for(var i=0;i<dropIds.length;i++){
            var str=dropIds[i].split('|')[3];
            if(str=='Active'){
                flog=true;
            }
        }
        if(flog){
            SOP.Crud.cfxSubmit("mainForm", "reqForInfo");
        }
        else {
            $("#selectDecisionMsg").show();
            dismissWaiting();
        }

    }
</script>
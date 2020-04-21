<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
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
                                        <iais:value width="18" cssClass="form-check">
                                            <c:choose>
                                                <c:when test="${count==1}">
                                                    <input class="form-check-input licenceCheck" id="hciChk" type="checkbox"
                                                           name="hciChk" checked >
                                                    <label class="form-check-label" for="hciChk">
                                                        <span class="check-square"></span>
                                                    </label> HCI Name
                                                </c:when>
                                                <c:otherwise>
                                                    <input class="form-check-input licenceCheck" id="hciChk" type="checkbox" name="hciChk"  >
                                                    <label class="form-check-label" for="hciChk">
                                                        <span class="check-square"></span>
                                                    </label> HCI Name
                                                </c:otherwise>
                                            </c:choose>
                                        </iais:value>
                                        <iais:value width="18" cssClass="form-check">
                                            <c:choose>
                                                <c:when test="${count==2}">
                                                    <input class="form-check-input licenceCheck" id="applicationChk" type="checkbox"
                                                           checked      name="applicationChk"  >
                                                    <label class="form-check-label" for="applicationChk">
                                                        <span class="check-square"></span>
                                                    </label> Application No
                                                </c:when>
                                                <c:otherwise><input class="form-check-input licenceCheck" id="applicationChk" type="checkbox"
                                                                    name="applicationChk"  >
                                                    <label class="form-check-label" for="applicationChk">
                                                        <span class="check-square"></span>
                                                    </label> Application No
                                                </c:otherwise>
                                            </c:choose>
                                        </iais:value>
                                        <iais:value width="18" cssClass="form-check">
                                            <c:choose>
                                                <c:when test="${count==3}">
                                                    <input class="form-check-input licenceCheck" id="licenceChk" type="checkbox" checked              name="licenceChk"  >
                                                    <label class="form-check-label" for="licenceChk"><span
                                                            class="check-square"></span>
                                                    </label> Licence No
                                                </c:when>
                                                <c:otherwise>
                                                    <input class="form-check-input licenceCheck" id="licenceChk" type="checkbox"
                                                           name="licenceChk"  >
                                                    <label class="form-check-label" for="licenceChk"><span
                                                            class="check-square"></span>
                                                    </label> Licence No</c:otherwise>
                                            </c:choose>
                                        </iais:value>
                                        <iais:value width="18" cssClass="form-check">
                                            <c:choose>
                                                <c:when test="${count==4}">
                                                    <input class="form-check-input licenceCheck" id="licenseeChk" type="checkbox"
                                                           checked         name="licenseeChk"  >
                                                    <label class="form-check-label" for="licenseeChk"><span
                                                            class="check-square"></span>
                                                    </label> Licensee Name</c:when>
                                                <c:otherwise>
                                                    <input class="form-check-input licenceCheck" id="licenseeChk" type="checkbox"
                                                           name="licenseeChk"  >
                                                    <label class="form-check-label" for="licenseeChk"><span
                                                            class="check-square"></span>
                                                    </label> Licensee Name</c:otherwise>
                                            </c:choose>
                                        </iais:value>
                                        <iais:value width="18" cssClass="form-check">
                                            <c:choose>
                                                <c:when test="${count==5}"><input class="form-check-input licenceCheck" id="servicePersonnelChk" type="checkbox"
                                                                                  checked             name="servicePersonnelChk"  >
                                                    <label class="form-check-label" for="servicePersonnelChk"><span
                                                            class="check-square"></span>
                                                    </label> Service Personnel Name</c:when>
                                                <c:otherwise><input class="form-check-input licenceCheck" id="servicePersonnelChk" type="checkbox"
                                                                    name="servicePersonnelChk"  >
                                                    <label class="form-check-label" for="servicePersonnelChk"><span
                                                            class="check-square"></span>
                                                    </label> Service Personnel Name</c:otherwise>
                                            </c:choose>
                                        </iais:value>
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
        <br>
        <div class="container">
            <div class="col-xs-15">
                <div class="components">

                    <iais:pagination  param="SearchParam" result="SearchResult"/>
                    <div class="table-gp">
                        <table class="table">
                            <thead>
                            <tr align="center">
                                <th class="form-check">
                                    <c:if test="${!empty SearchResult.rows}">
                                        <input class="form-check-input licenceCheck" type="checkbox" name="userUids" id="checkboxAll" onchange="javascirpt:checkAll();"/>
                                        <label class="form-check-label" for="checkboxAll">
                                            <span class="check-square"></span>
                                        </label>
                                    </c:if>
                                </th>
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
                                            <td class="form-check">
                                                <c:if test="${pool.licenceStatus!='Lapsed'&&pool.licenceStatus!='Ceased'&&pool.licenceStatus!='Expired'}">
                                                    <input class="form-check-input licenceCheck" id="licence${status.index + 1}" type="checkbox"
                                                           name="appIds" value="${pool.appId}|${pool.isCessation}"   >
                                                    <label class="form-check-label" for="licence${status.index + 1}"><span
                                                            class="check-square"></span>
                                                    </label>
                                                </c:if>
                                            </td>
                                            <td class="row_no">
                                                <c:out value="${status.index + 1+ (SearchParam.pageNo - 1) * SearchParam.pageSize}"/>
                                            </td>
                                            <td>
                                                <c:if test="${pool.appCorrId==null}">${pool.applicationNo}</c:if>
                                                <c:if test="${pool.appCorrId!=null}"><a onclick="javascript:doAppInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.appCorrId)}')">${pool.applicationNo}</a></c:if>
                                            </td>
                                            <td><c:out value="${pool.applicationType}"/></td>
                                            <td><a onclick="javascript:doLicInfo('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,pool.licenceId)}')">${pool.licenceNo}</a></td>
                                            <td><c:out value="${pool.hciCode}"/></td>
                                            <td><c:out value="${pool.hciName}"/></td>
                                            <td><c:out value="${pool.blkNo}-${pool.floorNo}-${pool.unitNo}-${pool.streetName}-${pool.buildingName}"/></td>
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
    </iais:body>
    <div height="1" style="display: none" id="selectDecisionMsg">
        <div class="col-sm-9">
            <p style="color:#ff0000;">
                There is a pending application for this licence, please withdraw the application before proceeding with cessation
            </p>
        </div>
    </div>
    <c:choose>
        <c:when test="${isASO==1}">
            <iais:action style="text-align:right;">
                <a class="btn btn-secondary" onclick="$(this).attr('class', 'btn btn-secondary disabled')" href="${pageContext.request.contextPath}/officer-online-enquiries-information-file">Download</a>
                <button type="button" class="btn btn-primary"
                        onclick="javascript:doCessation();">Cease</button>
            </iais:action>
        </c:when>
        <c:otherwise>
            <iais:action style="text-align:right;">
                <a class="btn btn-secondary" onclick="$(this).attr('class', 'btn btn-secondary disabled')" href="${pageContext.request.contextPath}/officer-online-enquiries-information-file">Download</a>
                <button type="button" class="btn btn-primary" disabled
                        onclick="javascript:doCessation();">Cease</button>
            </iais:action>
        </c:otherwise>
    </c:choose>

</form>
<%@include file="/WEB-INF/jsp/include/utils.jsp" %>
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
        search();
    }
    function doSearch(){
        $('input[name="pageJumpNoTextchangePage"]').val(1);
        search();
    }
    function search(){
        var chkNum = 0;
        var checkBox = $('input[type = checkbox][name$="Chk"]');
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

    function checkAll() {
        if ($('#checkboxAll').is(':checked')) {
            $("input[name='appIds']").attr("checked","true");
        } else {
            $("input[name='appIds']").removeAttr("checked");
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
</script>
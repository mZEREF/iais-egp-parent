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
                                            <iais:field value="Search No"/>
                                            <iais:value width="18">
                                                <label>
                                                    <input type="text" name="search_no" maxlength="20" value="${search_no}" style="width:180%; font-weight:normal;" />
                                                </label>
                                            </iais:value>
                                        </iais:row>
                                        <iais:row>
                                            <iais:value width="18">
                                                <input type="radio" name="select_search" value="application"  /> Application No
                                            </iais:value>
                                            <iais:value width="18">
                                                <input type="radio" name="select_search" value="licence" checked /> Licence No
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
                                                    <iais:action style="text-align:center;">
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


    function doClear(){
        $('input[name="search_no"]').val("");
    }

    function doAdvancedSearch(){
        var radios=document.getElementsByName("select_search");
        for (var i = 0, length = radios.length; i < length; i++) {
            if (radios[i].checked) {
                showWaiting();
                SOP.Crud.cfxSubmit("mainForm", radios[i].value);
                break;
            }
        }
    }
    function doSearch(){
        var radios=document.getElementsByName("select_search");
        for (var i = 0, length = radios.length; i < length; i++) {
            if (radios[i].checked) {
                showWaiting();
                if(radios[i].value=="application"){
                    SOP.Crud.cfxSubmit("mainForm", "search");
                }
                else {
                    SOP.Crud.cfxSubmit("mainForm", "searchLic");
                }
                break;
            }
        }
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        submit('licSort');
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
        doSearch()
    }
</script>
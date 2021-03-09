<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
  String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
  <form method="post" id="giroDeductionForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="beGiroDeductionType" value="">
    <input type="hidden" id="appCorrelationId" name="appCorrelationId" value="">
    <div class="main-content">
      <div class="row">
        <div class="col-lg-12 col-xs-12">
          <div class="center-content">
            <div class="intranet-content">
              <div class="bg-title">
                <h2>
                  <span>GIRO Deduction</span>
                </h2>
              </div>
              <iais:body>
                <iais:section title="" id = "demoList">
                  <div class="row">
                    <div class="col-xs-10 col-md-12">
                      <div class="components">
                        <a name="filterBtn" class="btn btn-secondary" data-toggle="collapse"
                           data-target="#giroDeductionPool">Filter</a>
                      </div>
                    </div>
                  </div>
                  <p></p>
                  <div id="giroDeductionPool" class="collapse">
                    <iais:row>
                      <iais:field value="Application No."/>
                      <iais:value width="18">
                        <input type="text" name="applicationNo" value="${giroDedSearchParam.filters['applicationNo']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Transaction ID"/>
                      <iais:value width="18">
                        <input type="text" name="transactionId" value="${giroDedSearchParam.filters['transactionId']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Bank Account No."/>
                      <iais:value width="18">
                        <input type="text" name="bankAccountNo" value="${giroDedSearchParam.filters['bankAccountNo']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Payment Reference No."/>
                      <iais:value width="18">
                        <input type="text" name="paymentRefNo" value="${giroDedSearchParam.filters['paymentRefNo']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Payment Amount"/>
                      <iais:value width="18">
                        <input type="text" name="paymentAmount" value="${giroDedSearchParam.filters['paymentAmount']}" />
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Payment Description"/>
                      <iais:value width="18">
                        <textarea id="paymentDescription" name="paymentDescription" maxlength="500" cols="60" rows="7" style="font-size:16px"><c:out value="${inspectionPreTaskDto.accCondMarks}"></c:out></textarea>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="HCI Name"/>
                      <iais:value width="18">
                        <input type="text" name="hci_name" value="${giroDedSearchParam.filters['hci_address']}" />
                      </iais:value>
                    </iais:row>
                    <iais:action style="text-align:right;">
                      <button name="clearBtn" class="btn btn-secondary" type="button" onclick="javascript:doGiroDeductionClear()">Clear</button>
                      <button name="searchBtn" class="btn btn-primary" type="button" onclick="javascript:doGiroDeductionSearch()">Search</button>
                    </iais:action>
                  </div>
                </iais:section>
                <h3>
                  <span>Search Results</span>
                </h3>
                <iais:pagination  param="giroDedSearchParam" result="giroDedSearchResult"/>
                <div class="table-gp">
                  <table class="table application-group">
                    <thead>
                    <tr align="center">
                      <th><input type="checkbox" name="allGiroDeductionCheck" id="allGiroDeductionCheck" <c:if test="${'check' eq giroDeductionCheck}">checked</c:if>
                                 onchange="javascript:giroDeductionCheckAll()" value="<c:out value="${giroDeductionCheck}"/>"/></th>
                      <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="HCI_NAME" value="HCI Name"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="GROUP_NO" value="Application No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="" value="Transaction Reference No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="INVOICE_NO" value="Invoice No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="" value="Bank Account No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="PMT_STATUS" value="Payment Status"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="PMT_STATUS" value="Payment Amount"></iais:sortableHeader>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                      <c:when test="${empty giroDedSearchResult.rows}">
                        <tr>
                          <td colspan="7">
                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                          </td>
                        </tr>
                      </c:when>
                      <c:otherwise>
                        <c:forEach var="pool" items="${giroDedSearchResult.rows}" varStatus="status">
                          <tr style = "display: table-row;" id = "advfilter${(status.index + 1) + (giroDedSearchParam.pageNo - 1) * giroDedSearchParam.pageSize}">
                            <td>
                              <input type="checkbox" name="giroDueCheck" id="giroDueCheck${status.index}"
                                     onchange="javascript:doGiroDeductionCheck()" value="<c:out value="${proRec.appPremisesPreInspectionNcItemDto.id}"/>"/>
                            </td>
                            <td class="row_no"><c:out value="${(status.index + 1) + (giroDedSearchParam.pageNo - 1) * giroDedSearchParam.pageSize}"/></td>
                            <td><c:out value="${pool.submissionType}"/></td>
                            <td><iais:code code="${pool.applicationType}"/></td>
                            <td><c:out value="${pool.submissionType}"/></td>
                            <td><iais:code code="${pool.paymentStatus}"/></td>
                            <td><iais:code code="${pool.paymentStatus}"/></td>
                            <td><iais:code code="${pool.paymentStatus}"/></td>
                            <td><iais:code code="${pool.paymentStatus}"/></td>
                          </tr>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>
                    </tbody>
                  </table>
                  <iais:action style="text-align:right;">
                    <button name="searchBtn" class="btn btn-primary" type="button" data-toggle= "modal" data-target= "#giroDeductionRetrigger">Search</button>
                    <iais:confirm yesBtnCls="btn btn-primary" msg="Are you sure you want to do this?" callBack="doGiroDeductionRetrigger()" popupOrder="giroDeductionRetrigger" needCancel="true"></iais:confirm>
                  </iais:action>
                </div>
              </iais:body>
            </div>
          </div>
        </div>
      </div>
    </div>
  </form>
</div>
<script type="text/javascript">

    function doGiroDeductionRetrigger() {
        showWaiting();
        giroDeductionSubmit('retrigger');
    }

    function doGiroDeductionCheck(){
        var flag = true;
        var allNcItemCheck = document.getElementById("allGiroDeductionCheck");
        var ncItemCheckList = document.getElementsByName("giroDueCheck");
        for (var x = 0; x < ncItemCheckList.length; x++) {
            if(ncItemCheckList[x].checked==false){
                flag = false;
                break;
            }
        }
        if(flag){
            allNcItemCheck.checked = true;
        }else{
            allNcItemCheck.checked = false;
        }
    }

    function giroDeductionCheckAll(){
        if ($('#allGiroDeductionCheck').is(':checked')) {
            $("input[name = 'giroDueCheck']").attr("checked","true");
        } else {
            $("input[name = 'giroDueCheck']").removeAttr("checked");
        }
    }

    function doGiroDeductionClear() {
        $('input[name="applicationNo"]').val("");
        $('input[name="transactionId"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="bankAccountNo"]').val("");
        $('input[name="paymentRefNo"]').val("");
        $('input[name="paymentAmount"]').val("");
        $('input[name="paymentDescription"]').val("");
    }

    function giroDeductionSubmit(action){
        $("[name='beGiroDeductionType']").val(action);
        var mainPoolForm = document.getElementById('giroDeductionForm');
        mainPoolForm.submit();
    }

    function doGiroDeductionSearch() {
        showWaiting();
        giroDeductionSubmit('search');
    }

    function jumpToPagechangePage(){
        showWaiting();
        giroDeductionSubmit('page');
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        giroDeductionSubmit('sort');
    }
</script>


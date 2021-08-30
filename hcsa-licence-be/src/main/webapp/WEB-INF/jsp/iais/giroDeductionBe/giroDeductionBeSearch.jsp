<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.grio.GrioConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
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
  <form method="post" id="giroDeductionForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="beGiroDeductionType" value="">
    <input type="hidden" id="appCorrelationId" name="appCorrelationId" value="">
    <input type="hidden" name="fileMaxSize" id="fileMaxSize" value="${String.valueOf(ConfigHelper.getInt("iais.system.upload.file.limit", 10))}">
    <input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
    <input type="hidden" id="fileMandatoryMessage" name="fileMandatoryMessage" value="<iais:message key=" GENERAL_ERR0006"/>">
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
                      <iais:field value="Application No." />
                      <iais:value width="6">
                        <input type="text" name="applicationNo" value="${giroDedSearchParam.filters['groupNo']}" maxlength="50"/>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Transaction Reference No."/>
                      <iais:value width="6">
                        <input type="text" name="txnRefNo" value="${giroDedSearchParam.filters['txnRefNo']}" maxlength="50"/>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Bank Account No."/>
                      <iais:value width="6">
                        <input type="text" name="bankAccountNo" value="${giroDedSearchParam.filters['acctNo']}" maxlength="35"/>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="Payment Amount"/>
                      <iais:value width="6">
                        <input type="text" name="paymentAmount" value="${giroDedSearchParam.filters['amount']}" maxlength="10"/>
                      </iais:value>
                    </iais:row>
                    <iais:row>
                      <iais:field value="HCI Name"/>
                      <iais:value width="6">
                        <input type="text" name="hci_name" value="${giroDedSearchParam.filters['hciName']}" maxlength="100"/>
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
                <div class="table-gp">
                  <table aria-describedby="" class="table application-group">
                    <iais:pagination  param="giroDedSearchParam" result="giroDedSearchResult"/>
                    <thead>
                    <tr >
                      <th scope="col" style="padding-bottom: 27px">
                        <input type="checkbox" name="allGiroDeductionCheck" id="allGiroDeductionCheck" <c:if test="${'check' eq giroDeductionCheck}">checked</c:if> onclick="javascript:giroDeductionCheckAll()" value="<c:out value="${giroDeductionCheck}"/>"/>
                      </th>
                      <th scope="col" style="display: none"></th>
                                <iais:sortableHeader needSort="false" field="" value="S/N"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="HCI_NAME" value="HCI Name"></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="GROUP_NO" value="Application No."></iais:sortableHeader>
                      <iais:sortableHeader needSort="false" field="" value="Transaction Reference No."></iais:sortableHeader>
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
                              <c:if test="${pool.pmtStatus=='PMT09'}">
                                <input type="checkbox" name="giroDueCheck" id="giroDueCheck${status.index}"
                                       onclick="javascript:doGiroDeductionCheck()" value="<c:out value="${pool.appGroupNo}"/>"/>
                              </c:if>
                            </td>
                            <td class="row_no"><c:out value="${(status.index + 1) + (giroDedSearchParam.pageNo - 1) * giroDedSearchParam.pageSize}"/></td>
                            <td>${pool.hciName}</td>
                            <td><iais:code code="${pool.appGroupNo}"/></td>
                            <td><c:out value="${pool.txnRefNo}"/></td>
                            <td><iais:code code="${pool.acctNo}"/></td>
                            <td>
                                ${pool.pmtStatus == 'PMT03' ? "Pending" : (pool.pmtStatus == 'PMT09' ? "Failed" : (pool.pmtStatus == 'PMT01' ? "Success" : ""))}</td>
                            <td><iais:code code="${pool.amount}"/></td>
                          </tr>
                        </c:forEach>
                      </c:otherwise>
                    </c:choose>
                    </tbody>
                  </table>
                  <iais:action style="text-align:right;">
                    <button name="searchBtn" class="btn btn-primary disabled" type="button" id="retrigger" onclick="doGiroDeductionRetrigger()">Re-trigger payment</button>
                    <a  class="btn btn-primary" id="download" href="${pageContext.request.contextPath}/generatorFileCsv">Download Spreadsheet</a>
                    <input class="selectedFile"  id="selectedFile" name = "selectedFile" style="display: none"  type="file"  aria-label="selectedFile1" onclick="fileClicked(event)" onchange="javascript:doUserRecUploadConfirmFile(event)">
                    <a class="btn btn-file-upload btn-secondary" id="uploadFile">Upload Status</a>
                    <br/>
                    <span id="error_selectedFileError" name="iaisErrorMsg" class="error-msg"></span>
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
<input type="hidden" id="reasult" value="${message}" >
<iais:confirm msg="${message}" needCancel="false"  callBack="cancel()" popupOrder="deleteFile"></iais:confirm>
<iais:confirm  msg="RGP_ACK001" callBack="doGiroDeductionRetriggerSaveAck()" popupOrder="giroDeductionRetrigger" needCancel="false"/>
<script type="text/javascript">
    $(document).ready(function (){
        if($('#reasult').val()!=''){
            $('#deleteFile').modal("show");
        }
        if(${'1' == saveRetriggerOK}){
            $('#giroDeductionRetrigger').modal("show");
        }
    });
    function cancel() {
        $('#deleteFile').modal("hide");
    }

    function retrigerClick(){
        var checkedNum = $("[name='giroDueCheck']:checked").length;
        if (checkedNum > 0){
            $('#retrigger').removeClass('disabled');
        }else{
            $("#retrigger").addClass('disabled');
        }
    }


    function doGiroDeductionRetrigger() {
        showWaiting();
        giroDeductionSubmit('retrigger');
    }
    function fileClicked(event) {
        var fileElement = event.target;
        if (fileElement.value != "") {
            console.log("Clone( #" + fileElement.id + " ) : " + fileElement.value.split("\\").pop())
            clone[fileElement.id] = $(fileElement).clone(); //'Saving Clone'
        }
        //What ever else you want to do when File Chooser Clicked
    }
    function doUserRecUploadConfirmFile(event){
        var fileElement = event.target;
        if (fileElement.value == "") {
            console.log("Restore( #" + fileElement.id + " ) : " + clone[fileElement.id].val().split("\\").pop())
            clone[fileElement.id].insertBefore(fileElement); //'Restoring Clone'
            $(fileElement).remove(); //'Removing Original'
            addEventListenersTo(clone[fileElement.id]) //If Needed Re-attach additional Event Listeners
        }
        var rslt = validateFileSizeMaxOrEmpty($("#fileMaxSize").val(),'selectedFile');
        if (rslt == 'N') {
            $("#error_selectedFileError").html($("#fileMaxMBMessage").val());
        } else if (rslt == 'E') {
            $("#error_selectedFileError").html($("#fileMandatoryMessage").val());
        }else {
            if(  $('#selectedFile').val()!=''){
                $("[name='beGiroDeductionType']").val('uploadCsv');
                var mainPoolForm =$('#giroDeductionForm');
                mainPoolForm.submit();
            }
        }
    }

    function validateFileSizeMaxOrEmpty(maxSize,selectedFileId) {
        var fileId= '#'+selectedFileId;
        var fileV = $( fileId).val();
        var file = $(fileId).get(0).files[0];
        if(fileV == null || fileV == "" ||file==null|| file==undefined){
            return "E";
        }
        var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        //alert('fileSize:'+fileSize);
        //alert('maxSize:'+maxSize);
        fileSize = parseInt(fileSize);
        if(fileSize>= maxSize){
            $(fileId).after( $( fileId).clone().val(""));
            $(fileId).remove();
            return "N";
        }
        return "Y";
    }
    $('#uploadFile').click(function (){
        $('#selectedFile').trigger('click');
    });
    /*$('#download').click(function (){
        $("[name='beGiroDeductionType']").val('uploadCsv');
        var mainPoolForm = document.getElementById('giroDeductionForm');
        mainPoolForm.submit();
    });*/
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
        var checkedNum = $("[name='giroDueCheck']:checked").length;
        if (checkedNum > 0){
            $('#retrigger').removeClass('disabled');
        }else{
            $("#retrigger").addClass('disabled');
        }
        checkNum()
    }
    function giroDeductionCheckAll(){
        if ($('#allGiroDeductionCheck').is(':checked')) {
            $("input[name ='giroDueCheck']").prop("checked",true);
        } else {
            $("input[name ='giroDueCheck']").prop("checked",false);
        }
        checkNum()
    }
    
    function checkNum() {
        var checkedNum = $("[name='giroDueCheck']:checked").length;
        if (checkedNum > 0){
            $('#retrigger').removeClass('disabled');
        }else{
            $("#retrigger").addClass('disabled');
        }
        
    }
    function doGiroDeductionClear() {
        $('input[name="applicationNo"]').val("");
        $('input[name="txnRefNo"]').val("");
        $('input[name="hci_name"]').val("");
        $('input[name="bankAccountNo"]').val("");
        $('input[name="paymentRefNo"]').val("");
        $('input[name="paymentAmount"]').val("");
        $('textarea[name="paymentDescription"]').val("");
    }

    function giroDeductionSubmit(action){
        $("[name='beGiroDeductionType']").val(action);
        var mainPoolForm = $('#giroDeductionForm');
        mainPoolForm.submit();
    }

    function doGiroDeductionSearch() {
        showWaiting();
        giroDeductionSubmit('search');
    }

    function jumpToPagechangePage(){
        showWaiting();
        inspectionCommonPoolSubmit('page');
    }

    function sortRecords(sortFieldName,sortType){
        $("[name='crud_action_value']").val(sortFieldName);
        $("[name='crud_action_additional']").val(sortType);
        inspectionCommonPoolSubmit('sort');
    }
    function inspectionCommonPoolSubmit(action){
        $("[name='beGiroDeductionType']").val(action);
        var mainPoolForm =$('#giroDeductionForm');
        mainPoolForm.submit();
    }

    function doGiroDeductionRetriggerSaveAck() {
        $('#giroDeductionRetrigger').modal("hide");
    }
</script>


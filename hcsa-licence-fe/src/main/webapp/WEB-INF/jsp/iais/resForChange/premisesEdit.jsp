<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<style>
    @media only screen and (min-width: 630px) {
        #ackMessageConfim.modal .modal-dialog {
            max-width: 630px;
            width: 630px;
        }
    }
</style>

<form method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
  <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
  <input type="hidden" name="crud_action_type_form_value" value="">
  <input type="hidden" name="crud_action_type_value" value="">
  <input type="hidden" name="crud_action_additional" value="">
  <%@include file="dashboard.jsp" %>
  <%@include file="../common/dashboard.jsp" %>
  <%--Validation fields Start--%>
  <input type="hidden" name="paramController" id="paramController"
         value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
  <input type="hidden" name="licenceName" value=""/>
  <input type="hidden" name="crud_action_type_continue" value="">
  <%--Validation fields End--%>
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="">
            <div class="">
              <div class="tab-pane active" id="premisesTab" role="tabpanel">
                <div class="row premContent">
                  <div class="col-xs-12" style="margin-top:3%;">
                    <div>
                      <h2>Address:&nbsp;${PremisesListQueryDto.address}</h2>
                    </div>
                  </div>
                </div>
                <div class="form-check col-sm-12">
                  <ul>
                    <li>Changes made will be applied to other licences associated with this mode of service delivery:</li>
                  </ul>
                </div>
                <!--todo:slect prem issue -->
                <%@include file="../common/premisesContent.jsp"%>
                  <div class="row">
                    <div class="col-xs-12 col-md-6 ">
                      <c:choose>
                        <c:when test="${requestInformationConfig != null}">
                          <a class="back" id="back" href="/main-web/eservice/INTERNET/MohInternetInbox"><em class="fa fa-angle-left"></em> Back</a>
                        </c:when>
                        <c:otherwise>
                          <a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </c:otherwise>
                      </c:choose>
                    </div>
                    <div class="col-xs-12 col-md-6 text-right">
                      <div class="button-group">
                        <a class="btn btn-primary next" id="previewAndSub" href="javascript:void(0);">Preview</a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    <input type="hidden" value="${rfcPendingApplication}" id="rfcPendingApplication" name="rfcPendingApplication">
    <div class="modal fade" id="ackMessageConfim" role="dialog" aria-labelledby="myModalLabel">
      <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
          <div class="modal-body" >
            <div class="modal-body">
              <div class="col-md-12"><span style="font-size: 2rem;">The changes you have made affect licences with pending application</span></div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="cancel()">Continue amending</button>
              <button type="button" class="next btn btn-primary" data-dismiss="modal" onclick="exitAndSave()">Exit and save as draft</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <input type="text" style="display:none;" value="${hciNameUsed}" name="hciNameUsedInput" id="hciNameUsedInput">
    <iais:confirm msg="${newAppPopUpMsg}" needCancel="false" callBack="Continue()" popupOrder="hciNameUsed" yesBtnDesc="Continue"
                  needEscapHtml="false" />
    <input type="text" style="display:none;" name="continueStep" id="continueStep" value="${continueStep}">
    <input type="text" style="display: none" name="crudActionTypeContinue" id="crudActionTypeContinue" value="${crudActionTypeContinue}">
    <%--Validation Field--%>
      <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
      <%@include file="../common/premFun.jsp"%>
    </div>
  </div>
  <%--<div class="modal fade" id="rfc_ERROR" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        </div>
        <div class="modal-body" style="text-align: center;">
          <div class="row">
            <div class="col-md-8 col-md-offset-2"><span style="font-size: 2rem;">${RFC_ERROR_NO_CHANGE}</span></div>
          </div>
        </div>
      </div>
    </div>
  </div>--%>
  <iais:confirm msg="${showOtherError}" callBack="$('#showOtherError').modal('hide');" popupOrder="showOtherError"
                needCancel="false" needEscapHtml="false"/>
  <input type="hidden" value="${not empty showOtherError ? '1' : ''}" id="showOtherErrorCheck">
  <iais:confirm msg="${RFC_ERROR_NO_CHANGE}" callBack="cancel()"  needCancel="false" popupOrder="rfc_ERROR"></iais:confirm>
  <iais:confirm msg="${SERVICE_CONFIG_CHANGE}" callBack="cancel()"  needCancel="false" popupOrder="SERVICE_CONFIG_CHANGE"></iais:confirm>
  <iais:confirm msg="${postalCodeAckMsg}" needCancel="false" callBack="postalCodeCon()" popupOrder="postalCodePop" yesBtnDesc="" needEscapHtml="false" needFungDuoJi="false"></iais:confirm>
  <input type="hidden" value="${RFC_ERROR_NO_CHANGE}" id="RFC_ERROR_NO_CHANGE">
  <input type="hidden" id="SERVICE_CONFIG_HAVE_CHANGE" value="${SERVICE_CONFIG_CHANGE}">
  <input type="text" style="display: none" name="errorMapIs" id="errorMapIs" value="${errormapIs}">
  <input type="hidden" id="eqHciNameChange" name="eqHciNameChange" value="${eqHciCode}">
</form>


<script>
    var init;
    $(document).ready(function () {
        <!-- init start-->
        $('#postalCodePop').modal('hide');
        if($('#RFC_ERROR_NO_CHANGE').val()!=''){
            $('#rfc_ERROR').modal('show');
        }
        if($('#rfcPendingApplication').val()=='errorRfcPendingApplication'){
            $('#ackMessageConfim').modal('show');
        }
        if($('#SERVICE_CONFIG_HAVE_CHANGE').val()!=''){
            $('#SERVICE_CONFIG_CHANGE').modal('show');
        }
        if($('#hciNameUsedInput').val()=='hciNameUsed'){
            $('#hciNameUsed').modal('show');
        }
        if($('#showOtherErrorCheck').val()!=''){
            $('#showOtherError').modal('show');
        }
        init = 0;
        <!--for reload -->
        reloadPage();
        cl();
        preperChange();
        retrieveAddr();
        coLocation();
        doEdit();
        addOperational();
        operationDel();
        addPubHolDayHtml();
        addWeeklyHtml();
        addEventHtml();
        removeWeekly();
        removePh();
        removeEvent();
        clickAllDay();
        useType();
        $("input[name='easMtsUseOnly0']").trigger('change');
        $("select[name='onSiteAddressType']").trigger('change');
        $("select[name='conveyanceAddrType']").trigger('change');
        $("select[name='offSiteAddrType']").trigger('change');
        $("select[name='easMtsAddrType']").trigger('change');
        premSelect();
        <c:if test="${PageCanEdit}">
          var $PremEle = $('#mainPrem');
          unreadonlyPartPage($PremEle);
          $PremEle.find('div.other-lic-content .check-circle').removeClass('radio-disabled');
        </c:if>
        <!-- init end-->
        //68744
        $('.premSelect').addClass('disabled');


        var mainContent =$('.main-content');
        mainContent.find('input.allDay:checked').each(function (k) {
            console.log(k);
            var $allDayDiv = $(this).closest('div.all-day-div');
            disabeleForAllDay($allDayDiv);
        });
        init = 1;
        /*if($('#eqHciNameChange').val()=='false'){
            $("input[name='isPartEdit']").val('1');
            $("input[name='chooseExistData']").val('1');
            $('.premSelect').removeClass('disabled');
            $('.premisesEdit').addClass('hidden');
        }else  if($('#eqHciNameChange').val()=='true'){
            $('.premisesEdit').removeClass('hidden');
            $("input[name='isPartEdit']").val('0');
            $("input[name='chooseExistData']").val('0');
        }*/
        if($("#errorMapIs").val()=='error' ){
            $('.premisesEdit').trigger('click');
        }
    });

    function cancel(){
        $('#rfcPendingApplication').modal('hide');
        $('#rfc_ERROR').modal('hide');
        $('#SERVICE_CONFIG_CHANGE').modal('hide');
    }
    $('#previewAndSub').click(function () {
        showWaiting();
        doSubmitForm('prePayment','', '');
    });

    $('#back').click(function () {
        showWaiting();
        doBack('prePremisesList');
    });
    function exitAndSave() {
        showWaiting();
        doSubmitForm('prePayment','', 'exitSaveDraft');
    }
    function readonlyPartPage($Ele) {
        $Ele.find('input[type="radio"]').prop('readonly',true);
        $Ele.find('input[type="text"]').prop('readonly',true);
        $Ele.find('input[type="file"]').prop('readonly',true);
        $Ele.find('input[type="checkbox"]').prop('readonly',true);
        $Ele.find('div.nice-select').addClass('disabled');
        <!--add disabled bg color-->
        $Ele.find('input[type="text"]').css('border-color','#ededed');
        $Ele.find('input[type="text"]').css('color','#999');
        //$Ele.find('.date_picker').unbind();
        <!--multi -->
        $Ele.find('div.multi-select input').prop('disabled',true);
    }

    function unreadonlyPartPage($Ele) {
        $Ele.find('input[type="radio"]').prop('readonly',false);
        $Ele.find('input[type="text"]').prop('readonly',false);
        $Ele.find('input[type="file"]').prop('readonly',false);
        $Ele.find('input[type="checkbox"]').prop('readonly',false);
        $Ele.find('div.nice-select').removeClass('disabled');
        <!--remove disabled bg color-->
        $Ele.find('input[type="text"]').css('border-color','');
        $Ele.find('input[type="text"]').css('color','');
        $Ele.find('.date_picker').datepicker({
            format:"dd/mm/yyyy",
            autoclose:true,
            todayHighlight:true,
            orientation:'bottom'
        });
        <!--multi -->
        $Ele.find('div.multi-select input').prop('disabled',false);
    }

    var unbindAllTabs = function () {
        $('#premises').unbind();
        $('#premisesli').unbind();
        $('#documents').unbind();
        $('#documentsli').unbind();
        $('#serviceForms').unbind();
        $('#serviceFormsli').unbind();
        $('#preview').unbind();
        $('#previewli').unbind();
        $('#payment').unbind();
        $('#paymentli').unbind();
    }

    function Continue() {
        $('#hciNameUsed').modal('hide');
        $("[name='crud_action_type_continue']").val("continue");
        doSubmitForm($('#continueStep').val(),'',$('#crudActionTypeContinue').val());
    }

    function unDisabledPartPage($Ele){
        $Ele.find('input[type="radio"]').prop('disabled',false);
        $Ele.find('input[type="text"]').prop('disabled',false);
        $Ele.find('input[type="file"]').prop('disabled',false);
        $Ele.find('input[type="checkbox"]').prop('disabled',false);
        $Ele.find('div.nice-select').removeClass('disabled');
        $Ele.find('input[type="text"]').css('border-color','');
        $Ele.find('input[type="text"]').css('color','');
        <!--multi -->
        $Ele.find('div.multi-select input').prop('disabled',false);
    }

    function postalCodeCon(){
        $('#postalCodePop').modal('hide');
    }
</script>
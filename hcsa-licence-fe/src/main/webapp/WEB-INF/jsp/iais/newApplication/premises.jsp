<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
  //handle to the Engine APIs
  sop.webflow.rt.api.BaseProcessClass process =
          (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<style>
  .app-font-size-22{
    font-size: 22px;
  }
  .ack-font-16{
    font-size: 16px;
  }
</style>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
  <%--Validation fields Start--%>
  <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator"/>
  <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto"/>
  <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
  <input type="hidden" name="crud_action_type_continue" value="">
  <%--Validation fields End--%>
  <div class="main-content">
    <div class="container">
      <div class="row">
        <div class="col-xs-12">
          <div class="tab-gp steps-tab">
            <%@ include file="./navTabs.jsp" %>
            <div class="tab-content  ">
              <div class="tab-pane active" id="premisesTab" role="tabpanel">
                  <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <div class="premises-content" >
                  <div class="row ">
                    <div class="col-xs-12">
                      <div class="premises-txt">
                        <p><iais:message escape="false" key="NEW_ACK018"/></p>
                      </div>
                    </div>
                  </div>
                  <c:if test="${AppSubmissionDto.appType=='APTY005'||AppSubmissionDto.appType=='APTY004'}">
                    <c:if test="${fn:length(appGrpPremisesDto.licenceDtos)>0}">
                      <div class="form-check col-sm-12">
                        <ul>
                          <li>Changes made will be applied to other licences associated with this mode of service delivery:</li>
                        </ul>
                      </div>
                    </c:if>
                  </c:if>
                  <c:set var="readOnly" value="false"/>
                  <c:forEach var="appSvcDto" items="${AppSubmissionDto.appSvcRelatedInfoDtoList}">
                    <c:if test="${!empty appSvcDto.relLicenceNo || !empty appSvcDto.alignLicenceNo }">
                      <c:set var="readOnly" value="true"/>
                    </c:if>
                  </c:forEach>
                  <!--prem content -->
                  <%@include file="../common/premisesContent.jsp"%>
                </div>
                <div class="row">
                  <div class="col-xs-12" id="addPremBody" hidden>
                    <%--<c:if test="${requestInformationConfig == null && 'APTY005' != AppSubmissionDto.appType && !multiBase && 'APTY004' != AppSubmissionDto.appType && !AppSubmissionDto.onlySpecifiedSvc}">--%>
                    <c:if test="${requestInformationConfig == null && 'APTY005' != AppSubmissionDto.appType && !multiBase && 'APTY004' != AppSubmissionDto.appType && !readOnly && isMultiPremService}">
                      <button id="addPremBtn" class="btn btn-primary" type="button">Add Mode of Service Delivery</button>
                    </c:if>
                  </div>
                </div>
                <div class="application-tab-footer">
                    <c:choose>
                        <c:when test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                            <%@include file="../common/rfcFooter.jsp"%>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <div class="col-xs-12 col-md-4">
                                  <a class="back" id="Back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                </div>
                                <div class="col-xs-12 col-sm-8">
                                    <div class="button-group">
                                        <c:if test="${requestInformationConfig==null}">
                                          <input type="text" style="display: none; " id="selectDraftNo" value="${selectDraftNo}">
                                          <input type="text" style="display: none; " id="saveDraftSuccess" value="${saveDraftSuccess}">
                                          <a class="btn btn-secondary premiseSaveDraft" id="SaveDraft"    >Save as Draft</a>
                                        </c:if>
                                        <a class="btn btn-primary next premiseId" id="Next" >Next</a></div>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <%--Validation Field--%>
  <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
  <%@include file="../common/premFun.jsp"%>
  <input type="hidden" name="pageCon" value="valPremiseList" >
  <%--<c:if test="${ not empty selectDraftNo }">
    <iais:confirm msg="There is an existing draft for the chosen service, if you choose to continue, the draft application will be discarded." callBack="cancelSaveDraft()" popupOrder="saveDraft"  yesBtnDesc="Resume from draft" cancelBtnDesc="Continue" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="saveDraft()"></iais:confirm>
  </c:if>--%>
  <c:if test="${!('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
    <iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft" yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()"></iais:confirm>
  </c:if>

  <input type="text" style="display: none" value="${AckMessage}" id="ackMessage" name="ackMessage">
  <iais:confirm msg="There is a pending application for a licence associated to this mode of service delivery" callBack="" popupOrder="ackMessageConfim"></iais:confirm>
  <input type="text" style="display:none;" value="${hciNameUsed}" name="hciNameUsedInput" id="hciNameUsedInput">
  <%--<div class="modal fade" id="hciNameUsed" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <div class="modal-title" style="font-size: 2rem;">Confirmation Box</div>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        </div>
        <div class="modal-body" style="text-align: center;">
          <div class="row">
            <div class=""><span style="font-size: 16px;">${newAppPopUpMsg}</span></div>
          </div>
        </div>
        <div class="row " style="margin-top: 5%;margin-bottom: 5%">
          <button type="button" style="margin-left: 60%" class="next btn btn-primary col-md-4" data-dismiss="modal" onclick="Continue()">Continue</button>
        </div>
      </div>
    </div>
  </div>--%>
  <iais:confirm msg="${newAppPopUpMsg}" needCancel="false" callBack="Continue()" popupOrder="hciNameUsed" yesBtnDesc="Continue" needEscapHtml="false"></iais:confirm>
  <iais:confirm msg="${postalCodeAckMsg}" needCancel="false" callBack="postalCodeCon()" popupOrder="postalCodePop" yesBtnDesc="" needEscapHtml="false" needFungDuoJi="false"></iais:confirm>
  <input type="text" style="display:none;" name="continueStep" id="continueStep" value="${continueStep}">
  <input type="text" style="display: none" name="crudActionTypeContinue" id="crudActionTypeContinue" value="${crudActionTypeContinue}">
  <input type="text" style="display: none" name="errorMapIs" id="errorMapIs" value="${errormapIs}">
  <%--<input type="hidden" id="rfc_eqHciNameChange" value="${rfc_eqHciCode}">--%>
</form>
<script type="text/javascript">
    var init;
    $(document).ready(function() {
        $('#postalCodePop').modal('hide');
        cl();
        $("input[name='easMtsUseOnly0']").trigger('change');
        preperChange();
        $("select[name='onSiteAddressType']").trigger('change');
        $("select[name='conveyanceAddrType']").trigger('change');
        $("select[name='offSiteAddrType']").trigger('change');
        $("select[name='easMtsAddrType']").trigger('change');
        <!-- init start-->
        init = 0;
        if($('#ackMessage').val()=='ACKMESSAGE'){
            $('#ackMessageConfim').modal('show');
        }
        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }
        if($('#hciNameUsedInput').val()=='hciNameUsed'){
            $('#hciNameUsed').modal('show');
        }
        var checkedType = "";

        $('.prem-summary').addClass('hidden');

        $('.table-condensed').css("background-color","#d9edf7");

        reloadPage();

        premType();

        premSelect();

        retrieveAddr();

        removePremises();

        doEdit();

        coLocation();

        addPubHolDayHtml();

        //removePH();

        addOperational();

        operationDel();

        addWeeklyHtml();

        addEventHtml();

        removeWeekly();

        removePh();

        removeEvent();

        clickAllDay();

        useType();

        //Binding method
        $('#Back').click(function(){
            showWaiting();
            submit('licensee', 'back',null);
        });
        $('#Next').click(function(){
            showWaiting();
            $('input[type="radio"]').prop('disabled',false);
            submit('documents',null,null);
        });
        $('#SaveDraft').click(function(){
            showWaiting();
            $('input[type="radio"]').prop('disabled',false);
            submit('premises','saveDraft',$('#selectDraftNo').val());
        });
        <c:if test="${(!AppSubmissionDto.needEditController && readOnly) || AppSubmissionDto.needEditController}">
        readonlyPartPage($('div.premises-content'));
        $('div.premises-content').each(function () {
            handlePage($(this));
        });
        </c:if>
        <c:if test="${AppSubmissionDto.appType == 'APTY002' && requestInformationConfig == null}">
        $('div.premContent').each(function () {
            doEditPremise($(this));
        });
        </c:if>

        var mainContent =$('.main-content');
        mainContent.find('input.allDay:checked').each(function (k) {
            console.log(k);
            var $allDayDiv = $(this).closest('div.all-day-div');
            disabeleForAllDay($allDayDiv);
        });

        init = 1;
        //68859
        /*if ($('#rfc_eqHciNameChange').val()=='false') {
            $("input[name='isPartEdit']").val('1');
            $("input[name='chooseExistData']").val('1');
            $('.premSelect').removeClass('disabled');
            $('.premisesEdit').addClass('hidden');
        } else if ($('#rfc_eqHciNameChange').val()=='true') {
            $("input[name='isPartEdit']").val('0');
            $("input[name='chooseExistData']").val('0');
            $('.premisesEdit').removeClass('hidden');
        }*/
        <c:if test="${'APTY002' !=AppSubmissionDto.appType || requestInformationConfig != null}">
        if ($("#errorMapIs").val()=='error') {
            $('div.premContent').each(function () {
                var $premisesEdit = $(this).find('.premisesEdit');
                if ($premisesEdit.length > 0) {
                    $premisesEdit.trigger('click');
                    $premisesEdit.addClass('hidden');
                }
            });
        }
        </c:if>
    });


    $("#onSiteSel").change(function(){
        $("#addPremBody").removeAttr("hidden");
    })
    var oval = $("#onSiteSel").val();
    var cval = $("#conveyanceSel").val();
    var offval =  $('#offSiteSel').val();
    console.log("oval"+cval);

    if (oval != -1 || cval != -1 || offval != -1) {
        $("#addPremBody").removeAttr("hidden");
    }
    $("#conveyanceSel").change(function(){
        $("#addPremBody").removeAttr("hidden");
    })

    $('#offSiteSel').change(function () {
        $("#addPremBody").removeAttr("hidden");
    });


    function saveDraft() {
        $('input[type="radio"]').prop('disabled',false);
        submit('premises','saveDraft',$('#selectDraftNo').val());
    }

  function cancelSaveDraft() {
      submit('premises','saveDraft','cancelSaveDraft');
  }

  function cancel() {
      $('#saveDraft').modal('hide');
  }

  function jumpPage() {
      submit('premises','saveDraft','jumpPage');
  }

  function Continue() {
      $('#hciNameUsed').modal('hide');
      $("[name='crud_action_type_continue']").val("continue");
      submit($('#continueStep').val(),null,$('#crudActionTypeContinue').val());
  }

  function postalCodeCon(){
      $('#postalCodePop').modal('hide');
    }

  var handlePage = function($Ele){
      $Ele.find('div.other-lic-content .other-lic:checked').closest('div').find('span.check-circle').addClass('radio-disabled');;
      $Ele.find('input[name="onSiteFireSafetyCertIssuedDate"]').addClass('disabled-placeHolder');
      $Ele.find('.addOperational').addClass('hidden');
      $Ele.find('.opDel').addClass('hidden');
      $Ele.find('button.addPubHolDay').addClass('hidden');
      $Ele.find('.removePhBtn').addClass('hidden');
      $Ele.find('.weeklyDel').addClass('hidden');
      $Ele.find('.pubHolidayDel').addClass('hidden');
      $Ele.find('.eventDel').addClass('hidden');
      $Ele.find('.addWeeklyDiv').addClass('hidden');
      $Ele.find('.addPhDiv').addClass('hidden');
      $Ele.find('.addEventDiv').addClass('hidden');
      $Ele.find('input.allDay').attr('disabled',true);
      //for rfi
      $Ele.find('.date_picker').attr('disabled',true);
      $Ele.find('.date_picker').addClass('disabled-placeHolder');
      $Ele.find('a.retrieveAddr').addClass('hidden');
      $Ele.find('span.multi-select-button').css('border-color','#ededed');
      $Ele.find('span.multi-select-button').css('color','#999');
      $Ele.find('.multi-select-container input[type="checkbox"]').prop('disabled',true);
      $Ele.find('input.useType:checked').closest('div').find('label span.check-circle').addClass('radio-disabled');
  }
</script>




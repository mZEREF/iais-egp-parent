<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>

<webui:setLayout name="iais-intranet"/>

<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%--Validation fields Start--%>
        <input type="hidden" name="crud_action_type_continue" value="">
        <%--Validation fields End--%>
        <div class="main-content">
            <div class="row">
                <div class="center-content">
                    <div class="col-xs-12 intranet-content">
                        <div class="tab-gp steps-tab tab-be">
                            <%@ include file="/WEB-INF/jsp/iais/application/common/navTabs.jsp" %>
                            <div class="tab-content  ">
                                <div class="tab-pane active" id="premisesTab" role="tabpanel">
                                    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                                    <div class="premises-content">
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
                                                        <li>
                                                            Changes made will be applied to other licences associated with this mode of
                                                            service delivery:
                                                        </li>
                                                    </ul>
                                                </div>
                                            </c:if>
                                        </c:if>
                                        <!--prem content -->
                                        <%@include file="section/premisesContent.jsp" %>
                                    </div>
                                    <%@ include file="/WEB-INF/jsp/iais/application/common/appFooter.jsp" %>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%--Validation Field--%>
        <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
        <%@include file="common/premFun.jsp" %>
        <input type="hidden" name="pageCon" value="valPremiseList">
        <c:if test="${!(isRfc || isNew)}">
            <iais:confirm msg="This application has been saved successfully" callBack="cancel()" popupOrder="saveDraft"
                          yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary"
                          yesBtnCls="btn btn-secondary" cancelFunc="jumpPage()" />
        </c:if>
        <input type="hidden" value="${AckMessage}" id="ackMessage" name="ackMessage" />
        <iais:confirm msg="There is a pending application for a licence associated to this mode of service delivery" callBack=""
                      popupOrder="ackMessageConfim"></iais:confirm>
        <input type="hidden" value="${hciNameUsed}" name="hciNameUsedInput" id="hciNameUsedInput">
        <iais:confirm msg="${newAppPopUpMsg}" needCancel="false" callBack="Continue()" popupOrder="hciNameUsed" yesBtnDesc="Continue"
                      needEscapHtml="false"></iais:confirm>
        <iais:confirm msg="${postalCodeAckMsg}" needCancel="false" callBack="postalCodeCon()" popupOrder="postalCodePop" yesBtnDesc=""
                      needEscapHtml="false" needFungDuoJi="false"></iais:confirm>
        <input type="hidden" name="continueStep" id="continueStep" value="${continueStep}">
        <input type="hidden" name="crudActionTypeContinue" id="crudActionTypeContinue" value="${crudActionTypeContinue}">
        <input type="hidden" name="errorMapIs" id="errorMapIs" value="${errormapIs}">
    </form>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        reSetPremTypeTooltip();
        window.addEventListener('resize', reSetPremTypeTooltip);
        if ($('#ackMessage').val() == 'ACKMESSAGE') {
            $('#ackMessageConfim').modal('show');
        }
        if ($('#hciNameUsedInput').val() == 'hciNameUsed') {
            $('#hciNameUsed').modal('show');
        }
        //Binding method
        $('#Back').click(function () {
            showWaiting();
            submit('licensee', 'back', null);
        });
        $('#Next').click(function () {
            showWaiting();
            $('input[type="radio"]').prop('disabled', false);
            submit('specialised', null, null);
        });
        $('#SaveDraft').click(function () {
            showWaiting();
            $('input[type="radio"]').prop('disabled', false);
            submit('premises', 'saveDraft', $('#selectDraftNo').val());
        });
        // init page
        initPremiseEvent();
        premTypeChangeEvent();
        checkSelectedLicence();
        $('div.premContent').each(function (k, v) {
            checkPremiseContent($(v), k);
        });
        if ($('div.premContent').length == 1) {
            $('div.premContent').find('.premHeader').html('');
        }
        <c:if test="${AppSubmissionDto.needEditController}">
        $('div.premContent').each(function () {
            disablePremiseContent($(this));
        });
        </c:if>
        <c:if test="${readonly}">
        $('div.premContent').each(function () {
            let $premContent = $(this);
            disableContent($premContent.find('.premisesTypeDiv'));
            disableContent($premContent.find('.permanentSel'));
            checkPremDisabled($premContent, true);
        });
        </c:if>
        <c:if test="${not empty errormapIs}">
        $('div.premContent').each(function () {
            doEditPremise($(this));
        });
        </c:if>
    });

    // check premises type for clicking teh navigate tab eveent
    var premTypeChangeEvent = function () {
        $('.premContent .premTypeValue').on('change', function () {
            let val = $(this).val();
            let $premContent = $(this).closest('.premContent');
            let oldVal = $premContent.find('.oldPremTypeValue').val();
            if (oldVal != val) {
                navTabEvent('specialised');
            }
        });
    }

    function Continue() {
        $('#hciNameUsed').modal('hide');
        $("[name='crud_action_type_continue']").val("continue");
        submit($('#continueStep').val(), null, $('#crudActionTypeContinue').val());
    }

    /*var init;
    $(document).ready(function() {
      reSetPremTypeTooltip();
      window.addEventListener('resize', reSetPremTypeTooltip);
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
            submit('specialised',null,null);
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
        <c:if test="${AppSubmissionDto.appType == 'APTY002' && requestInformationConfig == null && !readOnly}">
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
        <c:if test="${'APTY002' !=AppSubmissionDto.appType || requestInformationConfig != null}">
        if ($("#errorMapIs").val()=='error') {
            $('div.premContent').each(function () {
                doEditPremise($(this), '1');
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
  }*/
</script>




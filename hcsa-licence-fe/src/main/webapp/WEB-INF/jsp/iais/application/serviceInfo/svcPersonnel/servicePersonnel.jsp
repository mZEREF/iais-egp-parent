<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>

<c:set var="isRfi" value="${requestInformationConfig != null}"/>

<input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>

<%--<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">--%>
<%--  <div id="wizard-page-title"></div>--%>
<%--  <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">--%>

<%--    <div id="control--runtime--0" class="page control control-area  container-p-1">--%>


<%--      <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>--%>
<%--      <table aria-describedby="" class="control-grid columns1" style="width:100%;">--%>
<%--&lt;%&ndash;        <thead style="display: none">&ndash;%&gt;--%>
<%--&lt;%&ndash;        <tr><th scope="col"></th></tr>&ndash;%&gt;--%>
<%--&lt;%&ndash;        </thead>&ndash;%&gt;--%>
<%--        <tbody>--%>
<%--&lt;%&ndash;        <tr height="1">&ndash;%&gt;--%>
<%--&lt;%&ndash;          <td class="first last" style="width: 100%;">&ndash;%&gt;--%>
<%--&lt;%&ndash;            <div id="control--runtime--85" class="control control-caption-horizontal">&ndash;%&gt;--%>
<%--&lt;%&ndash;            </div>&ndash;%&gt;--%>
<%--&lt;%&ndash;          </td>&ndash;%&gt;--%>
<%--&lt;%&ndash;        </tr>&ndash;%&gt;--%>
<%--&lt;%&ndash;        <tr>&ndash;%&gt;--%>
<%--&lt;%&ndash;        </tr>&ndash;%&gt;--%>


<%--        <tr height="1">--%>
<%--          <td class="" style="width: 100%;">--%>
            <div id="control--runtime--1" class="section control  container-s-1">
              <div class="control-set-font control-font-header control-font-header section-header">
                <c:choose>
                  <c:when test="${'BLB' ==currentSvcCode}">
                    <h4>The blood donation centre and/or mobile donation drive is/are under the supervision of</h4>
                  </c:when>
                  <c:when test="${'TSB' ==currentSvcCode}">
                    <strong style="font-size: 20px;">Laboratory Director (Cord Blood Banking Service)</strong>
                    <h4><iais:message key="NEW_ACK023"/></h4>
                  </c:when>
                  <c:when test="${'NMI' ==currentSvcCode}">
                    <h4>Please appoint at least one person for each role listed under "Service Personnel".</h4>
                  </c:when>
                  <c:when test="${'NMA' ==currentSvcCode}">
                    <h4>The Nuclear Medicine Assay Service have the following personnel that satisfy the minimum requirements at all times</h4>
                  </c:when>
                </c:choose>
              </div>
              <p>
                <span class="error-msg" name="iaisErrorMsg" id="error_psnMandatory"></span>
              </p>
              <c:set var="editControl" value="${(!empty AppSvcPersonnelDtoList && AppSubmissionDto.needEditController) || !AppSubmissionDto.needEditController}" />
              <div class="personnel-edit">
                <c:if test="${AppSubmissionDto.needEditController }">
                  <c:set var="isClickEdit" value="false"/>
                  <c:if test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && !isRfi}">
                    <div class="text-right app-font-size-16">
                      <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span>&nbsp;</span><em class="fa fa-angle-right"></em>
                      </a>
                    </div>
                  </c:if>
                  <c:if test="${'true' != isClickEdit}">
                    <c:set var="locking" value="true"/>
                    <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
                  </c:if>
                </c:if>
              </div>
              <input type="hidden" name="prsFlag" value="${prsFlag}"/>
              <c:if test="${ServicePersonnelMandatory>0 && editControl}">
                <c:set var="spMandatoryCount" value="${spHcsaSvcPersonnelDto.mandatoryCount}"/>
                <c:forEach begin="0" end="${ServicePersonnelMandatory-1}" step="1" varStatus="status">
                  <input class="not-refresh not-clear premIndex" type="hidden" name="premIndex" value="${status.index}"/>
                  <c:if test="${AppSvcPersonnelDtoList != null && AppSvcPersonnelDtoList.size()>0}">
                    <c:set value="${AppSvcPersonnelDtoList[status.index]}" var="appSvcPersonnelDto"/>
                  </c:if>
                  <%@include file="servicePersonnelDetail.jsp" %>
                </c:forEach>
              </c:if>
              <div class="personnel-content-point"></div>
              <c:if test="${requestInformationConfig==null}">
                <c:choose>
                  <c:when test="${!empty AppSvcPersonnelDtoList}">
                    <c:set var="spDtoLength" value="${AppSvcPersonnelDtoList.size()}"/>
                  </c:when>
                  <c:otherwise>
                    <c:choose>
                      <c:when test="${AppSubmissionDto.needEditController}">
                        <c:set var="spDtoLength" value="0"/>
                      </c:when>
                      <c:otherwise>
                        <c:set var="spDtoLength" value="${spHcsaSvcPersonnelDto.mandatoryCount}"/>
                      </c:otherwise>
                    </c:choose>
                  </c:otherwise>
                </c:choose>

                <c:set var="needAddPsn" value="true"/>
                <c:choose>
                  <c:when test="${spHcsaSvcPersonnelDto.status =='CMSTAT003'}">
                    <c:set var="needAddPsn" value="false"/>
                  </c:when>
                  <c:when test="${spDtoLength >= spHcsaSvcPersonnelDto.maximumCount}">
                    <c:set var="needAddPsn" value="false"/>
                  </c:when>
                  <c:when test="${AppSubmissionDto.needEditController && !canEdit}">
                    <c:set var="needAddPsn" value="false"/>
                  </c:when>
                </c:choose>

                <div id="addPsnDiv" class="row <c:if test="${!needAddPsn}">hidden</c:if>">
                  <div class="col-sm-5">
                    <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another Service Personnel</span>
                  </div>
                  <div  class="col-sm-5 col-md-5">
                    <span class="spErrorMsg" style="color: red;"></span>
                  </div>
                </div>

              </c:if>
            </div>
<%--          </td>--%>
<%--        </tr>--%>
<%--        </tbody>--%>
<%--      </table>--%>
<%--    </div>--%>

<%--  </div>--%>

<%--</div>--%>
<%@include file="servicePersonnelFun.jsp"%>



<script type="text/javascript">
  var init = 0;
  $(document).ready(function() {
    //Binding method
    $('#governanceOfficersBack').click(function(){
      submitForms('laboratoryDisciplines',null,null,'clinical');
    });
    $('#governanceOfficersSaveDraft').click(function(){
      submitForms('governanceOfficers','saveDraft',null,'clinical');
    });
    $('#governanceOfficersNext').click(function(){
      /*   doValidation();
      if (getErrorMsg()) {
      dismissWaiting();
      } else {*/
      var controlFormLi = $('#controlFormLi').val();
      /*submitForms('disciplineAllocation',null,null,controlFormLi);*/
      submitForms('nuclearMedicineImaging',null,null,controlFormLi);
      /*  }*/
    });

    // aaa();
    if($('#PRS_SERVICE_DOWN_INPUT').val()=='PRS_SERVICE_DOWN'){
      $('#PRS_SERVICE_DOWN').modal('show');
    }
    pageController('');

    <!--for reload when have personnelSel-->
    <c:if test="${'NMI' ==currentSvcCode || 'NMA'== currentSvcCode}">
    $('.personnel-content').each(function (k,v) {
      var personnelSel = $(this).find('.personnelSel').val();
      var $personnelContentEle = $(this);
      personnelSelFun(personnelSel,$personnelContentEle);
    });
    </c:if>

    if(${AppSubmissionDto.needEditController && !isClickEdit}){
      disabledPage();
      <%--if(${editControl}){--%>
      <%--  $('.addListBtn').addClass('hidden');--%>
      <%--}--%>
      <%--$('.text-danger').addClass('hidden');--%>
    }

    doEdit();
    spRemove();
    designationChange();

    $('input[name="prsLoading"]').each(function () {
      if($(this).val() == 'true'){
        var $currContent = $(this).closest('.personnel-content');
        inputReadonly($currContent.find('input[name="name"]'));
      }
    });

    if("${errormapIs}"=='error'){
      $('.svcPsnEdit').trigger('click');
    }

    /* notLoadingSpl();*/
    init = 1;
    let btn = document.getElementsByClassName('removeSpBtn')[0];
    $(btn).hide()
  });

  var absencePsnSel = function (val,$Ele) {
    $('.svcPsnSel').addClass('hidden');
    var prsFlag = $('input[name="prsFlag"]').val();
    let $eles = $('.personnel-content');
    if($Ele != ''){
      $eles = $($Ele)
    }
    $eles.each(function (k,v) {
      if('Blood Banking' == val){
        $(this).find('.personnel-sel').addClass('hidden');
        $(this).find('.new-svc-personnel-form').removeClass('hidden');
        $(this).find('.personnel-designation').removeClass('hidden');
        $(this).find('.personnel-name').removeClass('hidden');
        $(this).find('.personnel-qualification').addClass('hidden');
        $(this).find('.personnel-regnNo ').removeClass('hidden');
        $(this).find('.personnel-wrkExpYear').removeClass('hidden');
        if("Y" == prsFlag){
          inputReadonly($(this).find('input[name="name"]'));
        }
      }else if('Tissue Banking p1' == val){
        $(this).find('.personnel-sel').addClass('hidden');
        $(this).find('.new-svc-personnel-form').removeClass('hidden');
        $(this).find('.personnel-designation').addClass('hidden');
        $(this).find('.personnel-name').removeClass('hidden');
        $(this).find('.personnel-qualification').removeClass('hidden');
        $(this).find('.personnel-regnNo ').addClass('hidden');
        $(this).find('.personnel-wrkExpYear').removeClass('hidden');
        if("Y" == prsFlag){
          inputCancelReadonly($(this).find('input[name="name"]'));
        }
      }else {
        $(this).find('.personnel-sel').addClass('hidden');
        $(this).find('.new-svc-personnel-form').removeClass('hidden');
        $(this).find('.personnel-designation').addClass('hidden');
        $(this).find('.personnel-name').removeClass('hidden');
        $(this).find('.personnel-qualification').removeClass('hidden');
        $(this).find('.personnel-regnNo ').addClass('hidden');
        $(this).find('.personnel-wrkExpYear').removeClass('hidden');
        if("Y" == prsFlag){
          inputCancelReadonly($(this).find('input[name="name"]'));
        }
      }
    });
  };

  var personnelSel = function(){
    $('.personnelSel').change(function () {
      var personnelSel = $(this).val();
      var $personnelContentEle = $(this).closest('.personnel-content');
      var prsFlag = $('input[name="prsFlag"]').val();
      if(init != 0){
        //clear data;
        $personnelContentEle.find('.personnel-designation select[name="designation"]').val('');
        var designation = $personnelContentEle.find('.personnel-designation  option[value=""]').html();
        $personnelContentEle.find('select[name="designation"]').next().find('.current').html(designation);

        $personnelContentEle.find('.personnel-name input[name="name"]').val('');
        $personnelContentEle.find('.personnel-regnNo input[name="regnNo"]').val('');
        $personnelContentEle.find('.personnel-wrkExpYear input[name="wrkExpYear"]').val('');
        $personnelContentEle.find('.personnel-qualification input[name="qualification"]').val('');
        if('Y' == prsFlag){
          inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
        }
      }
      personnelSelFun(personnelSel,$personnelContentEle);
    });
  };

  var personnelSelFun = function(personnelSel,$personnelContentEle){
    $personnelContentEle.find('.new-svc-personnel-form ').removeClass('hidden');
    var prsFlag = $('input[name="prsFlag"]').val();
    if('SPPT001' == personnelSel){
      $personnelContentEle.find('.personnel-designation').removeClass('hidden');
      $personnelContentEle.find('.personnel-name').removeClass('hidden');
      $personnelContentEle.find('.personnel-qualification').removeClass('hidden');
      $personnelContentEle.find('.personnel-wrkExpYear').removeClass('hidden');
      $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
      if('Y' == prsFlag){
        inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
      }
    }else if('SPPT002' == personnelSel){
      $personnelContentEle.find('.personnel-designation').addClass('hidden');
      $personnelContentEle.find('.personnel-name').removeClass('hidden');
      $personnelContentEle.find('.personnel-qualification').removeClass('hidden');
      $personnelContentEle.find('.personnel-wrkExpYear').removeClass('hidden');
      $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
      if('Y' == prsFlag){
        inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
      }
    }else if('SPPT003' == personnelSel){
      $personnelContentEle.find('.personnel-designation').addClass('hidden');
      $personnelContentEle.find('.personnel-name').removeClass('hidden');
      $personnelContentEle.find('.personnel-qualification').addClass('hidden');
      $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
      $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
      inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
    }else if('SPPT004' == personnelSel){
      $personnelContentEle.find('.personnel-designation').addClass('hidden');
      $personnelContentEle.find('.personnel-name').removeClass('hidden');
      $personnelContentEle.find('.personnel-qualification').addClass('hidden');
      $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
      $personnelContentEle.find('.personnel-regnNo ').removeClass('hidden');
      //regnNo.
      if('Y' == prsFlag){
        inputReadonly($personnelContentEle.find('input[name="name"]'));
      }
    }else if('' == personnelSel){
      $personnelContentEle.find('.personnel-designation').addClass('hidden');
      $personnelContentEle.find('.personnel-name').addClass('hidden');
      $personnelContentEle.find('.personnel-qualification').addClass('hidden');
      $personnelContentEle.find('.personnel-regnNo ').addClass('hidden');
      $personnelContentEle.find('.personnel-wrkExpYear').addClass('hidden');
      if('Y' == prsFlag){
        inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
      }
    }
  }

  $('.addListBtn').click(function () {
    showWaiting();
    let target =  $('.personnel-content:last')
    let src = target.clone();
    src.find('.removeSpBtn').show()
    clearFields(src);

    $('.personnel-content-point').before(src);
    pageController($('.personnel-content:last'));
    spRemove();
    var psnLength = $('.personnel-content').length;
    if(psnLength >='${spHcsaSvcPersonnelDto.maximumCount}'){
      $('#addPsnDiv').addClass('hidden');
    }
    if(psnLength <= '${spHcsaSvcPersonnelDto.mandatoryCount}'){
      $('.personnel-content:last em.removeSpBtn').remove();
    }
    $('#isEditHiddenVal').val('1');
    changePsnItem();
    designationChange();
    dismissWaiting();
  });


  function refreshIndex($premContent, k){
    var $target = getJqueryNode($premContent);
    if (isEmptyNode($target)) {
      return;
    }
    $target.find('.premIndex').val(k);
    toggleTag($target.find('.removeEditDiv'), k != 0);
  }

  function removeSp(){
    console.log("SP --->");
    $('.spErrorMsg').html("");
  }

  var pageController = function ($Ele) {
    <c:choose>
    <c:when test="${'NMI' ==currentSvcCode || 'NMA'== currentSvcCode}">
    personnelSel();
    if($Ele == ''){
      //triggering event
      $('.personnelSel').trigger('change');
    }else {
      $Ele.find('.personnelSel').trigger('change');
    }
    </c:when>
    <c:when test="${'BLB' ==currentSvcCode}">
    absencePsnSel('Blood Banking',$Ele);
    </c:when>
    <c:when test="${'TSB'== currentSvcCode}">
    absencePsnSel('Tissue Banking p1',$Ele);
    </c:when>
    <c:otherwise>
    absencePsnSel('other service',$Ele);
    </c:otherwise>
    </c:choose>
  }


  var doEdit = function () {
    $('.svcPsnEdit').click(function () {
      let $context = $(this).closest("td")
      $(this).addClass('hidden');
      unDisabledPartPage($context);
      $('#isEditHiddenVal').val('1');
      $context.find('input[name="isPartEdit"]').val('1');
      $(this).find('input[name="prsLoading"]').each(function () {
        var prsLoading = $(this).val();
        if(prsLoading  == 'true'){
          var $currContent = $(this).closest('.personnel-content');
          inputReadonly($currContent.find('input[name="name"]'));
        }
      });
    });
  }
  var spRemove = function(){
    $('.removeSpBtn').click(function () {
      var $psnContentEle= $(this).closest('.personnel-content');
      $psnContentEle.remove();
      //reset number
      $('.personnel-content').each(function (k,v) {
        $(this).find('.assign-psn-item').html(k+1);
      });

      //show add more
      var psnLength = $('.personnel-content').length;
      if(psnLength <'${spHcsaSvcPersonnelDto.maximumCount}'){
        $('#addPsnDiv').removeClass('hidden');
      }
      if(psnLength <= 1){
        $('.assign-psn-item:eq(0)').html('');
      }
      $('#isEditHiddenVal').val('1');
    });
  }

  function aaa(obj) {
    console.log('loading prs info ...');
    showWaiting();
    var $loadingContent =$(obj).closest('.personnel-content');
    var prgNo =  $(obj).val();
    if(prgNo == "" || prgNo == null || prgNo == undefined){
      clearPrsInfo($loadingContent);
      dismissWaiting();
      return;
    }
    var no = $(obj).val();
    var jsonData = {
      'prgNo': no
    };
    $.ajax({
      'url': '${pageContext.request.contextPath}/prg-input-info',
      'dataType': 'json',
      'data': jsonData,
      'type': 'GET',
      'success': function (data) {
        if (isEmpty(data)) {
          console.log("The return data is null for PRS");
        } else if('-1' == data.statusCode || '-2' == data.statusCode) {
          $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0042" escape="false" />');
          $('#PRS_SERVICE_DOWN').modal('show');
          clearPrsInfo($loadingContent);
        } else if (data.hasException) {
          $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0048" escape="false" />');
          $('#PRS_SERVICE_DOWN').modal('show');
          clearPrsInfo($loadingContent);
        } else if ('401' == data.statusCode) {
          $('#prsErrorMsg').html('<iais:message key="GENERAL_ERR0054" escape="false" />');
          $('#PRS_SERVICE_DOWN').modal('show');
          clearPrsInfo($loadingContent);
        } else {
          loadingSp(data,obj);
        }
        dismissWaiting();
      },
      'error': function () {
        clearPrsInfo($loadingContent);
        dismissWaiting();
      }
    });

  }

  var clearPrsInfo = function ($loadingContent) {
    $loadingContent.find('input[name="name"]').val('');
    // inputCancelReadonly($loadingContent.find('input[name="name"]'));
  };

  function loadingSp(data,obj) {
    var $CurrentPsnEle = $(obj).closest('.personnel-content');
    const name = data.name;
    $CurrentPsnEle.find("input[name='name']").val(name);
    var prsFlag = $('input[name="prsFlag"]').val();
    if('Y' == prsFlag){
      inputReadonly($CurrentPsnEle.find('input[name="name"]'));
    }
  }

  function inputReadonly($content){
    $content.prop('readonly', true);
    $content.css('border-color', '#ededed');
    $content.css('color', '#999');
  }
  function inputCancelReadonly($content){
    $content.prop('readonly', false);
    $content.css('border-color', '');
    $content.css('color', '');
  }

  function notLoadingSp() {
    $("input[name='name']").prop('readonly', false);
    $("input[name='name']").css('border-color', '');
    $("input[name='name']").css('color', '');
  }

  function notLoadingSpl() {
    var val = $("input[name='regnNo']").val();
    if(val!=""){
      $("input[name='name']").prop('readonly', true);
      $("input[name='name']").css('border-color', '#ededed');
      $("input[name='name']").css('color', '#999');
    }
  }
  function cancel() {
    $('#PRS_SERVICE_DOWN').modal('hide');
  }

  var changePsnItem = function () {
    $('.assign-psn-item').each(function (k, v) {
      $(this).html(k + 1);
    });

  };

  var designationChange = function () {
    $('.designation').unbind('change');
    $('.designation').change(function () {
      var thisVal = $(this).val();
      if("Others" == thisVal){
        $(this).closest('.personnel-content').find('.otherDesignationDiv').removeClass('hidden');
      }else{
        $(this).closest('.personnel-content').find('.otherDesignationDiv').addClass('hidden');
      }
    });
  };

</script>








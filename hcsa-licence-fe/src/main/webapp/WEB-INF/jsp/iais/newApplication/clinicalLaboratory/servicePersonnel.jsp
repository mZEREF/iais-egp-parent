<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>

<c:set var="isRfi" value="${requestInformationConfig != null}"/>

<input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>

<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
  <div id="wizard-page-title"></div>
  <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
    <div id="control--runtime--0" class="page control control-area  container-p-1">
      <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
      <table aria-describedby="" class="control-grid columns1" style="width:100%;">
        <thead style="display: none">
        <tr><th scope="col"></th></tr>
        </thead>
        <tbody>
        <tr height="1">
          <td class="first last" style="width: 100%;">
            <div id="control--runtime--85" class="control control-caption-horizontal">
            </div>
          </td>
        </tr>
        <tr>
        </tr>
        <tr height="1">
          <td class="" style="width: 100%;">
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
                  <%--<c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
                    <c:if test="${'APPSPN07' == clickEditPage}">
                      <c:set var="isClickEdit" value="true"/>
                    </c:if>
                  </c:forEach>
                  <c:choose>
                    <c:when test="${'true' != isClickEdit}">
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                    </c:when>
                    <c:otherwise>
                      <input id="isEditHiddenVal" type="hidden" name="isEdit" value="1"/>
                    </c:otherwise>
                  </c:choose>--%>
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
                    <div id="edit-content">
                      <c:choose>
                        <c:when test="${canEdit && editControl}">
                          <div class="text-right app-font-size-16">
                            <a id="edit" class="svcPsnEdit" href="javascript:void(0);">
                              <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                            </a>
                          </div>
                        </c:when>
                      </c:choose>
                    </div>
                  </c:if>
                </c:if>
              </div>
              <input type="hidden" name="prsFlag" value="${prsFlag}"/>
              <c:if test="${ServicePersonnelMandatory>0 && editControl}">
                <c:set var="spMandatoryCount" value="${spHcsaSvcPersonnelDto.mandatoryCount}"/>
                <c:forEach begin="0" end="${ServicePersonnelMandatory-1}" step="1" varStatus="status">
                  <c:if test="${AppSvcPersonnelDtoList != null && AppSvcPersonnelDtoList.size()>0}">
                    <c:set value="${AppSvcPersonnelDtoList[status.index]}" var="appSvcPersonnelDto"/>
                  </c:if>
                  <table aria-describedby="" class="personnel-content" id="personnelRemoveId${status.index}">
                    <thead style="display: none">
                    <tr><th scope="col"></th></tr>
                    </thead>
                    <input type="hidden" name="indexNo" value="${appSvcPersonnelDto.indexNo}"/>
                    <tbody>
                    <tr height="1" class="personnel-header">
                      <td class="" style="width: 100%;">
                        <div  class="personnel-header control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-8 control-label formtext ">
                              <div class="cgo-header">
                                <strong>Service Personnel <label class="assign-psn-item"><c:if test="${AppSvcPersonnelDtoList.size() > 1}">${status.index+1}</c:if></label></strong>
                              </div>
                            </div>
                            <div class="col-sm-5 col-md-4 text-right">
                              <c:if test="${status.index - spMandatoryCount >=0}">
                                <h4 class="text-danger"><em class="fa fa-times-circle del-size-36 removeSpBtn cursorPointer"></em></h4>
                              </c:if>
                            </div>
                          </div>
                        </div>
                      </td>
                    </tr>
                    <tr height="1">
                      <td class="" >
                        <div id="control--runtime--2" class="control control-caption-horizontal svcPsnSel">
                          <div class=" form-group form-horizontal personnel-sel">
                            <div class="col-sm-5 control-label formtext ">
                              <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Select Service Personnel</label>
                              <span class="mandatory">*</span>
                              <span class="upload_controls"></span>
                            </div>
                            <div class="col-sm-5 col-md-7">
                              <div class="">
                                <iais:select cssClass="personnelSel"  name="personnelSel" options="ServicePersonnelType" value="${appSvcPersonnelDto.personnelType}" firstOption="Please Select"></iais:select>
                                <span class="error-msg" name="iaisErrorMsg" id="error_personnelSelErrorMsg${status.index}" ></span>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="new-svc-personnel-form">
                          <table aria-describedby="" class="control-grid" style="width:100%;">
                            <thead style="display: none">
                            <tr><th scope="col"></th></tr>
                            </thead>
                            <tbody>
                            <tr height="1" class="">
                              <td class="">
                                <div  class="personnel-name hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Name</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <input type="hidden" name="prsLoading" value="${appSvcPersonnelDto.prsLoading}" />
                                      <iais:input maxLength="66" type="text" name="name"  value="${appSvcPersonnelDto.name}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_name${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="" style="width: 100%;">
                                <div  class="personnel-designation hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Designation</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:select cssClass="designation"  name="designation" options="NuclearMedicineImagingDesignation"  value="${appSvcPersonnelDto.designation}" firstOption="Please Select"></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_designation${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="otherDesignationDiv
                                                                <c:if test="${appSvcPersonnelDto.designation != 'Others' }">
                                                                    hidden
                                                                </c:if>">
                              <td class="" style="width: 100%;">
                                <div  class="personnel-designation hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 col-md-5">
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:input  maxLength="100" type="text" cssClass="otherDesignation" name="otherDesignation" value="${appSvcPersonnelDto.otherDesignation}"/>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_otherDesignation${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="">
                                <div  class="personnel-qualification hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Qualification</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:input maxLength="100" type="text" name="qualification"  value="${appSvcPersonnelDto.qualification}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_qualification${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="">
                                <div  class="personnel-regnNo hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Professional Regn. No. </label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <input maxLength="20" type="text" name="regnNo"  value="${appSvcPersonnelDto.profRegNo}" onblur="aaa(this)">
                                      <span class="error-msg" name="iaisErrorMsg" id="error_regnNo${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="">
                                <div  class="personnel-wrkExpYear hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Relevant working experience (Years)</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:input maxLength="2" type="text" name="wrkExpYear"  value="${appSvcPersonnelDto.wrkExpYear}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_wrkExpYear${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            </tbody>
                          </table>
                        </div>
                      </td>
                    </tr>
                    </tbody>
                  </table>
                </c:forEach>
              </c:if>
              <div class="personnel-content-point"></div>
              <c:if test="${requestInformationConfig==null}">
                <%-- <c:choose>
                   <c:when test="${!empty AppSvcPersonnelDtoList}">
                     <c:set var="spDtoLength" value="${AppSvcPersonnelDtoList.size()}"/>
                   </c:when>
                   <c:otherwise>
                     <c:set var="spDtoLength" value="0"/>
                   </c:otherwise>
                 </c:choose>--%>
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
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</div>
<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-body" >
        <div class="row">
          <div class="col-md-12">
            <span style="font-size: 2rem;" id="prsErrorMsg">
              <iais:message key="GENERAL_ERR0048" escape="false" />
            </span>
          </div>
        </div>
      </div>
      <div class="row " style="margin-top: 5%;margin-bottom: 5%">
        <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancel()">OK</button>
      </div>
    </div>
  </div>
</div>
<div style="display: none;">
  <select id="nice_select_effect"></select>
</div>
<input type="hidden" value="goveOffice" name="pageCon">
<input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT" >
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
      if(${editControl}){
        $('.addListBtn').addClass('hidden');
      }
      $('.text-danger').addClass('hidden');
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

    /* notLoadingSpl();*/
    init = 1;
  });

  var absencePsnSel = function (val) {
    $('.svcPsnSel').addClass('hidden');
    var prsFlag = $('input[name="prsFlag"]').val();
    $('.personnel-content').each(function (k,v) {
      if('Blood Banking' == val){
        $(this).find('div.personnel-sel').addClass('hidden');
        $(this).find('div.new-svc-personnel-form').removeClass('hidden');
        $(this).find('div.personnel-designation').removeClass('hidden');
        $(this).find('div.personnel-name').removeClass('hidden');
        $(this).find('div.personnel-qualification').addClass('hidden');
        $(this).find('div.personnel-regnNo ').removeClass('hidden');
        $(this).find('div.personnel-wrkExpYear').removeClass('hidden');
        if("Y" == prsFlag){
          inputReadonly($(this).find('input[name="name"]'));
        }
      }else if('Tissue Banking p1' == val){
        $(this).find('div.personnel-sel').addClass('hidden');
        $(this).find('div.new-svc-personnel-form').removeClass('hidden');
        $(this).find('div.personnel-designation').addClass('hidden');
        $(this).find('div.personnel-name').removeClass('hidden');
        $(this).find('div.personnel-qualification').removeClass('hidden');
        $(this).find('div.personnel-regnNo ').addClass('hidden');
        $(this).find('div.personnel-wrkExpYear').removeClass('hidden');
        if("Y" == prsFlag){
          inputCancelReadonly($(this).find('input[name="name"]'));
        }
      }else {
        $(this).find('div.personnel-sel').addClass('hidden');
        $(this).find('div.new-svc-personnel-form').removeClass('hidden');
        $(this).find('div.personnel-designation').addClass('hidden');
        $(this).find('div.personnel-name').removeClass('hidden');
        $(this).find('div.personnel-qualification').removeClass('hidden');
        $(this).find('div.personnel-regnNo ').addClass('hidden');
        $(this).find('div.personnel-wrkExpYear').removeClass('hidden');
        if("Y" == prsFlag){
          inputCancelReadonly($(this).find('input[name="name"]'));
        }
      }
    });
  };

  var personnelSel = function(){
    $('.personnelSel').change(function () {
      var personnelSel = $(this).val();
      var $personnelContentEle = $(this).closest('table.personnel-content');
      var prsFlag = $('input[name="prsFlag"]').val();
      if(init != 0){
        //clear data;
        $personnelContentEle.find('div.personnel-designation select[name="designation"]').val('');
        var designation = $personnelContentEle.find('div.personnel-designation  option[value=""]').html();
        $personnelContentEle.find('select[name="designation"]').next().find('.current').html(designation);

        $personnelContentEle.find('div.personnel-name input[name="name"]').val('');
        $personnelContentEle.find('div.personnel-regnNo input[name="regnNo"]').val('');
        $personnelContentEle.find('div.personnel-wrkExpYear input[name="wrkExpYear"]').val('');
        $personnelContentEle.find('div.personnel-qualification input[name="qualification"]').val('');
        if('Y' == prsFlag){
          inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
        }
      }
      personnelSelFun(personnelSel,$personnelContentEle);
    });
  };

  var personnelSelFun = function(personnelSel,$personnelContentEle){
    $personnelContentEle.find('div.new-svc-personnel-form ').removeClass('hidden');
    var prsFlag = $('input[name="prsFlag"]').val();
    if('SPPT001' == personnelSel){
      $personnelContentEle.find('div.personnel-designation').removeClass('hidden');
      $personnelContentEle.find('div.personnel-name').removeClass('hidden');
      $personnelContentEle.find('div.personnel-qualification').removeClass('hidden');
      $personnelContentEle.find('div.personnel-wrkExpYear').removeClass('hidden');
      $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      if('Y' == prsFlag){
        inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
      }
    }else if('SPPT002' == personnelSel){
      $personnelContentEle.find('div.personnel-designation').addClass('hidden');
      $personnelContentEle.find('div.personnel-name').removeClass('hidden');
      $personnelContentEle.find('div.personnel-qualification').removeClass('hidden');
      $personnelContentEle.find('div.personnel-wrkExpYear').removeClass('hidden');
      $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      if('Y' == prsFlag){
        inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
      }
    }else if('SPPT003' == personnelSel){
      $personnelContentEle.find('div.personnel-designation').addClass('hidden');
      $personnelContentEle.find('div.personnel-name').removeClass('hidden');
      $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
      $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
      $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
    }else if('SPPT004' == personnelSel){
      $personnelContentEle.find('div.personnel-designation').addClass('hidden');
      $personnelContentEle.find('div.personnel-name').removeClass('hidden');
      $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
      $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
      $personnelContentEle.find('div.personnel-regnNo ').removeClass('hidden');
      //regnNo.
      if('Y' == prsFlag){
        inputReadonly($personnelContentEle.find('input[name="name"]'));
      }
    }else if('' == personnelSel){
      $personnelContentEle.find('div.personnel-designation').addClass('hidden');
      $personnelContentEle.find('div.personnel-name').addClass('hidden');
      $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
      $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
      if('Y' == prsFlag){
        inputCancelReadonly($personnelContentEle.find('input[name="name"]'));
      }
    }
  }


  $('.addListBtn').click(function () {
    showWaiting();
    var HasNumber = $(".personnel-content").length;
    console.log("HasNumber"+HasNumber);
    $.ajax({
      url:'${pageContext.request.contextPath}/nuclear-medicine-imaging-html',
      dataType:'json',
      data:{
        'HasNumber':HasNumber
      },
      type:'POST',
      success:function (data) {
        if ("success" == data.res){
          $('.personnel-content-point').before(data.sucInfo);
          pageController($('.personnel-content:last'));
          spRemove();
          //hidden add more
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
          /*$("div.designation->ul").mCustomScrollbar({
                  advanced: {
                      updateOnContentResize: true
                  }
              }
          );*/
        }else{
          $('.spErrorMsg').html(data.errInfo);
        }
        dismissWaiting();
      },
      error:function (data) {
        dismissWaiting();
      }
    });
  });

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
    absencePsnSel('Blood Banking');
    </c:when>
    <c:when test="${'TSB'== currentSvcCode}">
    absencePsnSel('Tissue Banking p1');
    </c:when>
    <c:otherwise>
    absencePsnSel('other service');
    </c:otherwise>
    </c:choose>
  }


  var doEdit = function () {
    $('#edit').click(function () {
      unDisabledPage();
      $('#isEditHiddenVal').val('1');
      $('#edit-content').addClass('hidden');
      $('.addListBtn').removeClass('hidden');
      $('.text-danger').removeClass('hidden');
      $('input[name="prsLoading"]').each(function () {
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
    var $loadingContent =$(obj).closest('table.personnel-content');
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
          $('#prsErrorMsg').val('<iais:message key="GENERAL_ERR0042" escape="false" />');
          $('#PRS_SERVICE_DOWN').modal('show');
          clearPrsInfo($loadingContent);
        } else if (data.hasException) {
          $('#prsErrorMsg').val('<iais:message key="GENERAL_ERR0048" escape="false" />');
          $('#PRS_SERVICE_DOWN').modal('show');
          clearPrsInfo($loadingContent);
        } else if ('401' == data.statusCode) {
          $('#prsErrorMsg').val('<iais:message key="GENERAL_ERR0054" escape="false" />');
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
    var $CurrentPsnEle = $(obj).closest('table.personnel-content');
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
        $(this).closest('table.personnel-content').find('tr.otherDesignationDiv').removeClass('hidden');
      }else{
        $(this).closest('table.personnel-content').find('tr.otherDesignationDiv').addClass('hidden');
      }
    });
  };

</script>







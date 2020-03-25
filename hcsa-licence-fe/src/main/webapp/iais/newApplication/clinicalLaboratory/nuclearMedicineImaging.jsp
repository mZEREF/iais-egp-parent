<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
  <div id="wizard-page-title">Service Personnel</div>
  <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
    <div id="control--runtime--0" class="page control control-area  container-p-1">
      <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
      <table class="control-grid columns1" style="width:100%;">
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
                    <label>The blood donation centre and/or mobile donation drive is/are under the supervision of</label>
                  </c:when>
                  <c:when test="${'TCB' ==currentSvcCode}">
                    <label>There is an appointed Laboratory Director who has the appropriate qualifications</label>
                  </c:when>
                  <c:when test="${'NMI' ==currentSvcCode}">
                    <label>The Nuclear Medicine Imagine Service have the following personnel that satisfy the minimum requirements at all times</label>
                  </c:when>
                </c:choose>
              </div>

              <span class="upload_controls"></span>

              <div id="control--runtime--1--errorMsg_section_top" class="error_placements"></div>
              <div class="personnel-edit">
                <c:if test="${'APTY005' ==AppSubmissionDto.appType}">
                  <c:forEach var="clickEditPage" items="${AppSubmissionDto.clickEditPage}">
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
                  </c:choose>
                  <c:if test="${'true' != isClickEdit}">
                    <c:set var="locking" value="true"/>
                    <c:forEach var="amendType"  items="${AppSubmissionDto.amendTypes}">
                      <c:if test="${amendType =='RFCATYPE05'}">
                        <c:set var="canEdit" value="1"/>
                      </c:if>
                    </c:forEach>
                    <div id="edit-content">
                      <c:choose>
                        <c:when test="${'1' == canEdit}">
                          <p class="text-right"><a id="edit"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
                        </c:when>
                        <c:otherwise>
                          <p class="text-right" style="color: gray"><em class="fa fa-pencil-square-o"></em>Edit</p>
                        </c:otherwise>
                      </c:choose>
                    </div>
                  </c:if>
                </c:if>
              </div>
              
              <c:if test="${ServicePersonnelMandatory>0}">
              <c:forEach begin="0" end="${ServicePersonnelMandatory-1}" step="1" varStatus="status">
                <c:if test="${AppSvcPersonnelDtoList != null && AppSvcPersonnelDtoList.size()>0}">
                  <c:set value="${AppSvcPersonnelDtoList[status.index]}" var="appSvcPersonnelDto"/>
                </c:if>
                  <table class="personnel-content">
                    <tbody>
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
                                <span class="error-msg" name="iaisErrorMsg" id=""></span>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="new-svc-personnel-form">
                          <table class="control-grid" style="width:100%;">
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
                                      <iais:select  name="designation" options="NuclearMedicineImagingDesignation"  value="${appSvcPersonnelDto.designation}" firstOption="Please Select"></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_designation${status.index}"></span>
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
                                      <iais:input maxLength="20" type="text" name="qualification"  value="${appSvcPersonnelDto.quaification}"></iais:input>
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
                                      <label  class="control-label control-set-font control-font-label">Professional Registration No. </label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5 col-md-7">
                                      <iais:input maxLength="20" type="text" name="regnNo"  value="${appSvcPersonnelDto.profRegNo}"></iais:input>
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
              <c:if test="${'APTY005' !=AppSubmissionDto.appType}">
                <div>
                  <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another Service Personnel</span>
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

<script>
  $(document).ready(function () {
      pageController('');

      if('APTY005' == '${AppSubmissionDto.appType}' && 'true' != '${isClickEdit}'){
          $('input[type="text"]').prop('disabled',true);
          //nice-select
          $('div.nice-select').addClass('disabled');
      }

      <!--for reload when have personnelSel-->
      <c:if test="${'NMI' ==currentSvcCode || 'NMA'== currentSvcCode}">
      $('.personnel-content').each(function (k,v) {
          var personnelSel = $(this).find('.personnelSel').val();
          var $personnelContentEle = $(this);
          personnelSelFun(personnelSel,$personnelContentEle);
      });
      </c:if>

  });

  var absencePsnSel = function (val) {
      $('.svcPsnSel').addClass('hidden');
      $('.personnel-content').each(function (k,v) {
          if('Blood Banking' == val){
              $(this).find('div.personnel-sel').addClass('hidden');
              $(this).find('div.new-svc-personnel-form').removeClass('hidden');
              $(this).find('div.personnel-designation').removeClass('hidden');
              $(this).find('div.personnel-name').removeClass('hidden');
              $(this).find('div.personnel-qualification').addClass('hidden');
              $(this).find('div.personnel-regnNo ').removeClass('hidden');
              $(this).find('div.personnel-wrkExpYear').removeClass('hidden');
          }else if('Tissue Banking p1' == val){
              $(this).find('div.personnel-sel').addClass('hidden');
              $(this).find('div.new-svc-personnel-form').removeClass('hidden');
              $(this).find('div.personnel-designation').addClass('hidden');
              $(this).find('div.personnel-name').removeClass('hidden');
              $(this).find('div.personnel-qualification').removeClass('hidden');
              $(this).find('div.personnel-regnNo ').addClass('hidden');
              $(this).find('div.personnel-wrkExpYear').removeClass('hidden');
          }
      });

  }

  var personnelSel = function(){
      $('.personnelSel').change(function () {
          var personnelSel = $(this).val();
          var $personnelContentEle = $(this).closest('table.personnel-content');
          personnelSelFun(personnelSel,$personnelContentEle);
      });

  };

  var personnelSelFun = function(personnelSel,$personnelContentEle){
      $personnelContentEle.find('div.new-svc-personnel-form ').removeClass('hidden');

      if('SPPT001' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').removeClass('hidden');
          $personnelContentEle.find('div.personnel-name').removeClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').removeClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').removeClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      }else if('SPPT002' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').addClass('hidden');
          $personnelContentEle.find('div.personnel-name').removeClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').removeClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').removeClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      }else if('SPPT003' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').addClass('hidden');
          $personnelContentEle.find('div.personnel-name').removeClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
      }else if('SPPT004' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').addClass('hidden');
          $personnelContentEle.find('div.personnel-name').removeClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').removeClass('hidden');
      }else if('' == personnelSel){
          $personnelContentEle.find('div.personnel-designation').addClass('hidden');
          $personnelContentEle.find('div.personnel-name').addClass('hidden');
          $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
          $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
          $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
      }
  }


  $('.addListBtn').click(function () {
      $.ajax({
          'url':'${pageContext.request.contextPath}/nuclear-medicine-imaging-html',
          'dataType':'text',
          'type':'GET',
          'success':function (data) {
              $('.personnel-content:last').after(data);

              pageController($('.personnel-content:last'));
          },
          'error':function (data) {

          }
      });



  });


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
      <c:when test="${'TCB'== currentSvcCode}">
      absencePsnSel('Tissue Banking p1');
      </c:when>
      </c:choose>
  }


</script>

<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
  <div id="wizard-page-title">Service Personnel</div>
  <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
    <div id="control--runtime--0" class="page control control-area  container-p-1">
      <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
      <table class="control-grid columns1">
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
          <td class="first last" style="width: 100%;">
            <div id="control--runtime--1" class="section control  container-s-1">
              <div class="control-set-font control-font-header control-font-header section-header"><label>The Nuclear Medicine Imagine Service have the following personnel that satisfy the minimum requirements at all times</label></div>

              <span class="upload_controls"></span>

              <div id="control--runtime--1--errorMsg_section_top" class="error_placements"></div>
              <div class="personnel-content ">
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
                          <p class="text-right"><a id="edit"><i class="fa fa-pencil-square-o"></i>Edit</a></p>
                        </c:when>
                        <c:otherwise>
                          <p class="text-right" style="color: gray"><i class="fa fa-pencil-square-o"></i>Edit</p>
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
                  <table class="personnel-content ">
                    <tbody>
                    <tr height="1">
                      <td class="first last" style="width: 100%;">
                        <div id="control--runtime--2" class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-5 control-label formtext ">
                              <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Select Service Personnel</label>
                              <span class="mandatory">*</span>
                              <span class="upload_controls"></span>
                            </div>
                            <div class="col-sm-5">
                              <div class="">
                                <iais:select cssClass="personnelSel"  name="personnelSel" options="ServicePersonnelType" value="${appSvcPersonnelDto.personnelType}" firstOption="Please Select"></iais:select>
                                <span class="error-msg" name="iaisErrorMsg" id=""></span>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="new-officer-form">
                          <table class="control-grid">
                            <tbody>
                            <tr height="1" class="">
                              <td class="first last" style="width: 100%;">
                                <div  class="personnel-designation hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Designation</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5">
                                      <iais:select  name="designation" options="NuclearMedicineImagingDesignation"  value="${appSvcPersonnelDto.designation}" firstOption="Please Select"></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_designation${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="first last" style="width: 100%;">
                                <div  class="personnel-name hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Name</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5">
                                      <iais:input maxLength="66" type="text" name="name"  value="${appSvcPersonnelDto.name}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_name${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="first last" style="width: 100%;">
                                <div  class="personnel-qualification hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Qualification</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5">
                                      <iais:input maxLength="20" type="text" name="qualification"  value="${appSvcPersonnelDto.quaification}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_qualification${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="first last" style="width: 100%;">
                                <div  class="personnel-wrkExpYear hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Relevant working experience (Years)</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5">
                                      <iais:input maxLength="2" type="text" name="wrkExpYear"  value="${appSvcPersonnelDto.wrkExpYear}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_wrkExpYear${status.index}"></span>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr height="1" class="">
                              <td class="first last" style="width: 100%;">
                                <div  class="personnel-regnNo hidden control-caption-horizontal">
                                  <div class=" form-group form-horizontal formgap">
                                    <div class="col-sm-5 control-label formtext ">
                                      <label  class="control-label control-set-font control-font-label">Professional Regn No</label>
                                      <span class="mandatory">*</span>
                                      <span class="upload_controls"></span>
                                    </div>
                                    <div class="col-sm-5">
                                      <iais:input maxLength="20" type="text" name="regnNo"  value="${appSvcPersonnelDto.profRegNo}"></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id="error_regnNo${status.index}"></span>
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
      personnelSel();
      //triggering event
      $('.personnelSel').trigger('change');

      if('APTY005' == '${AppSubmissionDto.appType}' && 'true' != '${isClickEdit}'){
          $('input[type="text"]').prop('disabled',true);
          //nice-select
          $('div.nice-select').addClass('disabled');
      }

  });

  var personnelSel = function(){
      $('.personnelSel').change(function () {
          var personnelSel = $(this).val();
          $personnelContentEle = $(this).closest('table.personnel-content');
          $personnelContentEle.find('div.new-officer-form ').removeClass('hidden');
          /*//clear
          $personnelContentEle.find('div.personnel-designation input[name="designation"]').val('');
          $personnelContentEle.find('div.personnel-name input[name="name"]').val('');
          $personnelContentEle.find('div.personnel-qualification input[name="qualification"]').val('');
          $personnelContentEle.find('div.personnel-wrkExpYear input[name="wrkExpYear"]').val('');
          $personnelContentEle.find('div.personnel-regnNo input[name="regnNo"]').val('');*/

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
          }

      });

  };


  $('.addListBtn').click(function () {
      $.ajax({
          'url':'${pageContext.request.contextPath}/nuclear-medicine-imaging-html',
          'dataType':'text',
          'type':'GET',
          'success':function (data) {
              $('.personnel-content:last').after(data);
              personnelSel();
          },
          'error':function (data) {

          }
      });



  });


</script>

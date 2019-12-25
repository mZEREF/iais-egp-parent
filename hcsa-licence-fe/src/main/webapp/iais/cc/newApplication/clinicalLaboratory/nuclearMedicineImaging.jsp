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
              <div class="personnel-content "></div>
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
                              <span class="upload_controls"></span>
                            </div>
                            <div class="col-sm-5">
                              <div class="">
                                <iais:select cssClass="personnelSel"  name="personnelSel" options="NuclearMedicineImagingPersonnel"  value="${appSvcPersonnelDto.personnelSel}" firstOption="Please Select"></iais:select>
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
              <div>
                <span class="addListBtn" style="color:deepskyblue;cursor:pointer;">+ Add Another Service Personnel</span>
              </div>
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

          if('Radiology Professional' == personnelSel){
              $personnelContentEle.find('div.personnel-designation').removeClass('hidden');
              $personnelContentEle.find('div.personnel-name').removeClass('hidden');
              $personnelContentEle.find('div.personnel-qualification').removeClass('hidden');
              $personnelContentEle.find('div.personnel-wrkExpYear').removeClass('hidden');
              $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
          }else if('Medical Physicist' == personnelSel){
              $personnelContentEle.find('div.personnel-designation').addClass('hidden');
              $personnelContentEle.find('div.personnel-name').removeClass('hidden');
              $personnelContentEle.find('div.personnel-qualification').removeClass('hidden');
              $personnelContentEle.find('div.personnel-wrkExpYear').removeClass('hidden');
              $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
          }else if('Radiation Safety Officer' == personnelSel){
              $personnelContentEle.find('div.personnel-designation').addClass('hidden');
              $personnelContentEle.find('div.personnel-name').removeClass('hidden');
              $personnelContentEle.find('div.personnel-qualification').addClass('hidden');
              $personnelContentEle.find('div.personnel-wrkExpYear').addClass('hidden');
              $personnelContentEle.find('div.personnel-regnNo ').addClass('hidden');
          }else if('Registered Nurse' == personnelSel){
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

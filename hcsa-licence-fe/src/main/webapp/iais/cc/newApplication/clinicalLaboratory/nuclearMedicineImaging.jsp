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
              <div class="control-set-font control-font-header section-header"><label>The Nuclear Medicine Imagine Service have the following personnel that satisfy the minimum requirements at all times</label></div>

              <span class="upload_controls"></span>
              <div id="control--runtime--1--errorMsg_section_top" class="error_placements"></div>
                  <table class="personnel-content control-grid ">
                    <tbody>
                    <tr height="1">
                      <td class="first last" style="width: 100%;">
                        <div id="control--runtime--2" class="control control-caption-horizontal">
                          <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-4 control-label formtext ">
                              <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Select Service Personnel</label>
                              <span class="upload_controls"></span>
                            </div>
                            <div class="col-sm-5">
                              <div class="">
                                <iais:select cssClass="personnelSel"  name="personnel" options="NuclearMedicineImagingPersonnel"  value="" firstOption="Please Select"></iais:select>
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
                                      <iais:select  name="radiologyProfessionalDesignation" options="designation"  value="" firstOption="Please Select"></iais:select>
                                      <span class="error-msg" name="iaisErrorMsg" id=""></span>
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
                                      <iais:input maxLength="66" type="text" name="name"  value=""></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id=""></span>
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
                                      <iais:input maxLength="20" type="text" name="qualification"  value=""></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id=""></span>
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
                                      <iais:input maxLength="2" type="text" name="wrkExpYear"  value=""></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id=""></span>
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
                                      <iais:input maxLength="20" type="text" name="regnNo"  value=""></iais:input>
                                      <span class="error-msg" name="iaisErrorMsg" id=""></span>
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

  });

  var personnelSel = function(){
      $('.personnelSel').change(function () {
          var personnelSel = $(this).val();
          $personnelContentEle = $(this).closest('table.personnel-content');
          $personnelContentEle.find('div.new-officer-form ').removeClass('hidden');
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


</script>

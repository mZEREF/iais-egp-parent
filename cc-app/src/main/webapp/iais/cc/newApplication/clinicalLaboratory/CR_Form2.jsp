<style>
  /* label in the component */

  div.capital-info div.control div.control-label-span {
    display: none;
  }
  /* label component */

  div.capital-info div.control div.label-control {
    display: block;
  }

  div.capital-info div.info-header div.label-control {
    padding-left: 16px;
    text-align: left;
  }

  div.capital-info td.first div.control div.label-control {
    width: 100%;
  }

  div.capital-info td.first {
    width: 350px;
  }

  div.capital-info div.no-label div.control-input-span {
    width: 90%;
  }

  div.capital-info .control .control-input-span input[type="text"] {
    max-width: none;
    width: 90%;
  }

  div.capital-info table.control-grid tr td td {
    width: auto;
    padding-right: 25px;
  }
</style>

<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
  <div id="wizard-page-title">Clinical Governance Officer</div>
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
              <div class="control-set-font control-font-header section-header"><label>A Clinical Governance Officer is responsible for the clinical and technical oversight of a medical service.</label></div>
              <span class="upload_controls"></span>
              <div id="control--runtime--1--errorMsg_section_top" class="error_placements"></div>
              <table class="control-grid">
                <tbody>
                <tr height="1">
                  <td class="first last" style="width: 100%;">
                    <div id="control--runtime--2" class="control control-caption-horizontal">
                      <div class=" form-group form-horizontal formgap">
                        <div class="col-sm-4 control-label formtext ">
                          <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Assign a Clinical Governance Officer</label>
                          <span class="upload_controls"></span>
                        </div>
                        <div class="col-sm-5">
                          <div class="">
                            <select id="cgoSelect" name="assignSelect" class="form-control control-input control-set-font control-font-normal">
                              <option>Select Personnel</option>
                              <option value="newOfficer">I'd like to add a new personnel</option>
                              <option>Linda Tan, XXX442 (NRIC)</option>
                              <option>Deng Jin, XXX675 (NRIC)</option>
                            </select>
                            <div id="control--runtime--2--errorMsg_right" style="display: none;" class="error_placements"></div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="profile-info-gp hidden">
                      <div class="officer-info">
                        <h3>Linda Tan</h3>
                        <div class="row profile-line">
                          <div class="col-xs-6 col-md-3">
                            <p>Name</p>
                          </div>
                          <div class="col-xs-6 col-md-7">
                            <p>Linda Tan</p>
                          </div>
                        </div>
                        <div class="row profile-line">
                          <div class="col-xs-6 col-md-3">
                            <p>ID No.</p>
                          </div>
                          <div class="col-xs-6 col-md-7">
                            <p>XXXX422 (NRIC)</p>
                          </div>
                        </div>
                        <div class="row profile-line">
                          <div class="col-xs-6 col-md-3">
                            <p>Mobile No.</p>
                          </div>
                          <div class="col-xs-6 col-md-7">
                            <p>+65 90009098</p>
                          </div>
                        </div>
                        <div class="row profile-line">
                          <div class="col-xs-6 col-md-3">
                            <p>Email Address</p>
                          </div>
                          <div class="col-xs-6 col-md-7">
                            <p>lindatan@hotmail.com</p>
                          </div>
                        </div>
                        <div class="row">
                          <div class="col-xs-12">
                            <p>To edit these details, you will need to <a data-toggle="modal" data-target="#officerAmend">amend</a> it first before proceeding with your application.</p>
                          </div>
                        </div>
                      </div>
                    </div>

                    <div id="newOfficer" class="new-officer-form hidden">
                      <table class="control-grid">
                        <tbody>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div  class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext ">
                                  <label  class="control-label control-set-font control-font-label">Salutation</label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <select name="salutation" class="form-control control-input control-set-font control-font-normal">
                                    <option>Select Salution</option>
                                    <option selected>Mr</option>
                                    <option>Mrs</option>
                                    <option>Miss</option>
                                    <option>Madam</option>
                                  </select>
                                  <div  style="display: none;" class="error_placements"></div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div  class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext ">
                                  <label  class="control-label control-set-font control-font-label">
                                    Name
                                  </label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <input name="name" type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">
                                    <div  style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div id="control--runtime--3" class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext ">
                                  <label id="control--runtime--3--label" class="control-label control-set-font control-font-label">ID Type</label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <select name="idType" class="form-control control-input control-set-font control-font-normal">
                                      <option>NRIC</option>
                                      <option>FIN</option>
                                    </select>
                                    <div id="control--runtime--3--errorMsg_right" style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div id="control--runtime--28" class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext ">
                                  <label id="control--runtime--28--label" class="control-label control-set-font control-font-label">
                                    ID No.
                                  </label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <input name="idNo" type="text" id="control--runtime--28--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">
                                    <div id="control--runtime--28--errorMsg_right" style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div  class="control control-caption-horizontal">
                              <div class="form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext">
                                  <label class="control-label control-set-font control-font-label">Designation</label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <select name="designation" class="form-control control-input control-set-font control-font-normal">
                                      <option>CEO</option>
                                      <option>CFO</option>
                                      <option>COO</option>
                                      <option>Others</option>
                                    </select>
                                    <div  style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div id="control--runtime--30" class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext ">
                                  <label id="control--runtime--30--label" class="control-label control-set-font control-font-label">
                                    Profession Type
                                  </label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <input name="professionType" type="text" id="control--runtime--30--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">
                                    <div id="control--runtime--30--errorMsg_right" style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div  class="control control-caption-horizontal">
                              <div class="form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext">
                                  <label  class="control-label control-set-font control-font-label">Professional Regn Type</label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <select name="professionRegoType"  class="form-control control-input control-set-font control-font-normal">
                                      <option>Doctor</option>
                                      <option>Option 1</option>
                                      <option>Option 2</option>
                                      <option>Option 3</option>
                                    </select>
                                    <div  style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div id="control--runtime--31" class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext ">
                                  <label id="control--runtime--31--label" class="control-label control-set-font control-font-label">
                                    Professional Regn No.
                                  </label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <input name="professionRegoNo" type="text" id="control--runtime--31--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">
                                    <div id="control--runtime--31--errorMsg_right" style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div id="control--runtime--29" class="control control-caption-horizontal">
                              <div class="form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext">
                                  <label id="control--runtime--29--label" class="control-label control-set-font control-font-label">Speciality</label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <select name="specialty" id="control--runtime--29--select" class="form-control control-input control-set-font control-font-normal">
                                      <option>Pathology</option>
                                      <option>Hematology</option>
                                      <option>Other</option>
                                    </select>
                                    <div id="control--runtime--29--errorMsg_right" style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div  class="control control-caption-horizontal">
                              <div class="form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext">
                                  <label class="control-label control-set-font control-font-label"></label>
                                  <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <input name="specialtyOther" type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">
                                    <div  style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div id="control--runtime--32" class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext ">
                                  <label id="control--runtime--32--label" class="control-label control-set-font control-font-label">
                                    Subspeciality or relevant qualification
                                  </label>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <input name="qualification" type="text" id="control--runtime--32--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">

                                    <div id="control--runtime--32--errorMsg_right" style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div  class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext ">
                                  <label  class="control-label control-set-font control-font-label">Mobile No.
                                  </label>                                                                                                                                        <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <input name="mobileNo" type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">

                                    <div  style="display: none;" class="error_placements"></div>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr height="1">
                          <td class="first last" style="width: 100%;">
                            <div id="control--runtime--33" class="control control-caption-horizontal">
                              <div class=" form-group form-horizontal formgap">
                                <div class="col-sm-4 control-label formtext ">
                                  <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">Email address
                                  </label>                                                                                                                                        <span class="mandatory">*</span>
                                  <span class="upload_controls"></span>
                                </div>
                                <div class="col-sm-5">
                                  <div class="">
                                    <input name="emailAddress" type="text" id="control--runtime--33--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">
                                    <div id="control--runtime--33--errorMsg_right" style="display: none;" class="error_placements"></div>
                                  </div>
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
    $('#cgoSelect').change(function () {
        $('#newOfficer').removeClass("hidden");

    });

</script>
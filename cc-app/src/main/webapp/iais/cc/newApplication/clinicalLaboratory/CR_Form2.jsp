<%--
  Created by IntelliJ IDEA.
  User: tai13
  Date: 10/12/2019
  Time: 4:38 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="container">
  <div class="row">
    <h4>Clinical Governance Officer</h4>
  </div>
  <div class="row">
    <h3> Clinical Governance Officer is responsible for the clinical and technical oversight of a medical service.</h3>
  </div>
  <div class="row">
    <span>Assign a Clinical Governance Officer</span>
    <select id="cgoSelect" class="form-control control-input control-set-font control-font-normal">
      <option>Select Personnel</option>
      <option>Linda Tan, XXX442 (NRIC)</option>
      <option>Deng Jin, XXX675 (NRIC)</option>
      <option value="newOfficer">I'd like to add a new personnel</option>
    </select>
  </div>
  <div class="row">
    <hr/>
  </div>
</div>
<div id="mainContent" class="new-officer-form hidden">
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
              <select class="form-control control-input control-set-font control-font-normal">
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
              <span
                      class="mandatory">*</span>
              <span class="upload_controls"></span>
            </div>
            <div class="col-sm-5">
              <div class="">
                <input type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">

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
              <span
                      class="mandatory">*</span>
              <span class="upload_controls"></span>
            </div>
            <div class="col-sm-5">
              <div class="">
                <select class="form-control control-input control-set-font control-font-normal">

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
              <span
                      class="mandatory">*</span>
              <span class="upload_controls"></span>
            </div>
            <div class="col-sm-5">
              <div class="">
                <input type="text" id="control--runtime--28--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">

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
              <label  class="control-label control-set-font control-font-label">Designation</label>
              <span class="mandatory">*</span>
              <span class="upload_controls"></span>
            </div>
            <div class="col-sm-5">
              <div class="">
                <select  class="form-control control-input control-set-font control-font-normal">

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
                <input type="text" id="control--runtime--30--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">

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
                <select  class="form-control control-input control-set-font control-font-normal">

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
                <input type="text" id="control--runtime--31--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">

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
                <select id="control--runtime--29--select" class="form-control control-input control-set-font control-font-normal">

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
                <input type="text" id="control--runtime--32--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">

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
                <input type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">

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
                <input type="text" id="control--runtime--33--text" class="form-control control-input control-set-font control-font-normal" value="" size="30">

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

<script>
    $('#cgoSelect').change(function () {
        $('#mainContent').removeClass("hidden");

    });

</script>
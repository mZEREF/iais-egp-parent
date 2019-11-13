
<div class="row">
  <%--  <div class="col-xs-12 col-md-10">
      <p>Please note that you will not be able to pay for this application if you have not provided the mandatory information and documents.</p>
    </div>--%>
  <%--<div class="col-xs-12 col-md-2 text-right">
    <p class="print"><a href="#"> <i class="fa fa-print"></i>Print</a></p>
  </div>--%>
</div>
<div class="row">
  <div class="col-xs-12">
    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
      <div class="panel panel-default">
        <div class="panel-heading completed" id="headingPrincipal" role="tab">
          <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePrincipal" aria-expanded="true" aria-controls="collapsePrincipal">Principal Officer</a></h4>
        </div>
        <div class="panel-collapse collapse" id="collapsePrincipal" role="tabpanel" aria-labelledby="headingPremise">
          <div class="panel-body">
            <%--<p class="text-right"><a href="application-premises.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>--%>
            <div class="panel-main-content">
              <div class="">
                <h2>Principal Officer</h2>
                <div class="row">
                  <div class="col-sm-4">
                    Deputy Principal Officer(Optional):
                  </div>
                  <div class="col-sm-4">
                    <select name="deputySelect" class="form-control control-input control-set-font control-font-normal">
                      <option>Please Select</option>
                      <option value="0">N</option>
                      <option value="1">Y</option>
                    </select>
                  </div>
                </div>
                <p><h4>A Principal Officer is responsible for overseeing the day-to-day operations of medical service</h4></p>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-6 control-label formtext ">
                        <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Assign a Clinical Governance Officer</label>
                        <span class="upload_controls"></span>
                      </div>
                      <div class="col-sm-5">
                        <div class="">
                          <select name="assign" id="cgoSelect" class="form-control control-input control-set-font control-font-normal">
                            <option>Select Personnel</option>
                            <option value="newOfficer">I'd like to add a new personnel</option>
                            <option>Deng Jin, XXX675 (NRIC)</option>
                          </select>
                          <div id="control--runtime--2--errorMsg_right" style="display: none;" class="error_placements"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="principalOfficers hidden">
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Name</label>
                      </div>
                      <div class="col-sm-2">
                        <select name="salutation" class="form-control control-input control-set-font control-font-normal">
                          <option>Mr</option>
                          <option>Mrs</option>
                          <option>Miss</option>
                          <option>Madam</option>
                        </select>
                      </div>
                      <div class="col-sm-4">
                        <input name="name" type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">ID No.
                        </label>
                      </div>
                      <div class="col-sm-2">
                        <div class="">
                          <select name="idType" class="form-control control-input control-set-font control-font-normal">
                            <option>NRIC</option>
                            <option>FIN</option>
                          </select>
                        </div>
                      </div>
                      <div class="col-sm-4">
                        <input name="idNo" type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Designation</label>
                      </div>
                      <div class="col-sm-2">
                        <select name="designation" class="form-control control-input control-set-font control-font-normal">
                          <option>CEO</option>
                          <option>CFO</option>
                          <option>COO</option>
                          <option>Others</option>
                        </select>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Mobile No.</label>
                      </div>
                      <div class="col-sm-4">
                        <input name="mobileNo" type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="control control-caption-horizontal">
                    <div class=" form-group form-horizontal formgap">
                      <div class="col-sm-3 control-label formtext ">
                        <label  class="control-label control-set-font control-font-label">Email Address</label>
                      </div>
                      <div class="col-sm-4">
                        <input name="emailAddress" type="text"  class="form-control control-input control-set-font control-font-normal" value="" size="30">
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="panel panel-default">
        <div class="panel-heading completed" id="headingDeputy" role="tab">
          <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#deputyContent" aria-expanded="true" aria-controls="deputyContent">Deputy Principal Officer(Optional)</a></h4>
        </div>
        <div class="panel-collapse collapse" id="deputyContent" role="tabpanel" aria-labelledby="headingDeputy">
          <div class="panel-body">
            <%--<p class="text-right"><a href="application-premises.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>--%>
            <div class="panel-main-content">
              <div class="principalOfficers">
                <h2>Deputy Principal Officer</h2>
                <div class="row"></div>
                <div class="row"></div>
                <div class="row"></div>
                <div class="row"></div>
                <div class="row"></div>
                <div class="row"></div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</div>

<script>
    $('#cgoSelect').change(function () {
        var selectVal = $(this).val();
        $('.principalOfficers').removeClass('hidden');
    });

</script>
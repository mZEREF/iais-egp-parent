<div class="form-check-gp">
    <p class="form-check-title">Please select the type of update:</p>

    <div class="form-check progress-step-check" style="width: 900px">
        <input class="form-check-input" id="amendLicence3_1"
               type="radio" name="amendLicenceType3"
               aria-invalid="false">
        <label class="form-check-label" for="amendLicence3_1">
            <span class="check-circle"></span>
            <span class="left-content">Change in licensee</span>
        </label>
    </div>
    <div class="form-check progress-step-check" style="width: 900px">
        <input class="form-check-input" data-toggle="modal" data-target="#SoloCompany"
               type="radio" name="amendLicenceType3"
               >
        <label class="form-check-label" >
            <span class="check-circle"></span>
            <span class="left-content">Change in licensee's information</span>
        </label>
    </div>
</div>
<div class="modal fade" id="SoloCompany" role="dialog" aria-labelledby="myModalLabel" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            </div>
            <input class="form-check-input" id="amendLicence3_2"
                   type="radio"
                   aria-invalid="false">
            <label class="form-check-label" for="amendLicence3_2">
                <span class="check-circle"></span>
                <span class="left-content">Solo</span>
            </label>
            <br>
            <input class="form-check-input" id="amendLicence3_3"
                   type="radio"
                   aria-invalid="false">
            <label class="form-check-label" for="amendLicence3_3">
                <span class="check-circle"></span>
                <span class="left-content">Company</span>
            </label>
        </div>
    </div>
</div>

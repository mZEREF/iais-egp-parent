<%--@elvariable id="organizationAddress" type="sg.gov.moh.iais.egp.bsb.dto.info.common.OrgAddressInfo"--%>
<h3 class="col-12 pl-0" style="border-bottom: 1px solid black">Company Profile</h3>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>UEN</label>
    </div>
    <div class="col-sm-6 col-md-7">
        <label>${organizationAddress.uen}</label>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Company Name</label>
    </div>
    <div class="col-sm-6 col-md-7">
        <label>${organizationAddress.compName}</label>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Postal Code</label>
    </div>
    <div class="col-sm-6 col-md-7">
        <label>${organizationAddress.postalCode}</label>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Address Type</label>
    </div>
    <div class="col-sm-6 col-md-7">
        <label><iais:code code="${organizationAddress.addressType}"/></label>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Block / House No</label>
    </div>
    <div class="col-sm-6 col-md-7">
        <label>${organizationAddress.blockNo}</label>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Floor No.</label>
    </div>
    <div class="col-sm-6 col-md-7">
        <label>${organizationAddress.floor}</label>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Unit No.</label>
    </div>
    <div class="col-sm-6 col-md-7">
        <label>${organizationAddress.unitNo}</label>
    </div>
</div>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label>Street Name</label>
    </div>
    <div class="col-sm-6 col-md-7">
        <label>${organizationAddress.street}</label>
    </div>
</div>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisCCConstant" %>
<%
    String webroot1=IaisCCConstant.FE_CSS_ROOT;
%>
<webui:setLayout name="iais-cc"/>


<div class="dashboard" style="background-image:url('<%=webroot1%>img/Masthead-banner.jpg')">
    <div class="container">
        <div class="navigation-gp">
            <div class="row">
                <div class="col-xs-10 col-xs-offset-1 col-lg-offset-9 col-lg-3">
                    <div class="dropdown profile-dropdown"><a class="profile-btn btn" id="profileBtn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" href="javascript:;">Tan Mei Ling Joyce</a>
                        <ul class="dropdown-menu" aria-labelledby="profileBtn">
                            <li class="dashboard-icon"><a href="#">Dashboard</a></li>
                            <li class="management-account"><a href="#">Manage Account</a></li>
                            <li class="logout"><a href="#">Logout</a></li>
                        </ul>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="dashboard-page-title">
                        <h1>New Licence Application</h1>
                        <h3>You are applying for <b>Clinical Laboratory</b> | <b>Blood Banking</b></h3>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div class="tab-gp steps-tab">
                    <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                        <li class="active" role="presentation"><a href="#premisesTab" aria-controls="premisesTab" role="tab" data-toggle="tab">Premises</a></li>
                        <li class="complete" role="presentation"><a href="#documentsTab" aria-controls="documentsTab" role="tab" data-toggle="tab">Primary <br> Documents</a></li>
                        <li class="incomplete" role="presentation"><a href="#serviceInformationTab" aria-controls="serviceInformationTab" role="tab" data-toggle="tab">Service-Related <br> Information</a></li>
                        <li role="presentation"><a href="#previewTab" aria-controls="previewTab" role="tab" data-toggle="tab">Preview & Submit</a></li>
                        <li class="disabled" role="presentation"><a href="#paymentTab" aria-controls="paymentTab" role="tab" data-toggle="tab">Payment</a></li>
                    </ul>
                    <div class="tab-nav-mobile visible-xs visible-sm">
                        <div class="swiper-wrapper" role="tablist">
                            <div class="swiper-slide"><a href="#premisesTab" aria-controls="tabInbox" role="tab" data-toggle="tab">Premises</a></div>
                            <div class="swiper-slide"><a href="#documentsTab" aria-controls="tabApplication" role="tab" data-toggle="tab">Primary Documents</a></div>
                            <div class="swiper-slide"><a href="#serviceInformationTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Service-Related Information</a></div>
                            <div class="swiper-slide"><a href="#previewTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Preview & Submit</a></div>
                            <div class="swiper-slide"><a href="#paymentTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Payment</a></div>
                        </div>
                        <div class="swiper-button-prev"></div>
                        <div class="swiper-button-next"></div>
                    </div>
                    <div class="tab-content">
                        <div class="tab-pane active" id="premisesTab" role="tabpanel">
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="premises-txt">
                                        <p>Premises are your service operation sites that can either be at a fixed address – <b>&#34;on-site&#34;</b>, or in a mobile clinic or ambulance – <b>&#34;conveyance&#34;</b>.</p>
                                    </div>
                                </div>
                                <div class="col-xs-12">
                                    <form class="form-horizontal">
                                        <div class="form-group" id="premisesType">
                                            <label class="col-xs-12 col-md-4 control-label" for="premisesType">What is your premises type?</label>
                                            <div class="col-xs-6 col-md-2">
                                                <div class="form-check">
                                                    <input class="form-check-input" id="premise_onsite" type="radio" name="premisesType" aria-invalid="false">
                                                    <label class="form-check-label" for="premise_onsite"><span class="check-circle"></span>On-site</label>
                                                </div>
                                            </div>
                                            <div class="col-xs-6 col-md-2">
                                                <div class="form-check">
                                                    <input class="form-check-input" id="premise_conveyance" type="radio" name="premisesType" aria-invalid="false">
                                                    <label class="form-check-label" for="premise_conveyance"><span class="check-circle"></span>Conveyance</label>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-group premiseLocationSelect hidden">
                                            <label class="col-xs-12 col-md-4 control-label" for="premisesSelect">Add or select a premises from the list</label>
                                            <div class="col-xs-11 col-sm-7 col-md-5">
                                                <select id="premisesSelect">
                                                    <option>Select One</option>
                                                    <option value="newPremise">Add a new premises</option>
                                                    <option>111 North Bridge Rd # 07-04, 179098</option>
                                                    <option>514 Chai Chee Lane # 06-03, 65432</option>
                                                    <option>8 Foch Rd, 209786</option>
                                                    <option>400 Orchard Rd, 21-06 Orchard Tower, 23654</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group vehicleSelectForm hidden">
                                            <label class="col-xs-12 col-md-4 control-label" for="vehicleSelect">Select a vehicle</label>
                                            <div class="col-xs-8 col-sm-5 col-md-3">
                                                <select id="vehicleSelect">
                                                    <option>Select One</option>
                                                    <option>SBS-1234-A</option>
                                                    <option>SGP-1872-U</option>
                                                </select>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="premises-summary hidden">
                                        <h3 class="without-header-line">Premises Summary</h3>
                                        <p class="premise-address-gp"> <span class="premise-type"><b>On-site: </b></span><span class="premise-address"></span></p>
                                        <p class="vehicle-txt hidden"><b>Vehicle No:</b> <span class="vehicle-info"></span></p>
                                    </div>
                                    <div class="new-premise-form-on-site hidden">
                                        <form class="form-horizontal">
                                            <div class="form-group">
                                                <label class="col-xs-10 col-md-4 control-label" for="sitePremiseName">Name of premises</label>
                                                <div class="col-xs-10 col-sm-7 col-md-6">
                                                    <input id="sitePremiseName" type="text">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="sitePostalCode">Postal Code</label>
                                                <div class="col-xs-5 col-sm-4 col-md-2">
                                                    <input id="sitePostalCode" type="text">
                                                </div>
                                                <div class="col-xs-7 col-sm-6 col-md-4">
                                                    <p><a href="#">Retrieve your address</a></p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="siteBlockNo">Block / House No.</label>
                                                <div class="col-xs-5 col-sm-4 col-md-2">
                                                    <input id="siteBlockNo" type="text">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-10 col-md-4 control-label" for="siteStreetName">Street Name</label>
                                                <div class="col-xs-10 col-sm-7 col-md-5">
                                                    <input id="siteStreetName" type="text">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="siteFloorNo">Floor No.</label>
                                                <div class="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                    <input id="siteFloorNo" type="text">
                                                    <p class="small-txt">(Optional)</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="siteUnitNo">Unit No.</label>
                                                <div class="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                    <input id="siteUnitNo" type="text">
                                                    <p class="small-txt">(Optional)</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="siteBuildingName">Building Name</label>
                                                <div class="col-xs-11 col-sm-7 col-md-6 input-with-label">
                                                    <input id="siteBuildingName" type="text">
                                                    <p class="small-txt">(Optional)</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="siteAddressType">Address Type</label>
                                                <div class="col-xs-7 col-sm-4 col-md-3">
                                                    <select id="siteAddressType">
                                                        <option>Select address type</option>
                                                        <option>Apt Blk</option>
                                                        <option>Without Apt Blk</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="siteSafefyNo">Fire Safety Shelter Bureau Ref. No. <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" title="&lt;p&gt;This is a xxx digit No. that you can access from the Life Saving Force Portral.&lt;/p&gt;">i</a></label>
                                                <div class="col-xs-9 col-sm-5 col-md-4">
                                                    <input id="siteSafefyNo" type="text">
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="new-premise-form-conveyance hidden">
                                        <form class="form-horizontal">
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="vehicleNo">Vehicle No.</label>
                                                <div class="col-xs-9 col-sm-7 col-md-6">
                                                    <input id="vehicleNo" type="text">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="vehicleOwnerName">Vehicle Owner's Name</label>
                                                <div class="col-xs-4 col-sm-2 col-md-2">
                                                    <select id="vehicleOwnerNameSalutation" aria-label="vehicleOwnerNameSalutation">
                                                        <option>Mr</option>
                                                        <option>Mrs</option>
                                                        <option>Ms</option>
                                                        <option>Mam</option>
                                                    </select>
                                                </div>
                                                <div class="col-xs-7 col-sm-4 col-md-5">
                                                    <input id="vehicleOwnerName" type="text">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="conveyancePostalCode">Postal Code</label>
                                                <div class="col-xs-5 col-sm-4 col-md-2">
                                                    <input id="conveyancePostalCode" type="text">
                                                </div>
                                                <div class="col-xs-7 col-sm-6 col-md-4">
                                                    <p><a href="#">Retrieve your address</a></p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="conveyanceBlockNo">Block / House No.</label>
                                                <div class="col-xs-5 col-sm-4 col-md-2">
                                                    <input id="conveyanceBlockNo" type="text">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-10 col-md-4 control-label" for="conveyanceStreetName">Street Name</label>
                                                <div class="col-xs-10 col-sm-7 col-md-5">
                                                    <input id="conveyanceStreetName" type="text">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="conveyanceFloorNo">Floor No.</label>
                                                <div class="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                    <input id="conveyanceFloorNo" type="text">
                                                    <p class="small-txt">(Optional)</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="conveyanceUnitNo">Unit No.</label>
                                                <div class="col-xs-7 col-sm-4 col-md-3 input-with-label">
                                                    <input id="conveyanceUnitNo" type="text">
                                                    <p class="small-txt">(Optional)</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="conveyanceBuildingName">Building Name </label>
                                                <div class="col-xs-11 col-sm-7 col-md-6 input-with-label">
                                                    <input id="conveyanceBuildingName" type="text">
                                                    <p class="small-txt">(Optional)</p>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="conveyanceAddressType">Address Type</label>
                                                <div class="col-xs-7 col-sm-4 col-md-3">
                                                    <select id="conveyanceAddressType">
                                                        <option>Select address type</option>
                                                        <option>Apt Blk</option>
                                                        <option>Without Apt Blk</option>
                                                        <option>Without Apt Blk</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="conveyanceEmail">Email Address </label>
                                                <div class="col-xs-9 col-sm-5 col-sm-4">
                                                    <input id="conveyanceEmail" type="email">
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-xs-12 col-md-4 control-label" for="conveyanceMobile">Mobile number </label>
                                                <div class="col-xs-7 col-sm-4 col-md-3">
                                                    <input id="conveyanceMobile" type="number">
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <p><a class="back" href="#"><i class="fa fa-angle-left"></i> Back</a></p>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <div class="button-group"><a class="btn btn-secondary" href="#">Save as Draft</a><a class="btn btn-primary next" href="application-document.html">Next</a></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="documentsTab" role="tabpanel">
                            <div class="document-info-list">
                                <ul>
                                    <li>
                                        <p>The maximum file size for each upload is 4MB. </p>
                                    </li>
                                    <li>
                                        <p>Acceptable file formats are PDF, JPG and PNG. </p>
                                    </li>
                                    <li>
                                        <p>All files are mandatory.</p>
                                    </li>
                                </ul>
                            </div>
                            <div class="document-upload-gp">
                                <h2>PRIMARY DOCUMENTS</h2>
                                <div class="document-upload-list">
                                    <h3>Fire Safety Certificate (FSC) from SCDF</h3>
                                    <p><a href="#" target="_blank">Preview</a></p>
                                </div>
                                <div class="document-upload-list">
                                    <h3>Urban Redevelopmenet Authority (URA) grant of written permission</h3>
                                    <div class="file-upload-gp">
                                        <input id="selectedFile1" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                                    </div>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <p><a class="back" href="application-premises.html"><i class="fa fa-angle-left"></i> Back</a></p>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <div class="button-group"><a class="btn btn-secondary" href="#">Save as Draft</a><a class="btn btn-primary next" href="application-service-related-clinical-lab-lab-discipline.html">Next</a></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="serviceInformationTab" role="tabpanel">
                            <div class="multiservice">
                                <div class="tab-gp side-tab clearfix">
                                    <ul class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
                                        <li class="active" role="presentation"><a href="#clinicalLab" aria-controls="lorem1" role="tab" data-toggle="tab">Clinical Laboratory</a></li>
                                        <li class="complete" role="presentation"><a href="#bloodBanking" aria-controls="lorem2" role="tab" data-toggle="tab">Blood Banking</a></li>
                                    </ul>
                                    <div class="mobile-side-nav-tab visible-xs visible-sm">
                                        <select id="serviceSelect">
                                            <option value="clinicalLab">Clinical Laboratory</option>
                                            <option value="bloodBanking">Blood Banking</option>
                                        </select>
                                    </div>
                                    <div class="tab-content">
                                        <div class="tab-pane active" id="clinicalLab" role="tabpanel">
                                            <h2 class="service-title">SERVICE 1 OF 2: <b>CLINICAL LABORATORY</b></h2>
                                            <div class="visible-xs visible-sm servive-subtitle">
                                                <p>Step 1 of 5</p>
                                                <h3>Header</h3>
                                            </div>
                                            <ul class="progress-tracker">
                                                <li class="tracker-item active" data-service-step="laboratory-disciplines">Laboratory Disciplines</li>
                                                <li class="tracker-item disabled" data-service-step="clinical-governance-officer">Clinical Governance Officers</li>
                                                <li class="tracker-item disabled" data-service-step="discipline-allocation">Discipline Allocation</li>
                                                <li class="tracker-item disabled" data-service-step="principal-officers">Principal Officers</li>
                                                <li class="tracker-item disabled">Documents</li>
                                            </ul>
                                            <div class="application-service-steps">
                                                <div class="laboratory-disciplines">
                                                    <h2>Laboratory Disciplines</h2>
                                                    <p>Please select the service disciplines you would like to apply at your premises.</p>
                                                    <iframe id="__egovform-iframe" name="__egovform-iframe" frameborder="no" width="100%" heihgt="100%" scrolling="no" src="cr_form/CR_Discipline.html"></iframe>
                                                </div>
                                                <div class="clinical-governance-officer">
                                                    <iframe id="__egovform-iframe" name="__egovform-iframe" frameborder="no" width="100%" heihgt="100%" scrolling="no" src="cr_form/CR_Form2.html"></iframe>
                                                </div>
                                                <div class="discipline-allocation">
                                                    <h2>Discipline Allocation</h2>
                                                    <p>Please ensure that each laboratory discipline is assigned to a clinical governance officer.</p>
                                                    <div class="table-gp">
                                                        <table class="table discipline-table">
                                                            <thead>
                                                            <tr>
                                                                <th>Premises</th>
                                                                <th>Laboratory Disciplines</th>
                                                                <th>Clinical Governance Officers</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <tr>
                                                                <td rowspan="4">
                                                                    <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                                    <p>16 Raffles Quay # 01-03, 048581</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                                                    <p>Cytology</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                                                    <select class="table-select officer-allocation-select" id="officerAllocationSelect1" aria-labelledby="officerAllocationSelect1">
                                                                        <option>Option 1</option>
                                                                        <option>Option 2</option>
                                                                        <option>Option 3</option>
                                                                        <option>Option 4</option>
                                                                        <option>Option 5</option>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                                                    <p>HIV Testing</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                                                    <select class="table-select officer-allocation-select" id="officerAllocationSelect2" aria-labelledby="officerAllocationSelect2">
                                                                        <option>Option 1</option>
                                                                        <option>Option 2</option>
                                                                        <option>Option 3</option>
                                                                        <option>Option 4</option>
                                                                        <option>Option 5</option>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                                                    <p>Medical Microbiology</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                                                    <select class="table-select officer-allocation-select" id="officerAllocationSelect3" aria-labelledby="officerAllocationSelect3">
                                                                        <option>Option 1</option>
                                                                        <option>Option 2</option>
                                                                        <option>Option 3</option>
                                                                        <option>Option 4</option>
                                                                        <option>Option 5</option>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Laboratory Disciplines</p>
                                                                    <p>Immunology</p>
                                                                </td>
                                                                <td>
                                                                    <p class="visible-xs visible-sm table-row-title">Clinical Governance Officers</p>
                                                                    <select class="table-select officer-allocation-select" id="officerAllocationSelect4" aria-labelledby="officerAllocationSelect4">
                                                                        <option>Option 1</option>
                                                                        <option>Option 2</option>
                                                                        <option>Option 3</option>
                                                                        <option>Option 4</option>
                                                                        <option>Option 5</option>
                                                                    </select>
                                                                </td>
                                                            </tr>
                                                            </tbody>
                                                        </table>
                                                        <p>Click <a href="#">here</a> to assign a laboratory discipline to multiple clinical governance officers.</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="tab-pane" id="bloodBanking" role="tabpanel"></div>
                                    </div>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6"><a class="back" href="application-document.html"><i class="fa fa-angle-left"></i> Back</a></div>
                                    <div class="col-xs-12 col-sm-6">
                                        <div class="button-group"><a class="btn btn-secondary" href="#">Save as Draft</a><a class="next btn btn-primary disabled hidden" href="application-preview.html"><i class="fa fa-angle-left"></i>) Next</a><a class="next btn btn-primary disabled" data-goto="clinical-governance-officer">Next</a></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="previewTab" role="tabpanel">
                            <div class="preview-gp">
                                <div class="row">
                                    <div class="col-xs-12 col-md-10">
                                        <p>Please note that you will not be able to pay for this application if you have not provided the mandatory information and documents.</p>
                                    </div>
                                    <div class="col-xs-12 col-md-2 text-right">
                                        <p class="print"><a href="#"> <i class="fa fa-print"></i>Print</a></p>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                                            <div class="panel panel-default">
                                                <div class="panel-heading completed" id="headingPremise" role="tab">
                                                    <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapsePremise" aria-expanded="true" aria-controls="collapsePremise">Premises</a></h4>
                                                </div>
                                                <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel" aria-labelledby="headingPremise">
                                                    <div class="panel-body">
                                                        <p class="text-right"><a href="application-premises.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>
                                                        <div class="panel-main-content">
                                                            <div class="preview-info">
                                                                <p><b>Premises</b></p>
                                                                <p>On-site: 16 Raffles Quay #01-03 Hong Leong Building, 048581</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="panel panel-default">
                                                <div class="panel-heading completed" id="headingOne" role="tab">
                                                    <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">Primary Documents</a></h4>
                                                </div>
                                                <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne">
                                                    <div class="panel-body">
                                                        <p class="text-right mb-0"><a href="application-document.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>
                                                        <iframe class="elemClass-1561088919456" src="cr_form/Confirm_upload.html" id="elemId-1561088919456" scrollbar="auto" style="height: 180px;" width="100%" height="100%" frameborder="0"></iframe>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="panel panel-default">
                                                <div class="panel-heading incompleted" id="headingServiceInfo" role="tab">
                                                    <h4 class="panel-title"><a role="button" data-toggle="collapse" href="#collapseServiceInfo" aria-expanded="true" aria-controls="collapseServiceInfo">Service Related Information</a></h4>
                                                </div>
                                                <div class="panel-collapse collapse in" id="collapseServiceInfo" role="tabpanel" aria-labelledby="headingServiceInfo">
                                                    <div class="panel-body">
                                                        <p class="text-right mb-0"><a href="application-service-related-clinical-lab-lab-discipline.html"><i class="fa fa-pencil-square-o"></i>Edit</a></p>
                                                        <iframe class="elemClass-1561088919456" src="cr_form/Confirm_Form.html" id="elemId-1561088919456" scrollbar="auto" style="height: 955px;" width="100%" height="100%" frameborder="0"></iframe>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" id="verifyInfoCheckbox" type="checkbox" name="verifyInfoCheckbox" aria-invalid="false">
                                            <label class="form-check-label" for="verifyInfoCheckbox"><span class="check-square"></span>Herby I certify that the information I provided is all correct and accurate</label>
                                        </div>
                                        <div class="form-check">
                                            <input class="form-check-input" id="declarationCheckbox" type="checkbox" name="declarationCheckbox" aria-invalid="false">
                                            <label class="form-check-label" for="declarationCheckbox"><span class="check-square"></span>&lt;Sample Declaration&gt;</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="application-tab-footer">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-6">
                                        <p><a class="back" href="application-service-related-clinical-lab-lab-discipline.html"><i class="fa fa-angle-left"></i> Back</a></p>
                                    </div>
                                    <div class="col-xs-12 col-sm-6">
                                        <div class="button-group"><a class="btn btn-secondary" href="#">Save as Draft</a><a class="next btn btn-primary disabled" href="#">SUBMIT & PAY </a></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="tab-pane" id="paymentTab" role="tabpanel"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="officerAmend" tabindex="-1" role="dialog" aria-labelledby="officerAmend">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header"><a class="modal-close" data-dismiss="modal" aria-label="Close"></a></div>
            <div class="modal-body">
                <h3>Edit Personnel's Details?</h3>
                <p>This application will be saved as a draft for you to continue later.</p>
                <div class="button-group modal-footer-button-group"><a data-dismiss="modal">Close</a><a class="btn btn-primary" href="#">PROCEED TO AMEND</a></div>
            </div>
        </div>
    </div>
</div>


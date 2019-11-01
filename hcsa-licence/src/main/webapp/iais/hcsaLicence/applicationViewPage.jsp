<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-cc"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" id = "premisesTypeValue" value="${appGrpPremisesDto.premisesType}">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="./navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tabInbox" role="tabpanel">

                            </div>
                            <div class="tab-pane" id="tabApplication" role="tabpanel">
                                <div class="tab-search">
                                    <form class="form-inline">
                                        <div class="form-group">
                                            <label class="control-label" for="applicationType">Type</label>
                                            <div class="col-xs-12 col-md-8 col-lg-9">
                                                <select id="applicationType">
                                                    <option>Select an type</option>
                                                    <option selected>All</option>
                                                    <option>Renewal</option>
                                                    <option>New Licence</option>
                                                    <option>Group</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label" for="applicationStatus">Status</label>
                                            <div class="col-xs-12 col-md-8 col-lg-9">
                                                <select id="applicationStatus">
                                                    <option>Select an status</option>
                                                    <option selected>All</option>
                                                    <option>Approved</option>
                                                    <option>Pending</option>
                                                    <option>Draft</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="form-group large right-side">
                                            <div class="search-wrap">
                                                <div class="input-group">
                                                    <input class="form-control" id="applicationAdvancedSearch" type="text" placeholder="Application no." name="applicationAdvancedSearch" aria-label="applicationAdvancedSearch"><span class="input-group-btn">
                              <button class="btn btn-default buttonsearch" title="Search by keywords"><i class="fa fa-search"></i></button></span>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Application No.</th>
                                                    <th>Type</th>
                                                    <th>Status</th>
                                                    <th>Service</th>
                                                    <th>Date Submitted <span class="sort"></span></th>
                                                    <th>Actions</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                        <p><a href="#">RW-2019-00004</a></p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Renewal</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Service</p>
                                                        <p>Tissue Banking</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Approved</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                                        <p>14 Feb 2019, 11:28</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title" for="selectApplication1">Actions</p>
                                                        <select class="table-select" id="selectApplication1" aria-label="selectApplication1">
                                                            <option>Select</option>
                                                            <option>Option one</option>
                                                            <option>Option two</option>
                                                            <option>Option three</option>
                                                            <option>Option four</option>
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                        <p><a href="#">RW-2019-00003</a></p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>New Licence Application</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Service</p>
                                                        <p>Blood Banking</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Pending</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                                        <p>05 Feb 2019, 09:24</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title" for="selectApplication2">Actions</p>
                                                        <select class="table-select" id="selectApplication2" aria-label="selectApplication2">
                                                            <option>Select</option>
                                                            <option>Option one</option>
                                                            <option>Option two</option>
                                                            <option>Option three</option>
                                                            <option>Option four</option>
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                        <p><a class="collapsed" data-toggle="collapse" data-target="tabelcollapseOne" href="javascript:;">RW-2019-00002</a></p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Group​​​​​​​ Application</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Service</p>
                                                        <p>Multiple</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Draft</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                                        <p>02 Feb 2019, 07:24</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title" for="selectApplication3">Actions</p>
                                                        <select class="table-select" id="selectApplication3" aria-label="selectApplication3">
                                                            <option>Select</option>
                                                            <option>Option one</option>
                                                            <option>Option two</option>
                                                            <option>Option three</option>
                                                            <option>Option four</option>
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr class="collapse" data-child-row="tabelcollapseOne">
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                        <p><a href="#">RW-2019-00002-01</a></p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>New Application</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Service</p>
                                                        <p>Radiological Service</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Draft</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                                        <p>02 Feb 2019, 07:24</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title" for="selectApplication4">Actions</p>
                                                        <select class="table-select" id="selectApplication4" aria-label="selectApplication4">
                                                            <option>Select</option>
                                                            <option>Option one</option>
                                                            <option>Option two</option>
                                                            <option>Option three</option>
                                                            <option>Option four</option>
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr class="collapse" data-child-row="tabelcollapseOne">
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                        <p><a href="#">RW-2019-00002-01</a></p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>New Application</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Service</p>
                                                        <p>Nuclear Medicine</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Draft</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                                        <p>02 Feb 2019, 07:24</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title" for="selectApplication5">Actions</p>
                                                        <select class="table-select" id="selectApplication5" aria-label="selectApplication5">
                                                            <option>Select</option>
                                                            <option>Option one</option>
                                                            <option>Option two</option>
                                                            <option>Option three</option>
                                                            <option>Option four</option>
                                                        </select>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Application No.</p>
                                                        <p><a href="#">RW-2019-00001</a></p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>New Licence Application</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Service</p>
                                                        <pClinical>Laboratory</pClinical>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Approved</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Date Submitted</p>
                                                        <p>01 Feb 2019, 08:24</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title" for="selectApplication6">Actions</p>
                                                        <select class="table-select" id="selectApplication6" aria-label="selectApplication6">
                                                            <option>Select</option>
                                                            <option>Option one</option>
                                                            <option>Option two</option>
                                                            <option>Option three</option>
                                                            <option>Option four</option>
                                                        </select>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane" id="tabLicence" role="tabpanel">
                                <div class="tab-search license-search clearfix">
                                    <div class="licence-btns"><a class="btn btn-primary disabled" href="javascript:;">Renew</a><a class="btn btn-secondary disabled" href="javascript:;">Cease</a><a class="btn btn-secondary disabled" href="javascript:;">Amend</a></div>
                                    <div class="search-wrap">
                                        <div class="input-group">
                                            <input class="form-control" id="licenseAdvancedSearcch" type="text" placeholder="Licence no." name="licenseAdvancedSearcch" aria-label="licenseAdvancedSearcch"><span class="input-group-btn">
                          <button class="btn btn-default buttonsearch" title="Search by keywords"><i class="fa fa-search"></i></button></span>
                                        </div>
                                    </div><a class="advanced-search" href="#">Advanced Search</a>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>Licence No.</th>
                                                    <th>Type <span class="sort"></span></th>
                                                    <th>Status <span class="sort"></span></th>
                                                    <th>Premises <span class="sort"></span></th>
                                                    <th>Start Date <span class="desc"></span></th>
                                                    <th>Expiry Date <span class="sort"></span></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check">
                                                            <input class="form-check-input licenceCheck" id="licence1" type="checkbox" name="licence1" aria-invalid="false">
                                                            <label class="form-check-label" for="licence1"><span class="check-square"></span><a href="#">RW-2019-00004</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Clinical Laboratory</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Active</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p>111 North Bridge Rd. <br> # 07-04, 179098</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check">
                                                            <input class="form-check-input licenceCheck" id="licence2" type="checkbox" name="licence2" aria-invalid="false">
                                                            <label class="form-check-label" for="licence2"><span class="check-square"></span><a href="#">RW-2019-00003</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Nuclear Medicine</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Active</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p>16 Raffles Quay <br> # 01-03, 048581</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check disabled">
                                                            <input class="form-check-input licenceCheck" id="licence3" type="checkbox" name="licence3" aria-invalid="false">
                                                            <label class="form-check-label" for="licence3"><span class="check-square"></span><a href="#">RW-2019-00002</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Tissue Banking</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Expired</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p><a class="collapsed" data-toggle="collapse" href="#serviceCollapse" aria-expanded="false" aria-controls="serviceCollapse">Multiple</a></p>
                                                        <div class="collapse" id="serviceCollapse">
                                                            <ul>
                                                                <li>16 Raffles Quay <br> # 01-03, 048581</li>
                                                                <li>111 North Bridge Rd. <br> # 07-04, 179098</li>
                                                                <li>568 Raffles Place. <br> # 08-01, 589098</li>
                                                            </ul>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check disabled">
                                                            <input class="form-check-input licenceCheck" id="licence4" type="checkbox" name="licence4" aria-invalid="false">
                                                            <label class="form-check-label" for="licence4"><span class="check-square"></span><a href="#">RW-2019-00001</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Blood Banking</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Expired</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p>16 Raffles Quay <br> # 01-03, 048581</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check">
                                                            <input class="form-check-input licenceCheck" id="licence5" type="checkbox" name="licence5" aria-invalid="false">
                                                            <label class="form-check-label" for="licence5"><span class="check-square"></span><a href="#">RW-2019-00004</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Clinical Laboratory</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Active</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p>111 North Bridge Rd. <br> # 07-04, 179098</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check">
                                                            <input class="form-check-input licenceCheck" id="licence6" type="checkbox" name="licence6" aria-invalid="false">
                                                            <label class="form-check-label" for="licence6"><span class="check-square"></span><a href="#">RW-2019-00003</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Nuclear Medicine</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Active</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p>16 Raffles Quay <br> # 01-03, 048581</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check">
                                                            <input class="form-check-input licenceCheck" id="licence7" type="checkbox" name="licence7" aria-invalid="false">
                                                            <label class="form-check-label" for="licence7"><span class="check-square"></span><a href="#">RW-2019-00004</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Clinical Laboratory</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Active</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p>111 North Bridge Rd. <br> # 07-04, 179098</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check">
                                                            <input class="form-check-input licenceCheck" id="licence8" type="checkbox" name="licence8" aria-invalid="false">
                                                            <label class="form-check-label" for="licence8"><span class="check-square"></span><a href="#">RW-2019-00003</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Nuclear Medicine</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Active</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p>16 Raffles Quay <br> # 01-03, 048581</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021			</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check disabled">
                                                            <input class="form-check-input licenceCheck" id="licence9" type="checkbox" name="licence9" aria-invalid="false">
                                                            <label class="form-check-label" for="licence9"><span class="check-square"></span><a href="#">RW-2019-00001</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Blood Banking</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Expired</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p>16 Raffles Quay <br> # 01-03, 048581</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021</p>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                                        <div class="form-check disabled">
                                                            <input class="form-check-input licenceCheck" id="licence10" type="checkbox" name="licence10" aria-invalid="false">
                                                            <label class="form-check-label" for="licence10"><span class="check-square"></span><a href="#">RW-2019-00001</a></label>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Type</p>
                                                        <p>Blood Banking</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Status</p>
                                                        <p>Expired</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Premises</p>
                                                        <p>16 Raffles Quay <br> # 01-03, 048581</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Start Date</p>
                                                        <p>14 Feb 2019</p>
                                                    </td>
                                                    <td>
                                                        <p class="visible-xs visible-sm table-row-title">Expiry Date</p>
                                                        <p>14 Feb 2021</p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    $(document).ready(function() {
        var premisesTypeValue = $('#premisesTypeValue').val();
        if('<%=ApplicationConsts.PREMISES_TYPE_ON_SITE%>'==premisesTypeValue){
            $('#premise_onsite').attr("checked","checked");
            $('#premise_conveyance').removeAttr("checked");
            $('.premiseLocationSelect').removeClass('hidden');
            $('.premises-summary, .new-premise-form-on-site, .new-premise-form-conveyance, .vehicleSelectForm').addClass('hidden');
        }else if('<%=ApplicationConsts.PREMISES_TYPE_CONVEYANCE%>'==premisesTypeValue){
            $('#premise_conveyance').attr("checked","checked");
            $('#premise_onsite').removeAttr("checked");
            $('.premiseLocationSelect').removeClass('hidden');
            $('.premises-summary, .new-premise-form-on-site, .new-premise-form-conveyance, .vehicleSelectForm').addClass('hidden');
        }
        premisesSelectChange();

        //Binding method
        $('#premiseId').click(function(){
            submit('documents',null,null);
        });
        $('#premiseSaveDraft').click(function(){
            submit('premises','saveDraft',null);
        });
    });


</script>




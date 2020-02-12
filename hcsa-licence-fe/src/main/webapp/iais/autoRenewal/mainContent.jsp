<div class="main-content">
    <form class="form-horizontal" method="post" id="MasterCodeForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <div class="main-content">
            <div class="container">
                <div class="row">
                    <br/>
                    <br/>
                    <br/>
                    <div class="col-xs-12">
                        <div class="tab-gp dashboard-tab">
                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                <li class="active" role="presentation"><a href="#tabIns" aria-controls="tabIns" role="tab"
                                                                          data-toggle="tab">Instructions</a></li>
                                <li class="complete" role="presentation"><a href="#tabLicRe"
                                                                            aria-controls="tabLicRe" role="tab"
                                                                            data-toggle="tab">Licence Review</a></li>
                                <li class="complete" role="presentation"><a href="#tabPay"
                                                                            aria-controls="tabPay" role="tab"
                                                                            data-toggle="tab">Payment</a></li>
                                <li class="incomplete" role="presentation"><a href="#tabAck"
                                                                              aria-controls="tabAck" role="tab"
                                                                              data-toggle="tab">Ackownledgement</a></li>
                            </ul>
                            <div class="tab-nav-mobile visible-xs visible-sm">
                                <div class="swiper-wrapper" role="tablist">
                                    <div class="swiper-slide"><a href="#tabIns" aria-controls="tabIns" role="tab"
                                                                 data-toggle="tab">Instructions</a></div>
                                    <div class="swiper-slide"><a href="#tabLicRe" aria-controls="tabLicRe"
                                                                 role="tab" data-toggle="tab">Licence Review</a></div>

                                    <div class="swiper-slide"><a href="#tabPay" aria-controls="tabPay"
                                                                 role="tab" data-toggle="tab">Payment</a></div>
                                    <div class="swiper-slide"><a href="#tabAck" aria-controls="tabAck"
                                                                 role="tab" data-toggle="tab">Ackownledgement</a></div>
                                </div>
                                <div class="swiper-button-prev"></div>
                                <div class="swiper-button-next"></div>
                            </div>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tabIns" role="tabpanel">
                                    <div class="panel panel-default">
                                        <!-- Default panel contents -->
                                        <div class="text ">
                                            <p><span>Your licences to renew are listed below you are not allowed to make any changes to the licences:</span></p>
                                        </div>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="table-gp">
                                                    <table class="table table-bordered">
                                                        <tbody>
                                                        <tr>
                                                            <td class="col-xs-2"><b>Licence No.</b>></td>
                                                            <td class="col-xs-2"><b>Type</b>></td>
                                                            <td class="col-xs-4"><b>Premises</b>></td>
                                                            <td class="col-xs-2"><b>Start date</b>></td>
                                                            <td class="col-xs-2"><b>Expires on</b>></td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <div>
                                            <span><a>&lt Back</a></span>
                                            <input class="button" value="Proceed" align="center">
                                        </div>>
                                    </div>
                                </div>

                                              <div class="tab-pane" id="tabPay" role="tabpanel">

                                <div class="tab-pane" id="tabAck" role="tabpanel">
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h3>Submission successful</h3>
                                        </strong>
                                    </div>
                                    <form method="post" action=<%=process.runtime.continueURL()%>>
                                        <input type="hidden" name="sopEngineTabRef"
                                               value="<%=process.rtStatus.getTabRef()%>">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="table-gp">
                                                    <table class="table">
                                                        <tr>
                                                            <td class="col-xs-4"><p>Current Status:</p></td>
                                                            <td class="col-xs-8"><p>${applicationViewDto.currentStatus}</p>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td><span>Internal Remarks(</span><span
                                                                    style="color: red">*</span><span>):</span></td>
                                                            <td>
                                                                <div class="input-group">
                                                                    <div class="ax_default text_area">
                                                                    <textarea id="internalRemarksId"
                                                                              name="internalRemarks" cols="70"
                                                                              rows="7"></textarea>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <span>Processing Decision(</span><span
                                                                    style="color: red">*</span><span>):</span></span>
                                                            </td>
                                                            <td>
                                                                <select name="nextStage" class="table-select">
                                                                    <option>---select---</option>
                                                                    <option value="VERIFIED">Verified</option>
                                                                    <option value="ROLLBACK">Roll back</option>
                                                                </select>
                                                            </td>
                                                        </tr>
                                                        <tr id="verifiedDropdown" class="hidden">
                                                            <td>
                                                                <span>Verified</span>
                                                            </td>
                                                            <td>
                                                                <select name="verified" class="table-select">
                                                                    <option>---select---</option>

                                                                </select>
                                                            </td>
                                                        </tr>
                                                        <tr id="lienceStartDate" class="hidden">
                                                            <td>
                                                                <span>Lience Start Date</span>
                                                            </td>
                                                            <td>
                                                                <iais:datePicker id = "lienceStartDate" name = "tuc" value=""></iais:datePicker>
                                                            </td>
                                                        </tr>
                                                        <tr id="rollBackDropdown" class="hidden">
                                                            <td>
                                                                <span>Roll Back:</span>
                                                            </td>
                                                            <td>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                    <div align="center">
                                                        <button id="submitButton" type="submit" class="btn btn-primary">
                                                            Submit
                                                        </button>
                                                    </div>
                                                    <div>&nbsp;</div>
                                                </div>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4>Processing History</h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <thead>
                                                    <tr>
                                                        <th>Username</th>
                                                        <th>Working Group</th>
                                                        <th>Status Update</th>
                                                        <th>Remarks</th>
                                                        <th>Last Updated</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                        <tr>
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
</div>
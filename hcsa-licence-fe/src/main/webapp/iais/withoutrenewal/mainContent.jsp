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
                                                        <thead>
                                                        <tr>
                                                            <td class="col-xs-2"><b>Licence No.</b></td>
                                                            <td class="col-xs-2"><b>Type</b></td>
                                                            <td class="col-xs-4"><b>Premises</b></td>
                                                            <td class="col-xs-2"><b>Start date</b></td>
                                                            <td class="col-xs-2"><b>Expires on</b></td>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach items="${renewDto.appSubmissionDtos}"
                                                                   var="appSubmissionDtos">
                                                        <tr>
                                                            <td>${appSubmissionDtos.licenceNo}</td>
                                                            <td>${appSubmissionDtos.serviceName}</td>
                                                            <td><c:forEach items="${appSubmissionDtos.appGrpPremisesDtoList}" var="appGrpPremisesDtoList"><span>${appGrpPremisesDtoList.address}</span><br/></c:forEach></td>
                                                            <td>${appSubmissionDtos.licStartDate}</td>
                                                            <td>${appSubmissionDtos.licExpiryDate}</td>
                                                        </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-xs-12 col-md-4">
                                            <a class="back" href="https://egp.sit.inter.iais.com/hcsa-licence-web/eservice/INTERNET/MohRequestForChange?licenceId=${prepareTranfer.licenceId}"><em class="fa fa-angle-left"></em> Back</a>
                                        </div>
                                        <div class="col-xs-12 col-md-3">
                                            <a class="btn btn-primary next premiseId" id="Next">Proceed</a>
                                        </div>
                                    </div>
                                </div>

                                <div class="tab-pane active" id="tabLicRe" role="tabpanel">
                                    <c:forEach items="${renewDto.appSubmissionDtos}"
                                               var="appSubmissionDtos">
                                        <h3>Clinical Laboratory;Licence No${appSubmissionDtos.licenceNo}</h3>
                                    </c:forEach>
                                </div>

                                <div class="tab-pane" id="tabPay" role="tabpanel">
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="tab-gp steps-tab">
                                                <div class="tab-content">

                                                    <div class="tab-pane active" id="paymentTab" role="tabpanel">
                                                        <br/>
                                                        <h2>Payment Summary</h2>
                                                        <p >
                                                            Total amount due:
                                                            <c:out value="${AppSubmissionDto.amountStr}"></c:out>
                                                        </p>
                                                        <table class="table">
                                                            <thead>
                                                            <tr>
                                                                <th>Service</th>
                                                                <th>Application Type</th>
                                                                <th>Application No.</th>
                                                                <th>Amount</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <c:forEach var="appSubmissionDtos" items="${renewDto.appSubmissionDtos}">
                                                            <c:forEach var="svc" items="${appSubmissionDtos.appSvcRelatedInfoDtoList}">
                                                                <tr>
                                                                    <td>
                                                                        <p><c:out value="${AppSubmissionDto.serviceName}"></c:out></p>
                                                                    </td>
                                                                    <td>
                                                                        <p>Amendment</p>
                                                                    </td>
                                                                    <td>
                                                                        <p><c:out value="${AppSubmissionDto.appGrpNo}"></c:out></p>
                                                                    </td>
                                                                    <td>
                                                                        <p><c:out value="${AppSubmissionDto.amountStr}"></c:out></p>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                            </c:forEach>
                                                            </tbody>
                                                        </table>
                                                        <h2>Payment Method</h2>
                                                        <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="Credit">
                                                        <label class="form-check-label" ><span class="check-circle"></span>Credit/Debit Card</label>&nbsp&nbsp&nbsp&nbsp
                                                        <input class="form-check-input premTypeRadio"  type="radio" name="payMethod" value="GIRO">
                                                        <label class="form-check-label" ><span class="check-circle"></span>GIRO</label>
                                                        <span name="iaisErrorMsg" id="error_pay" class="error-msg"></span>
                                                        <br>

                                                        &nbsp&nbsp&nbsp&nbsp<img src="<%=webroot1%>img/mastercard.png" width="40" height="25" alt="mastercard">&nbsp
                                                        <img src="<%=webroot1%>img/paymentVISA.png" width="66" height="25" alt="VISA">&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                                                        <img src="<%=webroot1%>img/payments.png" width="36" height="30" alt="GIRO">
                                                        <p class="visible-xs visible-sm table-row-title">Proceed</p>
                                                        <p id="previewAndSub" class="text-right text-center-mobile"><iais:input type="button" id="proceed" cssClass="proceed btn btn-primary" value="Proceed"></iais:input></p>
                                                    </div>

                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="tab-pane" id="tabAck" role="tabpanel">
                                    <h2><b>Submission successful</b></h2>
                                    <h3><b>-Clinical Laboratory</b>(Renewal+Amendment)</h3>
                                    <h3><b>-Clinical Laboratory</b>(Amendment)</h3>
                                    <br>
                                    <h4>A confirmation email will be sent to xxxxx</h4>
                                    <br>
                                    <h4>We will review your application and notify you if any changes are required.</h4>
                                    <h4>An inspection date will be arranged if necessary.</h4>
                                </div>
                                </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="main-content">
    <form class="form-horizontal" method="post" id="MasterCodeForm" action=<%=process.runtime.continueURL()%>>
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
                                <li id="licReLi" class="complete" role="presentation"><a id="licReA" href="#tabLicRe"
                                                                            aria-controls="tabLicRe" role="tab"
                                                                            data-toggle="tab">Licence Review</a></li>
                                <li class="complete" role="presentation"><a id="licPmt" href="#tabPay"
                                                                            aria-controls="tabPay" role="tab"
                                                                            data-toggle="tab">Payment</a></li>
                                <li class="incomplete" role="presentation"><a href="#tabAck"
                                                                              aria-controls="tabAck" role="tab"
                                                                              data-toggle="tab">Ackownledgement</a></li>
                            </ul>
                            <div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
                                <div class="swiper-wrapper" role="tablist">
                                    <div class="swiper-slide"><a href="#tabIns" aria-controls="tabIns" role="tab"
                                                                 data-toggle="tab">Instructions</a></div>
                                    <div class="swiper-slide"><a href="#tabLi+cRe" aria-controls="tabLicRe"
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
                                                    <table aria-describedby="" class="table table-bordered">
                                                        <thead>
                                                        <tr>
                                                            <th scope="col" class="col-xs-2"><strong>Licence No.</strong></th>
                                                            <th scope="col" class="col-xs-2"><strong>Type</strong></th>
                                                            <th scope="col" class="col-xs-4"><strong>Mode of Service Delivery</strong></th>
                                                            <th scope="col" class="col-xs-2"><strong>Start date</strong></th>
                                                            <th scope="col" class="col-xs-2"><strong>Expires on</strong></th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach items="${renewDto.appSubmissionDtos}"
                                                                   var="appSubmissionDtos">
                                                        <tr>
                                                            <td>${appSubmissionDtos.licenceNo}</td>
                                                            <td>${appSubmissionDtos.serviceName}</td>
                                                            <td><c:forEach items="${appSubmissionDtos.appGrpPremisesDtoList}" var="appGrpPremisesDtoList"><span>${appGrpPremisesDtoList.renewPremises}</span><br/></c:forEach></td>
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

                                <form class="form-inline" method="post" id="menuListForm" action=<%=process.runtime.continueURL()%>>
                                    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                                    <input id="EditValue" type="hidden" name="EditValue" value="" />
                                </form>
                                <div class="tab-pane" id="tabLicRe" role="tabpanel">
                                    <c:forEach items="${renewDto.appSubmissionDtos}"
                                               var="appSubmissionDtos" >
                                        <h3>Clinical Laboratory;Licence No${appSubmissionDtos.licenceNo}</h3>
                                        <c:forEach items="${appSubmissionDtos.appGrpPremisesDtoList}"
                                                   var="appGrpPremisesDtoList" varStatus="status">
                                            <c:if test="${renewDto.appSubmissionDtos.size()==1}">
                                                <p class="text-right"><a href="#" id="premisesEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></p>
                                            </c:if>
                                            <STRONG>Premises${status.index+1}</STRONG><br/>
                                            <div>${appGrpPremisesDtoList.renewPremises}</div>
                                            <br/>
                                            <br/>
                                            <STRONG>Primary Documents${status.index+1}</STRONG>
                                            <div></div>
                                        </c:forEach>
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
                                                        <table aria-describedby="" class="table">
                                                            <thead>
                                                            <tr>
                                                                <th scope="col" >Service</th>
                                                                <th scope="col" >Application Type</th>
                                                                <th scope="col" >Application No.</th>
                                                                <th scope="col" >Amount</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <c:forEach var="appSubmissionDtos" items="${renewDto.appSubmissionDtos}">
                                                            <c:forEach var="svc" items="${appSubmissionDtos.appSvcRelatedInfoDtoList}">
                                                                <tr>
                                                                    <td>
                                                                        <p>${svc.serviceName}</p>
                                                                    </td>
                                                                    <td>
                                                                        <p>Amendment</p>
                                                                    </td>
                                                                    <td>
                                                                        <p></p>
                                                                    </td>
                                                                    <td>
                                                                        <p></p>
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
                                    <h2><strong>Submission successful</strong></h2>
                                    <h3><strong>-Clinical Laboratory</strong>(Renewal+Amendment)</h3>
                                    <h3><strong>-Clinical Laboratory</strong>(Amendment)</h3>
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

<script>
    $(document).ready(function () {
        if("Y" == '${jumpEdit}'){
            $('#licReA').click();
        }
        if("Y" == '${jumpPmt}'){
            $('#licPmt').click();
        }

    });

    $('#premisesEdit').click(function () {
        $('#EditValue').val('premises');
        $('#MasterCodeForm').submit();
    });

</script>
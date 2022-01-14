<input type="hidden" id="section_repeat_section_id_prefix" value="ackSection" readonly disabled>

<div class="panel panel-default">
    <div class="panel-heading"><strong>Biological Agent/Toxin</strong></div>
    <div class="row form-horizontal">
        <div class="col-xs-12 col-sm-12" style="padding: 20px 30px 10px 30px; border-radius: 15px;margin: 0 auto">
            <div class="col-xs-12 col-sm-12">
                <c:forEach var="item" items="${receiptSaved.ackReceiptBats}" varStatus="status">
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="scheduleType--v--${status.index}">Schedule Type</label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <label id="scheduleType--v--${status.index}">${item.scheduleType}</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="batName--v--${status.index}">Biological Agent/Toxin</label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <label id="batName--v--${status.index}">${item.batName}</label>
                        </div>
                    </div>

                    <c:if test="${item.scheduleType ne 'SCHTYPE006'}">
                        <%--Displayed for First, Second, Third and Fourth Schedule biological agent--%>
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label for="transferredType--v--${status.index}">Type Transferred:</label>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <label id="transferredType--v--${status.index}">${item.transferredType}</label>
                            </div>
                        </div>

                        <%--Displayed for First, Second, Third and Fourth Schedule biological agent--%>
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label for="receivedBatQty--v--${status.index}">Quantity of Biological Agent</label>
                                <span class="mandatory otherQualificationSpan">*</span>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <input type="number" name="receivedBatQty--v--${status.index}"
                                       id="receivedBatQty--v--${status.index}" value="${item.receivedBatQty}">
                                <span data-err-ind="receivedBatQty--v--${status.index}" class="error-msg"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <%@include file="ackDocument.jsp" %>
                        </div>
                    </c:if>


                    <c:if test="${item.scheduleType eq 'SCHTYPE006'}">
                        <%--Displayed for Fifth Schedule toxin--%>
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label for="transferredQty--v--${status.index}">Quantity Transferred:</label>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <label id="transferredQty--v--${status.index}">${item.transferredQty}</label>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label for="transferredUnit--v--${status.index}">Unit of Measurement Transferred:</label>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <label id="transferredUnit--v--${status.index}">${item.transferredUnit}</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label for="receivedQty--v--${status.index}">Quantity Received</label>
                                <span class="mandatory otherQualificationSpan">*</span>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <input type="number" name="receivedQty--v--${status.index}"
                                       id="receivedQty--v--${status.index}" value="${item.receivedQty}"
                                       maxlength="11"
                                       oninput="this.value=this.value.replace(/\D*(\d*)(\.?)(\d{0,3})\d*/,'$1$2$3')">
                                <span data-err-ind="receivedQty--v--${status.index}" class="error-msg"></span>
                            </div>
                        </div>

                        <%--Displayed for Fifth Schedule toxin--%>
                        <div class="form-group">
                            <div class="col-sm-5 control-label">
                                <label for="receivedUnit--v--${status.index}">Unit of Measurement Received</label>
                                <span class="mandatory otherQualificationSpan">*</span>
                            </div>
                            <div class="col-sm-6 col-md-7">
                                <iais:select name="receivedUnit--v--${status.index}"
                                             id="receivedUnit--v--${status.index}"
                                             value="${item.receivedUnit}"
                                             codeCategory="CATE_ID_BSB_DATA_SUBMISSION_UNIT_OF_MEASUREMENT"
                                             firstOption="Please Select"/>
                                <span data-err-ind="receivedUnit--v--${status.index}" class="error-msg"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <%@include file="ackDocument.jsp" %>
                        </div>
                    </c:if>

                </c:forEach>
                <div class="row form-horizontal">
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label>Facility Name</label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <label id="facName">${receiptSaved.facId}</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label>Receiving Facility</label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <label id="receivingFacId"><p>${receiptSaved.receivingFacId}</p></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="actualReceiptDate">Actual Date of Receipt</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" autocomplete="off" name="actualReceiptDate" id="actualReceiptDate"
                                   data-date-start-date="01/01/1900" placeholder="dd/mm/yyyy" maxlength="10"
                                   class="date_picker form-control" value="${receiptSaved.actualReceiptDate}"/>
                            <span data-err-ind="actualReceiptDate" class="error-msg"></span>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="actualReceiptTime">Actual Time of Receipt</label>
                            <span class="mandatory otherQualificationSpan">*</span>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <input type="text" name="actualReceiptTime" id="actualReceiptTime" maxlength="10"
                                   value="${receiptSaved.actualReceiptTime}"/>
                            <span data-err-ind="actualReceiptTime" class="error-msg"></span>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-sm-5 control-label">
                            <label for="remark">Remarks</label>
                        </div>
                        <div class="col-sm-6 col-md-7">
                            <textarea id="remark" style="width: 100%;margin-bottom: 15px;" rows="6" name="remark"
                                      maxlength="500">${receiptSaved.remark}</textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


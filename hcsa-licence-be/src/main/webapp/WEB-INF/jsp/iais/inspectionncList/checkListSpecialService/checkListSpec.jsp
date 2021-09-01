<c:forEach var="service" items="${specialServiceForChecklistDecideDtos}" >
    <c:set value = "ServiceInfo${service.identify}" var = "errorTabName"/>
<div class="tab-pane ${errorSpecTab == errorTabName ? 'active' : ''}" id="ServiceInfo${service.identify}" role="tabpanel">
    <c:if test="${not empty service.fdtoList}">
        <span><strong>do/total:</strong></span>&nbsp;<c:out value="${service.serviceDo}"/>/<c:out value="${service.serviceTotal}"/><br>
        <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${service.serviceNc}"/>
    </c:if>
    <c:forEach var ="cdto" items ="${service.fdtoList}" varStatus="one">
        <h3>${cdto.subType}</h3>
        <div class="table-gp">
            <c:forEach var ="section" items ="${cdto.sectionDtoList}" varStatus="two">
                <br/>
                <h4><c:out value="${section.sectionName}"></c:out></h4>
                <table aria-describedby="" class="table">
                    <thead>
                    <tr>
                        <th scope="col" >No.</th>
                        <th scope="col" >Regulation Clause Number</th>
                        <th scope="col" width="30%">Item</th>
                        <th scope="col" class="text-left">Yes</th>
                        <th scope="col" class="text-left">No</th>
                        <th scope="col" class="text-left">N/A</th>
                        <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status)}">
                            <th scope="col">Self-assessment Answer</th>
                        </c:if>
                        <th scope="col" >Findings/NCs</th>
                        <th scope="col" >Actions Required</th>
                        <th scope="col" class="text-left">Rectified</th>
                        <th scope="col" ></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                        <tr>
                            <td class="row_no">    <div class="form-check"><span>${(status.index + 1) }</span></div></td>
                            <td><div class="form-check"><a data-toggle="modal" data-target="#DeleteTemplateModalSer${item.incqDto.itemId}">${item.incqDto.regClauseNo}</a></div> </td>
                            <div class="modal fade" id="DeleteTemplateModalSer${item.incqDto.itemId}" tabindex="-1" role="dialog" aria-labelledby="regOutsideWindow">
                                <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                                    <div class="modal-content">
                                            <%--                                                                          <div class="modal-header">--%>
                                            <%--                                                                              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
                                            <%--                                                                              <div class="modal-title" style="font-size:2rem;"></div>--%>
                                            <%--                                                                          </div>--%>
                                        <div class="modal-body">
                                            <div class="row"  style="height:500px;overflow:auto;">
                                                <div class="col-md-8 col-md-offset-2" style="width: 100%; margin: 0;white-space:pre-wrap;"><span style="font-size: 2rem">${item.incqDto.regClause}</span></div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <td><div class="form-check"><span>${item.incqDto.checklistItem}</span></div></td>
                            <c:set value = "${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                            <td class="text-right">
                                <div class="form-check">
                                    <input name="<c:out value="${ckkId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" class="form-check-input" />
                                    <label class="form-check-label" for="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxYes"><span class="check-circle"></span></label>
                                </div>
                            </td>
                            <td class="text-right">
                                <div class="form-check">
                                    <input name="<c:out value="${ckkId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" class="form-check-input" />
                                    <label class="form-check-label" for="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNo"><span class="check-circle"></span></label>
                                </div>
                            </td>
                            <td class="text-right">
                                <div class="form-check">
                                    <input name="<c:out value="${ckkId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" class="form-check-input" />
                                    <label class="form-check-label" for="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNa"><span class="check-circle"></span></label>
                                </div>
                            </td>
                            <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status)}">
                                <td>
                                    <div class="form-check">
                                            ${item.incqDto.selfAnswer}
                                    </div>
                                </td>
                            </c:if>
                            <td>
                                <textarea cols="35" rows="4" name="<c:out value="${ckkId}"/>FindNcs" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxFindNcs" maxlength="500"><c:out value="${item.incqDto.ncs}"/></textarea>
                                <br/>
                                <c:set value = "error_${ckkId}FindNcs" var = "err"/>
                                <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                            </td>
                            <td>
                                <textarea cols="35" rows="4" name="<c:out value="${ckkId}"/>remark" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxRemark" maxlength="500"><c:out value="${item.incqDto.remark}"/></textarea>
                                <br/>
                                <c:set value = "error_${ckkId}Remark" var = "err"/>
                                <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                            </td>
                            <td class="text-left">
                                <div id="<c:out value="${ckkId}"/>ck"   <c:if test="${item.incqDto.chkanswer != 'No'}">style="display: none;"</c:if>>
                                    <div class="form-check">
                                        <input name="<c:out value="${ckkId}"/>rec" id="<c:out value="${cdto.subName}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                    </div>
                                </div>
                            </td>
                            <td>
                                <c:set value = "error_${ckkId}" var = "err"/>
                                <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:forEach>
        </div>
    </c:forEach>
    <c:if test="${not empty service.adchklDto.adItemList}">

        <span><strong>do/total:</strong></span>&nbsp;<c:out value="${service.adhocDo}"/>/<c:out value="${service.adhocTotal}"/><br>
        <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${service.adhocNc}"/>
        <div class="table-gp">
            <h3>Adhoc</h3>
            <br/>
            <h4></h4>
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" >No.</th>
                    <th scope="col" width="35%">Item</th>
                    <th scope="col" class="ttext-left">Yes</th>
                    <th scope="col" class="text-left">No</th>
                    <th scope="col" class="text-left">N/A</th>
                    <th scope="col" >Findings/NCs</th>
                    <th scope="col" >Actions Required</th>
                    <th scope="col" class="text-left">Rectified</th>
                    <th scope="col" ></th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var = "item" items = "${service.adchklDto.adItemList}" varStatus="status">
                    <tr>
                        <td class="row_no">   <div class="form-check">${(status.index + 1) }</div></td>
                        <td> <div class="form-check"><c:out value="${item.question}"/></div></td>
                        <c:set value = "${item.id}${service.identify}" var = "ckkId"/>
                        <td class="text-right">
                            <div class="form-check"><input name="<c:out value="${ckkId}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'Yes'}">checked</c:if> value="Yes" class="form-check-input"/>
                                <label class="form-check-label" for="<c:out value="${ckkId}"/>adhocitemCheckboxYes"><span class="check-circle"></span></label>
                            </div>
                        </td>
                        <td class="text-right">
                            <div class="form-check">
                                <input name="<c:out value="${ckkId}"/>adhocrad" id="<c:out value="${ckkId}"/>adhocitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'No'}">checked</c:if> value="No" class="form-check-input"/>
                                <label class="form-check-label" for="<c:out value="${ckkId}"/>adhocitemCheckboxNo"><span class="check-circle"></span></label>
                            </div>
                        </td>
                        <td class="text-right">
                            <div class="form-check">
                                <input name="<c:out value="${ckkId}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'N/A'}">checked</c:if> value="N/A"  class="form-check-input"/>
                                <label class="form-check-label" for="<c:out value="${ckkId}"/>adhocitemCheckboxNa"><span class="check-circle"></span></label>
                            </div>
                        </td>
                        <td>
                            <textarea cols="35" rows="4" name="<c:out value="${ckkId}"/>adhocFindNcs" id="<c:out value="${ckkId}"/>adhocitemCheckboxFindNcs" id="" maxlength="500"><c:out value="${item.ncs}"/></textarea>
                            <br/>
                            <c:set value = "error_${ckkId}adhocFindNcs" var = "err"/>
                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                        </td>
                        <td>
                            <textarea cols="35" rows="4" name="<c:out value="${ckkId}"/>adhocremark" id="<c:out value="${ckkId}"/>adhocitemCheckboxRemark" id="" maxlength="500"><c:out value="${item.remark}"/></textarea>
                            <br/>
                            <c:set value = "error_${ckkId}adhocRemark" var = "err"/>
                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                        </td>
                        <td class="text-left">
                            <div id="<c:out value="${ckkId}"/>ck"<c:if test="${item.adAnswer != 'No'}">style="display: none;"</c:if>>
                                <div class="form-check">
                                    <input name="<c:out value="${ckkId}"/>adhocrec" id="<c:out value="${ckkId}"/>adhocrec" type="checkbox" <c:if test="${item.rectified}">checked</c:if> value="rec"/>
                                </div>
                            </div>

                        </td>
                        <td>
                            <c:set value = "error_${ckkId}adhoc" var = "err"/>
                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </div>
    </c:if>
</div>
</c:forEach>
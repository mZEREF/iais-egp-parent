<c:forEach var = "inspector" items="${inspectorsParticipant}"  varStatus="inspectorsStatus">
<div  ${(nowComTabIn== 'ServiceInfo' && ( nowTabIn == inspector.id)) ? '' : 'hidden'} >
<c:forEach var ="cdto" items ="${serListDto.fdtoList}" varStatus="one">
    <h3>${cdto.subType}</h3>
    <div class="table-gp">
        <c:forEach var ="section" items ="${cdto.sectionDtoList}" varStatus="two">
            <br/>
            <h4><c:out value="${section.sectionName}"></c:out></h4>
            <table class="table">
                <thead>
                <tr>
                    <th>No.</th>
                    <th>Regulation Clause Number</th>
                    <th  width="30%">Item</th>
                    <th class="text-center">Yes</th>
                    <th class="text-center">No</th>
                    <th class="text-center">N/A</th>
                    <th>Findings/NCs</th>
                    <th>Actions Required</th>
                    <th class="text-center">Rectified</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                    <tr>
                        <td class="row_no"><span>${(status.index + 1) }</span></td>
                        <td><a data-toggle="modal" data-target="#DeleteTemplateModal${item.incqDto.itemId}Ins${inspectorsStatus.index}">${item.incqDto.regClauseNo}</a> </td>
                        <div class="modal fade" id="DeleteTemplateModal${item.incqDto.itemId}Ins${inspectorsStatus.index}" tabindex="-1" role="dialog" aria-labelledby="regOutsideWindow" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                            <div class="modal-dialog modal-lg" role="document">
                                <div class="modal-content">
<%--                                    <div class="modal-header">--%>
<%--                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                                        <div class="modal-title" style="font-size: 2rem;"></div>--%>
<%--                                    </div>--%>
                                    <div class="modal-body">
                                        <div class="row" style="height:500px;overflow:auto;">
                                            <div class="col-md-8 col-md-offset-2" style="width: 100%; margin: 0;white-space:pre-wrap;"><span style="font-size: 2rem">${item.incqDto.regClause}</span></div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <td><span>${item.incqDto.checklistItem}</span></td>
                        <c:set value = "${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                        <c:set value="${item.incqDto.answerForDifDtoMaps[inspector.id]}" var="inspSerAnswer"/>
                        <td class="text-center"><input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>radIns${inspectorsStatus.index}" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxYesIns${inspectorsStatus.index}" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspSerAnswer.answer eq'Yes'}">checked</c:if> value="Yes" ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'}/></td>
                        <td class="text-center">
                            <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>radIns${inspectorsStatus.index}" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNoIns${inspectorsStatus.index}"  onclick="showCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspSerAnswer.answer eq'No'}">checked</c:if> value="No"  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'}/>
                        </td>
                        <td class="text-center"><input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>radIns${inspectorsStatus.index}" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNaIns${inspectorsStatus.index}" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}" type="radio" <c:if test="${inspSerAnswer.answer  eq'N/A'}">checked</c:if> value='N/A'  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'}/></td>
                        <td>
                            <textarea  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} cols="35" rows="4" name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>FindNcsIns${inspectorsStatus.index}" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxFindNcsIns${inspectorsStatus.index}" maxlength="500"><c:out value="${inspSerAnswer.ncs}"/></textarea>
                            <br/>
                            <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}${inspector.id}DraftFindNcs" var = "err"/>
                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                        </td>
                        <td>
                            <textarea  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} cols="35" rows="4" name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>remarkIns${inspectorsStatus.index}" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxRemarkIns${inspectorsStatus.index}" maxlength="500"><c:out value="${inspSerAnswer.remark}"/></textarea>
                            <br/>
                            <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}${inspector.id}DraftRemark" var = "err"/>
                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                        </td>
                        <td class="text-center">
                            <div id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>ckIns${inspectorsStatus.index}"   <c:if test="${inspSerAnswer.answer  != 'No'}">hidden</c:if>>
                                <input ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>recIns${inspectorsStatus.index}" id="<c:out value="${cdto.subName}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>recIns${inspectorsStatus.index}" type="checkbox" <c:if test="${inspSerAnswer.isRec == '1'}">checked</c:if> value="rec"/>
                            </div>
                        </td>
                        <td>
                            <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}${inspector.id}Draft" var = "err"/>
                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                        </td>
                    </tr>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:forEach>
    </div>
</c:forEach>
<c:if test="${adchklDto.adItemList != null}">
    <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.adhocDo}"/>/<c:out value="${serListDto.adhocTotal}"/><br>
    <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.adhocNc}"/>
    <div class="table-gp">
        <h3>Adhoc</h3>
        <br/>
        <h4></h4>
        <table class="table">
            <thead>
            <tr>
                <th>No.</th>
                <th  width="35%">Item</th>
                <th class="text-center">Yes</th>
                <th class="text-center">No</th>
                <th class="text-center">N/A</th>
                <th>Findings/NCs</th>
                <th>Actions Required</th>
                <th class="text-center">Rectified</th>
                <th></th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var = "item" items = "${adchklDto.adItemList}" varStatus="status">
                <tr>
                    <td class="row_no">${(status.index + 1) }</td>
                    <td><c:out value="${item.question}"/></td>
                    <c:set value = "${item.id}" var = "ckkId"/>
                    <c:set value = "${item.answerForDifDtoMaps[inspector.id]}" var = "inspAhocAnswer"/>
                    <td class="text-center"><input name="<c:out value="${item.id}"/>adhocradIns${inspectorsStatus.index}" id="<c:out value="${item.id}"/>adhocitemCheckboxYes" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspAhocAnswer.answer eq'Yes'}">checked</c:if> value="Yes" ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'}/></td>
                    <td class="text-center">
                        <input name="<c:out value="${item.id}"/>adhocradIns${inspectorsStatus.index}" id="<c:out value="${item.id}"/>adhocitemCheckboxNo"  onclick="showCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspAhocAnswer.answer eq'No'}">checked</c:if> value="No" ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'}/>
                    </td>
                    <td class="text-center"><input name="<c:out value="${item.id}"/>adhocradIns${inspectorsStatus.index}" id="<c:out value="${item.id}"/>adhocitemCheckboxNa" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspAhocAnswer.answer eq'N/A'}">checked</c:if> value='N/A' ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'}/></td>
                    <td>
                        <textarea ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} cols="35" rows="4" name="<c:out value="${item.id}"/>adhocFindNcsIns${inspectorsStatus.index}" id="<c:out value="${item.id}"/>adhocitemCheckboxFindNcsIns${inspectorsStatus.index}"  maxlength="500"><c:out value="${inspAhocAnswer.ncs}"/></textarea>
                        <br/>
                        <c:set value = "error_${item.id}${inspector.id}DraftadhocFindNcs" var = "err"/>
                        <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                    </td>
                    <td>
                        <textarea ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} cols="35" rows="4" name="<c:out value="${item.id}"/>adhocremarkIns${inspectorsStatus.index}" id="<c:out value="${item.id}"/>adhocitemCheckboxRemarkIns${inspectorsStatus.index}" maxlength="500"><c:out value="${inspAhocAnswer.remark}"/></textarea>
                        <br/>
                        <c:set value = "error_${item.id}${inspector.id}DraftadhocRemark" var = "err"/>
                        <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                    </td>
                    <td class="text-center">
                        <div id="<c:out value="${item.id}"/>ckIns${inspectorsStatus.index}"<c:if test="${inspAhocAnswer.answer != 'No'}">hidden</c:if>>
                            <input name="<c:out value="${item.id}"/>adhocrecIns${inspectorsStatus.index}" id="<c:out value="${item.id}"/>adhocrecIns${inspectorsStatus.index}" type="checkbox" <c:if test="${inspAhocAnswer.isRec == '1'}">checked</c:if> value="rec" ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'}/>
                        </div>
                    </td>
                    <td>
                        <c:set value = "error_${item.id}${inspector.id}Draftadhoc" var = "err"/>
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
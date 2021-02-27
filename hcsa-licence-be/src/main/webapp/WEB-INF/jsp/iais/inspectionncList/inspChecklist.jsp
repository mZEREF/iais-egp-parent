<c:forEach var = "inspector" items="${inspectorsParticipant}"  varStatus="inspectorsStatus">
    <div ${((nowComTabIn == null || nowComTabIn== 'General') && ( nowTabIn == inspector.id)) ? '' : 'hidden'} >
        <div class="form-group">
            <label class="col-xs-12 col-md-4 control-label" >Remarks</label>
            <div class="col-xs-8 col-sm-6 col-md-5">
                <p> <c:out value="${commonDto.draftRemarkMaps[inspector.id]}"/> </p>
            </div>
        </div>
<div class="table-gp">
    <c:forEach var ="section" items ="${commonDto.sectionDtoList}" varStatus="one">
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
                <th>Remarks</th>
                <th class="text-center">Rectified</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="two">
                <tr>
                    <td class="row_no"><span>${(two.index + 1) }</span></td>
                    <td> <a data-toggle="modal" data-target="#DeleteTemplateModal${item.incqDto.itemId}Ins${inspectorsStatus.index}">${item.incqDto.regClauseNo}</a></td>
                    <div class="modal fade" id="DeleteTemplateModal${item.incqDto.itemId}Ins${inspectorsStatus.index}" tabindex="-1" role="dialog" aria-labelledby="regOutsideWindow" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
<%--                                <div class="modal-header">--%>
<%--                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                                    <div class="modal-title" style="font-size: 2rem;"></div>--%>
<%--                                </div>--%>
                                <div class="modal-body">
                                    <div class="row">
                                        <div class="col-md-8 col-md-offset-2" style="width: 100%; margin: 0;white-space:pre-wrap;"><span  style="font-size: 2rem">${item.incqDto.regClause}</span></div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <td><span >${item.incqDto.checklistItem}</span></td>
                    <c:set value = "${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                    <c:set value="${item.incqDto.answerForDifDtoMaps[inspector.id]}" var="inspComAnswer"/>
                    <td class="text-center"><input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comradIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxYesIns${inspectorsStatus.index}" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspComAnswer.answer eq'Yes'}">checked</c:if> value="Yes"  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} /></td>
                    <td class="text-center">
                        <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comradIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNoIns${inspectorsStatus.index}"  onclick="showCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspComAnswer.answer eq'No'}">checked</c:if> value="No"   ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} />
                    </td>
                    <td class="text-center"><input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comradIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNaIns${inspectorsStatus.index}" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspComAnswer.answer eq'N/A'}">checked</c:if> value='N/A'  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} /></td>
                    <td>
                        <textarea  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} cols="35" rows="4" name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremarkIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremarkIns${inspectorsStatus.index}" maxlength="500"><c:out value="${inspComAnswer.remark}"/></textarea>
                    </td>
                    <td class="text-center">
                        <div id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comckIns${inspectorsStatus.index}"   <c:if test="${inspComAnswer.answer != 'No'}">hidden</c:if>>
                            <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrecIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comrecIns${inspectorsStatus.index}" type="checkbox" <c:if test="${inspComAnswer.isRec == '1'}">checked</c:if> value="rec"  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'}/>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:forEach>
</div>
</div>
</c:forEach>
<c:forEach var = "inspector" items="${inspectorsParticipant}"  varStatus="inspectorsStatus">
    <div ${((nowComTabIn == null || nowComTabIn== 'General') && ( nowTabIn == inspector.id)) ? '' : 'hidden'} >
        <div class="form-group">
            <label class="col-xs-12 col-md-4 control-label" >Remarks</label>
            <div class="col-xs-8 col-sm-6 col-md-5">
                <p> <c:out value="${commonDto.draftRemarkMaps[inspector.id]}"/>
                    <c:if test="${not empty serListDto.fdtoList}">
                        <c:forEach var ="cdto" items ="${serListDto.fdtoList}" varStatus="one">
                            <c:if test="${not empty cdto.draftRemarkMaps[inspector.id]}">
                            <br>
                            <c:out value="${cdto.draftRemarkMaps[inspector.id]}"/>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </p>
            </div>
        </div>
<div class="table-gp">
    <c:forEach var ="section" items ="${commonDto.sectionDtoList}" varStatus="one">
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
                <th scope="col" >Findings/NCs</th>
                <th scope="col" >Actions Required</th>
                <th scope="col" class="text-left">Rectified</th>
                <th scope="col" ></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="two">
                <tr>
                    <td class="row_no">      <div class="form-check"><span>${(two.index + 1) }</span></div></td>
                    <td>      <div class="form-check"> <a href="javascript:void(0);" data-toggle="modal" data-target="#DeleteTemplateModal${item.incqDto.itemId}Ins${inspectorsStatus.index}">${item.incqDto.regClauseNo}</a></div></td>
                    <div class="modal fade" id="DeleteTemplateModal${item.incqDto.itemId}Ins${inspectorsStatus.index}" tabindex="-1" role="dialog" aria-labelledby="regOutsideWindow">
                        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                            <div class="modal-content">
<%--                                <div class="modal-header">--%>
<%--                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                                    <div class="modal-title" style="font-size: 2rem;"></div>--%>
<%--                                </div>--%>
                                <div class="modal-body">
                                    <div class="row" style="height:500px;overflow:auto;">
                                        <div class="col-md-8 col-md-offset-2" style="width: 100%; margin: 0;white-space:pre-wrap;"><span  style="font-size: 2rem">${item.incqDto.regClause}</span></div>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <td>      <div class="form-check"><span >${item.incqDto.checklistItem}</span></div></td>
                    <c:set value = "${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                    <c:set value="${item.incqDto.answerForDifDtoMaps[inspector.id]}" var="inspComAnswer"/>
                    <td class="text-left">
                        <div class="form-check">
                        <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comradIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxYesIns${inspectorsStatus.index}" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspComAnswer.answer eq'Yes'}">checked</c:if> value="Yes"  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} class="form-check-input"/>
                            <label class="form-check-label" for="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxYesIns${inspectorsStatus.index}" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')"><span class="check-circle"></span></label>
                        </div>
                    </td>
                    <td class="text-left">
                        <div class="form-check">
                        <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comradIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNoIns${inspectorsStatus.index}"  onclick="showCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspComAnswer.answer eq'No'}">checked</c:if> value="No"   ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} class="form-check-input"/>
                            <label class="form-check-label" for="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNoIns${inspectorsStatus.index}" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')"><span class="check-circle"></span></label>
                        </div>
                    </td>
                    <td class="text-left">
                        <div class="form-check">
                        <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comradIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNaIns${inspectorsStatus.index}" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')" type="radio" <c:if test="${inspComAnswer.answer eq'N/A'}">checked</c:if> value='N/A'  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} class="form-check-input"/>
                            <label class="form-check-label" for="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNaIns${inspectorsStatus.index}" onclick="hideCheckBox('${ckkId}','${inspectorsStatus.index}')"><span class="check-circle"></span></label>
                        </div>
                    </td>
                    <td>
                        <textarea  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} cols="35" rows="4" name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comFindNcsIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comFindNcsIns${inspectorsStatus.index}" maxlength="500"><c:out value="${inspComAnswer.ncs}"/></textarea>
                        <br/>
                        <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}${inspector.id}DraftcomFindNcs" var = "err"/>
                        <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                    </td>
                    <td>
                        <textarea  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'} cols="35" rows="4" name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremarkIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremarkIns${inspectorsStatus.index}" maxlength="500"><c:out value="${inspComAnswer.remark}"/></textarea>
                        <br/>
                        <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}${inspector.id}DraftcomRemark" var = "err"/>
                        <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                    </td>
                    <td class="text-left">
                        <div id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comckIns${inspectorsStatus.index}"   <c:if test="${inspComAnswer.answer != 'No'}">style="display: none;"</c:if>>
                            <div class="form-check">
                            <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrecIns${inspectorsStatus.index}" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comrecIns${inspectorsStatus.index}" type="checkbox" <c:if test="${inspComAnswer.isRec == '1'}">checked</c:if> value="rec"  ${inspectorUserFinishChecklistId == inspector.id ? "" : 'disabled'}/>
                            </div>
                        </div>
                    </td>
                    <td>
                        <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}${inspector.id}Draftcom" var = "err"/>
                        <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:forEach>
</div>
</div>
</c:forEach>
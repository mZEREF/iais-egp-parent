<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<div class="tab-pane ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" id="General" role="tabpanel">
    <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.generalDo}"/>/<c:out value="${serListDto.generalTotal}"/><br>
    <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.generalNc}"/>
    <h3>General</h3>
    <c:if test="${ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO == applicationViewDto.applicationDto.status}">
        <div class="form-group">
            <label class="col-xs-12 col-md-4 control-label" >Remarks</label>
            <div class="col-xs-8 col-sm-6 col-md-5">
                <p> <c:out value="${commonDto.draftRemarkMaps[inspectorUserFinishChecklistId]}"/>
                    <c:if test="${not empty serListDto.fdtoList}">
                        <c:forEach var ="cdto" items ="${serListDto.fdtoList}" varStatus="one">
                            <c:if test="${not empty cdto.draftRemarkMaps[inspectorUserFinishChecklistId]}">
                                <br>
                                <c:out value="${cdto.draftRemarkMaps[inspectorUserFinishChecklistId]}"/>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </p>
            </div>
        </div>
    </c:if>
    <div class="table-gp">
        <c:forEach var ="section" items ="${commonDto.sectionDtoList}" varStatus="one">
            <br/>
            <h4><c:out value="${section.sectionName}"></c:out></h4>
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <th scope="col" >No.</th>
                    <th scope="col" >Regulation Clause Number</th>
                    <th scope="col" >Item</th>
                    <th scope="col" class="text-left">Yes</th>
                    <th scope="col" class="text-left">No</th>
                    <th scope="col" class="text-left">N/A</th>
                    <th scope="col" >Self-assessment Answer</th>
                    <th scope="col" >Findings/NCs</th>
                    <th scope="col" >Actions Required</th>
                    <th scope="col" class="text-left">Rectified</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="two">
                    <tr>
                        <td class="row_no"> <div class="form-check"><span>${(two.index + 1) }</span></div></td>
                        <td> <div class="form-check"><a href="javascript:void(0);" data-toggle="modal" data-target="#DeleteTemplateModal${item.incqDto.itemId}">${item.incqDto.regClauseNo}</a></div></td>
                        <div class="modal fade" id="DeleteTemplateModal${item.incqDto.itemId}" tabindex="-1" role="dialog" aria-labelledby="regOutsideWindow">
                            <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                                <div class="modal-content">
                                    <div class="modal-body">
                                        <div class="row"  style="height:500px;overflow:auto;">
                                            <div class="col-md-8 col-md-offset-2" style="width: 100%; margin: 0;white-space:pre-wrap;"><span  style="font-size: 2rem">${item.incqDto.regClause}</span></div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <td><div class="form-check"><span >${item.incqDto.checklistItem}</span></div></td>
                        <c:set value = "${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                        <td class="text-left">
                            <div class="form-check">
                                <input disabled name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes"  class="form-check-input"/>
                                <label class="form-check-label" for="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxYes"><span class="check-circle"></span></label>
                            </div>
                        </td>
                        <td class="text-left">
                            <div class="form-check">
                                <input disabled name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" class="form-check-input" />
                                <label class="form-check-label" for="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNo"><span class="check-circle"></span></label>
                            </div>
                        </td>
                        <td class="text-left">
                            <div class="form-check">
                                <input disabled name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" class="form-check-input"/>
                                <label class="form-check-label" for="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNa"><span class="check-circle"></span></label>
                            </div>
                        </td>
                        <td>
                            <div class="form-check">
                                    ${item.incqDto.selfAnswer}
                            </div>
                        </td>
                        <td>
                            <c:out value="${item.incqDto.ncs}"/>
                        </td>
                        <td>
                            <c:out value="${item.incqDto.remark}"/>
                        </td>
                        <td class="text-left">
                            <div id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comck"   <c:if test="${item.incqDto.chkanswer != 'No'}">style="display: none;" </c:if>>
                                <div class="form-check">
                                    <input disabled name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrec" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comrec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:forEach>
    </div>
</div>




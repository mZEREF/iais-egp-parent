<div ${((nowComTabIn == null || nowComTabIn== 'General') && ( nowTabIn == null ||  nowTabIn == 'Combined')) ? '' : 'hidden'}>
                                    <div class="table-gp">
                                        <c:forEach var ="section" items ="${serListDto.fdtoList.get(0).sectionDtoList}" varStatus="one">
                                            <br/>
                                            <h4><c:out value="${section.sectionName}"></c:out></h4>
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr>
                                                    <th scope="col" >No.</th>
                                                    <th scope="col" >Item Description</th>
                                                    <th scope="col" >Item</th>
                                                    <th scope="col" >Inspector</th>
                                                    <th scope="col" >Compliance</th>
                                                    <th scope="col" >Findings/Non-Compliance</th>
                                                    <th scope="col" >Actions Required</th>
                                                    <th scope="col" >Rectified</th>
                                                    <th scope="col" class="text-center">Follow-Up Item</th>
                                                    <th scope="col" >Observations for Follow-up</th>
                                                    <th scope="col" >Action Required</th>
                                                    <th scope="col" class="text-center">Due Date</th>
                                                    <th scope="col" >Deconflict</th>
                                                    <th scope="col" ></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="two">
                                                    <c:set value = "${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                                                    <c:forEach var = "answerForDifDto" items = "${item.incqDto.answerForDifDtos}" varStatus="answerForDifDtoStatus">
                                                        <tr id="${ckkId}comDiv${answerForDifDtoStatus.index}"style="background-color:${ item.incqDto.sameAnswer ? 'white' : (empty item.incqDto.deconflict ? 'lightcoral': 'lightgreen')}">
                                                        <c:if test="${answerForDifDtoStatus.index == 0}">
                                                            <td  rowspan="${inspectorsParticipant.size()+1}" class="row_no"> <div class="form-check"><span>${(two.index + 1) }</span></div></td>
                                                            <td rowspan="${inspectorsParticipant.size()+1}" >  <div class="form-check"><span >${item.incqDto.regClauseNo}</span></div></td>
                                                            <td rowspan="${inspectorsParticipant.size()+1}" > <div class="form-check"><span >${item.incqDto.checklistItem}</span></div></td>
                                                        </c:if>
                                                            <td>        <div class="form-check">${answerForDifDto.submitName}</div></td>
                                                            <td>        <div class="form-check">${answerForDifDto.answer}</div></td>
                                                            <td>        <div class="form-check">${answerForDifDto.ncs}</div></td>
                                                            <td> <div class="form-check">${answerForDifDto.remark}</div></td>
                                                            <td class="text-center">
                                                                <c:if test="${'NO'== answerForDifDto.answer}">
                                                                    <div class="form-check">
                                                                <input  disabled type="checkbox" ${answerForDifDto.isRec == '1' ? 'checked' : null} />
                                                                    </div>
                                                                </c:if>
                                                            </td>
                                                            <td>        <div class="form-check">${answerForDifDto.followupItem}</div></td>
                                                            <td>        <div class="form-check">${answerForDifDto.observeFollowup}</div></td>
                                                            <td> <div class="form-check">${answerForDifDto.followupAction}</div></td>
                                                            <td> <div class="form-check">${answerForDifDto.dueDate}</div></td>
                                                            <td class="text-center">
                                                                <c:if test="${!item.incqDto.sameAnswer}">
                                                                    <div class="form-check">
                                                                      <input name="${ckkId}Deconflict" id="<${ckkId}Deconflict${answerForDifDtoStatus.index}" type="radio" <c:if test="${item.incqDto.deconflict == answerForDifDto.submitId}">checked</c:if> value="${answerForDifDto.submitId}"
                                                                       onclick="javascript: doChangeDeconflict(1,'${ckkId}','${inspectorsParticipant.size()}')"  class="form-check-input"/>
                                                                        <label class="form-check-label" for="<${ckkId}Deconflict${answerForDifDtoStatus.index}"><span class="check-circle"></span></label>
                                                                    </div>
                                                                </c:if>
                                                            </td>
                                                            <c:if test="${answerForDifDtoStatus.index == 0}">
                                                            <td rowspan="${inspectorsParticipant.size()}">
                                                                    <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}com" var = "err"/>
                                                                    <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                                    <span class="error-msg" id="<c:out value="${err}Remark"/>" name="iaisErrorMsg"></span>
                                                                    <span class="error-msg" id="<c:out value="${err}FindNcs"/>" name="iaisErrorMsg"></span>
                                                            </td>
                                                            </c:if>
                                                        </tr>
                                                    </c:forEach>

                                                  </c:forEach>
                                                  </tbody>
                                              </table>
                                          </c:forEach>
                                      </div>
   </div>




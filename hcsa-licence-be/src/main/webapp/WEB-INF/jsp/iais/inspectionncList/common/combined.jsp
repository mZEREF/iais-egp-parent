<div ${((nowComTabIn == null || nowComTabIn== 'General') && ( nowTabIn == null ||  nowTabIn == 'Combined')) ? '' : 'hidden'}>
                                    <div class="table-gp table-responsive">
                                        <c:forEach var ="section" items ="${commonDto.sectionDtoList}" varStatus="one">
                                            <br/>
                                            <h4><c:out value="${section.sectionName}"></c:out></h4>
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr>
                                                    <th scope="col" >No.</th>
                                                    <th scope="col" >Regulation Clause Number</th>
                                                    <th scope="col" width="30%">Item</th>
                                                    <th scope="col" >Inspector</th>
                                                    <th scope="col" >Yes/No/NA</th>
                                                    <th scope="col" >Findings/NCs</th>
                                                    <th scope="col" >Actions Required</th>
                                                    <th scope="col" >Rectified</th>
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
                                                                <c:if test="${'No'== answerForDifDto.answer}">
                                                                    <div class="form-check">
                                                                <input  disabled type="checkbox" ${answerForDifDto.isRec == '1' ? 'checked' : null} />
                                                                    </div>
                                                                </c:if>
                                                            </td>
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
                                                    <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType }">
                                                    <tr id="${ckkId}comDiv${inspectorsParticipant.size()}" style="background-color:${ item.incqDto.sameAnswer ? 'white' : (empty item.incqDto.deconflict ? 'lightcoral': 'lightgreen')}" >
                                                        <td>        <div class="form-check">Self Assessment</div></td>
                                                        <td>        <div class="form-check">${item.incqDto.selfAnswer}</div></td>
                                                        <td></td>
                                                        <td></td>
                                                        <td></td>
                                                        <td></td>
                                                        <td></td>
                                                    </tr>
                                                    </c:if>
                                                  </c:forEach>
                                                  </tbody>
                                              </table>
                                          </c:forEach>
                                      </div>
   </div>




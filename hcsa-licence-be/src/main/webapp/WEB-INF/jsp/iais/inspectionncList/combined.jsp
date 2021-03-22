<div ${((nowComTabIn == null || nowComTabIn== 'General') && ( nowTabIn == null ||  nowTabIn == 'Combined')) ? '' : 'hidden'}>
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
                                                    <th>Inspector</th>
                                                    <th>Yes/No/NA</th>
                                                    <th>Findings/NCS</th>
                                                    <th>Actions Required</th>
                                                    <th>Rectified</th>
                                                    <th>Deconflict</th>
                                                    <th></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="two">
                                                    <c:set value = "${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                                                    <c:forEach var = "answerForDifDto" items = "${item.incqDto.answerForDifDtos}" varStatus="answerForDifDtoStatus">
                                                        <tr id="${ckkId}comDiv${answerForDifDtoStatus.index}"style="background-color:${ item.incqDto.sameAnswer ? 'white' : (empty item.incqDto.deconflict ? 'lightcoral': 'lightgreen')}">
                                                        <c:if test="${answerForDifDtoStatus.index == 0}">
                                                            <td  rowspan="${inspectorsParticipant.size()+1}" class="row_no"><span>${(two.index + 1) }</span></td>
                                                            <td rowspan="${inspectorsParticipant.size()+1}" > <span >${item.incqDto.regClauseNo}</span></td>
                                                            <td rowspan="${inspectorsParticipant.size()+1}" ><span >${item.incqDto.checklistItem}</span></td>
                                                        </c:if>
                                                            <td>${answerForDifDto.submitName}</td>
                                                            <td>${answerForDifDto.answer}</td>
                                                            <td>${answerForDifDto.ncs}</td>
                                                            <td>${answerForDifDto.remark}</td>
                                                            <td class="text-center">
                                                                <c:if test="${'No'== answerForDifDto.answer}">
                                                                <input  disabled type="checkbox" ${answerForDifDto.isRec == '1' ? 'checked' : null} />
                                                                </c:if>
                                                            </td>
                                                            <td class="text-center">
                                                                <c:if test="${!item.incqDto.sameAnswer}">
                                                                <input name="${ckkId}Deconflict" id="<${ckkId}Deconflict${answerForDifDtoStatus.index}" type="radio" <c:if test="${item.incqDto.deconflict == answerForDifDto.submitId}">checked</c:if> value="${answerForDifDto.submitId}"
                                                                       onclick="javascript: doChangeDeconflict(1,'${ckkId}','${inspectorsParticipant.size()}')"/>
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <c:if test="${answerForDifDtoStatus.index == 0}">
                                                                    <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}com" var = "err"/>
                                                                    <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                                    <span class="error-msg" id="<c:out value="${err}Remark"/>" name="iaisErrorMsg"></span>
                                                                    <span class="error-msg" id="<c:out value="${err}FindNcs"/>" name="iaisErrorMsg"></span>
                                                            </c:if>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status)}">
                                                    <tr id="${ckkId}comDiv${inspectorsParticipant.size()}" style="background-color:${ item.incqDto.sameAnswer ? 'white' : (empty item.incqDto.deconflict ? 'lightcoral': 'lightgreen')}" >
                                                        <td>Self Assessment</td>
                                                        <td>${item.incqDto.selfAnswer}</td>
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




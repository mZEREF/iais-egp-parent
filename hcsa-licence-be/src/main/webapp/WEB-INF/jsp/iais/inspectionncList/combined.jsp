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
                                                    <th>Item</th>
                                                    <th>Inspector</th>
                                                    <th>Yes/No/NA</th>
                                                    <th>Remarks</th>
                                                    <th>Rectified</th>
                                                    <th>Deconflict</th>
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
                                                            <td>${answerForDifDto.remark}</td>
                                                            <td>
                                                                <c:if test="${'No'== answerForDifDto.answer}">
                                                                <input  disabled type="checkbox" ${answerForDifDto.isRec == '1' ? 'checked' : null} />
                                                                </c:if>
                                                            </td>
                                                            <td>
                                                                <c:if test="${!item.incqDto.sameAnswer}">
                                                                <input name="${ckkId}Deconflict" id="<${ckkId}Deconflict${answerForDifDtoStatus.index}" type="radio" <c:if test="${item.incqDto.deconflict == answerForDifDto.submitId}">checked</c:if> value="${answerForDifDto.submitId}"
                                                                       onclick="javascript: doChangeDeconflict(1,'${ckkId}','${inspectorsParticipant.size()}')"/>
                                                                </c:if>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    <tr id="${ckkId}comDiv${inspectorsParticipant.size()}" style="background-color:${ item.incqDto.sameAnswer ? 'white' : (empty item.incqDto.deconflict ? 'lightcoral': 'lightgreen')}" >
                                                        <td>Self Assessment</td>
                                                        <td>${item.incqDto.selfAnswer}</td>
                                                        <td></td>
                                                        <td></td>
                                                        <td></td>
                                                    </tr>
                                                  </c:forEach>
                                                  </tbody>
                                              </table>
                                          </c:forEach>
                                      </div>
   </div>




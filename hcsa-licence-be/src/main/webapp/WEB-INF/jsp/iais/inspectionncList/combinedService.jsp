<div ${(nowComTabIn== 'ServiceInfo' && ( nowTabIn == null ||  nowTabIn == 'Combined')) ? '' : 'hidden'}>
                                      <c:forEach var ="cdto" items ="${serListDto.fdtoList}" varStatus="one">
                                          <h3>${cdto.subType}</h3>
                                          <br>
                                          <div class="table-gp">
                                              <c:forEach var ="section" items ="${cdto.sectionDtoList}" varStatus="two">
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
                                                          <th>Remark</th>
                                                          <th>Rectified</th>
                                                          <th>Deconflict</th>
                                                      </tr>
                                                      </thead>
                                                      <tbody>
                                                      <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                          <c:set value = "${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                                                          <c:forEach var = "answerForDifDto" items = "${item.incqDto.answerForDifDtos}" varStatus="answerForDifDtoStatus">
                                                              <tr id="${ckkId}serDiv${answerForDifDtoStatus.index}" style="background-color:${ item.incqDto.sameAnswer ? 'white' : (empty item.incqDto.deconflict ? 'lightcoral': 'lightgreen')}">
                                                                  <c:if test="${answerForDifDtoStatus.index == 0}">
                                                                      <td  rowspan="${inspectorsParticipant.size()+1}" class="row_no"><span>${(status.index + 1) }</span></td>
                                                                      <td rowspan="${inspectorsParticipant.size()+1}" > <span >${item.incqDto.regClauseNo}</span></td>
                                                                      <td rowspan="${inspectorsParticipant.size()+1}" ><span >${item.incqDto.checklistItem}</span></td>
                                                                  </c:if>
                                                                  <td>${answerForDifDto.submitName}</td>
                                                                  <td>${answerForDifDto.answer}</td>
                                                                  <td>${answerForDifDto.remark}</td>
                                                                  <td>
                                                                      <input  disabled type="checkbox" ${answerForDifDto.isRec == '1' ? 'checked' : null} />
                                                                  </td>
                                                                  <td>
                                                                      <c:if test="${!item.incqDto.sameAnswer}">
                                                                          <input name="${ckkId}Deconflict" id="${ckkId}Deconflict${answerForDifDtoStatus.index}" type="radio" <c:if test="${item.incqDto.deconflict == answerForDifDto.submitId}">checked</c:if> value="${answerForDifDto.submitId}"
                                                                                 onclick="javascript: doChangeDeconflict(2,'${ckkId}',${inspectorsParticipant.size()})"/>
                                                                      </c:if>
                                                                  </td>
                                                              </tr>
                                                          </c:forEach>
                                                          <tr id="${ckkId}serDiv${inspectorsParticipant.size()}"style="background-color:${ item.incqDto.sameAnswer ? 'white' : (empty item.incqDto.deconflict ? 'lightcoral': 'lightgreen')}">
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
                                                          <th>Item</th>
                                                          <th>Inspector</th>
                                                          <th>Yes/No/NA</th>
                                                          <th>Remark</th>
                                                          <th>Rectified</th>
                                                          <th>Deconflict</th>
                                                      </tr>
                                                      </thead>
                                                      <tbody>

                                                      <c:forEach var = "item" items = "${adchklDto.adItemList}" varStatus="status">
                                                      <c:forEach var = "answerForDifDto" items = "${item.adhocAnswerForDifDtos}" varStatus="answerForDifDtoStatus">
                                                          <tr  id="${item.id}adhocDiv${answerForDifDtoStatus.index}" style="background-color: ${ item.sameAnswer ? 'white' : (empty item.deconflict ? 'lightcoral': 'lightgreen')}">
                                                              <c:if test="${answerForDifDtoStatus.index == 0}">
                                                              <td  rowspan="${inspectorsParticipant.size()}" class="row_no">${(status.index + 1)}</td>
                                                              <td rowspan="${inspectorsParticipant.size()}"><c:out value="${item.question}"/></td>
                                                              </c:if>
                                                              <td>${answerForDifDto.submitName}</td>
                                                              <td>${answerForDifDto.answer}</td>
                                                              <td>${answerForDifDto.remark}</td>
                                                              <td>
                                                                  <input  disabled type="checkbox" ${answerForDifDto.isRec == '1' ? 'checked' : null} />
                                                              </td>
                                                              <td>
                                                                  <c:if test="${ !item.sameAnswer}">
                                                                      <input name="<c:out value="${item.id}"/>adhocDeconflict" id="<c:out value="${item.id}"/>adhocDeconflict${answerForDifDtoStatus.index}" type="radio" <c:if test="${item.deconflict == answerForDifDto.submitId}">checked</c:if> value="${answerForDifDto.submitId}"
                                                                             onclick="javascript: doChangeDeconflict(3,'${item.id}',${inspectorsParticipant.size()})"
                                                                      />
                                                                  </c:if>
                                                             </td>
                                                       </tr>
                                                      </c:forEach>
                                                </c:forEach>

                                                </tbody>
                                            </table>
                                        </div>
                                    </c:if>
                                </div>




<div ${(nowComTabIn== errorTabName && ( nowTabIn == null ||  nowTabIn == 'Combined')) ? '' : 'hidden'}>
                                      <c:forEach var ="cdto" items ="${service.fdtoList}" varStatus="one">
                                          <h3>${cdto.subType}</h3>
                                          <br>
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
                                                      <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                          <c:set value = "${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                                                          <c:forEach var = "answerForDifDto" items = "${item.incqDto.answerForDifDtos}" varStatus="answerForDifDtoStatus">
                                                              <tr id="${ckkId}serDiv${answerForDifDtoStatus.index}" style="background-color:${ item.incqDto.sameAnswer ? 'white' : (empty item.incqDto.deconflict ? 'lightcoral': 'lightgreen')}">
                                                                  <c:if test="${answerForDifDtoStatus.index == 0}">
                                                                      <td  rowspan="${inspectorsParticipant.size()+1}" class="row_no"><div class="form-check"><span>${(status.index + 1) }</span></div></td>
                                                                      <td rowspan="${inspectorsParticipant.size()+1}" > <div class="form-check"><span >${item.incqDto.regClauseNo}</span></div></td>
                                                                      <td rowspan="${inspectorsParticipant.size()+1}" ><div class="form-check"><span >${item.incqDto.checklistItem}</span></div></td>
                                                                  </c:if>
                                                                  <td><div class="form-check">${answerForDifDto.submitName}</div></td>
                                                                  <td><div class="form-check">${answerForDifDto.answer}</div></td>
                                                                  <td><div class="form-check">${answerForDifDto.ncs}</div></td>
                                                                  <td><div class="form-check">${answerForDifDto.remark}</div></td>
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
                                                                          <input name="${ckkId}Deconflict" id="${ckkId}Deconflict${answerForDifDtoStatus.index}" type="radio" <c:if test="${item.incqDto.deconflict == answerForDifDto.submitId}">checked</c:if> value="${answerForDifDto.submitId}"
                                                                                 onclick="javascript: doChangeDeconflict(2,'${ckkId}',${inspectorsParticipant.size()})"  class="form-check-input"/>
                                                                              <label class="form-check-label" for="${ckkId}Deconflict${answerForDifDtoStatus.index}"><span class="check-circle"></span></label>
                                                                          </div>
                                                                      </c:if>
                                                                  </td>
                                                              <c:if test="${answerForDifDtoStatus.index == 0}">
                                                                  <td rowspan="${inspectorsParticipant.size()}">
                                                                  <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "err"/>
                                                                      <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                                      <span class="error-msg" id="<c:out value="${err}Remark"/>" name="iaisErrorMsg"></span>
                                                                      <span class="error-msg" id="<c:out value="${err}FindNcs"/>" name="iaisErrorMsg"></span>
                                                                  </td>
                                                                  </c:if>
                                                              </tr>
                                                          </c:forEach>
                                                          <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status)}">
                                                          <tr id="${ckkId}serDiv${inspectorsParticipant.size()}"style="background-color:${ item.incqDto.sameAnswer ? 'white' : (empty item.incqDto.deconflict ? 'lightcoral': 'lightgreen')}">
                                                              <td><div class="form-check">Self Assessment</div></td>
                                                              <td><div class="form-check">${item.incqDto.selfAnswer}</div></td>
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
                                          </c:forEach>
                                          <c:if test="${service.adchklDto.adItemList != null}">
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

                                                      <c:forEach var = "item" items = "${service.adchklDto.adItemList}" varStatus="status">
                                                          <c:set value = "${item.id}${service.identify}" var = "ckkId"/>
                                                      <c:forEach var = "answerForDifDto" items = "${item.adhocAnswerForDifDtos}" varStatus="answerForDifDtoStatus">
                                                          <tr  id="${ckkId}adhocDiv${answerForDifDtoStatus.index}" style="background-color: ${ item.sameAnswer ? 'white' : (empty item.deconflict ? 'lightcoral': 'lightgreen')}">
                                                              <c:if test="${answerForDifDtoStatus.index == 0}">
                                                                  <td  rowspan="${inspectorsParticipant.size()}" class="row_no">     <div class="form-check">${(status.index + 1)}</div></td>
                                                                  <td rowspan="${inspectorsParticipant.size()}"> <div class="form-check"><c:out value="${item.question}"/></div></td>
                                                              </c:if>
                                                              <td> <div class="form-check">${answerForDifDto.submitName}</div></td>
                                                              <td><div class="form-check">${answerForDifDto.answer}</div></td>
                                                              <td><div class="form-check">${answerForDifDto.ncs}</div></td>
                                                              <td><div class="form-check">${answerForDifDto.remark}</div></td>
                                                              <td class="text-center">
                                                                  <c:if test="${'No'== answerForDifDto.answer}">
                                                                      <div class="form-check">
                                                                  <input  disabled type="checkbox" ${answerForDifDto.isRec == '1' ? 'checked' : null} />
                                                                      </div>
                                                                  </c:if>
                                                              </td>
                                                              <td class="text-center">
                                                                  <c:if test="${ !item.sameAnswer}">
                                                                      <div class="form-check">
                                                                      <input name="<c:out value="${ckkId}"/>adhocDeconflict" id="<c:out value="${ckkId}"/>adhocDeconflict${answerForDifDtoStatus.index}" type="radio" <c:if test="${item.deconflict == answerForDifDto.submitId}">checked</c:if> value="${answerForDifDto.submitId}"
                                                                             onclick="javascript: doChangeDeconflict(3,'${ckkId}',${inspectorsParticipant.size()})"
                                                                             class="form-check-input" />
                                                                          <label class="form-check-label" for="<c:out value="${ckkId}"/>adhocDeconflict${answerForDifDtoStatus.index}"><span class="check-circle"></span></label>
                                                                      </div>
                                                                  </c:if>
                                                             </td>
                                                              <c:if test="${answerForDifDtoStatus.index == 0}">
                                                              <td rowspan="${inspectorsParticipant.size()}" >
                                                                      <c:set value = "error_${ckkId}adhoc" var = "err"/>
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
                                        </div>
                                    </c:if>
                                </div>




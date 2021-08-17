<c:forEach var="selfAssessmentConfig" items="${selfAssessmentDetail.selfAssessmentConfig}" varStatus="status">
    <c:if test="${selfAssessmentConfig.configId eq tagIndex}">
        <c:forEach var="sqMap" items="${selfAssessmentConfig.sqMap}">
            <div class="panel panel-default">
                <div class="panel-heading" id="headingPremise" role="tab">
                    <h4 class="panel-title"><a role="button" data-toggle=""
                                               aria-expanded="true" aria-controls="collapsePremise">${sqMap.key}</a>
                    </h4>
                </div>
                <c:forEach var="item" items="${sqMap.value}">
                    <div class="panel-collapse collapse in" id="collapsePremise" role="tabpanel"
                         aria-labelledby="headingPremise">
                        <div class="panel-body">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" width="25%">Regulation Clause Number</th>
                                    <th scope="col" width="40%">Item</th>
                                    <th scope="col" width="5%">Yes</th>
                                    <th scope="col" width="5%">No</th>
                                    <th scope="col" width="5%">N/A</th>
                                </tr>
                                </thead>
                                <tbody>

                                <tr>
                                    <td>
                                        <p class="visible-xs visible-sm table-row-title"></p>
                                        <p><a id="regOutsideWindow${status.index + 1}" data-toggle="modal"
                                              data-target="#DeleteTemplateModal${status.index + 1}">${item.regulation}</a>
                                        </p>
                                    </td>

                                    <div class="modal fade " id="DeleteTemplateModal${status.index + 1}" tabindex="-1"
                                         role="dialog" aria-labelledby="regOutsideWindow">
                                        <div class="modal-dialog modal-lg modal-dialog-centered" role="document">
                                            <div class="modal-content">
                                                <div class="modal-body">
                                                    <div class="row" style="height:500px;overflow:auto; ">
                                                        <div class="col-md-10 col-md-offset-2"
                                                             style="width: 100%; margin: 0;white-space:pre-wrap;"><span
                                                                id="regulationClauseText"
                                                                style="font-size: 2rem">${item.regulationClause}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary"
                                                            data-dismiss="modal">Close
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <td>
                                        <p class="visible-xs visible-sm table-row-title"></p>
                                        <p><c:out value="${item.checklistItem}"></c:out></p>
                                    </td>

                                    <td>
                                        <p><input name="${item.answerKey}" type="radio"
                                                  <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if>
                                                  <c:if test="${item.answer == 'YES'}">checked="checked"
                                                  value="YES" </c:if>
                                                  onclick="javascript:draftAnswer('YES', '${item.answerKey}')"/></p>
                                    </td>

                                    <td>
                                        <p><input name="${item.answerKey}" type="radio"
                                                  <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if>
                                                  <c:if test="${item.answer == 'NO'}">checked="checked"
                                                  value="NO"</c:if>
                                                  onclick="javascript:draftAnswer('NO', '${item.answerKey}')"/></p>
                                    </td>

                                    <td>
                                        <p><input name="${item.answerKey}" type="radio"
                                                  <c:if test="${canEditAnswerFlag eq 'true'}">disabled</c:if>
                                                  <c:if test="${item.answer == 'NA'}">checked="checked"
                                                  value="NA"</c:if>
                                                  onclick="javascript:draftAnswer('NA', '${item.answerKey}')"/></p>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>

    </c:if>
</c:forEach>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
          String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
<form method="post" id="mainForm"  action=<%=process.runtime.continueURL()%>  class="form-horizontal" >
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.FillupChklistDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <input type="hidden" name="doSubmitAction" id="doSubmitAction" value="">
    <div class="main-content">
        <div class="">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <div class="tab-content">
                        </div>
                        <div class="tab-pane <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
                            <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                <li class="complete ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" role="presentation"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General Regulations</a></li>
                                <li class="complete ${(nowComTabIn== 'ServiceInfo') ? 'active' : ''}" role="presentation"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab"
                                                                            data-toggle="tab"><c:out value="${serListDto.serviceName}"/></a></li>
                            </ul>

                            <div class="tab-nav-mobile visible-xs visible-sm">
                                <div class="swiper-wrapper" role="tablist">
                                    <div class="swiper-slide"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General</a></div>
                                    <div class="swiper-slide"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab" data-toggle="tab">ServiceInfo</a></div>
                                    <div class="swiper-slide"><a href="#chkInfo" aria-controls="chkInfo" role="tab" data-toggle="tab">chkInfo</a></div>
                                </div>
                                <div class="swiper-button-prev"></div>
                                <div class="swiper-button-next"></div>
                            </div>
                            <span class="error-msg" id="error_fillchkl" name="iaisErrorMsg"></span>
                            <div class="tab-content" id="checkLsitItemArea">
                                <div class="tab-pane ${(nowComTabIn == null || nowComTabIn== 'General') ? 'active' : ''}" id="General" role="tabpanel">
                                    <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.generalDo}"/>/<c:out value="${serListDto.generalTotal}"/><br>
                                    <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.generalNc}"/>
                                    <h3>General</h3>
                                    <c:if test="${ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status}">
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
                                            <table class="table">
                                                <thead>
                                                <tr>
                                                    <th>No.</th>
                                                    <th>Regulation Clause Number</th>
                                                    <th  width="30%">Item</th>
                                                    <th class="text-center">Yes</th>
                                                    <th class="text-center">No</th>
                                                    <th class="text-center">N/A</th>
                                                    <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status)}">
                                                    <th>Self-assessment Answer</th>
                                                    </c:if>
                                                    <th>Findings/NCs</th>
                                                    <th>Actions Required</th>
                                                    <th class="text-center">Rectified</th>
                                                    <th></th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="two">
                                                    <tr>
                                                        <td class="row_no"> <div class="form-check"><span>${(two.index + 1) }</span></div></td>
                                                        <td> <div class="form-check"><a data-toggle="modal" data-target="#DeleteTemplateModal${item.incqDto.itemId}">${item.incqDto.regClauseNo}</a></div></td>
                                                        <div class="modal fade" id="DeleteTemplateModal${item.incqDto.itemId}" tabindex="-1" role="dialog" aria-labelledby="regOutsideWindow" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                                            <div class="modal-dialog modal-lg" role="document">
                                                                <div class="modal-content">
<%--                                                                    <div class="modal-header">--%>
<%--                                                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                                                                        <div class="modal-title" style="font-size:2rem;"></div>--%>
<%--                                                                    </div>--%>
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
                                                            <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes"  class="form-check-input"/>
                                                                <label class="form-check-label" for="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxYes"><span class="check-circle"></span></label>
                                                            </div>
                                                        </td>
                                                        <td class="text-left">
                                                            <div class="form-check">
                                                            <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" class="form-check-input" />
                                                                <label class="form-check-label" for="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNo"><span class="check-circle"></span></label>
                                                            </div>
                                                        </td>
                                                        <td class="text-left">
                                                            <div class="form-check">
                                                            <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" class="form-check-input"/>
                                                                <label class="form-check-label" for="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comitemCheckboxNa"><span class="check-circle"></span></label>
                                                            </div>
                                                        </td>
                                                        <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status)}">
                                                       <td>
                                                           <div class="form-check">
                                                               ${item.incqDto.selfAnswer}
                                                       </div>
                                                       </td>
                                                        </c:if>
                                                        <td>
                                                            <textarea cols="35" rows="4" name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comFindNcs" id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comFindNcs" maxlength="500"><c:out value="${item.incqDto.ncs}"/></textarea>
                                                            <br/>
                                                            <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}comFindNcs" var = "err"/>
                                                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                        </td>
                                                        <td>
                                                            <textarea cols="35" rows="4" name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremark" id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comremark" maxlength="500"><c:out value="${item.incqDto.remark}"/></textarea>
                                                            <br/>
                                                            <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}comRemark" var = "err"/>
                                                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                        </td>
                                                        <td class="text-center">
                                                            <div id="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                <div class="form-check">
                                                                <input name="<c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>comrec" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>comrec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                                                </div>
                                                            </div>
                                                        </td>
                                                        <td>
                                                            <c:set value = "error_${item.incqDto.sectionNameShow}${item.incqDto.itemId}com" var = "err"/>
                                                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                          </td>
                                                      </tr>
                                                  </c:forEach>
                                                  </tbody>
                                              </table>
                                          </c:forEach>
                                      </div>
                                  </div>
                                  <div class="tab-pane ${(nowComTabIn== 'ServiceInfo') ? 'active' : ''}" id="ServiceInfo" role="tabpanel">
                                      <c:if test="${not empty serListDto.fdtoList}">
                                      <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.serviceDo}"/>/<c:out value="${serListDto.serviceTotal}"/><br>
                                      <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.serviceNc}"/>
                                      </c:if>
                                      <c:forEach var ="cdto" items ="${serListDto.fdtoList}" varStatus="one">
                                          <h3>${cdto.subType}</h3>
                                          <div class="table-gp">
                                              <c:forEach var ="section" items ="${cdto.sectionDtoList}" varStatus="two">
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
                                                          <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status)}">
                                                              <th >Self-assessment Answer</th>
                                                          </c:if>
                                                          <th>Findings/NCs</th>
                                                          <th>Actions Required</th>
                                                          <th class="text-center">Rectified</th>
                                                          <th></th>
                                                      </tr>
                                                      </thead>
                                                      <tbody>
                                                      <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                          <tr>
                                                              <td class="row_no">    <div class="form-check"><span>${(status.index + 1) }</span></div></td>
                                                              <td><div class="form-check"><a data-toggle="modal" data-target="#DeleteTemplateModalSer${item.incqDto.itemId}">${item.incqDto.regClauseNo}</a></div> </td>
                                                              <div class="modal fade" id="DeleteTemplateModalSer${item.incqDto.itemId}" tabindex="-1" role="dialog" aria-labelledby="regOutsideWindow" style="left: 50%;top: 50%;transform: translate(-50%,-50%);min-width:80%; overflow: visible;bottom: inherit;right: inherit;">
                                                                  <div class="modal-dialog modal-lg" role="document">
                                                                    <div class="modal-content">
<%--                                                                          <div class="modal-header">--%>
<%--                                                                              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>--%>
<%--                                                                              <div class="modal-title" style="font-size:2rem;"></div>--%>
<%--                                                                          </div>--%>
                                                                          <div class="modal-body">
                                                                              <div class="row"  style="height:500px;overflow:auto;">
                                                                                  <div class="col-md-8 col-md-offset-2" style="width: 100%; margin: 0;white-space:pre-wrap;"><span style="font-size: 2rem">${item.incqDto.regClause}</span></div>
                                                                              </div>
                                                                          </div>
                                                                          <div class="modal-footer">
                                                                              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                                                          </div>
                                                                      </div>
                                                                  </div>
                                                              </div>
                                                              <td><div class="form-check"><span>${item.incqDto.checklistItem}</span></div></td>
                                                              <c:set value = "${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "ckkId"/>
                                                              <td class="text-right">
                                                                  <div class="form-check">
                                                                  <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" class="form-check-input" />
                                                                      <label class="form-check-label" for="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxYes"><span class="check-circle"></span></label>
                                                                  </div>
                                                              </td>
                                                              <td class="text-right">
                                                                  <div class="form-check">
                                                                  <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" class="form-check-input" />
                                                                      <label class="form-check-label" for="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNo"><span class="check-circle"></span></label>
                                                                  </div>
                                                              </td>
                                                              <td class="text-right">
                                                                  <div class="form-check">
                                                                  <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" class="form-check-input" />
                                                                      <label class="form-check-label" for="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxNa"><span class="check-circle"></span></label>
                                                                  </div>
                                                              </td>
                                                              <c:if test="${ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK != applicationViewDto.applicationDto.applicationType && (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status)}">
                                                              <td>
                                                                  <div class="form-check">
                                                                      ${item.incqDto.selfAnswer}
                                                                 </div>
                                                              </td>
                                                              </c:if>
                                                              <td>
                                                                  <textarea cols="35" rows="4" name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>FindNcs" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxFindNcs" maxlength="500"><c:out value="${item.incqDto.ncs}"/></textarea>
                                                                  <br/>
                                                                  <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}FindNcs" var = "err"/>
                                                                  <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                              </td>
                                                              <td>
                                                                  <textarea cols="35" rows="4" name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>remark" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>itemCheckboxRemark" maxlength="500"><c:out value="${item.incqDto.remark}"/></textarea>
                                                                  <br/>
                                                                  <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}Remark" var = "err"/>
                                                                  <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                              </td>
                                                              <td class="text-center">
                                                                  <div id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>ck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                      <div class="form-check">
                                                                      <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameShow}"/><c:out value="${item.incqDto.itemId}"/>rec" id="<c:out value="${cdto.subName}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameShow}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec"/>
                                                                      </div>
                                                                  </div>
                                                              </td>
                                                              <td>
                                                                  <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameShow}${item.incqDto.itemId}" var = "err"/>
                                                                  <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                                  </td>
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
                                                          <th  width="35%">Item</th>
                                                          <th class="text-center">Yes</th>
                                                          <th class="text-center">No</th>
                                                          <th class="text-center">N/A</th>
                                                          <th>Findings/NCs</th>
                                                          <th>Actions Required</th>
                                                          <th class="text-center">Rectified</th>
                                                          <th></th>
                                                      </tr>
                                                      </thead>
                                                      <tbody>

                                                      <c:forEach var = "item" items = "${adchklDto.adItemList}" varStatus="status">
                                                          <tr>
                                                              <td class="row_no">   <div class="form-check">${(status.index + 1) }</div></td>
                                                              <td> <div class="form-check"><c:out value="${item.question}"/></div></td>
                                                              <c:set value = "${item.id}" var = "ckkId"/>
                                                               <td class="text-right">
                                                                   <div class="form-check"><input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'Yes'}">checked</c:if> value="Yes" class="form-check-input"/>
                                                                   <label class="form-check-label" for="<c:out value="${item.id}"/>adhocitemCheckboxYes"><span class="check-circle"></span></label>
                                                                   </div>
                                                               </td>
                                                              <td class="text-right">
                                                                  <div class="form-check">
                                                                  <input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'No'}">checked</c:if> value="No" class="form-check-input"/>
                                                                      <label class="form-check-label" for="<c:out value="${item.id}"/>adhocitemCheckboxNo"><span class="check-circle"></span></label>
                                                                  </div>
                                                              </td>
                                                              <td class="text-right">
                                                                  <div class="form-check">
                                                                  <input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'N/A'}">checked</c:if> value="N/A"  class="form-check-input"/>
                                                                      <label class="form-check-label" for="<c:out value="${item.id}"/>adhocitemCheckboxNa"><span class="check-circle"></span></label>
                                                                 </div>
                                                              </td>
                                                              <td>
                                                                  <textarea cols="35" rows="4" name="<c:out value="${item.id}"/>adhocFindNcs" id="<c:out value="${item.id}"/>adhocitemCheckboxFindNcs" id="" maxlength="500"><c:out value="${item.ncs}"/></textarea>
                                                                  <br/>
                                                                  <c:set value = "error_${item.id}adhocFindNcs" var = "err"/>
                                                                  <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                              </td>
                                                              <td>
                                                                  <textarea cols="35" rows="4" name="<c:out value="${item.id}"/>adhocremark" id="<c:out value="${item.id}"/>adhocitemCheckboxRemark" id="" maxlength="500"><c:out value="${item.remark}"/></textarea>
                                                                  <br/>
                                                                  <c:set value = "error_${item.id}adhocRemark" var = "err"/>
                                                                  <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                              </td>
                                                              <td class="text-center">
                                                                  <div id="<c:out value="${item.id}"/>ck"<c:if test="${item.adAnswer != 'No'}">hidden</c:if>>
                                                                      <div class="form-check">
                                                                      <input name="<c:out value="${item.id}"/>adhocrec" id="<c:out value="${item.id}"/>adhocrec" type="checkbox" <c:if test="${item.rectified}">checked</c:if> value="rec"/>
                                                                      </div>
                                                                  </div>

                                                              </td>
                                                              <td>
                                                                  <c:set value = "error_${item.id}adhoc" var = "err"/>
                                                                  <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                             </td>
                                                    </tr>
                                                </c:forEach>

                                                </tbody>
                                            </table>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-12">
                                    <a style="float:left;padding-top: 1.1%;" class="back" onclick="javascript:doBackToMain()"><em class="fa fa-angle-left"></em> Back</a>
                                  <c:if test="${inspectionNcCheckListDelegator_before_finish_check_list != '1'}">
                                   <div style="float:right">
                                    <c:if test="${ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION == applicationViewDto.applicationDto.status || ApplicationConsts.APPLICATION_STATUS_BEFORE_INSP_DATE_PENDING_INSPECTION == applicationViewDto.applicationDto.status}">
                                        <button class="btn btn-primary next" type="button" onclick="javascript:doBack()">Submit</button>
                                        <button class="btn btn-primary next" type="button" onclick="javascript:doSaveDraftCheckList();">Save Draft</button>
                                    </c:if>
                                   </div>
                                  </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
</div>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">
    function doBack(){
        showWaiting();
        $("#doSubmitAction").val("next");
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function doBackToMain(){
        showWaiting();
        $("#doSubmitAction").val("");
        SOP.Crud.cfxSubmit("mainForm", "back");
    }
    function showCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comId = str+'comck'
        divId  = divId.replace(/\s*/g,"");
        comId = comId.replace(/\s*/g,"");
        var comdivck =document.getElementById(divId);
        var divck =document.getElementById(comId);
        $("#"+divId).show();
        $("#"+comId).show();
    }
    function hideCheckBox(str){
        var name = str;
        var divId = str+'ck';
        var comdivId = str+'comck';
        divId  = divId.replace(/\s*/g,"");
        comdivId = comdivId.replace(/\s*/g,"");
        var divck =document.getElementById(divId);
        var comdivck =document.getElementById(comdivId);
        $("#"+divId).hide();
        $("#"+comdivId).hide();
    }

    function doSaveDraftCheckList(){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "saveDraft");
    }

    $(document).ready(function (){
        readOnlyAllForCheckList('${applicationViewDto.applicationDto.status}');
        var beforeFinishCheck = ${(inspectionNcCheckListDelegator_before_finish_check_list == null || inspectionNcCheckListDelegator_before_finish_check_list == "0") ? '0' : '1'};
        if( beforeFinishCheck == '1'){
            readOnlyAllForCheckListOnly();
        }
    });

   function readOnlyAllForCheckList(status) {
       if (status == 'APST032') {
           readOnlyAllForCheckListOnly();
       }
   }
    function readOnlyAllForCheckListOnly() {
               $("#checkLsitItemArea textarea").attr('readonly','readonly');
               $("#checkLsitItemArea textarea").attr('Enabled',false);
               $("#checkLsitItemArea input[type='checkbox']").attr("disabled",true);
               $("#checkLsitItemArea  input[type='radio']").attr("disabled",true);
     }
</script>

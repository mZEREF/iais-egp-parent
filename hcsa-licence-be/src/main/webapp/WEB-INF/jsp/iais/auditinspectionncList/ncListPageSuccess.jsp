<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <br>
    <br>
    <br>
    <br>
    <br>
    <input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.FillupChklistDelegator"/>
    <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto"/>
    <input type="hidden" name="valProfiles" id="valProfiles" value=""/>

    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp dashboard-tab">
                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                            <li class="<c:choose>
                                            <c:when test="${serListDto.checkListTab=='chkList'}">
                                                   complete
                                                </c:when>
                                                <c:otherwise>
                                                    active
                                                </c:otherwise>
                                            </c:choose>" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></li>
                            <li class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                                        data-toggle="tab">Documents</a></li>
                            <li class="<c:choose>
                                            <c:when test="${serListDto.checkListTab=='chkList'}">
                                                   active
                                                </c:when>
                                                <c:otherwise>
                                                    complete
                                                </c:otherwise>
                                            </c:choose>" role="presentation"><a href="#tabPayment" aria-controls="tabPayment" role="tab"
                                                                                data-toggle="tab">Success</a></li>
                        </ul>
                        <div class="tab-nav-mobile visible-xs visible-sm">
                            <div class="swiper-wrapper" role="tablist">
                                <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></div>
                                <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a></div>
                                <div class="swiper-slide"><a href="#tabPayment" aria-controls="tabPayment" role="tab" data-toggle="tab">Payment</a></div>
                            </div>
                            <div class="swiper-button-prev"></div>
                            <div class="swiper-button-next"></div>
                        </div>
                        <div class="tab-content">
                            <div class="tab-pane  <c:if test="${serListDto.checkListTab!='chkList'}">active</c:if>" id="tabInfo" role="tabpanel">
                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="panel-heading"><strong>Submission Details</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table aria-describedby="" class="table table-bordered">
                                                    <thead style="display: none">
                                                    <tr>
                                                        <th scope="col" ></th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Application No. (Overall)
                                                        </td>
                                                        <td class="col-xs-6">${applicationViewDto.applicationNoOverAll}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Application No.</td>
                                                        <td>${applicationViewDto.applicationDto.applicationNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Application Type</td>
                                                        <td>${applicationViewDto.applicationType}</td>
                                                    </tr>
                                                    <tr>
                                              <td align="right">Service Type</td>
                                                        <td>${applicationViewDto.serviceType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Submission Date</td>
                                                        <td>${applicationViewDto.submissionDate}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Current Status</td>
                                                        <td>${applicationViewDto.currentStatus}</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div align="center">
                                    <a href="/hcsa-licence-web/eservice/INTRANET/LicenceBEViewService?appId=${applicationViewDto.applicationDto.id}" target="_blank">
                                        <button type="button" class="btn btn-primary">
                                            View Application
                                        </button>
                                    </a>
                                </div>
                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><strong>Applicant Details</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table aria-describedby="" class="table table-bordered">
                                                    <thead style="display: none">
                                                    <tr>
                                                        <th scope="col" ></th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">HCI Code</td>
                                                        <td class="col-xs-6">-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">HCI Name</td>
                                                        <td>${applicationViewDto.hciName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">HCI Address</td>
                                                        <td>${applicationViewDto.hciAddress}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Telephone</td>
                                                        <td>${applicationViewDto.telephone}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Fax</td>
                                                        <td>-</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="tab-pane" id="tabDocuments" role="tabpanel">
                                <div class="alert alert-info" role="alert"><strong>
                                    <h4>Supporting Document</h4>
                                </strong></div>
                                <div id="u8522_text" class="text ">
                                    <p><span>These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed
												documents are those defined for this digital service only.</span></p>
                                </div>
                                <div class="row">
                                    <div class="col-xs-12">
                                        <div class="table-gp">
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr>
                                                    <th scope="col" >Document</th>
                                                    <th scope="col" >File</th>
                                                    <th scope="col" >Size</th>
                                                    <th scope="col" >Submitted By</th>
                                                    <th scope="col" >Date Submitted</th>
                                                </tr>
                                                </thead>

                                                <tbody>
                                                <c:forEach items="${applicationViewDto.appSupDocDtoList}"
                                                           var="appSupDocDto">
                                                    <tr>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.file}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><a href="#"><c:out value="${appSupDocDto.document}"></c:out></a></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.size}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.submittedBy}"></c:out></p>
                                                        </td>
                                                        <td>
                                                            <p><c:out value="${appSupDocDto.dateSubmitted}"></c:out></p>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>

                                            </table>
                                            <div class="alert alert-info" role="alert"><strong>
                                                <h4>Internal Document</h4>
                                            </strong></div>
                                            <div class="text ">
                                                <p><span>These are documents uploaded by an agency officer to support back office processing.</span>
                                                </p>
                                            </div>
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr>
                                                    <th scope="col" >Document</th>
                                                    <th scope="col" >File</th>
                                                    <th scope="col" >Size</th>
                                                    <th scope="col" >Submitted By</th>
                                                    <th scope="col" >Date Submitted</th>
                                                    <th scope="col" >Action</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td colspan="5" align="center">
                                                        <p>No record found.</p>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="tab-pane <c:if test="${serListDto.checkListTab=='chkList'}">active</c:if>" id="tabPayment" role="tabpanel">
                                <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                    <li class="complete" role="presentation"><a href="#General" aria-controls="General" role="tab" data-toggle="tab">General</a></li>
                                    <li class="complete" role="presentation"><a href="#ServiceInfo" aria-controls="ServiceInfo" role="tab"
                                                                                data-toggle="tab"><c:out value="${serListDto.serviceName}"/></a></li>
                                    <li class="complete" role="presentation"><a href="#chkInfo" aria-controls="ServiceInfo" role="tab"
                                                                                data-toggle="tab">Other Info</a></li>
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

                                <div class="tab-content">
                                    <div class="tab-pane active" id="General" role="tabpanel">
                                        <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.generalDo}"/>/<c:out value="${serListDto.generalTotal}"/><br>
                                        <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.generalNc}"/>
                                        <h3>General</h3>
                                        <div class="table-gp">
                                            <c:forEach var ="section" items ="${commonDto.sectionDtoList}">
                                                <br/>
                                                <h4><c:out value="${section.sectionName}"></c:out></h4>
                                                <table aria-describedby="" class="table">
                                                    <thead>
                                                    <tr>
                                                        <th scope="col" >No.</th>
                                                        <th scope="col" >Regulation Clause Number</th>
                                                        <th scope="col" >Item</th>
                                                        <th scope="col" >Yes</th>
                                                        <th scope="col" >No</th>
                                                        <th scope="col" >N/A</th>
                                                        <th scope="col" >Remark</th>
                                                        <th scope="col" >Rectified</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                        <tr>
                                                            <td class="row_no">${(status.index + 1) }</td>
                                                            <td>${item.incqDto.regClauseNo}</td>
                                                            <td>${item.incqDto.checklistItem}</td>
                                                            <c:set value = "${item.incqDto.sectionName}${item.incqDto.itemId}" var = "ckkId"/>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" disabled/></td>
                                                            <td>
                                                                <input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" disabled/>
                                                            </td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrad" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" disabled/></td>
                                                            <td><input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comremark" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comitemCheckboxRemark" type="text" value="<c:out value="${item.incqDto.remark}"/>" disabled/></td>
                                                            <td>
                                                                <div id="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                    <input name="<c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>comrec" id="<c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>comrec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec" disabled/>
                                                                </div>
                                                                <c:set value = "error_${item.incqDto.sectionName}${item.incqDto.itemId}com" var = "err"/>
                                                                <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </c:forEach>
                                        </div>
                                    </div>
                                    <div class="tab-pane" id="ServiceInfo" role="tabpanel">
                                        <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.serviceDo}"/>/<c:out value="${serListDto.serviceTotal}"/><br>
                                        <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.serviceNc}"/>
                                        <c:forEach var ="cdto" items ="${serListDto.fdtoList}" varStatus="status">
                                            <h3>${cdto.subType}</h3>
                                            <div class="table-gp">
                                                <c:forEach var ="section" items ="${cdto.sectionDtoList}">
                                                    <br/>
                                                    <h4><c:out value="${section.sectionName}"></c:out></h4>
                                                    <table aria-describedby="" class="table">
                                                        <thead>
                                                        <tr>
                                                            <th scope="col" >No.</th>
                                                            <th scope="col" >Regulation Clause Number</th>
                                                            <th scope="col" >Item</th>
                                                            <th scope="col" >Yes</th>
                                                            <th scope="col" >No</th>
                                                            <th scope="col" >N/A</th>
                                                            <th scope="col" >Remark</th>
                                                            <th scope="col" >Rectified</th>
                                                        </tr>
                                                        </thead>
                                                        <tbody>
                                                        <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                            <tr>
                                                                <td class="row_no">${(status.index + 1) }</td>
                                                                <td>${item.incqDto.regClauseNo}</td>
                                                                <td>${item.incqDto.checklistItem}</td>
                                                                <c:set value = "${cdto.subName}${item.incqDto.sectionName}${item.incqDto.itemId}" var = "ckkId"/>
                                                                <td><input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'Yes'}">checked</c:if> value="Yes" disabled/></td>
                                                                <td>
                                                                    <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'No'}">checked</c:if> value="No" disabled/>
                                                                </td>
                                                                <td><input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rad" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.incqDto.chkanswer eq'N/A'}">checked</c:if> value="N/A" disabled/></td>
                                                                <td><input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>remark" id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>itemCheckboxRemark" type="text" value="<c:out value="${item.incqDto.remark}"/>" disabled/></td>
                                                                <td>
                                                                    <div id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>ck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                        <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionName}"/><c:out value="${item.incqDto.itemId}"/>rec" id="<c:out value="${cdto.subName}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionName}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec" disabled/>
                                                                    </div>
                                                                    <c:set value = "error_${cdto.subName}${item.incqDto.sectionName}${item.incqDto.itemId}" var = "err"/>
                                                                    <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </c:forEach>
                                            </div>
                                        </c:forEach>
                                        <span><strong>do/total:</strong></span>&nbsp;<c:out value="${serListDto.adhocDo}"/>/<c:out value="${serListDto.adhocTotal}"/><br>
                                        <span><strong>No of Non-Compliance:</strong></span>&nbsp;<c:out value="${serListDto.adhocNc}"/>
                                        <div class="table-gp">
                                            <h3>Adhoc</h3>
                                            <br/>
                                            <h4></h4>
                                            <table aria-describedby="" class="table">
                                                <thead>
                                                <tr>
                                                    <th scope="col" >No.</th>
                                                    <th scope="col" >Item</th>
                                                    <th scope="col" >Yes</th>
                                                    <th scope="col" >No</th>
                                                    <th scope="col" >N/A</th>
                                                    <th scope="col" >Remark</th>
                                                    <th scope="col" >Rectified</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var = "item" items = "${adchklDto.adItemList}" varStatus="status">
                                                    <tr>
                                                        <td class="row_no">${(status.index + 1) }</td>

                                                        <td><c:out value="${item.question}"/></td>
                                                        <c:set value = "${item.id}" var = "ckkId"/>
                                                        <td><input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxYes" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'Yes'}">checked</c:if> value="Yes" disabled/></td>
                                                        <td>
                                                            <input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNo"  onclick="showCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'No'}">checked</c:if> value="No" disabled/>
                                                        </td>
                                                        <td><input name="<c:out value="${item.id}"/>adhocrad" id="<c:out value="${item.id}"/>adhocitemCheckboxNa" onclick="hideCheckBox('${ckkId}')" type="radio" <c:if test="${item.adAnswer eq'N/A'}">checked</c:if> value="N/A" disabled/></td>
                                                        <td><input name="<c:out value="${item.id}"/>adhocremark" id="<c:out value="${item.id}"/>adhocitemCheckboxRemark" type="text" value="<c:out value="${item.remark}"/>" disabled/></td>
                                                        <td>
                                                            <div id="<c:out value="${item.id}"/>ck"<c:if test="${item.adAnswer != 'No'}">hidden</c:if>>
                                                                <input name="<c:out value="${item.id}"/>adhocrec" id="<c:out value="${item.id}"/>adhocrec" type="checkbox" <c:if test="${item.rectified}">checked</c:if> value="rec" disabled/>
                                                            </div>
                                                            <c:set value = "error_${item.id}adhoc" var = "err"/>
                                                            <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                    <div class="tab-pane" id="chkInfo" role="tabpanel">
                                        <div class="col-xs-12">
                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Inspection Date</h4> <c:out value="${serListDto.inspectionDate}"/>
                                                </div>
                                            </div>
                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Inspection Start Time (HH MM)</h4> <c:out value="${serListDto.inspectionStartDate}"/>
                                                </div>
                                            </div>

                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Inspection End Time (HH MM)</h4> <c:out value="${serListDto.inspectionEndDate}"/>
                                                </div>
                                            </div>
                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Inspection Leader</h4> <c:out value="${serListDto.inspectionLeader}"/>
                                                </div>
                                            </div>

                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Inspection Officer(s)</h4>
                                                    <c:forEach var = "officer" items = "${serListDto.inspectionofficer}" varStatus="status">
                                                        <c:out value="${officer}"/>
                                                    </c:forEach>
                                                </div>
                                            </div>

                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Other Inspection Officer(s)</h4>
                                                    <textarea cols="70" rows="7" name="otherinspector" id="otherinspector" disabled maxlength="300"><c:out value="${serListDto.otherinspectionofficer}"></c:out></textarea>
                                                    <span class="error-msg" id="error_otherofficer" name="iaisErrorMsg"></span>
                                                </div>
                                            </div>

                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Best Practices</h4>
                                                    <textarea cols="70" rows="7" name="bestpractice" id="bestpractice" disabled maxlength="500"><c:out value="${serListDto.bestPractice}"></c:out></textarea>
                                                    <span class="error-msg" id="error_bestPractice" name="iaisErrorMsg"></span>
                                                </div>
                                            </div>
                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>TCU Date</h4> &nbsp; <c:out value="${serListDto.tuc}"/><br>
                                                    <span class="error-msg" id="error_tcuDate" name="iaisErrorMsg"></span>
                                                </div>
                                            </div>
                                            <div class="input-group">
                                                <div class="ax_default text_area">
                                                    <h4>Remarks</h4> <textarea cols="70" rows="7" name="tcuRemark" id="tcuRemark"  maxlength="300" disabled><c:out value="${serListDto.tcuRemark}"></c:out></textarea>
                                                    <span class="error-msg" id="error_tcuRemark" name="iaisErrorMsg"></span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">

</script>

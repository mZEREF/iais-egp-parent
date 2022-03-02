<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#transferInOutDetails">
                Transfer In And Out
            </a>
        </h4>
    </div>
    <div id="transferInOutDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="transferInOutStageDto" value="${arSuperDataSubmissionDto.transferInOutStageDto}" />
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Is this a Transfer In or Out?"/>
                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                        <c:if test="${transferInOutStageDto.transferType eq 'in'}">
                          <c:out value=" Transfer In"/>
                        </c:if>
                        <c:if test="${transferInOutStageDto.transferType eq'out'}">
                            <c:out value=" Transfer Out"/>
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="What was Transferred?"/>
                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                        <c:forEach items="${transferInOutStageDto.transferredList}" var="transferred" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${transferred}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>
                        <c:forEach var="transferredObj" items="${transferInOutStageDto.transferredList}">
                            <c:if test="${transferredObj =='AR_WWT_001'}">
                             <iais:row>
                                 <label class="col-xs-5 col-md-4 control-label">No. of Oocyte(s) Transferred
                                     <c:if test="${diffOocyte}">
                                         <a class="btn-tooltip styleguide-tooltip flag2" href="javascript:void(0);"
                                            data-toggle="tooltip"
                                            data-html="true"
                                            title="&lt;p&gt;<iais:message key="DS_ERR056"/>&lt;/p&gt;">!</a>
                                     </c:if>
                                 </label>
                                 <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                                     <c:out value="${transferInOutStageDto.oocyteNum}"/>
                                 </iais:value>
                             </iais:row>
                            </c:if>
                            <c:if test="${transferredObj =='AR_WWT_002'}">
                                <iais:row>
                                    <label class="col-xs-5 col-md-4 control-label">No. of Embryo(s) Transferred
                                        <c:if test="${diffEmbryo}">
                                            <a class="btn-tooltip styleguide-tooltip flag2" href="javascript:void(0);"
                                               data-toggle="tooltip"
                                               data-html="true"
                                               title="&lt;p&gt;<iais:message key="DS_ERR056"/>&lt;/p&gt;">!</a>
                                        </c:if>
                                    </label>
                                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                                        <c:out value="${transferInOutStageDto.embryoNum}"/>
                                    </iais:value>
                                </iais:row>
                            </c:if>
                            <c:if test="${transferredObj =='AR_WWT_003'}">
                                <iais:row>
                                    <label class="col-xs-5 col-md-4 control-label">Vials of Sperm Transferred
                                        <c:if test="${diffSpermVial}">
                                            <a class="btn-tooltip styleguide-tooltip flag2" href="javascript:void(0);"
                                               data-toggle="tooltip"
                                               data-html="true"
                                               title="&lt;p&gt;<iais:message key="DS_ERR056"/>&lt;/p&gt;">!</a>
                                        </c:if>
                                    </label>
                                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                                        <c:out value="${transferInOutStageDto.spermVialsNum}"/>
                                    </iais:value>
                                </iais:row>
                            </c:if>
                        </c:forEach>
                <iais:row>
                    <iais:field width="5" value="Were the Gamete(s) or Embryo(s) from a Donor?" />
                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                            <c:out value= "${transferInOutStageDto.fromDonor ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>
                <div class="inFromParts" <c:if test="${transferInOutStageDto.transferType !='in'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Transferred In From" />
                        <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                            <c:set value="${transferInOutStageDto.transInFromHciCode eq 'Others'
                                            ?transferInOutStageDto.transInFromHciCode:
                                            transferInOutStageDto.transInFromLicenseeId.concat('/').concat(transferInOutStageDto.transInFromHciCode)}"
                                   var="selecctInValue"/>
                            <c:forEach items="${transferOutInPremisesSelect}" var="premisesSelect" varStatus="s">
                                <c:if test="${premisesSelect.value eq selecctInValue}">
                                    <c:out value="${premisesSelect.text}"/>
                                </c:if>
                            </c:forEach>
                        </iais:value>
                    </iais:row>
                    <iais:row style="${transferInOutStageDto.transInFromHciCode eq'Others' ? '' : 'display:none;'}">
                        <iais:field width="5" value="Transferred In From (Others)"/>
                        <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                            <c:out value="${transferInOutStageDto.transInFromOthers}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div class="outFromParts" <c:if test="${transferInOutStageDto.transferType !='out'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Transfer Out To" />
                        <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                            <c:set value="${transferInOutStageDto.transOutToHciCode eq 'Others'
                                            ?transferInOutStageDto.transOutToHciCode
                                            :transferInOutStageDto.transOutToLicenseeId.concat('/').concat(transferInOutStageDto.transOutToHciCode)}"
                                   var="selecctOutValue"/>
                            <c:forEach items="${transferOutInPremisesSelect}" var="premisesSelect" varStatus="s">
                                <c:if test="${premisesSelect.value eq selecctOutValue}">
                                    <c:out value="${premisesSelect.text}"/>
                                </c:if>
                            </c:forEach>
                        </iais:value>
                    </iais:row>
                    <iais:row style="${transferInOutStageDto.transOutToHciCode eq'Others' ? '' : 'display:none;'}">
                        <iais:field width="5" value="Transfer Out To (Others)"/>
                        <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                            <c:out value="${transferInOutStageDto.transOutToOthers}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Date of Transfer" />
                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                        <c:out value="${transferInOutStageDto.transferDate}"/>
                    </iais:value>
                </iais:row>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#previewInfo">
                Approval Info
            </a>
        </h4>
    </div>
    <div id="previewInfo" class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>">
        <div class="panel-body">
            <p><div class="text-right app-font-size-16"><a href="#" id="subLicenseeEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
            <div class="panel-main-content form-horizontal min-row">
                <div class="licensee-com">
                    <iais:row>
                        <iais:field value="Facility Name" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${approvalApplicationDto.facilityName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Schedule" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${approvalApplicationDto.schedule}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field value="List of Agents/Toxins" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${approvalApplicationDto.listOfAgentsOrToxins}" />
                        </iais:value>
                    </iais:row>
                    <c:if test="${taskList=='Approval To Possess'}">
                        <iais:row>
                            <iais:field value="Nature of the Sample" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.sampleNature}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Others, please specify" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.sampleNatureOth}" />
                            </iais:value>
                        </iais:row>
                    </c:if>
                    <c:if test="${taskList=='Approval To LargeScaleProduce'}">
                        <iais:row>
                            <iais:field value="Estimated maximum volume (in litres) of production at any one time" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.productionMaximumVolumeLitres}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Method or system used for large scale production " width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.largeScaleProductionMethod}" />
                            </iais:value>
                        </iais:row>
                    </c:if>
                    <c:if test="${(taskList=='Approval To Possess' || taskList=='Approval To LargeScaleProduce')}">
                        <iais:row>
                            <iais:field value="Mode of Procurement" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.procurementMode}" />
                            </iais:value>
                        </iais:row>
                        <c:if test="${approvalApplicationDto.procurementMode=='BMOP001'}">
                            <iais:row>
                                <iais:field value="Transfer From Facility Name" width="5"/>
                                <iais:value width="7" display="true">
                                    <c:out value="${approvalApplicationDto.facilityTransferFrom}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Expected Date of Transfer" width="5"/>
                                <iais:value width="7" display="true">
                                    <c:out value="${approvalApplicationDto.transferExpectedDt}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Contact Person from Transferring Facility" width="5"/>
                                <iais:value width="7" display="true">
                                    <c:out value="${approvalApplicationDto.importContactPersonName}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Contact No of Contact Person from Transferring Facility" width="5"/>
                                <iais:value width="7" display="true">
                                    <c:out value="${approvalApplicationDto.importContactPersonNo}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Email Address of Contact Person from Transferring Facility" width="5"/>
                                <iais:value width="7" display="true">
                                    <c:out value="${approvalApplicationDto.importContactPersonEmail}" />
                                </iais:value>
                            </iais:row>
                        </c:if>
                        <c:if test="${approvalApplicationDto.procurementMode=='BMOP002'}">
                            <iais:row>
                                <iais:field value="Overseas Facility Name" width="5"/>
                                <iais:value width="7" display="true">
                                    <c:out value="${approvalApplicationDto.facilityTransferFrom}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Expected Date of Import" width="5"/>
                                <iais:value width="7" display="true">
                                    <c:out value="${approvalApplicationDto.transferExpectedDt}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Contact person from Source Facility" width="5"/>
                                <iais:value width="7" display="true">
                                    <c:out value="${approvalApplicationDto.importContactPersonName}" />
                                </iais:value>
                            </iais:row>
                            <iais:row>
                                <iais:field value="Email address of Contact person from Source Facility" width="5"/>
                                <iais:value width="7" display="true">
                                    <c:out value="${approvalApplicationDto.importContactPersonEmail}" />
                                </iais:value>
                            </iais:row>
                        </c:if>
                        <iais:row>
                            <iais:field value="Facility Address 1" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.transferFacilityAddr1}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Facility Address 2" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.transferFacilityAddr2}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Facility Address 3" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.transferFacilityAddr3}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Country" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.transferCountry}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="City" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.transferCity}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="State" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.transferState}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Postal Code" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.transferPostalCode}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Name of Courier Service Provider" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.courierServiceProviderName}" />
                            </iais:value>
                        </iais:row>
                    </c:if>
                    <c:if test="${taskList=='Approval To Special'}">
                        <iais:row>
                            <iais:field value="Name of Project" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.projectName}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Name of Principal Investigator" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.principalInvestigatorName}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Intended Work Activity" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.workActivityIntended}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="Start Date" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.startDt}" />
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field value="End Date" width="5"/>
                            <iais:value width="7" display="true">
                                <c:out value="${approvalApplicationDto.endDt}" />
                            </iais:value>
                        </iais:row>
                    </c:if>
                    <iais:row>
                        <iais:field value="Remarks" width="5"/>
                        <iais:value width="7" display="true">
                            <c:out value="${approvalApplicationDto.remarks}" />
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>
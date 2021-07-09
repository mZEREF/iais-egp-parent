<div class="panel panel-default">
    <div class="panel-heading" id="headingOne" role="tab">
        <h4 class="panel-title">
            <a class="collapsed" role="button" data-toggle="collapse" href="#keyRoles" aria-expanded="true"
               aria-controls="collapseOne">Key Roles</a>
        </h4>
    </div>
    <div class="panel-collapse collapse" id="keyRoles" role="tabpanel" aria-labelledby="headingOne">
        <div class="panel-body">
            <p class="text-right">
                <!--<input class="form-check-input" id="primaryCheckbox" type="checkbox" name="editCheckbox" aria-invalid="false" value="primary">-->
            </p>
            <div class="elemClass-1561088919456">
                <div id="control--runtime--34" class="page section control container-s-1" style="margin: 10px 0px">
                    <div class="control-set-font control-font-header section-header">
                        <%--  <p class="summary-header">Licensee (Company)</p>--%>
                    </div>
                    <div class="pop-up">
                        <div class="pop-up-body">
                            <div class="field col-sm-12 control-label formtext">

                                <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                                    <div class="col-md-6">
                                        <label>Licensee Information</label>
                                    </div>
                                    <div class="col-md-6">
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-8">
                                        Who is the Licensee?
                                    </div>
                                    <div class="col-md-6">
                                        Local Company
                                    </div>
                                </div>

                                <div class="row img-show">
                                    <div class="col-md-6">
                                        UEN Number
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.uenNo}">${newLicenceDto.uenNo}
                                              <c:if test="${empty hashMap[newLicenceDto.uenNo]}">
                                                  <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                              </c:if>
                                              <c:if test="${not empty hashMap[newLicenceDto.uenNo]}">
                                                  <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                              </c:if>
                                            </span>
                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " style="display: none" attr="${oldLicenceDto.uenNo}">
                                              ${oldLicenceDto.uenNo}
                                                   <c:if test="${empty hashMap[oldLicenceDto.uenNo]}">
                                                       <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                                   </c:if>
                                              <c:if test="${not empty hashMap[oldLicenceDto.uenNo]}">
                                                  <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                              </c:if>
                                            </span>
                                        </div>
                                    </div>
                                </div>
                                <c:if test="${not empty hashMap[newLicenceDto.uenNo]}">
                                    <div class="row new-img-show" >
                                        <div class="col-xs-12 col-md-12" style="position: absolute;z-index: 100;background-color: #F5F5F5">
                                            <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                            <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                                <tr>
                                                    <td>Indentification No.</td>
                                                    <td>Case No.</td>
                                                    <td>Case Type Description</td>
                                                    <td>Case Status Description</td>
                                                    <td>Offence Description</td>
                                                    <td>Outcome Description</td>
                                                    <td>Outcome Issue Date</td>
                                                    <td>Prosecution Outcome Description</td>
                                                    <td>Created Date</td>
                                                    <td>Update Date</td>
                                                </tr>
                                                <c:forEach items="${hashMap[newLicenceDto.uenNo]}" var="map">
                                                    <tr>
                                                        <td>${map.identificationNo}</td>
                                                        <td>${map.caseNo}</td>
                                                        <td>${map.caseType}</td>
                                                        <td>Case Status Description</td>
                                                        <td>${map.offenceDesc}</td>
                                                        <td>${map.outcome}</td>
                                                        <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                        <td>${map.prosecutionOutcome}</td>
                                                        <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                        <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                    </tr>
                                                </c:forEach>
                                                <tr></tr>

                                            </table>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${not empty hashMap[oldLicenceDto.uenNo]}">
                                    <div class="row old-img-show" >
                                        <div class="col-xs-12 col-md-12" style="position: absolute;z-index: 100;background-color: #F5F5F5">
                                            <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                            <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                                <tr>
                                                    <td>Indentification No.</td>
                                                    <td>Case No.</td>
                                                    <td>Case Type Description</td>
                                                    <td>Case Status Description</td>
                                                    <td>Offence Description</td>
                                                    <td>Outcome Description</td>
                                                    <td>Outcome Issue Date</td>
                                                    <td>Prosecution Outcome Description</td>
                                                    <td>Created Date</td>
                                                    <td>Update Date</td>
                                                </tr>
                                                <c:forEach items="${hashMap[oldLicenceDto.uenNo]}" var="map">
                                                    <tr>
                                                        <td>${map.identificationNo}</td>
                                                        <td>${map.caseNo}</td>
                                                        <td>${map.caseType}</td>
                                                        <td>Case Status Description</td>
                                                        <td>${map.offenceDesc}</td>
                                                        <td>${map.outcome}</td>
                                                        <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                        <td>${map.prosecutionOutcome}</td>
                                                        <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                        <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                    </tr>
                                                </c:forEach>
                                                <tr></tr>

                                            </table>
                                        </div>
                                    </div>
                                </c:if>
                                <div class="row">
                                    <div class="col-md-6">
                                        Name of Licensee
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6 ">
                                                <span class="newVal " attr="${newLicenceDto.name}">
                                                  <c:out value="${newLicenceDto.name}"/>
                                                </span>
                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " attr="${oldLicenceDto.name}" style="display: none"><c:out value="${oldLicenceDto.name}"/></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        Postal Code
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.postalCode}"><c:out value="${newLicenceDto.postalCode}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " attr="${oldLicenceDto.postalCode}" style="display: none"><c:out value="${oldLicenceDto.postalCode}"/></span>

                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        Address Type
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.addrType}"><c:out value="${newLicenceDto.addrType}"/></span>

                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal" attr="${oldLicenceDto.addrType}" style="display: none"><c:out value="${oldLicenceDto.addrType}"/></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        Blk No.
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.unitNo}"><c:out value="${newLicenceDto.unitNo}"/></span>

                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " attr="${oldLicenceDto.unitNo}" style="display: none"><c:out value="${oldLicenceDto.unitNo}"/></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        Floor No.
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.floorNo}"><c:out value="${newLicenceDto.floorNo}"/></span>

                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " attr="${oldLicenceDto.floorNo}" style="display: none"><c:out value="${oldLicenceDto.floorNo}"/></span>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        Unit No.
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.unitNo}"><c:out value="${newLicenceDto.unitNo}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " attr="${oldLicenceDto.unitNo}" style="display: none">${oldLicenceDto.unitNo}</span>

                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        Street Name
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.streetName}"><c:out value="${newLicenceDto.streetName}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " attr="${oldLicenceDto.streetName}" style="display: none"><c:out value="${oldLicenceDto.streetName}"/></span>

                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        Building Name
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.buildingName}"><c:out value="${newLicenceDto.buildingName}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " attr="${oldLicenceDto.buildingName}" style="display: none"><c:out value="${oldLicenceDto.buildingName}"/></span>

                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        Office Telephone No.
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.officeTelNo}"><c:out value="${newLicenceDto.officeTelNo}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " attr="${oldLicenceDto.officeTelNo}" style="display: none"><c:out value="${oldLicenceDto.officeTelNo}"/></span>

                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        Office Email Address
                                    </div>
                                    <div class="col-md-6">
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${newLicenceDto.emilAddr}"><c:out value="${newLicenceDto.emilAddr}"/></span>
                                        </div>
                                        <div class="col-md-6">
                                            <span class="oldVal " attr="${oldLicenceDto.emilAddr}" style="display: none"><c:out value="${oldLicenceDto.emilAddr}"/></span>

                                        </div>
                                    </div>
                                </div>
                                <c:forEach items="${appSubmissionDto.boardMember}" var="Board" varStatus="status">
                                    <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                                        <div class="col-md-6"><label>Board Member ${status.index+1}</label></div>
                                    </div>
                                    <div class="row">
                                        <div class="col-md-6">
                                            Salutation
                                        </div>
                                        <div class="col-md-6">
                                            <span class="newVal " attr="${Board.salutation}"><c:out value="${Board.salutation}"/></span>
                                        </div>
                                    </div>


                                    <div class="row">
                                        <div class="col-md-6">
                                            Name
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-md-6">
                                                <span class="newVal " attr="${Board.name}">${Board.name}</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row img-show">
                                        <div class="col-md-6">
                                            ID No.
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-md-6">
                                              <span class="newVal " attr="${Board.idNo}">${Board.idNo}
                                          <%--      <c:if test="${empty hashMap[Board.idNo]}">--%>
                                                  <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                            <%--    </c:if>
                                                <c:if test="${not empty hashMap[Board.idNo]}">
                                                  <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                                </c:if>--%>
                                              </span>
                                            </div>
                                        </div>
                                    </div>
                                    <%--     <c:if test="${not empty hashMap[Board.idNo]}">
                                           <div class="row new-img-show" style="display: none">
                                             <div  style="position: absolute;z-index: 100;background-color: #F5F5F5;width: 140%">
                                               <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                               <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                                 <tr>
                                                   <td>Indentification No.</td>
                                                   <td>Case No.</td>
                                                   <td>Case Type Description</td>
                                                   <td>Case Status Description</td>
                                                   <td>Offence Description</td>
                                                   <td>Outcome Description</td>
                                                   <td>Outcome Issue Date</td>
                                                   <td>Prosecution Outcome Description</td>
                                                   <td>Created Date</td>
                                                   <td>Update Date</td>
                                                 </tr>
                                                 <c:forEach items="${hashMap[Board.idNo]}" var="map">
                                                   <tr>
                                                     <td>${map.identificationNo}</td>
                                                     <td>${map.caseNo}</td>
                                                     <td>${map.caseType}</td>
                                                     <td>${map.caseStatus}</td>
                                                     <td>${map.offenceDesc}</td>
                                                     <td>${map.outcome}</td>
                                                     <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                     <td>${map.prosecutionOutcome}</td>
                                                     <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                     <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                   </tr>
                                                 </c:forEach>
                                                 <tr></tr>

                                               </table>
                                             </div>
                                           </div>
                                         </c:if>--%>
                                    <div class="row">
                                        <div class="col-md-6">
                                            Designation
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-md-6">
                                                <span  class="newVal " attr="${Board.designation}"><iais:code code="${Board.designation}"/></span>
                                            </div>
                                        </div>
                                    </div>


                                </c:forEach>

                                <c:forEach items="${appSubmissionDto.authorisedPerson}" var="Authorised" varStatus="status">
                                    <div class="row" style="margin-top: 1%;margin-bottom: 1%">
                                        <div class="col-md-6">
                                            <label>Authorised Person ${status.index+1}</label>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            Name
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-md-6">
                                                <span  class="newVal " attr="${Authorised.displayName}">${Authorised.displayName}</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row img-show">
                                        <div class="col-md-6">
                                            ID No.
                                        </div>
                                        <div class="col-md-6">
                                            <div  class="col-md-6">
                                            <span  class="newVal " attr="${Authorised.idNumber}">${Authorised.idNumber}
                                            <%--  <c:if test="${empty hashMap[Authorised.idNumber]}">--%>
                                                <img src="/hcsa-licence-web/img/20200707152208.png" width="25" height="25" alt="NETS">
                                             <%-- </c:if>
                                              <c:if test="${not empty hashMap[Authorised.idNumber]}">
                                                <img src="/hcsa-licence-web/img/2020109171436.png" onclick="showThisTableNew(this)" width="25" height="25" alt="NETS">
                                              </c:if>--%>
                                            </span>
                                            </div>
                                        </div>
                                    </div>
                                    <%-- <c:if test="${not empty hashMap[Authorised.idNumber]}">
                                       <div class="row new-img-show" style="display:none;">
                                         <div style="position: absolute;z-index: 100;background-color: #F5F5F5;width: 140%">
                                           <label style="font-weight: normal">The Professional has existing disciplinary records in HERIMS</label><span style="position: absolute;right: 0px;color: black" onclick="closeThis(this)">X</span>
                                           <table   border="1px" style="border-collapse: collapse;border-top: 0px solid #000000;padding: 8px;background-color: #ffffff;text-align: center">
                                             <tr>
                                               <td>Indentification No.</td>
                                               <td>Case No.</td>
                                               <td>Case Type Description</td>
                                               <td>Case Status Description</td>
                                               <td>Offence Description</td>
                                               <td>Outcome Description</td>
                                               <td>Outcome Issue Date</td>
                                               <td>Prosecution Outcome Description</td>
                                               <td>Created Date</td>
                                               <td>Update Date</td>
                                             </tr>
                                             <c:forEach items="${hashMap[Authorised.idNumber]}" var="map">
                                               <tr>
                                                 <td>${map.identificationNo}</td>
                                                 <td>${map.caseNo}</td>
                                                 <td>${map.caseType}</td>
                                                 <td>${map.caseStatus}</td>
                                                 <td>${map.offenceDesc}</td>
                                                 <td>${map.outcome}</td>
                                                 <td><fmt:formatDate value="${map.issueDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                 <td>${map.prosecutionOutcome}</td>
                                                 <td><fmt:formatDate value="${map.createdDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                                 <td><fmt:formatDate value="${map.updatedDate}" pattern="dd/MM/yyyy"></fmt:formatDate></td>
                                               </tr>
                                             </c:forEach>
                                             <tr></tr>

                                           </table>
                                         </div>
                                       </div>
                                     </c:if>--%>


                                    <div class="row">
                                        <div class="col-md-6">
                                            Designation
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-md-6">
                                                <span  class="newVal " attr="${Authorised.designation}"><iais:code code="${Authorised.designation}"/></span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            Office Telephone
                                        </div>
                                        <div class="col-md-6">
                                            <div  class="col-md-6">
                                                <span  class="newVal " attr="${Authorised.officeTelNo}">${Authorised.officeTelNo}</span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="row">
                                        <div class="col-md-6">
                                            Mobile No.
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-md-6">
                                                <span class="newVal" attr="${Authorised.mobileNo}">${Authorised.mobileNo}</span>
                                            </div>
                                        </div>
                                    </div>


                                    <div class="row">
                                        <div class="col-md-6">
                                            Email Address
                                        </div>
                                        <div class="col-md-6">
                                            <div class="col-md-6">
                                                <span class="newVal " attr="${Authorised.email}">${Authorised.email}</span>
                                            </div>
                                        </div>
                                    </div>


                                </c:forEach>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
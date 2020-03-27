<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>

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
                <table class="table">
                    <thead>
                    <tr>
                        <th>Document</th>
                        <th>File</th>
                        <th>Size</th>
                        <th>Submitted By</th>
                        <th>Date Submitted</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:if test="${empty applicationViewDto.appSupDocDtoList}">
                        <tr>
                            <td colspan="5">
                                <iais:message key="ACK018"
                                              escape="true"/>
                            </td>
                        </tr>
                    </c:if>
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
                <table class="table">
                    <thead>
                    <tr>
                        <th>Document</th>
                        <th>File</th>
                        <th>Size</th>
                        <th>Submitted By</th>
                        <th>Date Submitted</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td colspan="6">
                            <iais:message key="ACK018"
                                          escape="true"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>



<%--
  Created by IntelliJ IDEA.
  User: dongchi
  Date: 2023/2/23
  Time: 16:07
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<c:forEach var="embryoTransferDetailDto"
           items="${embryoTransferStageDtoVersion.embryoTransferDetailDtos}"
           varStatus="status">
    <div id="${seq.index+1}EmbryoVersion">
        <iais:row>
            <iais:field  value="" mandatory="false" cssClass="col-md-4"/>
            <iais:value display="true" cssClass="col-md-4">
                <iais:code code="${embryoTransferDetailDto.embryoAge}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field  value="" mandatory="false" cssClass="col-md-4"/>
            <iais:value display="true" cssClass="col-md-4">
                <c:out value="${embryoTransferDetailDto.embryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
            </iais:value>
        </iais:row>
    </div>
</c:forEach>

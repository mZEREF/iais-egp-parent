<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<div class="main-content">
    <form method="post" action=<%=process.runtime.continueURL()%>>
        <div class="row">
            <div class="col-xs-12">
                <div class="table-gp">
                    <table aria-describedby="" class="table">
                        <tr>
                            <th scope="col" class="col-xs-6">KeyType:</th>
                            <th scope="col" class="col-xs-6">
                                <iais:select name="keyType">
                                    <option>---select---</option>
                                    <option value="UEN">UEN</option>
                                    <option value="NRIC">NRIC</option>
                                </iais:select>
                            </th>
                        </tr>
                        <tr>
                            <td>Number:</td>
                            <td>
                                <iais:input type="text" name="UNID"></iais:input>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </form>
</div>
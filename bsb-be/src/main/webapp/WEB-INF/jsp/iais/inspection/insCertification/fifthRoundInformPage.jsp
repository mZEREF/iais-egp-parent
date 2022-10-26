<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<%--@elvariable id="finalRound" type="java.lang.Boolean"--%>
<input type="hidden" id="haveConfirm" name="haveConfirm" value=""/>
<input type="hidden" id="finalRound" name="finalRound" value="${finalRound}" readonly disabled/>
<div class="modal fade" id="beforeSubmit" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsMsg">
                            <iais:message key="BISACKINS007" escape="false" />
                        </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancelBeforeSubmitModule()">CLOSE</button>
            </div>
        </div>
    </div>
</div>
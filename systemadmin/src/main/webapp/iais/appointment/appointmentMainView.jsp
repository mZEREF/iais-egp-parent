<div class="main-content">
    <form class="form-horizontal" method="post" id="AppointmentForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="bg-title">
                        <h2>System Proposes Online Appointment</h2>
                    </div>
                    <div class="row" style="padding-bottom: 40px;">
                        <div class="col-md-4">
                            <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Online Appt ID:</span>
                        </div>
                        <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                2222
                            </span>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                            <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Application Date:</span>
                        </div>
                        <div class="col-md-1">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                From
                            </span>
                        </div>
                        <div class="col-md-3">
                            <iais:datePicker id="esd" name="esd"/>
                        </div><div class="col-md-1">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                To
                            </span>
                        </div><div class="col-md-3">
                            <iais:datePicker id="esd" name="esd"/>
                        </div>
                    </div>
                    <div class="row" style="padding-bottom: 40px;">
                        <div class="col-md-4">
                            <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Inspection Team ID:</span>
                        </div>
                        <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                2222
                            </span>
                        </div>
                    </div>
                    <div class="row" style="padding-bottom: 40px;">
                        <div class="col-md-4">
                            <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Creator ID:</span>
                        </div>
                        <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                2222
                            </span>
                        </div>
                    </div>
                    <div class="row" style="padding-bottom: 40px;">
                        <div class="col-md-4">
                            <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Date Creation:</span>
                        </div>
                        <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                2222
                            </span>
                        </div>
                    </div>
                    <div class="row" style="padding-bottom: 40px;">
                        <div class="col-md-4">
                            <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Last Update ID:</span>
                        </div>
                        <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                2222
                            </span>
                        </div>
                    </div>
                    <div class="row" style="padding-bottom: 40px;">
                        <div class="col-md-4">
                            <span style="font-size: 2rem;font-family: Open Sans, sans-serif;font-weight:600;">Date Last Update:</span>
                        </div>
                        <div class="col-md-8">
                            <span style="font-size: 1.5rem;font-weight:600;">
                                2222
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="text-right col-md-12    ">
            <button type="button" class="btn btn-primary" onclick="">Confirm Recommendation</button>
        </div>
    </form>
</div>
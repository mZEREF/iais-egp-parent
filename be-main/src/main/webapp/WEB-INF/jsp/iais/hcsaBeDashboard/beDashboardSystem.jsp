<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/5/24
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>

<input type="hidden" name="overAllVal" value='${dashOverAllCircleKpi}'/>
<input type="hidden" name="asoVal" value='${dashAsoCircleKpi}'/>
<input type="hidden" name="psoVal" value='${dashPsoCircleKpi}'/>
<input type="hidden" name="preInspVal" value='${dashPreInspCircleKpi}'/>
<input type="hidden" name="inspVal" value='${dashInspCircleKpi}'/>
<input type="hidden" name="postInspVal" value='${dashPostInspCircleKpi}'/>
<input type="hidden" name="ao1Val" value='${dashAo1CircleKpi}'/>
<input type="hidden" name="ao2Val" value='${dashAo2CircleKpi}'/>
<input type="hidden" name="ao3Val" value='${dashAo3CircleKpi}'/>
<input type="hidden" name="dashSysStageVal" value=''/>

<div class="main-content">
    <div class="row">
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="row">
                        <div class="col-md-4 col-xs-12">
                            <a data-tab="#" href="javascript:;">
                                <div class="cursor-default" id="canvas-holder">
                                    <canvas id="overAllCanvas"></canvas>
                                </div>
                                <p class="dashboard-txt main-chart-text cursor-default font-color-black"> Overall</p>
                            </a>
                        </div>
                        <div class="col-md-8 col-xs-12">
                            <div class="form-horizontal filter-box">
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">Application type</label>
                                    <div class="col-xs-12 col-sm-6 col-md-5" style="padding-left: unset;padding-top: 1%;">
                                        <iais:select cssClass="" name="appType" firstOption="" options="appTypeOption" multiValues="" multiSelect="true" />
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-xs-12 col-md-4 control-label">Service Licence</label>
                                    <div class="col-xs-12 col-sm-6 col-md-5" style="padding-left: unset;padding-top: 1%;">
                                        <iais:select cssClass="" name="svcLic" firstOption="" needSort="true" options="dashServiceOption" multiValues="" multiSelect="true" />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12 col-xs-12">
                            <div style="text-align:right;">
                                <button class="btn btn-secondary" type="button" id="sysClearBtn" name="sysClearBtn">Clear</button>
                                <button class="btn btn-primary" type="button" id="sysSearchBtn" name="sysSearchBtn">Search</button>
                            </div>
                        </div>
                        <hr>
                        <div class="col-xs-12">
                            <h3>
                                <span>Applications at each stage </span>
                            </h3>
                            <div class="dashboard-chart">
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a id="sysAsoCanvas" data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="asoCanvas"></canvas>
                                            </div>
                                            <p align="center" > Admin Screening</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="psoCanvas"></canvas>
                                            </div>
                                            <p align="center" > Professional Screening</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="preInspCanvas"></canvas>
                                            </div>
                                            <p align="center" > Pre-Inspection</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="inspCanvas"></canvas>
                                            </div>
                                            <p align="center" > Inspection</p>
                                        </a>
                                    </div>
                                </div>

                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="postInspCanvas"></canvas>
                                            </div>
                                            <p align="center" > Post Inspection</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="ao1Canvas"></canvas>
                                            </div>
                                            <p align="center" > AO1</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="ao2Canvas"></canvas>
                                            </div>
                                            <p align="center" > AO2</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="ao3Canvas"></canvas>
                                            </div>
                                            <p align="center" > AO3</p>
                                        </a>
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
<%@include file="beDashboardFun.jsp"%>
<script>
    $(document).ready(function () {

        chartRegister();

        initChart('aso');

        initChart('pso');

        initChart('preInsp');

        initChart('insp');

        initChart('postInsp');

        initChart('ao1');

        initChart('ao2');

        initChart('ao3');

        initChart('overAll');

        $('#sysAsoCanvas').click(function () {
            showWaiting();
            $('#switchAction').val('ASO');
            intraDashboardSubmit('sysdet');
        });

        $('#sysClearBtn').click(function () {
            doClear();
        });

        $('#sysSearchBtn').click(function () {
            showWaiting();
            let dashSwitchActionValue = $('#switchAction').val();
            intraDashboardSubmit(dashSwitchActionValue);
        });
    });


</script>

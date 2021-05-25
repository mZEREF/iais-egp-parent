<%--
  Created by IntelliJ IDEA.
  User: ZiXian
  Date: 2021/5/24
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<style>
    .font-color-black{
        color: #333333;
    }
</style>

<input type="hidden" name="overAllVal" value='${dashOverAllCircleKpi}'/>
<input type="hidden" name="asoVal" value='${dashAsoCircleKpi}'/>
<input type="hidden" name="psoVal" value='${dashPsoCircleKpi}'/>
<input type="hidden" name="preInspVal" value='${dashPreInspCircleKpi}'/>
<input type="hidden" name="inspVal" value='${dashInspCircleKpi}'/>
<input type="hidden" name="postInspVal" value='${dashPostInspCircleKpi}'/>
<input type="hidden" name="ao1Val" value='${dashAo1CircleKpi}'/>
<input type="hidden" name="ao2Val" value='${dashAo2CircleKpi}'/>
<input type="hidden" name="ao3Val" value='${dashAo3CircleKpi}'/>

<div class="main-content">
    <div class="row">
        <div class="col-lg-12 col-xs-12">
            <div class="center-content">
                <div class="intranet-content">
                    <div class="bg-title">
                        <h2>Dashboard</h2>
                    </div>
                    <div class="row">
                        <div class="col-md-4 col-xs-12">
                            <a data-tab="#" href="javascript:;">
                                <div id="canvas-holder">
                                    <canvas id="overAllCanvas"></canvas>
                                </div>
                                <p class="dashboard-txt main-chart-text"> Overall</p>
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
                        <hr>
                        <div class="col-xs-12">
                            <h3>
                                <span>Applications at each stage </span>
                            </h3>
                            <div class="dashboard-chart">
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="asoCanvas"></canvas>
                                            </div>
                                            <p align="center" class="font-color-black"> Admin Screening</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="psoCanvas"></canvas>
                                            </div>
                                            <p align="center" class="font-color-black"> Professional Screening</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="preInspCanvas"></canvas>
                                            </div>
                                            <p align="center" class="font-color-black" > Pre-Inspection</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="inspCanvas"></canvas>
                                            </div>
                                            <p align="center" class="font-color-black"> Inspection</p>
                                        </a>
                                    </div>
                                </div>

                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="postInspCanvas"></canvas>
                                            </div>
                                            <p align="center" class="font-color-black"> Post Inspection</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="ao1Canvas"></canvas>
                                            </div>
                                            <p align="center" class="font-color-black"> AO1</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="ao2Canvas"></canvas>
                                            </div>
                                            <p align="center" class="font-color-black"> AO2</p>
                                        </a>
                                    </div>
                                </div>
                                <div class="dashboard-tile-item">
                                    <div class="dashboard-tile">
                                        <a data-tab="#" href="javascript:;">
                                            <div>
                                                <canvas id="ao3Canvas"></canvas>
                                            </div>
                                            <p align="center" class="font-color-black"> AO3</p>
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


<script>
    $(document).ready(function () {
        Chart.pluginService.register({
            beforeDraw: function (chart) {
                var width = chart.chart.width,
                    height = chart.chart.height,
                    ctx = chart.chart.ctx;
                ctx.restore();
                var fontSize = (height / 114).toFixed(2);
                ctx.font = fontSize + "em sans-serif";
                ctx.textBaseline = "middle";
                var text = chart.config.options.elements.center.text,
                    textX = Math.round((width - ctx.measureText(text).width) / 2),
                    textY = height / 2;
                ctx.fillText(text, textX, textY);
                ctx.save();
            }
        });


        initChart('aso');

        initChart('pso');

        initChart('preInsp');

        initChart('insp');

        initChart('postInsp');

        initChart('ao1');

        initChart('ao2');

        initChart('ao3');

        initChart('overAll');

    });

    var initChart = function (canvasName) {
        var jsonStr = $('input[name="'+canvasName+'Val"]').val();

        var redSize = 0;
        var blueSize = 0;
        var yellowSize = 0;
        var redCount = 0;
        if(jsonStr != null && jsonStr != '' && jsonStr != undefined){
            var canvasJsonObj = eval(JSON.parse(jsonStr))[0];
            redSize = canvasJsonObj.dashRedCount;
            blueSize = canvasJsonObj.dashBlueCount;
            yellowSize = canvasJsonObj.dashAmberCount;
            redCount = canvasJsonObj.dashRedCount;
        }
        var data = {
            labels: ["Red", "Blue", "Yellow"],
            datasets: [{
                data: [redSize, blueSize, yellowSize],
                backgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"],
                hoverBackgroundColor: ["#FF6384", "#36A2EB", "#FFCE56"]
            }]
        };

        var canvasId = canvasName+'Canvas';
        var promisedDeliveryChart = new Chart(document.getElementById(canvasId), {
            type: 'doughnut',
            data: data,
            options: {
                elements: {
                    center: {
                        text: redCount
                    }
                },
                cutoutPercentage: 75,
                legend: {
                    display: false
                }
            }
        });

        //reset chart title size
        var chart1Title = $('#'+canvasId).closest('div.dashboard-tile');
        chart1Title.css('width',$('#'+canvasId).width() * 1.2);
        chart1Title.css('height',$('#'+canvasId).height() * 1.4);
    };

</script>

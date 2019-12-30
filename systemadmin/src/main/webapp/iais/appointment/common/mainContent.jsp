<link rel="stylesheet" href="/system-admin-web/iais/appointment/fullcalendar/core/main.css">
<link rel="stylesheet" href="/system-admin-web/iais/appointment/fullcalendar/daygrid/main.css">
<link href='/system-admin-web/iais/appointment/fullcalendar/timegrid/main.css' rel='stylesheet'/>
<link href='/system-admin-web/iais/appointment/fullcalendar/list/main.css' rel='stylesheet'/>
<link href="/system-admin-web/iais/appointment/fullcalendar/bootstrap/main.css" rel="stylesheet">

<script src='/system-admin-web/iais/appointment/fullcalendar/core/main.js'></script>
<script src='/system-admin-web/iais/appointment/fullcalendar/interaction/main.js'></script>
<script src='/system-admin-web/iais/appointment/fullcalendar/daygrid/main.js'></script>
<script src='/system-admin-web/iais/appointment/fullcalendar/timegrid/main.js'></script>
<script src='/system-admin-web/iais/appointment/fullcalendar/list/main.js'></script>

<%--<script src='/system-admin-web/iais/appointment/fullcalendar/bootstrap/main.js'></script>--%>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        var Calendar = FullCalendar.Calendar;
        var Draggable = FullCalendarInteraction.Draggable

        var containerEl = document.getElementById('external-events-list');
        new Draggable(containerEl, {
            itemSelector: '.fc-event',
            eventData: function (eventEl) {
                return {
                    title: eventEl.innerText.trim()
                }
            }
        });

        var calendarEl = document.getElementById('calendar');
        var calendar = new Calendar(calendarEl, {
            plugins: ['interaction', 'dayGrid', 'timeGrid', 'list'],
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay,listWeek'
            },
            editable: true,
            droppable: true,
            drop: function (arg) {
                if (document.getElementById('drop-remove').checked) {
                    arg.draggedEl.parentNode.removeChild(arg.draggedEl);
                }
            }
        });
        calendar.render();
    });

</script>
<style>
    body {
        margin-top: 40px;
        font-size: 14px;
        font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
    }

    #wrap {
        margin: 0 auto;
    }

    #external-events {
        float: left;
        width: 17%;
        margin-top: 4px;
        padding: 0 10px;
        border: 1px solid #ccc;
        background: #eee;
        text-align: left;
    }

    #external-events h4 {
        font-size: 16px;
        margin-top: 0;
        padding-top: 1em;
    }

    #external-events .fc-event {
        margin: 10px 0;
        cursor: pointer;
    }

    #external-events p {
        margin: 1.5em 0;
        font-size: 11px;
        color: #666;
    }

    #external-events p input {
        margin: 0;
        vertical-align: middle;
    }

    #calendar {
        float: right;
        width: 83%;
    }

</style>
<div class="main-content">
    <form class="form-horizontal" method="post" id="TemplatesForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content" style="padding-left: 5px">
                    <div id='wrap'>
                        <div id='external-events'>
                            <h4>Draggable Events</h4>
                            <div id='external-events-list'>
                                <div class='fc-event'>My Event 1</div>
                                <div class='fc-event'>My Event 2</div>
                                <div class='fc-event'>My Event 3</div>
                                <div class='fc-event'>My Event 4</div>
                            </div>
                            <p>
                                <input type='checkbox' id='drop-remove'/>
                                <label for='drop-remove'>remove after drop</label>
                            </p>
                        </div>
                        <div id='calendar'></div>
                        <div style='clear:both'></div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

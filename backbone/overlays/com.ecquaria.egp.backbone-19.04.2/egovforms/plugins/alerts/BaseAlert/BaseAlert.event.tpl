{var events = [['task-response-time', 'Task Response Time'], ['task-response-time-warning', 'Task Response Time Warning'], ['process-sla-danger', 'Process SLA Danger'], ['process-sla-warning', 'Process SLA Warning']]}
{if stageSla}
{eval}events = [['task-response-time', 'Task Response Time'], ['task-response-time-warning', 'Task Response Time Warning']];{/eval}
{/if}
<select>
	{for event in events}
	<option value="${event[0]}"
		{if selected == event[0]}selected{/if}>${event[1]}</option>
	{/for}
</select>
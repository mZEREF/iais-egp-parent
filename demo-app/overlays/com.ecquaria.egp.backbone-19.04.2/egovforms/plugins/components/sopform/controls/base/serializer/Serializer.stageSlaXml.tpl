{var config}
{var alerts}
{var triggers}
<Stage-slas>
{for stage in stages}
	{eval}config = stage.config;{/eval}
	{if config}
	<Stage-sla stage="${stage.id}">
		{eval}alerts = config.alerts || [];{/eval}
		{eval}triggers = config.triggers || [];{/eval}
		<Sla>
			<Task-response-time value="${config.expectedResponseTimeValue}" unit="${config.expectedResponseTimeUnit}" />
			<Task-warning-time value="${config.responseTimeWarning}" unit="${config.expectedResponseTimeUnit}" />

			<Alerts>
			{for alert in alerts}
				<Alert type="${alert.type}" event="${alert.event}">
					<PerformBefore value="${alert.beforeValue}" unit="${alert.beforeUnit}" />
					{if alert.params}
					<Properties>
						{for param in alert.params}
						<Entry name="${param_index|h}">
							<Value>${param|h}</Value>
						</Entry>
						{/for}
					</Properties>
					{/if}
				</Alert>
			{/for}
			</Alerts>
			<Triggers>
			{for trigger in triggers}
				<Trigger type="${trigger.type}" event="${trigger.event}">
					<PerformBefore value="${trigger.beforeValue}" unit="${trigger.beforeUnit}" />
					{if trigger.params}
					<Properties>
						{for param in trigger.params}
						<Entry name="${param_index|h}">
							<Value>${param|h}</Value>
						</Entry>
						{/for}
					</Properties>
					{/if}
				</Trigger>
			{/for}
			</Triggers>
		</Sla>
	</Stage-sla>
	{/if}
{/for}
</Stage-slas>
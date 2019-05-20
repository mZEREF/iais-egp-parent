<Case-sla>
	<Sla>
		<Task-response-time value="${expectedResponseTimeValue}" unit="${expectedResponseTimeUnit}" />
		<Task-warning-time value="${responseTimeWarning}" unit="${expectedResponseTimeUnit}" />

		<Sla-warning value="${warningPercentage}" />
		<Sla-fail value="${failPercentage}" />

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
</Case-sla>

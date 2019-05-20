{var slaConfig}
{var alerts}
{var triggers}
<Milestone-slas>
{for milestone in milestones}
	<Milestone-sla milestone="${milestone.id}">
		{eval}slaConfig = milestone.slaConfig;{/eval}
		{eval}alerts = slaConfig.alerts;{/eval}
		{eval}triggers = slaConfig.triggers;{/eval}
		<Sla>
			<Task-response-time value="${slaConfig.expectedResponseTimeValue}" unit="${slaConfig.expectedResponseTimeUnit}" />
			<Task-warning-time value="${slaConfig.responseTimeWarning}" unit="${slaConfig.expectedResponseTimeUnit}" />

			<Sla-warning value="${slaConfig.warningPercentage}" />
			<Sla-fail value="${slaConfig.failPercentage}" />

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
	</Milestone-sla>
{/for}
</Milestone-slas>
<Grouping>
	<Segments>
	{for segment in segments}
		<Segment name="${segment.id}">
		{for milestone in segment.milestones}
			<Included-milestone name="${milestone}" />
		{/for}
		</Segment>
	{/for}
	</Segments>
	<Milestones>
	{for milestone in milestones}
		<Milestone name="${milestone.id}">
		{for stage in milestone.stages}
			<Included-stage name="${stage.id}" />
		{/for}
		</Milestone>
	{/for}
	</Milestones>
</Grouping>

package com.NxIndustries.Sapphire.shortcutmaker;

import android.content.Context;

public class AllTasksListAsyncProvider extends AsyncProvider<AllTasksListAdapter> {
	public AllTasksListAsyncProvider(
			Context context,
			AsyncProvider.Listener<AllTasksListAdapter> listener) {
		super(context, listener, true);
	}

	@Override
	protected AllTasksListAdapter run(Updater updater) {
		AllTasksListAdapter adapter = new AllTasksListAdapter(this.context, updater);
		return adapter;
	}
}

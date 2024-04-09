/******************************************************************************
 *  Copyright (c) 2024 Thomas Wolf <twolf@apache.org> and others
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 *****************************************************************************/
package org.eclipse.egit.ui.internal.history.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Adapters;
import org.eclipse.egit.ui.internal.commit.DiffViewer;
import org.eclipse.egit.ui.internal.history.FileDiff;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jgit.diff.DiffEntry;

/**
 * Opens the "new" version of a {@link FileDiff}.
 */
public class FileDiffOpenPreviousHandler extends AbstractHistoryCommandHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = getSelection(event);
		List<FileDiff> diffs = getDiffs(selection);
		if (!diffs.isEmpty()) {
			for (FileDiff diff : diffs) {
				DiffViewer.openInEditor(diff, DiffEntry.Side.OLD, -1);
			}
		}
		return null;
	}

	private List<FileDiff> getDiffs(IStructuredSelection selection) {
		List<FileDiff> result = new ArrayList<>();
		Iterator<?> items = selection.iterator();
		while (items.hasNext()) {
			FileDiff diff = Adapters.adapt(items.next(), FileDiff.class);
			if (diff != null && !diff.isSubmodule()
					&& !DiffEntry.ChangeType.ADD.equals(diff.getChange())) {
				result.add(diff);
			}
		}
		return result;
	}

}

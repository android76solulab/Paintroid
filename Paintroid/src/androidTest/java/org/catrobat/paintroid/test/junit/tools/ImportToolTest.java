/*
 * Paintroid: An image manipulation application for Android.
 * Copyright (C) 2010-2015 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.paintroid.test.junit.tools;

import android.graphics.Bitmap;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.paintroid.MainActivity;
import org.catrobat.paintroid.command.CommandManager;
import org.catrobat.paintroid.tools.ContextCallback;
import org.catrobat.paintroid.tools.ToolPaint;
import org.catrobat.paintroid.tools.Workspace;
import org.catrobat.paintroid.tools.implementation.DefaultContextCallback;
import org.catrobat.paintroid.tools.implementation.ImportTool;
import org.catrobat.paintroid.tools.options.ToolOptionsController;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ImportToolTest {

	@Rule
	public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

	@Rule
	public MockitoRule mockito = MockitoJUnit.rule();

	@Mock
	private CommandManager commandManager;

	private int drawingSurfaceWidth;
	private int drawingSurfaceHeight;

	private ImportTool tool;

	@UiThreadTest
	@Before
	public void setUp() {
		MainActivity activity = activityTestRule.getActivity();
		Workspace workspace = activity.workspace;
		ToolPaint toolPaint = activity.toolPaint;
		ToolOptionsController toolOptionsController = activity.toolOptionsController;
		ContextCallback contextCallback = new DefaultContextCallback(InstrumentationRegistry.getTargetContext());
		tool = new ImportTool(contextCallback, toolOptionsController, toolPaint, workspace, commandManager);

		drawingSurfaceWidth = workspace.getWidth();
		drawingSurfaceHeight = workspace.getHeight();
	}

	@Test
	public void testImport() {
		final int width = drawingSurfaceWidth;
		final int height = drawingSurfaceHeight;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		tool.setBitmapFromFile(bitmap);

		assertEquals(width, tool.boxWidth, Float.MIN_VALUE);
		assertEquals(height, tool.boxHeight, Float.MIN_VALUE);
	}

	@Test
	public void testImportTooSmall() {
		final int width = 1;
		final int height = 1;
		final int minSize = ImportTool.DEFAULT_BOX_RESIZE_MARGIN;

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		tool.setBitmapFromFile(bitmap);

		assertEquals(minSize, tool.boxWidth, Float.MIN_VALUE);
		assertEquals(minSize, tool.boxHeight, Float.MIN_VALUE);
	}

	@Test
	public void testImportTooLarge() {
		final int width = (int) (drawingSurfaceWidth * ImportTool.MAXIMUM_BORDER_RATIO);
		final int height = (int) (drawingSurfaceHeight * ImportTool.MAXIMUM_BORDER_RATIO);

		Bitmap bitmap = Bitmap.createBitmap(width + 1, height + 1, Bitmap.Config.ARGB_8888);
		tool.setBitmapFromFile(bitmap);

		assertEquals(width, tool.boxWidth, Float.MIN_VALUE);
		assertEquals(height, tool.boxHeight, Float.MIN_VALUE);
	}
}

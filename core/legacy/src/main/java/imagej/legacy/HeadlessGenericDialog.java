/*
 * #%L
 * ImageJ software for multidimensional image processing and analysis.
 * %%
 * Copyright (C) 2009 - 2014 Board of Regents of the University of
 * Wisconsin-Madison, Broad Institute of MIT and Harvard, and Max Planck
 * Institute of Molecular Cell Biology and Genetics.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package imagej.legacy;

import ij.Macro;
import ij.gui.DialogListener;
import ij.plugin.filter.PlugInFilterRunner;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Rump implementation of a pseudo dialog intended to stand in for the
 * {@link ij.gui.GenericDialog} in headless mode.
 * 
 * @author Johannes Schindelin
 */
public class HeadlessGenericDialog {
	protected List<Double> numbers;
	protected List<String> strings;
	protected List<Boolean> checkboxes;
	protected List<String> choices;
	protected List<Integer> choiceIndices;
	protected String textArea1, textArea2;
	protected List<String> radioButtons;

	protected int numberfieldIndex = 0, stringfieldIndex = 0, checkboxIndex = 0, choiceIndex = 0, textAreaIndex = 0, radioButtonIndex = 0;
	protected boolean invalidNumber;
	protected String errorMessage;

	public HeadlessGenericDialog() {
		if (Macro.getOptions() == null)
			throw new RuntimeException("Cannot instantiate headless dialog except in macro mode");
		numbers = new ArrayList<Double>();
		strings = new ArrayList<String>();
		checkboxes = new ArrayList<Boolean>();
		choices = new ArrayList<String>();
		choiceIndices = new ArrayList<Integer>();
		radioButtons = new ArrayList<String>();
	}

	public void addCheckbox(String label, boolean defaultValue) {
		checkboxes.add(IJ1Helper.getMacroParameter(label, defaultValue));
	}

	@SuppressWarnings("unused")
	public void addCheckboxGroup(int rows, int columns, String[] labels, boolean[] defaultValues) {
		for (int i = 0; i < labels.length; i++)
			addCheckbox(labels[i], defaultValues[i]);
	}

	@SuppressWarnings("unused")
	public void addCheckboxGroup(int rows, int columns, String[] labels, boolean[] defaultValues, String[] headings) {
		addCheckboxGroup(rows, columns, labels, defaultValues);
	}

	public void addChoice(String label, String[] items, String defaultItem) {
		String item = IJ1Helper.getMacroParameter(label, defaultItem);
		int index = 0;
		for (int i = 0; i < items.length; i++)
			if (items[i].equals(item)) {
				index = i;
				break;
			}
		choiceIndices.add(index);
		choices.add(items[index]);
	}

	@SuppressWarnings("unused")
	public void addNumericField(String label, double defaultValue, int digits) {
		numbers.add(IJ1Helper.getMacroParameter(label, defaultValue));
	}

	@SuppressWarnings("unused")
	public void addNumericField(String label, double defaultValue, int digits, int columns, String units) {
		addNumericField(label, defaultValue, digits);
	}

	@SuppressWarnings("unused")
	public void addSlider(String label, double minValue, double maxValue, double defaultValue) {
		numbers.add(IJ1Helper.getMacroParameter(label, defaultValue));
	}

	public void addStringField(String label, String defaultText) {
		strings.add(IJ1Helper.getMacroParameter(label, defaultText));
	}

	@SuppressWarnings("unused")
	public void addStringField(String label, String defaultText, int columns) {
		addStringField(label, defaultText);
	}

	@SuppressWarnings("unused")
	public void addTextAreas(String text1, String text2, int rows, int columns) {
		textArea1 = text1;
		textArea2 = text2;
	}

	public boolean getNextBoolean() {
		return checkboxes.get(checkboxIndex++);
	}

	public String getNextChoice() {
		return choices.get(choiceIndex++);
	}

	public int getNextChoiceIndex() {
		return choiceIndices.get(choiceIndex++);
	}

	public double getNextNumber() {
		return numbers.get(numberfieldIndex++);
	}

	/** Returns the contents of the next text field. */
	public String getNextString() {
		return strings.get(stringfieldIndex++);
	}

	public String getNextText()  {
		switch (textAreaIndex++) {
		case 0:
			return textArea1;
		case 1:
			return textArea2;
		}
		return null;
	}

	/** Adds a radio button group. */
	@SuppressWarnings("unused")
	public void addRadioButtonGroup(String label, String[] items, int rows, int columns, String defaultItem) {
		radioButtons.add(IJ1Helper.getMacroParameter(label, defaultItem));
	}

	public List<String> getRadioButtonGroups() {
		return radioButtons;
	}

	/** Returns the selected item in the next radio button group. */
	public String getNextRadioButton() {
		return radioButtons.get(radioButtonIndex++);
	}

	public boolean invalidNumber() {
		boolean wasInvalid = invalidNumber;
		invalidNumber = false;
		return wasInvalid;
	}

	public void showDialog() {
		if (Macro.getOptions() == null)
			throw new RuntimeException("Cannot run dialog headlessly");
		numberfieldIndex = 0;
		stringfieldIndex = 0;
		checkboxIndex = 0;
		choiceIndex = 0;
		textAreaIndex = 0;
	}

	public boolean wasCanceled() {
		return false;
	}

	public boolean wasOKed() {
		return true;
	}

	public void dispose() {}
	@SuppressWarnings("unused")
	public void addDialogListener(DialogListener dl) {}
	@SuppressWarnings("unused")
	public void addHelp(String url) {}
	@SuppressWarnings("unused")
	public void addMessage(String text) {}
	@SuppressWarnings("unused")
	public void addMessage(String text, Font font) {}
	@SuppressWarnings("unused")
	public void addMessage(String text, Font font, Color color) {}
	@SuppressWarnings("unused")
	public void addPanel(Panel panel) {}
	@SuppressWarnings("unused")
	public void addPanel(Panel panel, int contraints, Insets insets) {}
	@SuppressWarnings("unused")
	public void addPreviewCheckbox(PlugInFilterRunner pfr) {}
	@SuppressWarnings("unused")
	public void addPreviewCheckbox(PlugInFilterRunner pfr, String label) {}
	@SuppressWarnings("unused")
	public void centerDialog(boolean b) {}
	public void enableYesNoCancel() {}
	@SuppressWarnings("unused")
	public void enableYesNoCancel(String yesLabel, String noLabel) {}
	@SuppressWarnings("unused")
	public void focusGained(FocusEvent e) {}
	@SuppressWarnings("unused")
	public void focusLost(FocusEvent e) {}
	public Button[] getButtons() { return null; }
	public Vector<?> getCheckboxes() { return null; }
	public Vector<?> getChoices() { return null; }
	public String getErrorMessage() { return errorMessage; }
	public Insets getInsets() { return null; }
	public Component getMessage() { return null; }
	public Vector<?> getNumericFields() { return null; }
	public Checkbox getPreviewCheckbox() { return null; }
	public Vector<?> getSliders() { return null; }
	public Vector<?> getStringFields() { return null; }
	public TextArea getTextArea1() { return null; }
	public TextArea getTextArea2() { return null; }
	public void hideCancelButton() {}
	@SuppressWarnings("unused")
	public void previewRunning(boolean isRunning) {}
	@SuppressWarnings("unused")
	public void setEchoChar(char echoChar) {}
	@SuppressWarnings("unused")
	public void setHelpLabel(String label) {}
	@SuppressWarnings("unused")
	public void setInsets(int top, int left, int bottom) {}
	@SuppressWarnings("unused")
	public void setOKLabel(String label) {}
	protected void setup() {}
	public void accessTextFields() {}
	public void showHelp() {}
}
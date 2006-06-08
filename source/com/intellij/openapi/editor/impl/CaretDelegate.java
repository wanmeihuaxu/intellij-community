package com.intellij.openapi.editor.impl;

import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.ex.EditorEx;

/**
 * @author Alexey
 */
public class CaretDelegate implements CaretModel {
  private final CaretModel myDelegate;
  private RangeMarker myRange;
  private final EditorEx myEditorDelegate;

  public CaretDelegate(CaretModel delegate, final RangeMarker range, EditorEx editorDelegate) {
    myDelegate = delegate;
    myRange = range;
    myEditorDelegate = editorDelegate;
  }

  public void moveCaretRelatively(final int columnShift,
                                  final int lineShift,
                                  final boolean withSelection,
                                  final boolean blockSelection,
                                  final boolean scrollToCaret) {
    myDelegate.moveCaretRelatively(columnShift, lineShift, withSelection, blockSelection, scrollToCaret);
  }

  public void moveToLogicalPosition(final LogicalPosition pos) {
    int rangeLine = myEditorDelegate.getDocument().getLineNumber(myRange.getStartOffset());
    LogicalPosition newPos = new LogicalPosition(pos.line + rangeLine, pos.column + myRange.getStartOffset() -
                                                                       myEditorDelegate.getDocument().getLineStartOffset(rangeLine));
    myDelegate.moveToLogicalPosition(newPos);
  }

  public void moveToVisualPosition(final VisualPosition pos) {
    int rangeLine = myEditorDelegate.getDocument().getLineNumber(myRange.getStartOffset());
    VisualPosition newPos = new VisualPosition(pos.line + rangeLine, pos.column + myRange.getStartOffset() -
                                                                       myEditorDelegate.getDocument().getLineStartOffset(rangeLine));
    myDelegate.moveToVisualPosition(newPos);
  }

  public void moveToOffset(final int offset) {
    myDelegate.moveToOffset(offset+myRange.getStartOffset());
  }

  public LogicalPosition getLogicalPosition() {
    Document hostDocument = myEditorDelegate.getDocument();
    int rangeLine = hostDocument.getLineNumber(myRange.getStartOffset());
    int hostLineNumber = hostDocument.getLineNumber(myDelegate.getOffset());
    int line = hostLineNumber - rangeLine;
    int hostLineOffset = hostDocument.getLineStartOffset(hostLineNumber);
    return new LogicalPosition(line, Math.max(0,myDelegate.getOffset() - hostLineOffset));
  }

  public VisualPosition getVisualPosition() {
    LogicalPosition logicalPosition = getLogicalPosition();
    return new VisualPosition(logicalPosition.line, logicalPosition.column);
  }

  public int getOffset() {
    return myDelegate.getOffset() - myRange.getStartOffset();
  }

  public void addCaretListener(final CaretListener listener) {
    myDelegate.addCaretListener(listener);
  }

  public void removeCaretListener(final CaretListener listener) {
    myDelegate.removeCaretListener(listener);
  }
}

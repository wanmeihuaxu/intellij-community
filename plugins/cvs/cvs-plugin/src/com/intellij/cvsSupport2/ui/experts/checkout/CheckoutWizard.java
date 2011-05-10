/*
 * Copyright 2000-2011 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.cvsSupport2.ui.experts.checkout;

import com.intellij.CvsBundle;
import com.intellij.cvsSupport2.config.CvsApplicationLevelConfiguration;
import com.intellij.cvsSupport2.config.CvsRootConfiguration;
import com.intellij.cvsSupport2.cvsBrowser.CvsElement;
import com.intellij.cvsSupport2.ui.experts.CvsWizard;
import com.intellij.cvsSupport2.ui.experts.SelectCVSConfigurationStep;
import com.intellij.cvsSupport2.ui.experts.SelectCvsElementStep;
import com.intellij.cvsSupport2.ui.experts.SelectLocationStep;
import com.intellij.openapi.project.Project;

import javax.swing.tree.TreeSelectionModel;
import java.io.File;

/**
 * author: lesya
 */
public class CheckoutWizard extends CvsWizard {

  private final SelectCVSConfigurationStep mySelectCVSConfigurationStep;
  private final SelectCvsElementStep mySelectCvsElementStep;
  private final SelectLocationStep mySelectLocationStep;

  private final ChooseCheckoutMode myChooseModeStep;

  public CheckoutWizard(final Project project) {
    super(CvsBundle.message("dialog.tittle.check.out.from.cvs.repository"), project);
    mySelectCVSConfigurationStep = new SelectCVSConfigurationStep(project, this);
    mySelectCvsElementStep = new SelectCvsElementStep(CvsBundle.message("dialog.title.select.cvs.element.to.check.out"),
                                                      this, project, mySelectCVSConfigurationStep,
                                                      true, TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION, false, true);

    mySelectLocationStep = new MySelectLocationStep(project);
    myChooseModeStep = new ChooseCheckoutMode(this);

    addStep(mySelectCVSConfigurationStep);
    addStep(mySelectCvsElementStep);
    addStep(mySelectLocationStep);
    addStep(myChooseModeStep);

    init();
  }

  protected void doOKAction() {
    CvsApplicationLevelConfiguration config = CvsApplicationLevelConfiguration.getInstance();

    config.MAKE_CHECKED_OUT_FILES_READONLY = myChooseModeStep.getMakeNewFilesReadOnly();
    config.CHECKOUT_PRUNE_EMPTY_DIRECTORIES = myChooseModeStep.getPruneEmptyDirectories();
    config.CHECKOUT_KEYWORD_SUBSTITUTION = myChooseModeStep.getKeywordSubstitution().toString();

    super.doOKAction();
  }

  public CvsElement[] getSelectedElements() {
    return mySelectCvsElementStep.getSelectedCvsElements();
  }

  public CvsRootConfiguration getSelectedConfiguration() {
    return mySelectCVSConfigurationStep.getSelectedConfiguration();
  }

  public boolean useAlternativeCheckoutLocation() {
    return myChooseModeStep.useAlternativeCheckoutLocation();
  }

  public File getCheckoutDirectory() {
    return myChooseModeStep.getCheckoutDirectory();
  }

  public File getSelectedLocation() {
    return mySelectLocationStep.getSelectedFile();
  }

  private class MySelectLocationStep extends SelectLocationStep {
    public MySelectLocationStep(Project project) {
      super(CvsBundle.message("dialog.title.select.check.out.location"), CheckoutWizard.this, project);
      init();
    }
  }

  protected String getHelpID() {
    return "cvs.checkOutPrj";
  }
}

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2010 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */
package org.equipmentmanager.viewer;

import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.ActionMap;
import javax.swing.SwingUtilities;
import org.equipmentmanager.persistence.Brand;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.Children;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.equipmentmanager.viewer//EquipmentManagerViewer//EN",
autostore = false)
@TopComponent.Description(preferredID = "EquipmentManagerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.myorg.myviewer.MyViewerTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_EquipmentManagerAction",
preferredID = "EquipmentManagerTopComponent")
@NbBundle.Messages({
    "CTL_EquipmentManagerAction=Open Editor",
    "CTL_EquipmentManagerTopComponent=Equipment Manager Window",
    "HINT_EquipmentManagerTopComponent=This is a Equipment Manager window"
})
public final class EquipmentManagerTopComponent extends TopComponent implements ExplorerManager.Provider {

    private static EquipmentManagerTopComponent instance;
    private static final String PREFERRED_ID = "EquipmentManagerTopComponent";
    private static ExplorerManager em = new ExplorerManager();

    public EquipmentManagerTopComponent() {
        initComponents();
        beanTreeView1.setRootVisible(false);
        setName(NbBundle.getMessage(EquipmentManagerTopComponent.class, "CTL_EquipmentManagerTopComponent"));
        setToolTipText(NbBundle.getMessage(EquipmentManagerTopComponent.class, "HINT_EquipmentManagerTopComponent"));
        ActionMap map = this.getActionMap();
//        map.put("delete", ExplorerUtils.actionDelete(em, true)); //NOI18N
        associateLookup(ExplorerUtils.createLookup(em, map));
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                readEquipmentManagerRoot();
            }
        });
    }

    private void readEquipmentManagerRoot() {
        /*        EntityManagerFactory factory = Persistence.createEntityManagerFactory("EquipmentManagerDBAccessPU");
         if (factory == null) {
         // XXX: message box?
         return;
         }
         EntityManager entityManager;
         try {
         entityManager = factory.createEntityManager();
         } catch (RuntimeException re) {
         // XXX: message box?
         return;
         }
         final Query query = entityManager.createQuery("SELECT b FROM Brand b");
         SwingUtilities.invokeLater(new Runnable() {

         @Override
         public void run() {
         @SuppressWarnings("unchecked")
         List<Brand> resultList = query.getResultList();
         em.setRootContext(new BrandRootNode(Children.create(new BrandChildFactory(resultList), true)));
         }
         });
         */

        em.setRootContext(new EquipmentRootNode(Children.create(new EquipmentManagerChildFactory(), true)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        beanTreeView1 = new org.openide.explorer.view.BeanTreeView();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(beanTreeView1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(beanTreeView1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.openide.explorer.view.BeanTreeView beanTreeView1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files
     * only, i.e. deserialization routines; otherwise you could get a
     * non-deserialized instance. To obtain the singleton instance, use
     * {@link #findInstance}.
     */
    public static synchronized EquipmentManagerTopComponent getDefault() {
        if (instance == null) {
            instance = new EquipmentManagerTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the EquipmentManagerTopComponent instance. Never call
     * {@link #getDefault} directly!
     */
    public static synchronized EquipmentManagerTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(EquipmentManagerTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof EquipmentManagerTopComponent) {
            return (EquipmentManagerTopComponent) win;
        }
        Logger.getLogger(EquipmentManagerTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    public static void refreshNode() {
        EntityManager entityManager = Persistence.createEntityManagerFactory("EquipmentManagerDBAccessPU").createEntityManager();
        Query query = entityManager.createQuery("SELECT b FROM Brand b");
        @SuppressWarnings("unchecked")
        List<Brand> resultList = query.getResultList();
        em.setRootContext(new BrandRootNode(Children.create(new BrandChildFactory(resultList), true)));
    }
}

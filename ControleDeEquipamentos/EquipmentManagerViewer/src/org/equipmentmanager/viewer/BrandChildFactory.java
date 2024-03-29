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

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.Action;
import org.equipmentmanager.persistence.Brand;
import org.openide.actions.DeleteAction;
import org.openide.actions.PropertiesAction;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.actions.SystemAction;
import org.openide.util.lookup.Lookups;

public class BrandChildFactory extends ChildFactory<Brand> {

    private List<Brand> resultList;

    public BrandChildFactory(List<Brand> resultList) {
        this.resultList = resultList;
    }

    @Override
    protected boolean createKeys(List<Brand> list) {
        for (Brand b : resultList) {
            list.add(b);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Brand b) {
        Node node = new AbstractNode(Children.LEAF, Lookups.singleton(b)) {

            @Override
            public Action[] getActions(boolean context) {
                Action[] result = new Action[]{
                    SystemAction.get(DeleteAction.class),
                    SystemAction.get(PropertiesAction.class)
                };
                return result;
            }

            @Override
            public boolean canDestroy() {
                Brand Brand = this.getLookup().lookup(Brand.class);
                return Brand != null;
            }

            @Override
            public void destroy() throws IOException {
                if (deleteBrand(this.getLookup().lookup(Brand.class).getId())) {
                    super.destroy();
                    EquipmentManagerTopComponent.refreshNode();
                }
            }

        };
        node.setDisplayName(b.getId().toString());
        node.setShortDescription(b.getName());
        return node;
    }

    private static boolean deleteBrand(Long BrandId) {
        EntityManager entityManager = Persistence.createEntityManagerFactory("EquipmentManagerDBAccessPU").createEntityManager();
        entityManager.getTransaction().begin();
        try {
            Brand toDelete = entityManager.find(Brand.class, BrandId);
            entityManager.remove(toDelete);
            // so far so good
            entityManager.getTransaction().commit();
        } catch(Exception e) {
            Logger.getLogger(BrandChildFactory.class.getName()).log(
                    Level.WARNING, "Cannot delete a Brand with id {0}, cause: {1}", new Object[]{BrandId, e});
            entityManager.getTransaction().rollback();
        }
        return true;
    }
}

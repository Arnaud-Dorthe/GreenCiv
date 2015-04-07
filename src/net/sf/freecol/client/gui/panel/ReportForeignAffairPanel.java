/**
 *  Copyright (C) 2002-2015   The FreeCol Team
 *
 *  This file is part of FreeCol.
 *
 *  FreeCol is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  FreeCol is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with FreeCol.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.freecol.client.gui.panel;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import net.sf.freecol.client.FreeColClient;
import net.sf.freecol.client.gui.FontLibrary;
import net.sf.freecol.client.gui.GUI;
import net.sf.freecol.common.i18n.Messages;
import net.sf.freecol.common.model.NationSummary;
import net.sf.freecol.common.model.Player;


/**
 * This panel displays the Foreign Affairs Report.
 */
public final class ReportForeignAffairPanel extends ReportPanel {

    /**
     * The constructor that will add the items to this panel.
     *
     * @param freeColClient The <code>FreeColClient</code> for the game.
     */
    public ReportForeignAffairPanel(FreeColClient freeColClient) {
        super(freeColClient, "reportForeignAction");

        // Display Panel
        reportPanel.removeAll();
        reportPanel.setLayout(new MigLayout("wrap 2", "[]push[]",
                                            "[align top]"));

        for (Player enemy : getGame().getLiveEuropeanPlayers(null)) {
            int n;
            NationSummary ns = igc().getNationSummary(enemy);
            if (ns == null) continue;
            JPanel enemyPanel = new MigPanel();
            enemyPanel.setLayout(new MigLayout("gapy 0",
                                               "[][]20[align right]0[]", ""));
            enemyPanel.setOpaque(false);
            JLabel coatLabel = new JLabel();
            final ImageIcon coatOfArms = new ImageIcon(freeColClient.getGUI()
                .getImageLibrary().getCoatOfArmsImage(enemy.getNation()));
            if (coatOfArms != null) {
                coatLabel.setIcon(coatOfArms);
            }
            enemyPanel.add(coatLabel, "spany, aligny top");
            
            JLabel label = Utility.localizedLabel(enemy.getNationName());
            Font font = FontLibrary.createFont(FontLibrary.FontType.NORMAL, FontLibrary.FontSize.SMALL, Font.BOLD);
            label.setFont(font);
            enemyPanel.add(label, "wrap 12");
            
            //TODO: Get stance only if not player's Nation.
            enemyPanel.add(Utility.localizedLabel("report.stance"), "newline");
            enemyPanel.add(Utility.localizedLabel(ns.getStance().getLabel()));

            n = ns.getNumberOfSettlements();
            enemyPanel.add(Utility.localizedLabel("report.numberOfColonies"), "newline");
            enemyPanel.add(new JLabel(Integer.toString(n)));

            n = ns.getNumberOfUnits();
            enemyPanel.add(Utility.localizedLabel("report.numberOfUnits"), "newline");
            enemyPanel.add(new JLabel(Integer.toString(n)));

            n = ns.getMilitaryStrength();
            enemyPanel.add(Utility.localizedLabel("report.militaryStrength"), "newline");
            enemyPanel.add(new JLabel(Integer.toString(n)));

            n = ns.getNavalStrength();
            enemyPanel.add(Utility.localizedLabel("report.navalStrength"), "newline");
            enemyPanel.add(new JLabel(Integer.toString(n)));

            n = ns.getGold();
            enemyPanel.add(Utility.localizedLabel("goldTitle"), "newline");
            enemyPanel.add(new JLabel(Integer.toString(n)));

            n = ns.getFoundingFathers();
            if (n >= 0) {
                enemyPanel.add(Utility.localizedLabel("report.continentalCongress.title"),
                               "newline 8");
                enemyPanel.add(new JLabel(Integer.toString(n)));
            }

            n = ns.getTax();
            if (n >= 0) {
                enemyPanel.add(Utility.localizedLabel("tax"), "newline");
                enemyPanel.add(new JLabel(Integer.toString(n)));
                enemyPanel.add(new JLabel("%"));
            }

            n = ns.getSoL();
            if (n >= 0) {
                enemyPanel.add(Utility.localizedLabel("report.sonsOfLiberty"), "newline");
                enemyPanel.add(new JLabel(Integer.toString(n)));
                enemyPanel.add(new JLabel("%"));
            }

            reportPanel.add(enemyPanel);
        }

        reportPanel.add(Utility.getDefaultTextArea(Messages.message("report.foreignAffairs.notice"), 40),
                        "newline 20, span 8");

        reportPanel.doLayout();
    }
}

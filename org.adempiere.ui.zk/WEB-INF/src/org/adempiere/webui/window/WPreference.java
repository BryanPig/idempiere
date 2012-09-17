/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.adempiere.webui.window;

import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Listbox;
import org.adempiere.webui.component.ListboxFactory;
import org.adempiere.webui.component.ToolBar;
import org.adempiere.webui.component.ToolBarButton;
import org.adempiere.webui.editor.WYesNoEditor;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.util.UserPreference;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Space;

/**
 *
 * @author hengsin
 *
 */
public class WPreference extends Popup implements EventListener<Event> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8223456746437563389L;

	private WYesNoEditor autoCommit;
	private WYesNoEditor autoNew;
	private WYesNoEditor tabCollapsible;
	private WYesNoEditor adempiereSys;
	private WYesNoEditor logMigrationScript;
	private Listbox tabPlacement;

	public WPreference() {
		super();
		init();
	}

	private void init() {

		Div div = new Div();
		div.setStyle("background-color: transparent !important; border: none; margin: 5px;");
		autoCommit = new WYesNoEditor("AutoCommit", Msg.getMsg(Env.getCtx(), "AutoCommit", true),
				null, false, false, true);
		div.appendChild(autoCommit.getComponent());
		autoCommit.getComponent().setTooltiptext(Msg.getMsg(Env.getCtx(), "AutoCommit", false));
		this.appendChild(div);

		autoNew = new WYesNoEditor("AutoNew", Msg.getMsg(Env.getCtx(), "AutoNew", true),
				null, false, false, true);
		autoNew.getComponent().setTooltiptext(Msg.getMsg(Env.getCtx(), "AutoNew", false));
		div = new Div();
		div.setStyle("background-color: transparent !important; border: none; margin: 5px;");
		div.appendChild(autoNew.getComponent());
		this.appendChild(div);

		tabCollapsible = new WYesNoEditor("WindowTabCollapsible", Msg.getMsg(Env.getCtx(), "WindowTabCollapsible", true),
				null, false, false, true);
		tabCollapsible.getComponent().setTooltiptext(Msg.getMsg(Env.getCtx(), "WindowTabCollapsible", false));
		div = new Div();
		div.setStyle("background-color: transparent !important; border: none; margin: 5px;");
		div.appendChild(tabCollapsible.getComponent());
		this.appendChild(div);

		div = new Div();
		div.setStyle("background-color: transparent !important; border: none; margin: 5px;");
		Label label = new Label(Msg.getMsg(Env.getCtx(), "WindowTabPlacement", true));
		label.setTooltiptext(Msg.getMsg(Env.getCtx(), "WindowTabPlacement", false));
		div.appendChild(label);
		div.appendChild(new Space());
		tabPlacement = ListboxFactory.newDropdownListbox();
		tabPlacement.appendItem(Msg.getMsg(Env.getCtx(), "Left", true), "Left");
		tabPlacement.appendItem(Msg.getMsg(Env.getCtx(), "Right", true), "Right");
		div.appendChild(tabPlacement);
		this.appendChild(div);

		if (Env.getAD_Client_ID(Env.getCtx()) <= 20 && Env.getAD_User_ID(Env.getCtx()) <= 102) {
			adempiereSys = new WYesNoEditor("AdempiereSys", Msg.getMsg(Env.getCtx(), "AdempiereSys", true),
					null, false, false, true);
			adempiereSys.getComponent().setTooltiptext(Msg.getMsg(Env.getCtx(), "AdempiereSys", false));
			div = new Div();
			div.setStyle("background-color: transparent !important; border: none; margin: 5px;");
			div.appendChild(adempiereSys.getComponent());
			this.appendChild(div);
			adempiereSys.setValue(Env.getCtx().getProperty("AdempiereSys"));

			logMigrationScript = new WYesNoEditor("LogMigrationScript", Msg.getMsg(Env.getCtx(), "LogMigrationScript", true),
					null, false, false, true);
			logMigrationScript.getComponent().setTooltiptext(Msg.getMsg(Env.getCtx(), "LogMigrationScript", false));
			div = new Div();
			div.setStyle("background-color: transparent !important; border: none; margin: 5px;");
			div.appendChild(logMigrationScript.getComponent());
			this.appendChild(div);
			logMigrationScript.setValue(Env.getCtx().getProperty("LogMigrationScript"));
		}

		Separator separator = new Separator();
		separator.setSpacing("20px");
		div = new Div();
		div.setStyle("background-color: transparent !important; border: none; margin: 5px;");
		div.appendChild(separator);
		this.appendChild(div);

		ToolBar toolbar = new ToolBar();
		toolbar.setAlign("end");
		this.appendChild(toolbar);
		ToolBarButton btn = new ToolBarButton("");
        btn.setName("btnSave");
        btn.setImage("/images/Save24.png");
        btn.setTooltiptext(Msg.getMsg(Env.getCtx(),"Save"));
        btn.addEventListener(Events.ON_CLICK, this);
        toolbar.appendChild(btn);
        toolbar.setStyle("border: none");

		UserPreference preference = SessionManager.getSessionApplication().getUserPreference();
		autoCommit.setValue(preference.getProperty(UserPreference.P_AUTO_COMMIT));
		autoNew.setValue(preference.getProperty(UserPreference.P_AUTO_NEW));
		tabCollapsible.setValue(preference.getProperty(UserPreference.P_WINDOW_TAB_COLLAPSIBLE));
		tabPlacement.setValue(preference.getProperty(UserPreference.P_WINDOW_TAB_PLACEMENT));
	}

	public void onEvent(Event event) throws Exception {
		if (Events.ON_CLICK.equals(event.getName())) {
			onSave();
		}
	}

	private void onSave() {
		UserPreference preference = SessionManager.getSessionApplication().getUserPreference();
		preference.setProperty(UserPreference.P_AUTO_COMMIT,
				(Boolean)autoCommit.getValue() ? "Y" : "N");
		preference.setProperty(UserPreference.P_AUTO_NEW,
				(Boolean)autoNew.getValue() ? "Y" : "N");
		preference.setProperty(UserPreference.P_WINDOW_TAB_COLLAPSIBLE,
				(Boolean)tabCollapsible.getValue() ? "Y" : "N");
		preference.setProperty(UserPreference.P_WINDOW_TAB_PLACEMENT,
				(String)tabPlacement.getSelectedItem().getValue());

		preference.savePreference();

		//update context
		Env.setAutoCommit(Env.getCtx(), "y".equalsIgnoreCase(preference.getProperty(UserPreference.P_AUTO_COMMIT)));
		Env.setAutoNew(Env.getCtx(), "y".equalsIgnoreCase(preference.getProperty(UserPreference.P_AUTO_NEW)));
		
		// Log Migration Script and AdempiereSys are just in-memory preferences, must not be saved
		if (logMigrationScript != null)
			Env.getCtx().setProperty("LogMigrationScript", (Boolean)logMigrationScript.getValue() ? "Y" : "N");
		if (adempiereSys != null)
			Env.getCtx().setProperty("AdempiereSys", (Boolean)adempiereSys.getValue() ? "Y" : "N");

		this.detach();
	}
}

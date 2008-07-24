/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

/**
 *
 * @author  <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @date    Feb 25, 2007
 * @version $Revision: 0.10 $
 */
public class Listbox extends org.zkoss.zul.Listbox implements EventListener
{
    private static final long serialVersionUID = 1L;
    private PropertyChangeSupport m_propertyChangeListeners = new PropertyChangeSupport(this);
    
    private List<EventListener> doubleClickListeners = new ArrayList<EventListener>();
    private List<EventListener> onDropListeners = new ArrayList<EventListener>();
	private boolean draggable;
    
    public void setEnabled(boolean enabled)
    {
        this.setDisabled(!enabled);
    }
    
    public boolean isEnabled()
    {
        return !this.isDisabled();
    }
    
    public ListItem appendItem(String label, Object value)
    {
        ListItem item = new ListItem(label, value);
        super.appendChild(item);
        return item;
    }
    
    public ListItem appendItem(String label, String value)
    {
        ListItem item = new ListItem(label, value);
        super.appendChild(item);
        return item;
    }
    
    public ListItem getItemAtIndex(int index)
    {
        return (ListItem)super.getItemAtIndex(index);
    }
    
    public ListItem getSelectedItem()
    {
        return (ListItem)super.getSelectedItem();
    }
    
    @SuppressWarnings("unchecked")
    public List<ListItem> getItems()
    {
        return (List<ListItem>)super.getItems();
    }
    
    public synchronized void addPropertyChangeListener(PropertyChangeListener l)
	{
		m_propertyChangeListeners.addPropertyChangeListener(l);
	}
    
    /** 
     * Set selected item for the list box based on the value of list item
     * set selected to none if no item found matching the value given or 
     * value is null
     * @param value Value of ListItem to set as selected
     */
    public void setValue(Object value)
    {
        setSelectedItem(null);
        
        if (value == null)
        {
            return ;
        }
        
        List<ListItem> items = getItems();
        for (ListItem item : items)
        {
        	if (value.getClass() != item.getValue().getClass()) {
        		// if the classes of value and item are different convert both to String
        		String stringValue = value.toString();
        		String stringItem = item.getValue().toString();
                if (stringValue.equals(stringItem))
                {
                    setSelectedItem(item);
                    break;
                }
        	} else {
                if (value.equals(item.getValue()))
                {
                    setSelectedItem(item);
                    break;
                }
        	}
        }
    }
    
    public ListHead getListHead()
    {
    	return (ListHead)super.getListhead();
    }

	public int[] getSelectedIndices() {
		Set selectedItems = this.getSelectedItems();
		int[] selecteds = new int[this.getSelectedCount()];
		int i = 0;
		for (Object obj : selectedItems) {
			ListItem listItem = (ListItem) obj;
			selecteds[i] = this.getIndexOfItem(listItem);
			i++;
		}
		return selecteds;
	}
	
	public void setSelectedIndices(int[] selected) {
		this.clearSelection();
		for(int i : selected) {
			this.setSelectedIndex(i);
		}
	}

	public void addOnDropListener(EventListener listener) {
		onDropListeners.add(listener);
	}

	public void addDoubleClickListener(EventListener listener) {
		doubleClickListeners.add(listener);
	}
	
	@Override
	public boolean insertBefore(Component newChild, Component refChild) {
		if (newChild instanceof ListItem) {
			newChild.addEventListener(Events.ON_DOUBLE_CLICK, this);
			if (onDropListeners.size() > 0) {
				((ListItem)newChild).setDroppable("true");	
				newChild.addEventListener(Events.ON_DROP, this);
			}
			if (isItemDraggable()) {
				((ListItem)newChild).setDraggable("true");
			}
		}
		return super.insertBefore(newChild, refChild);
	}

	public boolean isItemDraggable() {
		return draggable;
	}
	
	public void setItemDraggable(boolean b) {
		draggable = b;
	}

	public void onEvent(Event event) throws Exception {
		if (Events.ON_DOUBLE_CLICK.equals(event.getName()) && !doubleClickListeners.isEmpty()) {
			for(EventListener listener : doubleClickListeners) {
				listener.onEvent(event);
			}
		} else if (Events.ON_DROP.equals(event.getName()) && !onDropListeners.isEmpty()) {
			for(EventListener listener : onDropListeners) {
				listener.onEvent(event);
			}
		}
	}    
}
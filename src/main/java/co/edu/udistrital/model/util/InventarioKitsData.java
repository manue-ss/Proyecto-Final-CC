package co.edu.udistrital.model.util;

import java.io.Serializable;
import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import co.edu.udistrital.model.structures.Stack;

public class InventarioKitsData implements Serializable {

    private static final long serialVersionUID = 1L;

    public SimpleLinkedList<Kit> fullInventory;

    public Stack<Kit> readyStack;
    public Stack<Kit> maintenanceStack;

    public InventarioKitsData() {
        this.fullInventory = new SimpleLinkedList<>();
        this.readyStack = new Stack<>();
        this.maintenanceStack = new Stack<>();
    }
}

package co.edu.udistrital.model.daos;

import co.edu.udistrital.model.entities.Kit;
import co.edu.udistrital.model.enums.EstadoKit;
import co.edu.udistrital.model.structures.SimpleLinkedList;
import co.edu.udistrital.model.util.GestorArchivosBinarios;
import co.edu.udistrital.model.util.InventarioKitsData;

public final class KitDAO {

    private static final String FILE_NAME = "kits.dat";

    private final GestorArchivosBinarios<InventarioKitsData> binaryManager;

    private InventarioKitsData data;

    public KitDAO() {
        this.binaryManager = new GestorArchivosBinarios<>();

        this.data = binaryManager.cargarDatos(FILE_NAME);

        if (this.data == null) {
            this.data = new InventarioKitsData();
        }
    }

    public void addKit(Kit newKit) {
        data.fullInventory.addLast(newKit);

        if (newKit.getEstado() == EstadoKit.DISPONIBLE) {
            data.readyStack.push(newKit);
        } else if (newKit.getEstado() == EstadoKit.EN_MANTENIMIENTO) {
            data.maintenanceStack.push(newKit);
        }

        saveAll();
    }

    public Kit popReadyKit() {
        if (!data.readyStack.isEmpty()) {
            Kit extracted = data.readyStack.pop();
            extracted.setEstado(EstadoKit.ASIGNADO);
            saveAll();
            return extracted;
        }
        return null;
    }

    public Kit popMaintenanceKit() {
        if (!data.maintenanceStack.isEmpty()) {

            Kit extracted = data.maintenanceStack.pop();
            saveAll();
            return extracted;
        }
        return null;
    }

    public void returnFromService(Kit returnedKit, boolean needsMaintenance) {
        if (needsMaintenance) {
            returnedKit.setEstado(EstadoKit.EN_MANTENIMIENTO);
            data.maintenanceStack.push(returnedKit);
        } else {
            returnedKit.setEstado(EstadoKit.DISPONIBLE);
            data.readyStack.push(returnedKit);
        }
        saveAll();
    }

    public void finishMaintenance(Kit repairedKit) {
        repairedKit.setEstado(EstadoKit.DISPONIBLE);
        data.readyStack.push(repairedKit);
        saveAll();
    }

    public SimpleLinkedList<Kit> getAll() {
        return data.fullInventory;
    }

    public void update() {
        saveAll();
    }

    private void saveAll() {
        binaryManager.guardarDatos(FILE_NAME, data);
    }

    public Kit getById(int id) {
        for (Kit kit : data.fullInventory) {
            if (kit.getId() == id) {
                return kit;
            }
        }
        return null;
    }
}

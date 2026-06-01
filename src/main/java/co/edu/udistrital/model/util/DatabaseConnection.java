package co.edu.udistrital.model.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Manuel Salazar
 * @since 0.1
 */
public class DatabaseConnection {

    private static DatabaseConnection instancia;
    private Connection conexion;

    private static final String URL;
    private static final String USER;
    private static final String PASS;
    public static final String DRIVER = "org.mariadb.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        }
        catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "CRÍTICO: El Driver de MariaDB no está en las librerías del proyecto."
            );
        }

        Properties props = new Properties();

        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new ExceptionInInitializerError("CRÍTICO: No se encontró el archivo db.properties en Source Packages.");
            }
            props.load(input);

        }
        catch (Exception e) {
            throw new ExceptionInInitializerError("CRÍTICO: Error leyendo db.properties: " + e.getMessage());
        }

        String host = props.getProperty("db.host");
        String port = props.getProperty("db.port");
        String name = props.getProperty("db.name");

        USER = props.getProperty("db.user");
        PASS = props.getProperty("db.pass");

        if (host == null || port == null || name == null || USER == null || PASS == null) {
            throw new ExceptionInInitializerError(
                    "CRÍTICO: Faltan variables en el archivo db.properties."
            );
        }

        URL = "jdbc:mariadb://" + host + ":" + port + "/" + name;
    }

    private DatabaseConnection() {
        try {
            this.conexion = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Conexión a MariaDB establecida con éxito.");
        }
        catch (SQLException e) {
            System.err.println("Error crítico al conectar con MariaDB.");
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instancia == null) {
            instancia = new DatabaseConnection();
        }
        return instancia;
    }

    public Connection getConnection() {
        return conexion;
    }
    
    /**
     * Cierra la conexión activa con la base de datos de forma segura.
     */
    public void closeConnection() {
        try {
            if (this.conexion != null && !this.conexion.isClosed()) {
                this.conexion.close();
                System.out.println("Conexión a MariaDB cerrada exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar cerrar la conexión a MariaDB.");
            e.printStackTrace();
        }
    }
}

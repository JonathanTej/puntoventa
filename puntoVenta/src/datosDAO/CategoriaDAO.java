package datosDAO;

import datos.interfaces.CRUDGenerallnterface;
import entidades.Categoria;
import database.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class CategoriaDAO implements CRUDGenerallnterface<Categoria> {

    private final Conexion conectar;
    private final Connection connection; // Agregamos esta variable para acceder a la conexión
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public CategoriaDAO() {
        conectar = Conexion.getInstance();
        connection = conectar.conectar(); // Obtenemos la conexión de la instancia
    }

    @Override
    public List<Categoria> getAll(String list) {
        List<Categoria> registros = new ArrayList<>();
        try {
            ps = connection.prepareStatement("SELECT * FROM categoria WHERE nombre LIKE ?");
            ps.setString(1, "%" + list + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Categoria(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4)));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            closeResources();
        }
        return registros;
    }

    @Override
    public boolean insert(Categoria object) {
        resp = false;
        try {
            ps = connection.prepareStatement("INSERT INTO categoria(nombre, descripcion, estado) VALUES (?, ?, 1)");
            ps.setString(1, object.getNombre());
            ps.setString(2, object.getDescripcion());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            closeResources();
        }
        return resp;
    }

    @Override
    public boolean update(Categoria object) {
        resp = false;
        try {
            ps = connection.prepareStatement("UPDATE categoria SET nombre=?, descripcion=? WHERE id=?");
            ps.setString(1, object.getNombre());
            ps.setString(2, object.getDescripcion());
            ps.setInt(3, object.getId());
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            closeResources();
        }
        return resp;
    }

    @Override
    public boolean onVariable(int id) {
        return updateEstado(id, 1);
    }

    @Override
    public boolean offVariable(int id) {
        return updateEstado(id, 0);
    }

    private boolean updateEstado(int id, int estado) {
        resp = false;
        try {
            ps = connection.prepareStatement("UPDATE categoria SET estado=? WHERE id=?");
            ps.setInt(1, estado);
            ps.setInt(2, id);
            if (ps.executeUpdate() > 0) {
                resp = true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            closeResources();
        }
        return resp;
    }

    @Override
    public boolean exist(String text) {
        resp = false;
        try {
            ps = connection.prepareStatement("SELECT nombre FROM categoria WHERE id = ?");
            ps.setInt(1, Integer.parseInt(text));
            rs = ps.executeQuery();
            if (rs.next()) {
                resp = true;
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            closeResources();
        }
        return resp;
    }

    @Override
    public int total() {
        int totalRegistro = 0;
        try {
            ps = connection.prepareStatement("SELECT COUNT(id) FROM categoria");
            rs = ps.executeQuery();
            if (rs.next()) {
                totalRegistro = rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            closeResources();
        }
        return totalRegistro;
    }

    private void closeResources() {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cerrar recursos: " + e.getMessage());
        } finally {
            ps = null;
            rs = null;
        }
    }
}

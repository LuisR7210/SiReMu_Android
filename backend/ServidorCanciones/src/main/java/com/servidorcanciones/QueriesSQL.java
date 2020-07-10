/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SiReMuCanciones;

/**
 *
 * @author IS Vega
 */
public class QueriesSQL {

    public String buscarCancion(int idCancion) {
        return "SELECT * FROM CancionSet WHERE Id = " + idCancion;
    }

    public String buscarCanciones(String nombreCancion) {
        return "SELECT TOP 20 c.*, a.companiaDisco, a.fechaLanzamiento, a.ilustracion, a.nombre AS nombreAlbum FROM CancionSet c\n"
                + "	INNER JOIN AlbumSet a ON c.Album_Id = a.Id\n"
                + "	WHERE c.esPublica = 1 AND c.nombre LIKE '%" + nombreCancion + "%'";
    }

    public String consultarMisAlbums(int idUsuario) {
        return "SELECT * FROM AlbumSet WHERE Usuario_Id = " + idUsuario;
    }

    public String consultarCancionesAlbum(int idAlbum) {
        return "SELECT * FROM CancionSet WHERE Album_Id = " + idAlbum;
    }

    public String consultarCancionesLista(int idLista) {
        return "SELECT c.*, a.companiaDisco, a.fechaLanzamiento, a.ilustracion, a.nombre AS nombreAlbum\n"
                + "    FROM ListaReproduccionCancion lc \n"
                + "    INNER JOIN (CancionSet c LEFT JOIN AlbumSet a ON a.Id = c.Album_Id) ON lc.Cancion_Id = c.Id\n"
                + "    WHERE lc.ListaReproduccion_Id = " + idLista;
    }

    public String consultarMisCanciones(int idUsuario) {
        return "SELECT DISTINCT c.*, a.companiaDisco, a.fechaLanzamiento, a.ilustracion, a.nombre AS nombreAlbum\n"
                + "    FROM ListaReproduccionCancion lc \n"
                + "	LEFT JOIN ListaReproduccionSet l ON lc.ListaReproduccion_Id = l.Id\n"
                + "    INNER JOIN (CancionSet c LEFT JOIN AlbumSet a ON a.Id = c.Album_Id) ON lc.Cancion_Id = c.Id\n"
                + "	LEFT JOIN UsuarioListaReproduccion ul ON ul.ListaReproduccion_Id = l.Id\n"
                + "    WHERE ul.Usuario_Id = " + idUsuario;
    }

}

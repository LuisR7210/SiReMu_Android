// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: manejoCanciones.proto

package mx.uv.manejoCanciones;

public interface CancionOrBuilder extends
    // @@protoc_insertion_point(interface_extends:mnjCanciones.Cancion)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 id = 1;</code>
   * @return The id.
   */
  int getId();

  /**
   * <code>string artista = 2;</code>
   * @return The artista.
   */
  java.lang.String getArtista();
  /**
   * <code>string artista = 2;</code>
   * @return The bytes for artista.
   */
  com.google.protobuf.ByteString
      getArtistaBytes();

  /**
   * <code>string duracion = 3;</code>
   * @return The duracion.
   */
  java.lang.String getDuracion();
  /**
   * <code>string duracion = 3;</code>
   * @return The bytes for duracion.
   */
  com.google.protobuf.ByteString
      getDuracionBytes();

  /**
   * <code>bool esPRomocion = 4;</code>
   * @return The esPRomocion.
   */
  boolean getEsPRomocion();

  /**
   * <code>bool esPublica = 5;</code>
   * @return The esPublica.
   */
  boolean getEsPublica();

  /**
   * <code>string genero = 6;</code>
   * @return The genero.
   */
  java.lang.String getGenero();
  /**
   * <code>string genero = 6;</code>
   * @return The bytes for genero.
   */
  com.google.protobuf.ByteString
      getGeneroBytes();

  /**
   * <code>int32 likes = 7;</code>
   * @return The likes.
   */
  int getLikes();

  /**
   * <code>string nombre = 8;</code>
   * @return The nombre.
   */
  java.lang.String getNombre();
  /**
   * <code>string nombre = 8;</code>
   * @return The bytes for nombre.
   */
  com.google.protobuf.ByteString
      getNombreBytes();

  /**
   * <code>.mnjCanciones.Album album = 9;</code>
   * @return Whether the album field is set.
   */
  boolean hasAlbum();
  /**
   * <code>.mnjCanciones.Album album = 9;</code>
   * @return The album.
   */
  mx.uv.manejoCanciones.Album getAlbum();
  /**
   * <code>.mnjCanciones.Album album = 9;</code>
   */
  mx.uv.manejoCanciones.AlbumOrBuilder getAlbumOrBuilder();
}

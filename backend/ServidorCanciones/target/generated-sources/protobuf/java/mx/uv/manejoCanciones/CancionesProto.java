// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: manejoCanciones.proto

package mx.uv.manejoCanciones;

public final class CancionesProto {
  private CancionesProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_NombreCancion_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_NombreCancion_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_AlbumAG_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_AlbumAG_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_CancionAG_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_CancionAG_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_IdUsuario_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_IdUsuario_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_IdLista_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_IdLista_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_ListaCanciones_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_ListaCanciones_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_ListaAlbums_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_ListaAlbums_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_RespuestaCanciones_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_RespuestaCanciones_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_CancionPP_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_CancionPP_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_Album_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_Album_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_mnjCanciones_Cancion_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_mnjCanciones_Cancion_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\025manejoCanciones.proto\022\014mnjCanciones\"\037\n" +
      "\rNombreCancion\022\016\n\006nombre\030\001 \001(\t\"@\n\007AlbumA" +
      "G\022\021\n\tidUsuario\030\001 \001(\005\022\"\n\005album\030\002 \001(\0132\023.mn" +
      "jCanciones.Album\"F\n\tCancionAG\022\021\n\tidUsuar" +
      "io\030\001 \001(\005\022&\n\007cancion\030\002 \001(\0132\025.mnjCanciones" +
      ".Cancion\"\027\n\tIdUsuario\022\n\n\002id\030\001 \001(\005\"\025\n\007IdL" +
      "ista\022\n\n\002id\030\001 \001(\005\":\n\016ListaCanciones\022(\n\tca" +
      "nciones\030\001 \003(\0132\025.mnjCanciones.Cancion\"2\n\013" +
      "ListaAlbums\022#\n\006albums\030\001 \003(\0132\023.mnjCancion" +
      "es.Album\"<\n\022RespuestaCanciones\022\021\n\trespue" +
      "sta\030\001 \001(\010\022\023\n\013codigoError\030\002 \001(\t\"1\n\tCancio" +
      "nPP\022\021\n\tidCancion\030\001 \001(\005\022\021\n\tcontenido\030\002 \001(" +
      "\014\"\202\001\n\005Album\022\n\n\002id\030\001 \001(\005\022\020\n\010compania\030\002 \001(" +
      "\t\022\030\n\020fechaLanzamiento\030\003 \001(\t\022\023\n\013ilustraci" +
      "on\030\004 \001(\014\022\034\n\024extensionIlustracion\030\005 \001(\t\022\016" +
      "\n\006nombre\030\006 \001(\t\"\263\001\n\007Cancion\022\n\n\002id\030\001 \001(\005\022\017" +
      "\n\007artista\030\002 \001(\t\022\020\n\010duracion\030\003 \001(\t\022\023\n\013esP" +
      "Romocion\030\004 \001(\010\022\021\n\tesPublica\030\005 \001(\010\022\016\n\006gen" +
      "ero\030\006 \001(\t\022\r\n\005likes\030\007 \001(\005\022\016\n\006nombre\030\010 \001(\t" +
      "\022\"\n\005album\030\t \001(\0132\023.mnjCanciones.Album2\250\007\n" +
      "\017ManejoCanciones\022L\n\017BuscarCanciones\022\033.mn" +
      "jCanciones.NombreCancion\032\034.mnjCanciones." +
      "ListaCanciones\022E\n\nCrearAlbum\022\025.mnjCancio" +
      "nes.AlbumAG\032 .mnjCanciones.RespuestaCanc" +
      "iones\022N\n\025ConsultarMisCanciones\022\027.mnjCanc" +
      "iones.IdUsuario\032\034.mnjCanciones.ListaCanc" +
      "iones\022V\n\035ConsultarCancionesEnPromocion\022\027" +
      ".mnjCanciones.IdUsuario\032\034.mnjCanciones.L" +
      "istaCanciones\022E\n\017ConsultarAlbums\022\027.mnjCa" +
      "nciones.IdUsuario\032\031.mnjCanciones.ListaAl" +
      "bums\022L\n\027ConsultarCancionesAlbum\022\023.mnjCan" +
      "ciones.Album\032\034.mnjCanciones.ListaCancion" +
      "es\022N\n\027ConsultarCancionesLista\022\025.mnjCanci" +
      "ones.IdLista\032\034.mnjCanciones.ListaCancion" +
      "es\022C\n\014GenerarRadio\022\025.mnjCanciones.Cancio" +
      "n\032\034.mnjCanciones.ListaCanciones\022I\n\016Modif" +
      "icarAlbum\022\025.mnjCanciones.AlbumAG\032 .mnjCa" +
      "nciones.RespuestaCanciones\022G\n\021Reproducir" +
      "Cancion\022\027.mnjCanciones.CancionAG\032\027.mnjCa" +
      "nciones.CancionPP0\001\022M\n\020RegistrarCancion\022" +
      "\027.mnjCanciones.CancionAG\032 .mnjCanciones." +
      "RespuestaCanciones\022K\n\014SubirCancion\022\027.mnj" +
      "Canciones.CancionPP\032 .mnjCanciones.Respu" +
      "estaCanciones(\001B.\n\025mx.uv.manejoCanciones" +
      "B\016CancionesProtoP\001\242\002\002MCb\006proto3"
    };
    descriptor = com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        });
    internal_static_mnjCanciones_NombreCancion_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_mnjCanciones_NombreCancion_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_NombreCancion_descriptor,
        new java.lang.String[] { "Nombre", });
    internal_static_mnjCanciones_AlbumAG_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_mnjCanciones_AlbumAG_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_AlbumAG_descriptor,
        new java.lang.String[] { "IdUsuario", "Album", });
    internal_static_mnjCanciones_CancionAG_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_mnjCanciones_CancionAG_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_CancionAG_descriptor,
        new java.lang.String[] { "IdUsuario", "Cancion", });
    internal_static_mnjCanciones_IdUsuario_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_mnjCanciones_IdUsuario_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_IdUsuario_descriptor,
        new java.lang.String[] { "Id", });
    internal_static_mnjCanciones_IdLista_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_mnjCanciones_IdLista_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_IdLista_descriptor,
        new java.lang.String[] { "Id", });
    internal_static_mnjCanciones_ListaCanciones_descriptor =
      getDescriptor().getMessageTypes().get(5);
    internal_static_mnjCanciones_ListaCanciones_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_ListaCanciones_descriptor,
        new java.lang.String[] { "Canciones", });
    internal_static_mnjCanciones_ListaAlbums_descriptor =
      getDescriptor().getMessageTypes().get(6);
    internal_static_mnjCanciones_ListaAlbums_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_ListaAlbums_descriptor,
        new java.lang.String[] { "Albums", });
    internal_static_mnjCanciones_RespuestaCanciones_descriptor =
      getDescriptor().getMessageTypes().get(7);
    internal_static_mnjCanciones_RespuestaCanciones_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_RespuestaCanciones_descriptor,
        new java.lang.String[] { "Respuesta", "CodigoError", });
    internal_static_mnjCanciones_CancionPP_descriptor =
      getDescriptor().getMessageTypes().get(8);
    internal_static_mnjCanciones_CancionPP_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_CancionPP_descriptor,
        new java.lang.String[] { "IdCancion", "Contenido", });
    internal_static_mnjCanciones_Album_descriptor =
      getDescriptor().getMessageTypes().get(9);
    internal_static_mnjCanciones_Album_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_Album_descriptor,
        new java.lang.String[] { "Id", "Compania", "FechaLanzamiento", "Ilustracion", "ExtensionIlustracion", "Nombre", });
    internal_static_mnjCanciones_Cancion_descriptor =
      getDescriptor().getMessageTypes().get(10);
    internal_static_mnjCanciones_Cancion_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_mnjCanciones_Cancion_descriptor,
        new java.lang.String[] { "Id", "Artista", "Duracion", "EsPRomocion", "EsPublica", "Genero", "Likes", "Nombre", "Album", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}

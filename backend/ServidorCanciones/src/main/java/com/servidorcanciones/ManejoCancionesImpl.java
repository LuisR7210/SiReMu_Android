/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servidorcanciones;

import SiReMuCanciones.QueriesSQL;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.uv.manejoCanciones.*;

/**
 *
 * @author IS Vega
 */
public class ManejoCancionesImpl extends ManejoCancionesGrpc.ManejoCancionesImplBase {

    private final int size = (int) (1024 * 1024 * 0.5);
    private Connection conn = null;
    private final String connectionUrl = "jdbc:sqlserver://CRIMSON-ROSE:1433;databaseName=SiReMu;user=SiReMuServer;password=Servidor2020SiReMu5129";

    public void subirCancion() {
//        if (conn != null) {
//            DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
//            System.out.println("Driver name: " + dm.getDriverName());
//            System.out.println("Driver version: " + dm.getDriverVersion());
//            System.out.println("Product name: " + dm.getDatabaseProductName());
//            System.out.println("Product version: " + dm.getDatabaseProductVersion());
//        }
    }

    @Override
    public void reproducirCancion(CancionAG request, StreamObserver<CancionPP> responseObserver) {
        File archivo;
        QueriesSQL queries = new QueriesSQL();
        try {
            conn = DriverManager.getConnection(connectionUrl);
            Statement stmt = conn.createStatement();
            ResultSet resultado = stmt.executeQuery(queries.buscarCancion(request.getCancion().getId()));
            if (resultado.next()) {
                archivo = new File(resultado.getString("archivo"));
                FileInputStream ios = new FileInputStream(archivo);
                byte[] buffer = new byte[size];
                while (ios.read(buffer) != -1) {
                    responseObserver.onNext(CancionPP.newBuilder().setContenido(ByteString.copyFrom(buffer)).build());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } catch (IOException ex) {
            System.out.println("Error IO: " + ex.getMessage());
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<CancionPP> subirCancion(StreamObserver<RespuestaCanciones> responseObserver) {
        return super.subirCancion(responseObserver); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registrarCancion(CancionAG request, StreamObserver<RespuestaCanciones> responseObserver) {
        super.registrarCancion(request, responseObserver); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modificarAlbum(AlbumAG request, StreamObserver<RespuestaCanciones> responseObserver) {
        super.modificarAlbum(request, responseObserver); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void generarRadio(Cancion request, StreamObserver<ListaCanciones> responseObserver) {
        super.generarRadio(request, responseObserver); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void consultarCancionesLista(IdLista request, StreamObserver<ListaCanciones> responseObserver) {
        QueriesSQL queries = new QueriesSQL();
        DateTimeFormatter formatoTiempo = DateTimeFormatter.ofPattern("mm:ss");
        ArrayList<Cancion> canciones = new ArrayList<>();
        File archivo;
        byte[] buffer;
        try {
            conn = DriverManager.getConnection(connectionUrl);
            Statement stmt = conn.createStatement();
            ResultSet resultados = stmt.executeQuery(queries.consultarCancionesLista(request.getId()));
            while (resultados.next()) {
                archivo = new File(resultados.getString("ilustracion"));
                buffer = Files.readAllBytes(archivo.toPath());
                Album album = Album.newBuilder()
                        .setId(resultados.getInt("Album_Id"))
                        .setCompania(resultados.getString("companiaDisco"))
                        .setFechaLanzamiento(resultados.getDate("fechaLanzamiento").toLocalDate().toString())
                        .setNombre(resultados.getString("nombreAlbum"))
                        .setIlustracion(ByteString.copyFrom(buffer))
                        .build();
                canciones.add(Cancion.newBuilder()
                        .setId(resultados.getInt("Id"))
                        .setArtista(resultados.getString("artista"))
                        .setDuracion(resultados.getTime("duracion").toLocalTime().format(formatoTiempo))
                        .setGenero(resultados.getString("genero"))
                        .setNombre(resultados.getString("nombre"))
                        .setEsPRomocion(resultados.getBoolean("esPromocion"))
                        .setEsPublica(resultados.getBoolean("esPublica"))
                        .setLikes(resultados.getInt("likes"))
                        .setAlbum(album)
                        .build());
            }
            responseObserver.onNext(ListaCanciones.newBuilder().addAllCanciones(canciones).build());
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(ManejoCancionesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void consultarCancionesAlbum(Album request, StreamObserver<ListaCanciones> responseObserver) {
        QueriesSQL queries = new QueriesSQL();
        DateTimeFormatter formatoTiempo = DateTimeFormatter.ofPattern("mm:ss");
        ArrayList<Cancion> canciones = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(connectionUrl);
            Statement stmt = conn.createStatement();
            ResultSet resultados = stmt.executeQuery(queries.consultarCancionesAlbum(request.getId()));
            while (resultados.next()) {
                canciones.add(Cancion.newBuilder()
                        .setId(resultados.getInt("Id"))
                        .setArtista(resultados.getString("artista"))
                        .setDuracion(resultados.getTime("duracion").toLocalTime().format(formatoTiempo))
                        .setGenero(resultados.getString("genero"))
                        .setNombre(resultados.getString("nombre"))
                        .setEsPRomocion(resultados.getBoolean("esPromocion"))
                        .setEsPublica(resultados.getBoolean("esPublica"))
                        .setLikes(resultados.getInt("likes"))
                        .build());
            }
            responseObserver.onNext(ListaCanciones.newBuilder().addAllCanciones(canciones).build());
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } 
        responseObserver.onCompleted();
    }

    @Override
    public void consultarAlbums(IdUsuario request, StreamObserver<ListaAlbums> responseObserver) {
        QueriesSQL queries = new QueriesSQL();
        ArrayList<Album> albums = new ArrayList<>();
        File archivo;
        byte[] buffer;
        try {
            conn = DriverManager.getConnection(connectionUrl);
            Statement stmt = conn.createStatement();
            ResultSet resultados = stmt.executeQuery(queries.consultarMisAlbums(request.getId()));
            while (resultados.next()) {
                archivo = new File(resultados.getString("ilustracion"));
                buffer = Files.readAllBytes(archivo.toPath());
                albums.add(Album.newBuilder()
                        .setId(resultados.getInt("Id"))
                        .setCompania(resultados.getString("companiaDisco"))
                        .setFechaLanzamiento(resultados.getDate("fechaLanzamiento").toLocalDate().toString())
                        .setNombre(resultados.getString("nombre"))
                        .setIlustracion(ByteString.copyFrom(buffer))
                        .build());
            }
            responseObserver.onNext(ListaAlbums.newBuilder().addAllAlbums(albums).build());
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(ManejoCancionesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void consultarCancionesEnPromocion(IdUsuario request, StreamObserver<ListaCanciones> responseObserver) {
        super.consultarCancionesEnPromocion(request, responseObserver); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void consultarMisCanciones(IdUsuario request, StreamObserver<ListaCanciones> responseObserver) {
        QueriesSQL queries = new QueriesSQL();
        DateTimeFormatter formatoTiempo = DateTimeFormatter.ofPattern("mm:ss");
        ArrayList<Cancion> canciones = new ArrayList<>();
        File archivo;
        byte[] buffer;
        try {
            conn = DriverManager.getConnection(connectionUrl);
            Statement stmt = conn.createStatement();
            ResultSet resultados = stmt.executeQuery(queries.consultarMisCanciones(request.getId()));
            while (resultados.next()) {
                archivo = new File(resultados.getString("ilustracion"));
                buffer = Files.readAllBytes(archivo.toPath());
                Album album = Album.newBuilder()
                        .setId(resultados.getInt("Album_Id"))
                        .setCompania(resultados.getString("companiaDisco"))
                        .setFechaLanzamiento(resultados.getDate("fechaLanzamiento").toLocalDate().toString())
                        .setNombre(resultados.getString("nombreAlbum"))
                        .setIlustracion(ByteString.copyFrom(buffer))
                        .build();
                canciones.add(Cancion.newBuilder()
                        .setId(resultados.getInt("Id"))
                        .setArtista(resultados.getString("artista"))
                        .setDuracion(resultados.getTime("duracion").toLocalTime().format(formatoTiempo))
                        .setGenero(resultados.getString("genero"))
                        .setNombre(resultados.getString("nombre"))
                        .setEsPRomocion(resultados.getBoolean("esPromocion"))
                        .setEsPublica(resultados.getBoolean("esPublica"))
                        .setLikes(resultados.getInt("likes"))
                        .setAlbum(album)
                        .build());
            }
            responseObserver.onNext(ListaCanciones.newBuilder().addAllCanciones(canciones).build());
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(ManejoCancionesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void crearAlbum(AlbumAG request, StreamObserver<RespuestaCanciones> responseObserver) {
        super.crearAlbum(request, responseObserver); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void buscarCanciones(NombreCancion request, StreamObserver<ListaCanciones> responseObserver) {
        QueriesSQL queries = new QueriesSQL();
        DateTimeFormatter formatoTiempo = DateTimeFormatter.ofPattern("mm:ss");
        ArrayList<Cancion> canciones = new ArrayList<>();
        File archivo;
        byte[] buffer;
        try {
            conn = DriverManager.getConnection(connectionUrl);
            Statement stmt = conn.createStatement();
            ResultSet resultados = stmt.executeQuery(queries.buscarCanciones(request.getNombre()));
            while (resultados.next()) {
                archivo = new File(resultados.getString("ilustracion"));
                buffer = Files.readAllBytes(archivo.toPath());
                Album album = Album.newBuilder()
                        .setId(resultados.getInt("Album_Id"))
                        .setCompania(resultados.getString("companiaDisco"))
                        .setFechaLanzamiento(resultados.getDate("fechaLanzamiento").toLocalDate().toString())
                        .setNombre(resultados.getString("nombreAlbum"))
                        .setIlustracion(ByteString.copyFrom(buffer))
                        .build();
                canciones.add(Cancion.newBuilder()
                        .setId(resultados.getInt("Id"))
                        .setArtista(resultados.getString("artista"))
                        .setDuracion(resultados.getTime("duracion").toLocalTime().format(formatoTiempo))
                        .setGenero(resultados.getString("genero"))
                        .setNombre(resultados.getString("nombre"))
                        .setEsPRomocion(resultados.getBoolean("esPromocion"))
                        .setEsPublica(resultados.getBoolean("esPublica"))
                        .setLikes(resultados.getInt("likes"))
                        .setAlbum(album)
                        .build());
            }
            responseObserver.onNext(ListaCanciones.newBuilder().addAllCanciones(canciones).build());
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(ManejoCancionesImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        responseObserver.onCompleted();
    }

}

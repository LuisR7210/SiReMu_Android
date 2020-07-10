/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servidorcanciones;

import SiReMuCanciones.QueriesSQL;
import com.google.protobuf.ByteString;
import io.grpc.Server;
import io.grpc.ServerBuilder;
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
import java.util.concurrent.TimeUnit;
import mx.uv.manejoCanciones.*;

/**
 *
 * @author IS Vega
 */
public class ServidorCanciones {

    private Server server;

    private void start() throws IOException {
        int port = 25112;
        server = ServerBuilder.forPort(port).addService(new ManejoCancionesImpl())
                .build()
                .start();
        System.out.println("Empieza el servidor de canciones, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    ServidorCanciones.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon
     * threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
//        Connection conn = null;
//        String connectionUrl = "jdbc:sqlserver://CRIMSON-ROSE:1433;databaseName=SiReMu;user=SiReMuServer;password=Servidor2020SiReMu5129";
//        QueriesSQL queries = new QueriesSQL();
        final ServidorCanciones servidor= new ServidorCanciones();
        servidor.start();
        servidor.blockUntilShutdown();
    }

}

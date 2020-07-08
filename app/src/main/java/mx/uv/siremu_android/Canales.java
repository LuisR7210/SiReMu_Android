package mx.uv.siremu_android;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Canales {

    public static ManagedChannel canalListas = ManagedChannelBuilder.forAddress("10.0.2.2", 25111).usePlaintext().build();
    public static ManagedChannel canalCanciones = ManagedChannelBuilder.forAddress("10.0.2.2", 25112).usePlaintext().build();

    public static ManagedChannel getCanalListas() {
        return canalListas;
    }

    public static ManagedChannel getCanalCanciones() {
        return canalCanciones;
    }
}

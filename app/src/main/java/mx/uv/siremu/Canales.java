package mx.uv.siremu;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Canales {

    public static ManagedChannel canalListas = ManagedChannelBuilder.forAddress("10.0.2.2", 50051).usePlaintext().build();

    public static ManagedChannel getCanalListas() {
        return canalListas;
    }
}

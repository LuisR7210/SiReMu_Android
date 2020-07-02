package mx.uv.siremu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.okhttp.OkHttpChannelBuilder;
import mx.uv.loginMS.LoginGrpc.*;
import mx.uv.loginMS.Cancion;
import mx.uv.loginMS.Cuenta;
import mx.uv.loginMS.LoginGrpc;
import mx.uv.loginMS.Pedazo;
import mx.uv.loginMS.RespuestaLogin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.a1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), crear_album.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    private static class GrpcTask extends AsyncTask<Void, Void, String> {
        private final GrpcRunnable grpcRunnable;
        private final ManagedChannel channel;
        private final WeakReference<Activity> activityReference;

        GrpcTask(GrpcRunnable grpcRunnable, ManagedChannel channel, Activity activity) {
            this.grpcRunnable = grpcRunnable;
            this.channel = channel;
            this.activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        protected String doInBackground(Void... nothing) {
            try {
                grpcRunnable.run(
                        LoginGrpc.newBlockingStub(channel), LoginGrpc.newStub(channel));
                return "";
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.flush();
                return "Failed... :\n" + sw;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Activity activity = activityReference.get();
            if (activity == null) {
                return;
            }
        }
    }

    private interface GrpcRunnable {
        /** Perform a grpcRunnable and return all the logs. */
        String run(LoginBlockingStub blockingStub, LoginStub asyncStub) throws Exception;
    }

    private static class ClaseCancion implements GrpcRunnable {
        @Override
        public String run(LoginBlockingStub blockingStub, LoginStub asyncStub)
                throws Exception {
            ReproducirCancion(blockingStub);
            return "";
        }

        /**
         * Blocking server-streaming example. Calls listFeatures with a rectangle of interest. Prints
         * each response feature as it arrives.
         */
        private String ReproducirCancion(LoginBlockingStub blockingStub)
                throws StatusRuntimeException {
            Cancion c = Cancion.newBuilder().setId(1).build();
            Iterator<Pedazo> pedazos;
            pedazos = blockingStub.buscarCancion(c);

            MediaPlayer mp = new MediaPlayer();

            while (pedazos.hasNext()) {
                Pedazo feature = pedazos.next();
                feature.getContenido();
            }
            return "";
        }
    }




    /*private static class GrpcTask extends AsyncTask<String, Void, String> {
        private final WeakReference<Activity> activityReference;
        private ManagedChannel channel;

        private GrpcTask(Activity activity) {
            this.activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            String host = params[0];
            String message = params[1];
            String portStr = params[2];
            int port = TextUtils.isEmpty(portStr) ? 0 : Integer.parseInt(portStr);
            try {
                channel = ManagedChannelBuilder.forAddress(host, 50051).usePlaintext().build();
                LoginGrpc.LoginBlockingStub stub = LoginGrpc.newBlockingStub(channel);
                Cuenta request = Cuenta.newBuilder().setUsuario(message).setContra(message).build();
                RespuestaLogin reply = stub.iniciarSesion(request);
                return "Listo";
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.flush();
                return String.format("Failed... : %n%s %s %s", sw, host, port);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Activity activity = activityReference.get();
            if (activity == null) {
                return;
            }
            TextView resultText = (TextView) activity.findViewById(R.id.tvHola);
            resultText.setText(result);
        }
    }*/
}

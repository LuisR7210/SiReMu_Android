package mx.uv.siremu;

import android.media.MediaDataSource;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.stream.Stream;

@RequiresApi(api = Build.VERSION_CODES.M)
public class StreamMediaDataSource extends MediaDataSource
{
    private byte[] data;

    public StreamMediaDataSource(byte[] data)
    {
        this.data = data;
    }

    @Override
    public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
        if (position >= data.length)
        {
            return -1;
        }
        if (position + size > data.length)
        {
            size -= (position + size) - data.length;
        }
//        Array.copy(data, position, buffer, offset, size);
        return size;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long getSize() throws IOException {
        return data.length;
    }

    @Override
    public void close() throws IOException {
        if (data != null)
        {
            data = null;
        }
    }
}

package mx.uv.siremu_android.adaptadores;

public class FilaSeleccionable {

    private String titulo;
    private String subtitulo;
    private String extra;
    private boolean seleccionado;

    public FilaSeleccionable(String titulo, String subtitulo, String extra) {
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.extra = extra;
        this.seleccionado = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
}

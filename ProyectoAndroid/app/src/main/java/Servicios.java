public class Servicios {


    Integer id;
     String nombre;
     String producto;
     Integer peso;
     String lugar;
     String fecha;

    public Servicios() {
    }

    public Servicios(Integer id, String nombre, String producto, Integer peso, String lugar, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.producto = producto;
        this.peso = peso;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

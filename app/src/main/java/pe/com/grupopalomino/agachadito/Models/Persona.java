
package pe.com.grupopalomino.agachadito.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Persona implements Parcelable {

    private String apellidos;
    private String dNI;
    private String direccion;
    private String dni;
    private Boolean estado;
    private String fechaIngreso;
    private String fechaNacimiento;
    private long idPersona;
    private String nombres;

    public Persona(String apellidos, String dNI, String direccion, String dni, Boolean estado, String fechaIngreso, String fechaNacimiento, long idPersona, String nombres) {
        this.apellidos = apellidos;
        this.dNI = dNI;
        this.direccion = direccion;
        this.dni = dni;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.fechaNacimiento = fechaNacimiento;
        this.idPersona = idPersona;
        this.nombres = nombres;
    }

    public Persona() {
    }

    protected Persona(Parcel in) {
        apellidos = in.readString();
        dNI = in.readString();
        direccion = in.readString();
        dni = in.readString();
        byte tmpEstado = in.readByte();
        estado = tmpEstado == 0 ? null : tmpEstado == 1;
        fechaIngreso = in.readString();
        fechaNacimiento = in.readString();
        idPersona = in.readLong();
        nombres = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apellidos);
        dest.writeString(dNI);
        dest.writeString(direccion);
        dest.writeString(dni);
        dest.writeByte((byte) (estado == null ? 0 : estado ? 1 : 2));
        dest.writeString(fechaIngreso);
        dest.writeString(fechaNacimiento);
        dest.writeLong(idPersona);
        dest.writeString(nombres);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Persona> CREATOR = new Creator<Persona>() {
        @Override
        public Persona createFromParcel(Parcel in) {
            return new Persona(in);
        }

        @Override
        public Persona[] newArray(int size) {
            return new Persona[size];
        }
    };

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getdNI() {
        return dNI;
    }

    public void setdNI(String dNI) {
        this.dNI = dNI;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(long idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
}

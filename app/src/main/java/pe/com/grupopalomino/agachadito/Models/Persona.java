
package pe.com.grupopalomino.agachadito.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Persona implements Parcelable {

    private String apellidos;
    private String direccion;
    private String dni;
    private Boolean estado;
    private String fechaIngreso;
    private String fechaNacimiento;
    private int id_persona;
    private String nombres;

    public Persona() {
    }

    public Persona(String apellidos, String direccion, String dni, Boolean estado, String fechaIngreso, String fechaNacimiento, int id_persona, String nombres) {
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.dni = dni;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.fechaNacimiento = fechaNacimiento;
        this.id_persona = id_persona;
        this.nombres = nombres;
    }

    protected Persona(Parcel in) {
        apellidos = in.readString();
        direccion = in.readString();
        dni = in.readString();
        byte tmpEstado = in.readByte();
        estado = tmpEstado == 0 ? null : tmpEstado == 1;
        fechaIngreso = in.readString();
        fechaNacimiento = in.readString();
        id_persona = in.readInt();
        nombres = in.readString();
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

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(apellidos);
        parcel.writeString(direccion);
        parcel.writeString(dni);
        parcel.writeByte((byte) (estado == null ? 0 : estado ? 1 : 2));
        parcel.writeString(fechaIngreso);
        parcel.writeString(fechaNacimiento);
        parcel.writeInt(id_persona);
        parcel.writeString(nombres);
    }
}